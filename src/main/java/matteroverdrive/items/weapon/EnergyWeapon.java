/*
 * This file is part of Matter Overdrive
 * Copyright (c) 2015., Simeon Radivoev, All rights reserved.
 *
 * Matter Overdrive is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Matter Overdrive is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Matter Overdrive.  If not, see <http://www.gnu.org/licenses>.
 */

package matteroverdrive.items.weapon;

import cofh.api.energy.IEnergyContainerItem;
import cofh.lib.util.helpers.EnergyHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.api.inventory.IEnergyPack;
import matteroverdrive.api.weapon.IWeapon;
import matteroverdrive.api.weapon.IWeaponModule;
import matteroverdrive.init.MatterOverdriveEnchantments;
import matteroverdrive.items.includes.MOItemEnergyContainer;
import matteroverdrive.network.packet.server.PacketReloadEnergyWeapon;
import matteroverdrive.proxy.ClientProxy;
import matteroverdrive.util.EntityDamageSourcePhaser;
import matteroverdrive.util.MOEnergyHelper;
import matteroverdrive.util.MOStringHelper;
import matteroverdrive.util.WeaponHelper;
import matteroverdrive.util.animation.MOEasing;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

/**
 * Created by Simeon on 7/26/2015.
 */
public abstract class EnergyWeapon extends MOItemEnergyContainer implements IWeapon {

    private final int defaultRange;
    private DecimalFormat damageFormater = new DecimalFormat("#.##");

