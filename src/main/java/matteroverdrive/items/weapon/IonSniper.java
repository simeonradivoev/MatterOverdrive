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

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import matteroverdrive.Reference;
import matteroverdrive.api.weapon.IWeaponModule;
import matteroverdrive.api.weapon.WeaponShot;
import matteroverdrive.client.sound.MOPositionedSound;
import matteroverdrive.client.sound.WeaponSound;
import matteroverdrive.entity.weapon.PlasmaBolt;
import matteroverdrive.handler.weapon.ClientWeaponHandler;
import matteroverdrive.items.weapon.module.WeaponModuleBarrel;
import matteroverdrive.util.WeaponHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;

import java.util.List;

/**
 * Created by Simeon on 12/8/2015.
 */
public class IonSniper extends EnergyWeapon
{
    private static final int MAX_HEAT = 100;
    private static final int MAX_USE_TIME = 512;
    private static final int ENERGY_PER_SHOT = 3072;
    public static final int RANGE = 96;

    public IonSniper(String name) {
        super(name, 32000, 128, 128, RANGE);
        setFull3D();
        leftClickFire = true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected void addCustomDetails(ItemStack weapon, EntityPlayer player, List infos) {

    }

    @Override
    public int getBaseEnergyUse(ItemStack item)
    {
        return ENERGY_PER_SHOT / getShootCooldown(item);
    }

    @Override
    protected int getBaseMaxHeat(ItemStack item)
    {
        return MAX_HEAT;
    }

    @Override
    public float getWeaponBaseDamage(ItemStack weapon) {
        return 21;
    }

    @Override
    public float getWeaponBaseAccuracy(ItemStack weapon,boolean zoomed)
    {
        if (zoomed)
        {
            return 1f + getHeat(weapon) * 0.1f;
        }else
        {
            return 5 + getHeat(weapon) * 0.3f;
        }
    }

    @Override
    public boolean canFire(ItemStack weapon,World world,EntityLivingBase shooter)
    {
        return DrainEnergy(weapon, getShootCooldown(weapon), true) && !isOverheated(weapon);
    }

    @Override
    public float getShotSpeed(ItemStack weapon, EntityLivingBase shooter) {
        return 8;
    }

    @Override
    public int getBaseShootCooldown(ItemStack weapon) {
        return 30;
    }

    @Override
    public float getBaseZoom(ItemStack weapon, EntityLivingBase shooter) {
        return 0.4f;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onShooterClientUpdate(ItemStack itemStack, World world, EntityPlayer entityPlayer, boolean sendServerTick)
    {
        if (Mouse.isButtonDown(0) && hasShootDelayPassed()) {
            if (canFire(itemStack,world,entityPlayer))
            {
                if (Minecraft.getMinecraft().gameSettings.thirdPersonView == 0) {
                    if (isWeaponZoomed(entityPlayer, itemStack)) {
                        ClientWeaponHandler.RECOIL_AMOUNT = 2f + Math.min(2,getAccuracy(itemStack, entityPlayer, true));
                        Minecraft.getMinecraft().renderViewEntity.hurtTime = 30 + (int) ((getHeat(itemStack) / getMaxHeat(itemStack)) * 8);
                        Minecraft.getMinecraft().renderViewEntity.maxHurtTime = 45;
                    } else {
                        ClientWeaponHandler.RECOIL_AMOUNT = 4f + Math.min(2,getAccuracy(itemStack, entityPlayer, true));
                        Minecraft.getMinecraft().renderViewEntity.hurtTime = 30 + (int) ((getHeat(itemStack) / getMaxHeat(itemStack)) * 8);
                        Minecraft.getMinecraft().renderViewEntity.maxHurtTime = 45;
                    }
                    ClientWeaponHandler.RECOIL_TIME = 1;
                }

                Vec3 dir = entityPlayer.getLook(1);
                Vec3 pos = getFirePosition(entityPlayer, dir, isWeaponZoomed(entityPlayer,itemStack));
                WeaponShot shot = createShot(itemStack,entityPlayer, isWeaponZoomed(entityPlayer,itemStack));
                onClientShot(itemStack, entityPlayer, pos, dir, shot);
                addShootDelay(itemStack);
                sendShootTickToServer(world,shot,dir,pos);
                return;
            }else if (needsRecharge(itemStack))
            {
                chargeFromEnergyPack(itemStack,entityPlayer);
            }
        }

        super.onShooterClientUpdate(itemStack,world,entityPlayer,sendServerTick);
    }

    @SideOnly(Side.CLIENT)
    private Vec3 getFirePosition(EntityPlayer entityPlayer,Vec3 dir,boolean isAiming)
    {
        Vec3 pos = Vec3.createVectorHelper(entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ);
        if (!isAiming) {
            pos.xCoord -= (double)(MathHelper.cos(entityPlayer.rotationYaw / 180.0F * (float) Math.PI) * 0.16F);
            pos.zCoord -= (double)(MathHelper.sin(entityPlayer.rotationYaw / 180.0F * (float) Math.PI) * 0.16F);
        }
        pos = pos.addVector(dir.xCoord,dir.yCoord,dir.zCoord);
        return pos;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onClientShot(ItemStack weapon, EntityLivingBase shooter, Vec3 position, Vec3 dir, WeaponShot shot)
    {
        MOPositionedSound sound = new MOPositionedSound(new ResourceLocation(Reference.MOD_ID + ":" + "sniper_rifle_fire"),3f + itemRand.nextFloat()*0.5f, 0.9f + itemRand.nextFloat() * 0.2f);
        sound.setPosition((float) position.xCoord,(float)position.yCoord,(float)position.zCoord);
        Minecraft.getMinecraft().getSoundHandler().playSound(sound);
        spawnProjectile(weapon,shooter,position,dir,shot);
    }

    @Override
    public void onProjectileHit(MovingObjectPosition hit, ItemStack weapon, World world, float amount) {

    }

    @Override
    @SideOnly(Side.CLIENT)
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
    @SideOnly(Side.CLIENT)
    public Vector2f getModuleScreenPosition(int slot, ItemStack weapon)
    {
        switch(slot)
        {
            case Reference.MODULE_BATTERY:
                return new Vector2f(165,80);
            case Reference.MODULE_COLOR:
                return new Vector2f(110,90);
            case Reference.MODULE_BARREL:
                return new Vector2f(90,95);
            case Reference.MODULE_SIGHTS:
                return new Vector2f(150,72);
        }
        return getSlotPosition(slot,weapon);
    }

    public PlasmaBolt spawnProjectile(ItemStack weapon,EntityLivingBase shooter,Vec3 position,Vec3 dir,WeaponShot shot)
    {
        //PlasmaBolt fire = new PlasmaBolt(entityPlayer.worldObj, entityPlayer,position,dir, getWeaponScaledDamage(weapon), 2, getAccuracy(weapon, zoomed), getRange(weapon), WeaponHelper.getColor(weapon).getColor(), zoomed,seed);
        PlasmaBolt fire = new PlasmaBolt(shooter.worldObj, shooter,position,dir, shot,getShotSpeed(weapon,shooter));
        fire.setWeapon(weapon);
        fire.setFireDamageMultiply(WeaponHelper.modifyStat(Reference.WS_FIRE_DAMAGE, weapon,0));
        fire.setKnockBack(1);
        shooter.worldObj.spawnEntityInWorld(fire);
        return fire;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack itemStack)
    {
        return itemStack.getItemDamage() == 1 ? EnumAction.bow : EnumAction.none;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack item)
    {
        return MAX_USE_TIME;
    }

    @Override
    public boolean supportsModule(int slot, ItemStack weapon) {
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
    public boolean supportsModule(ItemStack weapon, ItemStack module)
    {
        if (module != null && module.getItem() instanceof IWeaponModule && ((IWeaponModule) module.getItem()).getSlot(module) == Reference.MODULE_BARREL)
        {
            return module.getItemDamage() != WeaponModuleBarrel.EXPLOSION_BARREL_ID && module.getItemDamage() != WeaponModuleBarrel.HEAL_BARREL_ID;
        }
        return true;
    }

    @Override
    public boolean onServerFire(ItemStack weapon, EntityLivingBase shooter, WeaponShot shot,Vec3 position,Vec3 dir,int delay)
    {
        DrainEnergy(weapon, getShootCooldown(weapon), false);
        float newHeat =  getHeat(weapon) + getMaxHeat(weapon) * 0.8f;
        setHeat(weapon, newHeat);
        manageOverheat(weapon, shooter.worldObj, shooter);
        PlasmaBolt fire = spawnProjectile(weapon,shooter,position,dir,shot);
        fire.simulateDelay(delay);
        return true;
    }

    @Override
    public boolean isAlwaysEquipped(ItemStack weapon) {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean isWeaponZoomed(EntityPlayer entityPlayer, ItemStack weapon) {
        return Mouse.isButtonDown(1) && entityPlayer.isUsingItem();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public WeaponSound getFireSound(ItemStack weapon, EntityLivingBase entity) {
        return null;
    }
}
