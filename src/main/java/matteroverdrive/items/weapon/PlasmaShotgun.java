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
import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.api.weapon.WeaponShot;
import matteroverdrive.client.render.item.ItemRenderPlasmaShotgun;
import matteroverdrive.client.sound.MOPositionedSound;
import matteroverdrive.client.sound.WeaponSound;
import matteroverdrive.entity.weapon.PlasmaBolt;
import matteroverdrive.init.MatterOverdriveItems;
import matteroverdrive.items.weapon.module.WeaponModuleBarrel;
import matteroverdrive.network.packet.bi.PacketFirePlasmaShot;
import matteroverdrive.proxy.ClientProxy;
import matteroverdrive.util.WeaponHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;

import java.util.List;

/**
 * Created by Simeon on 12/6/2015.
 */
public class PlasmaShotgun extends EnergyWeapon
{
    private static final int MAX_CHARGE_TIME = 20;
    private static final int ENERGY_PER_SHOT = 1024*2;
    public static final int RANGE = 16;
    @SideOnly(Side.CLIENT)
    private MOPositionedSound lastChargingSound;

    public PlasmaShotgun(String name)
    {
        super(name, 32000, 128, 128, RANGE);
        this.setFull3D();
    }

    @Override
    protected void addCustomDetails(ItemStack weapon, EntityPlayer player, List infos) {

    }

    @Override
    public int getBaseEnergyUse(ItemStack item)
    {
        return ENERGY_PER_SHOT / getShootCooldown(item);
    }

    @Override
    protected int getBaseMaxHeat(ItemStack item) {
        return 80;
    }

    @Override
    public float getWeaponBaseDamage(ItemStack weapon) {
        return 16;
    }

    @Override
    public float getWeaponBaseAccuracy(ItemStack weapon, boolean zoomed) {
        return 5f + getHeat(weapon)*0.3f;
    }

    @Override
    public boolean canFire(ItemStack itemStack, World world, EntityLivingBase shooter) {
        return DrainEnergy(itemStack, getShootCooldown(itemStack), true) && !isOverheated(itemStack);
    }

    @Override
    public float getShotSpeed(ItemStack weapon, EntityLivingBase shooter) {
        return 3;
    }

    @Override
    public int getBaseShootCooldown(ItemStack weapon) {
        return 20;
    }

    @Override
    public void onClientShot(ItemStack weapon, EntityLivingBase shooter, Vec3 position, Vec3 dir, WeaponShot shot) {
        shooter.playSound(Reference.MOD_ID + ":" + "plasma_shotgun_shot", 0.3f + itemRand.nextFloat() * 0.2f, 0.9f + itemRand.nextFloat() * 0.2f);
        spawnProjectile(weapon,shooter,position,dir,shot);
    }

    public PlasmaBolt[] spawnProjectile(ItemStack weapon, EntityLivingBase shooter, Vec3 position, Vec3 dir, WeaponShot shot)
    {
        //PlasmaBolt fire = new PlasmaBolt(entityPlayer.worldObj, entityPlayer,position,dir, getWeaponScaledDamage(weapon), 2, getAccuracy(weapon, zoomed), getRange(weapon), WeaponHelper.getColor(weapon).getColor(), zoomed,seed);
        PlasmaBolt[] bolts = new PlasmaBolt[shot.getCount()];
        for (int i = 0;i < shot.getCount();i++) {
            WeaponShot newShot = new WeaponShot(shot);
            newShot.setSeed(shot.getSeed()+i);
            newShot.setDamage(shot.getDamage()/shot.getCount());
            bolts[i] = new PlasmaBolt(shooter.worldObj, shooter, position, dir, newShot, getShotSpeed(weapon, shooter));
            bolts[i].setWeapon(weapon);
            bolts[i].setRenderSize((getShotCount(weapon,shooter)/shot.getCount())*0.5f);
            if (WeaponHelper.hasStat(Reference.WS_FIRE_DAMAGE, weapon)) {
                bolts[i].setFireDamageMultiply((float) WeaponHelper.getStatMultiply(Reference.WS_FIRE_DAMAGE, weapon));
            }
            shooter.worldObj.spawnEntityInWorld(bolts[i]);
        }
        return bolts;
    }

    public int getShotCount(ItemStack weapon,EntityLivingBase shooter)
    {
        return 10;
    }