    public EnergyWeapon(String name, int capacity, int maxReceive, int maxExtract,int defaultRange) {
        super(name, capacity, maxReceive, maxExtract);
        this.defaultRange = defaultRange;
    }

    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tabs, List list)
    {
        ItemStack unpowered = new ItemStack(item);
        ItemStack powered = new ItemStack(item);
        setEnergyStored(powered,getMaxEnergyStored(powered));
        list.add(unpowered);
        list.add(powered);
    }

    @Override
    public boolean hasDetails(ItemStack itemStack)
    {
        return true;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack p_77661_1_)
    {
        return EnumAction.bow;
    }

    @Override
    public void addDetails(ItemStack weapon, EntityPlayer player, List infos)
    {
        super.addDetails(weapon, player, infos);
        String energyInfo = EnumChatFormatting.DARK_RED + "Power Use: " + MOEnergyHelper.formatEnergy(getEnergyUse(weapon));
        float energyMultiply = (float)getEnergyUse(weapon) / (float)getBaseEnergyUse(weapon);
        if (energyMultiply != 1)
        {
            energyInfo += " (" + DecimalFormat.getPercentInstance().format(energyMultiply) + ")";
        }
        infos.add(energyInfo);
        String damageInfo = EnumChatFormatting.DARK_GREEN + "Damage: " + damageFormater.format(getWeaponScaledDamage(weapon));
        double damageModify = getWeaponScaledDamage(weapon) / getWeaponBaseDamage(weapon);
        if (damageModify != 1)
        {
            damageInfo += " (" + DecimalFormat.getPercentInstance().format(damageModify) + ")";
        }
        infos.add(damageInfo);
        infos.add(EnumChatFormatting.DARK_RED + "Heat: " + DecimalFormat.getPercentInstance().format(getHeat(weapon) / getMaxHeat(weapon)));
        addCustomDetails(weapon,player,infos);
        AddModuleDetails(weapon, infos);
    }

    private void AddModuleDetails(ItemStack weapon,List infos)
    {
        ItemStack module = WeaponHelper.getModuleAtSlot(Reference.MODULE_BARREL, weapon);
        if (module != null)
        {
            infos.add(EnumChatFormatting.GRAY + "");
            infos.add(EnumChatFormatting.GRAY + "Barrel:");

            Object statsObject = ((IWeaponModule)module.getItem()).getValue(module);
            if (statsObject instanceof Map)
            {
                for (final Map.Entry<Integer, Double> entry : ((Map<Integer,Double>) statsObject).entrySet())
                {
                    if (entry.getKey() != Reference.WS_DAMAGE && entry.getKey() != Reference.WS_AMMO) {
                        infos.add("    " + MOStringHelper.weaponStatToInfo(entry.getKey(), entry.getValue()));
                    }
                }
            }
        }
    }

    protected void manageOverheat(ItemStack itemStack,World world,EntityPlayer entityPlayer)
    {
        if (getHeat(itemStack) >= getMaxHeat(itemStack))
        {
            itemStack.getTagCompound().setBoolean("Overheated", true);
            world.playSoundAtEntity(entityPlayer, Reference.MOD_ID + ":" + "overheat", 1F, 1f);
            world.playSoundAtEntity(entityPlayer,Reference.MOD_ID + ":" + "overheat_alarm",1,1);
        }
    }

    protected void manageCooling(ItemStack itemStack)
    {
        float heat = getHeat(itemStack);
        if (heat > 0) {
            float easing = MOEasing.Quart.easeOut(heat / getMaxHeat(itemStack),0,4,1);
            //System.out.println("Heat Easing: " + easing);
            float newHeat = heat - easing;
            if (newHeat < 0.001f) {
                newHeat = 0;
            }
            //System.out.println("old Heat: " + heat);
            //System.out.println("new Heat: " + newHeat);
            setHeat(itemStack, Math.max(0, newHeat));
        }

        if (isOverheated(itemStack))
        {
            if (getHeat(itemStack) < 2)
            {
                setOverheated(itemStack,false);
            }
        }
    }

    public void chargeFromEnergyPack(ItemStack weapon,EntityPlayer player)
    {
        if (!player.worldObj.isRemote)
        {
            for (int i = 0;i < player.inventory.mainInventory.length;i++) {
                if (player.inventory.mainInventory[i] != null && player.inventory.mainInventory[i].getItem() instanceof IEnergyPack && player.inventory.mainInventory[i].stackSize > 0) {
                    player.inventory.mainInventory[i].stackSize--;
                    setEnergyStored(weapon, Math.min(getEnergyStored(weapon) + ((IEnergyPack) player.inventory.mainInventory[i].getItem()).getEnergyAmount(player.inventory.mainInventory[i]), getMaxEnergyStored(weapon)));
                    player.inventory.inventoryChanged = true;
                    player.worldObj.playSoundAtEntity(player, Reference.MOD_ID + ":" + "reload", 0.7f + itemRand.nextFloat() * 0.2f, 0.9f + itemRand.nextFloat() * 0.2f);
                    if (player.inventory.mainInventory[i].stackSize <= 0)
                    {
                        player.inventory.mainInventory[i] = null;
                    }
                    return;
                }
            }
        }else
        {
            for (ItemStack stack : player.inventory.mainInventory)
            {
                if (stack != null && stack.getItem() instanceof IEnergyPack && stack.stackSize > 0)
                {
                    ClientProxy.weaponHandler.addReloadDelay(this, 40);
                    MatterOverdrive.packetPipeline.sendToServer(new PacketReloadEnergyWeapon());
                    return;
                }
            }
        }
    }

    @Override
    public void onUpdate(ItemStack itemStack, World world, Entity entity, int p_77663_4_, boolean p_77663_5_)
    {
        if (!world.isRemote)
        {
            manageCooling(itemStack);
        }
    }

    //region Abstract Functions
    protected abstract void addCustomDetails(ItemStack weapon,EntityPlayer player,List infos);
    protected abstract int getBaseEnergyUse(ItemStack item);
    protected abstract int getBaseMaxHeat(ItemStack item);
    public abstract float getWeaponBaseDamage(ItemStack weapon);
    public abstract boolean canFire(ItemStack itemStack,World world);
    //endregion

    //region Energy Functions
    @Override
    public int receiveEnergy(ItemStack container, int maxReceive, boolean simulate) {

        ItemStack energy_module = WeaponHelper.getModuleAtSlot(Reference.MODULE_BATTERY, container);
        if (energy_module != null && EnergyHelper.isEnergyContainerItem(energy_module))
        {
            IEnergyContainerItem e = ((IEnergyContainerItem)energy_module.getItem());
            int energy = e.receiveEnergy(energy_module, maxReceive, simulate);
            if (!simulate)
                WeaponHelper.setModuleAtSlot(Reference.MODULE_BATTERY,container,energy_module);
            return energy;
        }
        else
        {
            return super.receiveEnergy(container, maxReceive, simulate);
        }
    }

    //Returns 0 to disable extraction from outside sources
    @Override
    public int extractEnergy(ItemStack container, int maxExtract, boolean simulate)
    {
        return 0;
    }

    public int extractEnergyCustom(ItemStack weapon,int maxExtract,boolean simulate)
    {
        ItemStack energy_module = WeaponHelper.getModuleAtSlot(Reference.MODULE_BATTERY, weapon);
        if (energy_module != null && EnergyHelper.isEnergyContainerItem(energy_module))
        {
            IEnergyContainerItem e = ((IEnergyContainerItem)energy_module.getItem());
            int energy = e.extractEnergy(energy_module, maxReceive, simulate);
            if (!simulate)
                WeaponHelper.setModuleAtSlot(Reference.MODULE_BATTERY,weapon,energy_module);
            return energy;
        }
        else
        {
            return super.extractEnergy(weapon, maxReceive, simulate);
        }
    }

    @Override
    protected void setEnergyStored(ItemStack container,int amount)
    {
        ItemStack energy_module = WeaponHelper.getModuleAtSlot(Reference.MODULE_BATTERY, container);
        if (energy_module != null && EnergyHelper.isEnergyContainerItem(energy_module))
        {
            EnergyHelper.setDefaultEnergyTag(energy_module, amount);
            WeaponHelper.setModuleAtSlot(Reference.MODULE_BATTERY,container,energy_module);
        }
        else
        {
            super.setEnergyStored(container,amount);
        }

    }

    @Override
    public int getEnergyStored(ItemStack container)
    {
        ItemStack energy_module = WeaponHelper.getModuleAtSlot(Reference.MODULE_BATTERY, container);
        if (energy_module != null && EnergyHelper.isEnergyContainerItem(energy_module))
        {
            IEnergyContainerItem e = ((IEnergyContainerItem)energy_module.getItem());
            return e.getEnergyStored(energy_module);
        }
        else
        {
            return super.getEnergyStored(container);
        }
    }

    @Override
    public int getMaxEnergyStored(ItemStack container)
    {
        ItemStack energy_module = WeaponHelper.getModuleAtSlot(Reference.MODULE_BATTERY, container);
        if (energy_module != null && EnergyHelper.isEnergyContainerItem(energy_module))
        {
            IEnergyContainerItem e = ((IEnergyContainerItem)energy_module.getItem());
            return e.getMaxEnergyStored(energy_module);
        }
        else
        {
            return capacity;
        }
    }

    protected boolean DrainEnergy(ItemStack item,int ticks,boolean simulate)
    {
        int amount = getEnergyUse(item) * ticks;
        int hasEnergy = getEnergyStored(item);
        if (hasEnergy >= amount)
        {
            while (amount > 0)
            {
                if (extractEnergyCustom(item, amount, true) > 0)
                {
                    amount -= extractEnergyCustom(item,amount,simulate);
                }
                else
                {
                    return false;
                }
            }
        }else
        {
            return false;
        }
        return true;
    }

    //endregion

    //region Getters and setters
    public int getRange(ItemStack phaser)
    {
        int range = defaultRange;
        range = cofh.lib.util.helpers.MathHelper.round(range * getRangeMultiply(phaser));
        return  range;
    }

    protected double getPowerMultiply(ItemStack weapon)
    {
        return WeaponHelper.getStatMultiply(Reference.WS_AMMO, weapon) + EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId,weapon) * 0.04f;
    }

    protected float getDamageMultiplay(ItemStack weapon)
    {
        return (float)WeaponHelper.getStatMultiply(Reference.WS_DAMAGE, weapon) + EnchantmentHelper.getEnchantmentLevel(MatterOverdriveEnchantments.overclock.effectId,weapon) * 0.04f;
    }

    protected float getMaxHeatMultiply(ItemStack weapon)
    {
        return (float)WeaponHelper.getStatMultiply(Reference.WS_MAX_HEAT,weapon);
    }

    protected double getRangeMultiply(ItemStack phaser)
    {
        return WeaponHelper.getStatMultiply(Reference.WS_RANGE,phaser);
    }

    @Override
    public int getItemStackLimit(ItemStack item)
    {
        return 1;
    }

    public float getWeaponScaledDamage(ItemStack weapon)
    {
        return getWeaponBaseDamage(weapon) * getDamageMultiplay(weapon);
    }

    public DamageSource getDamageSource(ItemStack weapon,EntityPlayer player)
    {
        DamageSource damageInfo = new EntityDamageSourcePhaser(player);
        if (WeaponHelper.hasStat(Reference.WS_FIRE_DAMAGE,weapon))
        {
            damageInfo.setFireDamage();
        }
        else if (WeaponHelper.hasStat(Reference.WS_HEAL,weapon))
        {
            damageInfo.setMagicDamage();
        }

        if (WeaponHelper.hasStat(Reference.WS_EXPLOSION_DAMAGE, weapon))
        {
            damageInfo.setExplosion();
        }
        return damageInfo;
    }

    public int getEnergyUse(ItemStack weapon)
    {
        return (int)(getBaseEnergyUse(weapon) / getPowerMultiply(weapon));
    }

    public void addHeat(ItemStack itemStack,int amount)
    {
        if (itemStack.hasTagCompound())
        {
            setHeat(itemStack, getHeat(itemStack) + amount);
        }
    }

    public void setHeat(ItemStack item,float amount)
    {
        if (item.hasTagCompound())
        {
            item.getTagCompound().setFloat("heat", amount);
        }
    }

    @Override
    public float getHeat(ItemStack item) {
        if (item.hasTagCompound()) {
            return item.getTagCompound().getFloat("heat");
        }
        return 0;
    }

    @Override
    public float getMaxHeat(ItemStack itemStack)
    {
        return getBaseMaxHeat(itemStack) * getMaxHeatMultiply(itemStack);
    }

    public boolean isOverheated(ItemStack weapon)
    {
        if (weapon.hasTagCompound())
        {
            return weapon.getTagCompound().getBoolean("Overheated");
        }
        return false;
    }

    protected void setOverheated(ItemStack weapon,boolean overheated)
    {
        if (weapon.hasTagCompound())
        {
            weapon.getTagCompound().setBoolean("Overheated", overheated);
        }
    }

    @Override
    public int getAmmo(ItemStack weapon)
    {
        return getEnergyStored(weapon);
    }

    @Override
    public int getMaxAmmo(ItemStack weapon) {
        return getMaxEnergyStored(weapon);
    }

    public boolean needsRecharge(ItemStack weapon)
    {
        return !DrainEnergy(weapon,1,true);
    }

    @Override
    public int getItemEnchantability()
    {
        return ToolMaterial.IRON.getEnchantability();
    }

    @Override
    public boolean isItemTool(ItemStack p_77616_1_)
    {
        return true;
    }
    //endregion
}
