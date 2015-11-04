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

import cofh.lib.gui.GuiColor;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.api.weapon.WeaponShot;
import matteroverdrive.client.render.item.ItemRendererPhaserRifle;
import matteroverdrive.client.sound.WeaponSound;
import matteroverdrive.entity.weapon.PlasmaBolt;
import matteroverdrive.fx.PhaserBoltRecoil;
import matteroverdrive.network.packet.server.PacketFirePlasmaShot;
import matteroverdrive.proxy.ClientProxy;
import matteroverdrive.util.WeaponHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.particle.EntityExplodeFX;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
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
    public boolean onLeftClickTick(ItemStack weapon, EntityPlayer entityPlayer)
    {
        return false;
    }

    @Override
    public boolean isAlwaysEquipped(ItemStack weapon) {
        return true;
    }

    @Override
    public int getShootCooldown() {
        return 11;
    }

    @Override
    public boolean isWeaponZoomed(ItemStack weapon) {
        return Mouse.isButtonDown(1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public WeaponSound getFireSound(ItemStack weapon, EntityLivingBase entity) {
        return null;
    }

    public void onUpdate(ItemStack itemStack, World world, Entity entity, int p_77663_4_, boolean p_77663_5_)
    {
        if (world.isRemote
                && entity instanceof EntityClientPlayerMP
                && ((EntityPlayer) entity).getHeldItem() == itemStack
                && Minecraft.getMinecraft().currentScreen == null)
        {
            if (Mouse.isButtonDown(0)) {
                if (canFire(itemStack,world) && ClientProxy.weaponHandler.shootDelayPassed(this))
                {
                    itemStack.getTagCompound().setLong("LastShot", world.getTotalWorldTime());
                    if (isWeaponZoomed(itemStack)) {
                        ItemRendererPhaserRifle.RECOIL_AMOUNT = 0.5f + getAccuracy(itemStack,(EntityPlayer)entity,true);
                        Minecraft.getMinecraft().renderViewEntity.hurtTime = 6 + (int)((getHeat(itemStack) / getMaxHeat(itemStack)) * 8);
                        Minecraft.getMinecraft().renderViewEntity.maxHurtTime = 15;
                    }else
                    {
                        ItemRendererPhaserRifle.RECOIL_AMOUNT = 2 + getAccuracy(itemStack,(EntityPlayer)entity,true) * 2;
                        Minecraft.getMinecraft().renderViewEntity.hurtTime = 6 + (int)((getHeat(itemStack) / getMaxHeat(itemStack)) * 8);
                        Minecraft.getMinecraft().renderViewEntity.maxHurtTime = 20;
                    }

                    ItemRendererPhaserRifle.RECOIL_TIME = 1;
                    Vec3 dir = ((EntityPlayer) entity).getLook(1);
                    Vec3 pos = getFirePosition((EntityPlayer) entity, dir, isWeaponZoomed(itemStack));
                    WeaponShot shot = createShot(itemStack,(EntityPlayer) entity, isWeaponZoomed(itemStack));
                    onClientShot(itemStack, (EntityPlayer) entity, pos, dir, shot);
                    MatterOverdrive.packetPipeline.sendToServer(new PacketFirePlasmaShot(entity.getEntityId(),pos,dir,shot));
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
    public boolean onServerFire(ItemStack weapon, EntityPlayer entityPlayer, WeaponShot shot,Vec3 position,Vec3 dir)
    {
        DrainEnergy(weapon, 1, false);
        float newHeat =  (getHeat(weapon)+4) * 2f;
        setHeat(weapon, newHeat);
        manageOverheat(weapon, entityPlayer.worldObj, entityPlayer);
        PlasmaBolt fire = spawnProjectile(weapon,entityPlayer,position,dir,shot);
        weapon.getTagCompound().setLong("LastShot", entityPlayer.worldObj.getTotalWorldTime());
        entityPlayer.playSound(Reference.MOD_ID + ":" + "phaser_rifle_shot", 0.3f + itemRand.nextFloat() * 0.2f, 0.9f + itemRand.nextFloat() * 0.2f);
        return true;
    }

    @Override
    public void onClientShot(ItemStack weapon, EntityPlayer player, Vec3 position, Vec3 dir,WeaponShot shot)
    {
        //ClientProxy.weaponHandler.addShootDelay(this);
        player.playSound(Reference.MOD_ID + ":" + "phaser_rifle_shot", 0.3f + itemRand.nextFloat() * 0.2f, 0.9f + itemRand.nextFloat() * 0.2f);
        spawnProjectile(weapon,player,position,dir,shot);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onProjectileHit(MovingObjectPosition hit, ItemStack weapon, World world,float amount)
    {
        if (hit.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            Minecraft.getMinecraft().effectRenderer.addEffect(new EntityExplodeFX(world, hit.hitVec.xCoord, hit.hitVec.yCoord, hit.hitVec.zCoord, 0, 0, 0));
            if (itemRand.nextFloat() < 0.8f) {
                for (int i = 0; i < 5; i++) {
                    Minecraft.getMinecraft().effectRenderer.addEffect(new PhaserBoltRecoil(world, hit.hitVec.xCoord, hit.hitVec.yCoord, hit.hitVec.zCoord, new GuiColor(255, 255, 255)));
                }
                world.playSound(hit.hitVec.xCoord, hit.hitVec.yCoord, hit.hitVec.zCoord, Reference.MOD_ID + ":" + "sizzle", itemRand.nextFloat() * 0.2f + 0.4f, itemRand.nextFloat() * 0.6f + 0.7f, false);
            }
            if (itemRand.nextFloat() < 0.5f) {
                world.playSound(hit.hitVec.xCoord, hit.hitVec.yCoord, hit.hitVec.zCoord, Reference.MOD_ID + ":" + "bolt_hit_0", itemRand.nextFloat() * 0.1f + 0.2f, itemRand.nextFloat() * 0.4f + 0.8f, false);
            }
        }
    }

    public PlasmaBolt spawnProjectile(ItemStack weapon,EntityPlayer entityPlayer,Vec3 position,Vec3 dir,WeaponShot shot)
    {
        //PlasmaBolt fire = new PlasmaBolt(entityPlayer.worldObj, entityPlayer,position,dir, getWeaponScaledDamage(weapon), 2, getAccuracy(weapon, zoomed), getRange(weapon), WeaponHelper.getColor(weapon).getColor(), zoomed,seed);
        PlasmaBolt fire = new PlasmaBolt(entityPlayer.worldObj, entityPlayer,position,dir, shot,2);
        fire.setWeapon(weapon);
        if (WeaponHelper.hasStat(Reference.WS_FIRE_DAMAGE,weapon)) {
            fire.setFireDamageMultiply((float) WeaponHelper.getStatMultiply(Reference.WS_FIRE_DAMAGE, weapon));
        }
        entityPlayer.worldObj.spawnEntityInWorld(fire);
        return fire;
    }

    public WeaponShot createShot(ItemStack weapon,EntityPlayer entityPlayer,boolean zoomed)
    {
        return new WeaponShot(itemRand.nextInt(),getWeaponScaledDamage(weapon),getAccuracy(weapon,entityPlayer,zoomed),WeaponHelper.getColor(weapon).getColor());
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

    @Override
    public float getWeaponBaseAccuracy(ItemStack weapon,boolean zoomed)
    {
        return 0.3f + getHeat(weapon) / (zoomed ? 30f : 10f);
    }
}
