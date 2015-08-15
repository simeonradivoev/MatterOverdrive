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

import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.client.render.item.ItemRendererPhaserRifle;
import matteroverdrive.entity.weapon.PhaserFire;
import matteroverdrive.network.packet.server.PacketFirePhaserRifle;
import matteroverdrive.proxy.ClientProxy;
import matteroverdrive.util.WeaponHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;

import java.util.List;

/**
 * Created by Simeon on 7/25/2015.
 */
public class PhaserRifle extends EnergyWeapon
{

    private static final int HEAT_PER_SHOT = 20;
    private static final int MAX_HEAT = 80;
    private static final int MAX_USE_TIME = 512;
    private static final int ENERGY_PER_SHOT = 1024;
    public static final int RANGE = 24;

    public PhaserRifle(String name) {
        super(name,32000,128,128,RANGE);
        this.bFull3D = true;
    }

    @Override
    public void registerIcons(IIconRegister iconRegistry)
    {

    }

    @Override
    public int getMaxItemUseDuration(ItemStack item)
    {
        return MAX_USE_TIME;
    }

    @Override
    public Vector2f getSlotPosition(int slot, ItemStack weapon)
    {
        switch (slot)
        {
            case Reference.MODULE_BATTERY:
                return new Vector2f(170,115);
            case Reference.MODULE_COLOR:
                return new Vector2f(60,45);
            case Reference.MODULE_BARREL:
                return new Vector2f(60,115);
            case Reference.MODULE_OTHER:
                return new Vector2f(205,80);
            case Reference.MODULE_SIGHTS:
                return new Vector2f(150,35);
        }
        return new Vector2f(0,0);
    }

    @Override
    public Vector2f getModuleScreenPosition(int slot, ItemStack weapon)
    {
        switch(slot)
        {
            case Reference.MODULE_BATTERY:
                return new Vector2f(165,80);
            case Reference.MODULE_COLOR:
                return new Vector2f(100,80);
            case Reference.MODULE_BARREL:
                return new Vector2f(90,90);
            case Reference.MODULE_SIGHTS:
                return new Vector2f(140,72);
        }
        return getSlotPosition(slot,weapon);
    }

    @Override
    public boolean supportsModule(int slot,ItemStack weapon)
    {
        return true;

    }

    @Override
    public ItemStack onItemRightClick(ItemStack item, World world, EntityPlayer player)
    {
        player.setItemInUse(item, Integer.MAX_VALUE);
        if(world.isRemote)
        {
            for(int i = 0; i < 3; i++)
                Minecraft.getMinecraft().entityRenderer.itemRenderer.updateEquippedItem();
        }
        return item;
    }

    @Override
    public boolean onLeftClick(ItemStack weapon, EntityPlayer entityPlayer)
    {
        return true;
    }

    @Override
    public boolean isAlwaysEquipped(ItemStack weapon) {
        return true;
    }

    @Override
    public int getShootCooldown() {
        return 11;
    }

    public void onUpdate(ItemStack itemStack, World world, Entity entity, int p_77663_4_, boolean p_77663_5_)
    {
        if (world.isRemote
                && entity instanceof EntityClientPlayerMP
                && ((EntityPlayer) entity).getHeldItem() != null
                && ((EntityPlayer) entity).getHeldItem().getItem() == this
                && Minecraft.getMinecraft().currentScreen == null)
        {
            if (Mouse.isButtonDown(0)) {
                if (canFire(itemStack,world) && ClientProxy.weaponHandler.shootDelayPassed(this))
                {
                    int seed = itemRand.nextInt();
                    MatterOverdrive.packetPipeline.sendToServer(new PacketFirePhaserRifle(Mouse.isButtonDown(1),entity.getEntityId(),seed));
                    itemStack.getTagCompound().setLong("LastShot", world.getTotalWorldTime());
                    if (Mouse.isButtonDown(1)) {
                        ItemRendererPhaserRifle.RECOIL_AMOUNT = 0.5f + (getHeat(itemStack) / getMaxHeat(itemStack)) * 3;
                    }else
                    {
                        ItemRendererPhaserRifle.RECOIL_AMOUNT = 2 + (getHeat(itemStack) / getMaxHeat(itemStack)) * 6;
                    }
                    ItemRendererPhaserRifle.RECOIL_TIME = 1;
                    onClientFire(itemStack,(EntityPlayer)entity,Mouse.isButtonDown(1),seed);
                    ClientProxy.weaponHandler.addShootDelay(this);
                }else if (ClientProxy.weaponHandler.shootDelayPassed(this) && needsRecharge(itemStack))
                {
                    chargeFromEnergyPack(itemStack,(EntityPlayer)entity);
                }
            }
        }

        if (world.isRemote)
        {
            for(int i = 0; i < 3; i++)
                Minecraft.getMinecraft().entityRenderer.itemRenderer.updateEquippedItem();
        }

        super.onUpdate(itemStack, world, entity, p_77663_4_, p_77663_5_);
    }