    @Override
    public void onProjectileHit(MovingObjectPosition hit, ItemStack weapon, World world, float amount) {

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
    public boolean supportsModule(ItemStack weapon, ItemStack module)
    {
        if (module != null)
        {
            return module.getItem() == MatterOverdriveItems.weapon_module_color || (module.getItem() == MatterOverdriveItems.weapon_module_barrel && module.getItemDamage() != WeaponModuleBarrel.EXPLOSION_BARREL_ID && module.getItemDamage() != WeaponModuleBarrel.HEAL_BARREL_ID);
        }
        return false;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack weapon, World world, EntityPlayer entityPlayer)
    {
        if (world.isRemote && canFire(weapon,world,entityPlayer) && ClientProxy.weaponHandler.shootDelayPassed(this) && !entityPlayer.isUsingItem()) {
            entityPlayer.setItemInUse(weapon, getMaxItemUseDuration(weapon));
            playChargingSound(entityPlayer);
        }
        return weapon;
    }

    @SideOnly(Side.CLIENT)
    public void playChargingSound(EntityPlayer entityPlayer)
    {
        lastChargingSound = new MOPositionedSound(new ResourceLocation(Reference.MOD_ID + ":" + "plasma_shotgun_charging"),1,1);
        lastChargingSound.setPosition((float) entityPlayer.posX,(float)entityPlayer.posY,(float)entityPlayer.posZ);
        Minecraft.getMinecraft().getSoundHandler().playSound(lastChargingSound);
    }

    @SideOnly(Side.CLIENT)
    public void stopChargingSound()
    {
        Minecraft.getMinecraft().getSoundHandler().stopSound(lastChargingSound);
    }

    @Override
    public void onUsingTick(ItemStack stack, EntityPlayer player, int count)
    {
        if (getMaxItemUseDuration(stack) - count >= MAX_CHARGE_TIME)
        {
            player.stopUsingItem();
        }
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack weapon, World world, EntityPlayer entityPlayer, int time)
    {
        if (world.isRemote) {
            int maxCount = getShotCount(weapon,entityPlayer);
            int timeElapsed = (getMaxItemUseDuration(weapon) - time);
            int count = Math.max(1, (int) ((1f - (timeElapsed / (float) MAX_CHARGE_TIME)) * maxCount));
            float shotPercent = count / (float) getShotCount(weapon, entityPlayer);

            ItemRenderPlasmaShotgun.RECOIL_AMOUNT = 15 + (maxCount - count) * 2 + getAccuracy(weapon, entityPlayer, isWeaponZoomed(entityPlayer, weapon)) * 2;
            ItemRenderPlasmaShotgun.RECOIL_TIME = 1 + (maxCount - count) * 0.03f;
            Minecraft.getMinecraft().renderViewEntity.hurtTime = 15 + (maxCount - count);
            Minecraft.getMinecraft().renderViewEntity.maxHurtTime = 30 + (maxCount - count);
            Vec3 dir = entityPlayer.getLook(1);
            Vec3 pos = getFirePosition(entityPlayer, dir, Mouse.isButtonDown(1));
            WeaponShot shot = createShot(weapon, entityPlayer, Mouse.isButtonDown(1));
            shot.setCount(count);
            shot.setAccuracy(shot.getAccuracy() * shotPercent);
            shot.setRange(shot.getRange() + (int) (shot.getRange() * (1 - shotPercent)));
            onClientShot(weapon, entityPlayer, pos, dir, shot);
            MatterOverdrive.packetPipeline.sendToServer(new PacketFirePlasmaShot(entityPlayer.getEntityId(), pos, dir, shot));
            ClientProxy.weaponHandler.addShootDelay(this, weapon);
            stopChargingSound();
            entityPlayer.clearItemInUse();
        }
    }

    @Override
    public ItemStack onEaten(ItemStack weapon, World world, EntityPlayer entityPlayer)
    {
        return weapon;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack weapon)
    {
        return 72000;
    }

    @Override
    public boolean onLeftClick(ItemStack weapon, EntityPlayer entityPlayer)
    {
        return true;
    }

    @SideOnly(Side.CLIENT)
    private Vec3 getFirePosition(EntityPlayer entityPlayer,Vec3 dir,boolean isAiming)
    {
        Vec3 pos = Vec3.createVectorHelper(entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ);
        if (!isAiming) {
            //pos.xCoord -= (double)(MathHelper.cos(entityPlayer.rotationYaw / 180.0F * (float) Math.PI) * 0.16F);
            //pos.zCoord -= (double)(MathHelper.sin(entityPlayer.rotationYaw / 180.0F * (float) Math.PI) * 0.16F);
        }
        pos = pos.addVector(dir.xCoord,dir.yCoord,dir.zCoord);
        return pos;
    }

    public void onUpdate(ItemStack itemStack, World world, Entity entity, int p_77663_4_, boolean p_77663_5_) {
        super.onUpdate(itemStack,world,entity,p_77663_4_,p_77663_5_);

        if (world.isRemote
                && entity instanceof EntityPlayer
                && ((EntityPlayer) entity).getHeldItem() == itemStack
                && Minecraft.getMinecraft().currentScreen == null) {
            if (Mouse.isButtonDown(0)) {

                if (canFire(itemStack,world,(EntityLivingBase)entity) && ClientProxy.weaponHandler.shootDelayPassed(this))
                {
                    itemStack.getTagCompound().setLong("LastShot", world.getTotalWorldTime());
                    ItemRenderPlasmaShotgun.RECOIL_AMOUNT = 12 + getAccuracy(itemStack,(EntityPlayer)entity,isWeaponZoomed((EntityPlayer) entity,itemStack)) * 2;
                    ItemRenderPlasmaShotgun.RECOIL_TIME = 1;
                    Minecraft.getMinecraft().renderViewEntity.hurtTime = 15;
                    Minecraft.getMinecraft().renderViewEntity.maxHurtTime = 30;
                    Vec3 dir = ((EntityPlayer) entity).getLook(1);
                    Vec3 pos = getFirePosition((EntityPlayer) entity, dir, Mouse.isButtonDown(1));
                    WeaponShot shot = createShot(itemStack,(EntityPlayer)entity,Mouse.isButtonDown(1));
                    onClientShot(itemStack, (EntityPlayer) entity, pos, dir,shot);
                    MatterOverdrive.packetPipeline.sendToServer(new PacketFirePlasmaShot(entity.getEntityId(),pos,dir,shot));
                    ClientProxy.weaponHandler.addShootDelay(this,itemStack);
                }else if (ClientProxy.weaponHandler.shootDelayPassed(this) && needsRecharge(itemStack))
                {
                    chargeFromEnergyPack(itemStack,(EntityPlayer)entity);
                }
            }
        }
    }

    public WeaponShot createShot(ItemStack weapon, EntityLivingBase shooter, boolean zoomed)
    {
        WeaponShot shot = new WeaponShot(itemRand.nextInt(),getWeaponScaledDamage(weapon),getAccuracy(weapon,shooter,zoomed),WeaponHelper.getColor(weapon).getColor(),getRange(weapon));
        shot.setCount(getShotCount(weapon,shooter));
        return shot;
    }

    @Override
    public boolean onLeftClickTick(ItemStack weapon, EntityPlayer entityPlayer)
    {
        return false;
    }

    @Override
    public boolean onServerFire(ItemStack weapon, EntityLivingBase shooter, WeaponShot shot, Vec3 position, Vec3 dir) {
        DrainEnergy(weapon, getShootCooldown(weapon), false);
        int heatAdd = (getShotCount(weapon,shooter) - shot.getCount()) * 2;
        float newHeat =  (getHeat(weapon)+ heatAdd + 5) * 4f ;
        setHeat(weapon, Math.max(newHeat,0));
        manageOverheat(weapon, shooter.worldObj, shooter);
        PlasmaBolt[] fires = spawnProjectile(weapon,shooter,position,dir,shot);
        //weapon.getTagCompound().setLong("LastShot", shooter.worldObj.getTotalWorldTime());
        shooter.playSound(Reference.MOD_ID + ":" + "plasma_shotgun_shot", 0.3f + itemRand.nextFloat() * 0.2f, 0.9f + itemRand.nextFloat() * 0.2f);
        return true;
    }

    @Override
    public boolean isAlwaysEquipped(ItemStack weapon) {
        return false;
    }

    @Override
    public boolean isWeaponZoomed(EntityPlayer entityPlayer, ItemStack weapon) {
        return false;
    }

    @Override
    public WeaponSound getFireSound(ItemStack weapon, EntityLivingBase entity) {
        return null;
    }
}