    @Override
    public boolean onServerFire(ItemStack weapon, EntityPlayer entityPlayer, boolean zoomed,int seed,int latency)
    {
        DrainEnergy(weapon, 1, false);
        float newHeat =  (getHeat(weapon)+4) * 2f;
        setHeat(weapon, newHeat);
        manageOverheat(weapon, entityPlayer.worldObj, entityPlayer);
        PhaserFire fire = spawnProjectile(weapon,entityPlayer,zoomed,seed);
        weapon.getTagCompound().setLong("LastShot", entityPlayer.worldObj.getTotalWorldTime());
        fire.simulate(latency);
        entityPlayer.playSound(Reference.MOD_ID + ":" + "phaser_rifle_shot", 0.3f + itemRand.nextFloat() * 0.2f, 0.9f + itemRand.nextFloat() * 0.2f);
        return true;
    }

    public void onClientFire(ItemStack weapon, EntityPlayer player, boolean zoomed,int seed)
    {
        //ClientProxy.weaponHandler.addShootDelay(this);
        player.playSound(Reference.MOD_ID + ":" + "phaser_rifle_shot", 0.3f + itemRand.nextFloat() * 0.2f, 0.9f + itemRand.nextFloat() * 0.2f);
        spawnProjectile(weapon,player,zoomed,seed);
    }

    public PhaserFire spawnProjectile(ItemStack weapon,EntityPlayer entityPlayer,boolean zoomed,int seed)
    {
        PhaserFire fire = new PhaserFire(entityPlayer.worldObj, entityPlayer, getWeaponScaledDamage(weapon), 2, getAccuracy(weapon, zoomed), getRange(weapon), WeaponHelper.getColor(weapon).getColor(), zoomed,seed);
        if (WeaponHelper.hasStat(Reference.WS_FIRE_DAMAGE,weapon)) {
            fire.setFireDamageMultiply((float) WeaponHelper.getStatMultiply(Reference.WS_FIRE_DAMAGE, weapon));
        }
        entityPlayer.worldObj.spawnEntityInWorld(fire);
        return fire;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack itemStack)
    {
        return itemStack.getItemDamage() == 1 ? EnumAction.bow : EnumAction.none;
    }

    @Override
    protected void addCustomDetails(ItemStack weapon, EntityPlayer player, List infos)
    {

    }

    @Override
    public int getBaseEnergyUse(ItemStack item)
    {
        return ENERGY_PER_SHOT;
    }

    @Override
    protected int getBaseMaxHeat(ItemStack item)
    {
        return MAX_HEAT;
    }

    @Override
    public float getWeaponBaseDamage(ItemStack weapon) {
        return 3;
    }

    @Override
    public boolean canFire(ItemStack weapon,World world)
    {
        return DrainEnergy(weapon, 1, true) && !weapon.getTagCompound().getBoolean("Overheated");
    }

    public float getAccuracy(ItemStack weapon,boolean zoomed)
    {
        return getHeat(weapon) / (zoomed ? 30f : 10f);
    }
}
