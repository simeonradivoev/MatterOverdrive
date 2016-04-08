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

import matteroverdrive.Reference;
import matteroverdrive.api.weapon.IWeaponModule;
import matteroverdrive.api.weapon.WeaponShot;
import matteroverdrive.client.sound.MOPositionedSound;
import matteroverdrive.client.sound.WeaponSound;
import matteroverdrive.entity.weapon.PlasmaBolt;
import matteroverdrive.init.MatterOverdriveSounds;
import matteroverdrive.items.weapon.module.WeaponModuleBarrel;
import matteroverdrive.proxy.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
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
    private static final int MAX_USE_TIME = 72000;
    private static final int ENERGY_PER_SHOT = 1024;
    public static final int RANGE = 32;

    public PhaserRifle(String name) {
        super(name,32000,128,128,RANGE);
        this.bFull3D = true;
        this.leftClickFire = true;
    }

    /*@Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegistry)
    {

    }*/

    @Override
    public int getMaxItemUseDuration(ItemStack item)
    {
        return MAX_USE_TIME;
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
            case Reference.MODULE_SIGHTS:
                return new Vector2f(150,35);
            default:
                return new Vector2f(205,80 + ((slot - Reference.MODULE_OTHER) * 22));
        }
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
        if (module != null && module.getItem() instanceof IWeaponModule && ((IWeaponModule) module.getItem()).getSlot(module) == Reference.MODULE_BARREL)
        {
            return module.getItemDamage() != WeaponModuleBarrel.HEAL_BARREL_ID;
        }
        return true;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand)
    {
        if (hand == EnumHand.OFF_HAND) return ActionResult.newResult(EnumActionResult.PASS,itemStackIn);
        playerIn.setActiveHand(hand);
        if(worldIn.isRemote)
        {
            //for(int i = 0; i < 3; i++)
                //Minecraft.getMinecraft().entityRenderer.itemRenderer.updateEquippedItem();
        }
        return ActionResult.newResult(EnumActionResult.SUCCESS,itemStackIn);
    }

    @Override
    public boolean isAlwaysEquipped(ItemStack weapon) {
        return true;
    }

    @Override
    public int getBaseShootCooldown(ItemStack weapon) {
        return 11;
    }

    @Override
    public float getBaseZoom(ItemStack weapon, EntityLivingBase shooter) {
        return 0.2f;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean isWeaponZoomed(EntityLivingBase entityPlayer,ItemStack weapon) {
        return Mouse.isButtonDown(1) && entityPlayer.isHandActive() && entityPlayer.getActiveHand() == EnumHand.MAIN_HAND;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public WeaponSound getFireSound(ItemStack weapon, EntityLivingBase entity) {
        return null;
    }


    @Override
    @SideOnly(Side.CLIENT)
    public void onShooterClientUpdate(ItemStack itemStack, World world, EntityPlayer entityPlayer, boolean sendServerTick)
    {
        if (Mouse.isButtonDown(0) && hasShootDelayPassed()) {
            if (canFire(itemStack,world,entityPlayer))
            {
                itemStack.getTagCompound().setLong("LastShot", world.getTotalWorldTime());

                Vec3d dir = entityPlayer.getLook(1);
                Vec3d pos = getFirePosition(entityPlayer, dir, isWeaponZoomed(entityPlayer,itemStack));
                WeaponShot shot = createClientShot(itemStack,entityPlayer, isWeaponZoomed(entityPlayer,itemStack));
                onClientShot(itemStack, entityPlayer, pos, dir, shot);
                addShootDelay(itemStack);
                sendShootTickToServer(world,shot,dir,pos);
                if (Minecraft.getMinecraft().gameSettings.thirdPersonView == 0) {
                    if (isWeaponZoomed(entityPlayer, itemStack)) {
                        ClientProxy.instance().getClientWeaponHandler().setRecoil(0.5f + getAccuracy(itemStack, entityPlayer, true),1,0.05f);
                        ClientProxy.instance().getClientWeaponHandler().setCameraRecoil(0.5f + getAccuracy(itemStack, entityPlayer, true) * 0.1f,1);
                        //entityPlayer.hurtTime = 6 + (int) ((getHeat(itemStack) / getMaxHeat(itemStack)) * 8);
                        //entityPlayer.maxHurtTime = 15;
                    } else {
                        ClientProxy.instance().getClientWeaponHandler().setRecoil(2 + getAccuracy(itemStack, entityPlayer, true) * 2,1,0.07f);
                        ClientProxy.instance().getClientWeaponHandler().setCameraRecoil(0.5f + getAccuracy(itemStack, entityPlayer, true) * 0.5f,1);
                        //entityPlayer.hurtTime = 10 + (int) ((getHeat(itemStack) / getMaxHeat(itemStack)) * 8);
                        //entityPlayer.maxHurtTime = 25;
                    }
                }
                return;
            }else if (needsRecharge(itemStack))
            {
                chargeFromEnergyPack(itemStack,entityPlayer);
            }
        }

        super.onShooterClientUpdate(itemStack,world,entityPlayer,sendServerTick);
    }

    @SideOnly(Side.CLIENT)
    private Vec3d getFirePosition(EntityPlayer entityPlayer, Vec3d dir, boolean isAiming)
    {
        Vec3d pos = entityPlayer.getPositionEyes(1);
        if (!isAiming) {
            pos = pos.subtract((double)(MathHelper.cos(entityPlayer.rotationYaw / 180.0F * (float) Math.PI) * 0.16F),0,(double)(MathHelper.sin(entityPlayer.rotationYaw / 180.0F * (float) Math.PI) * 0.16F));
        }
        pos = pos.addVector(dir.xCoord,dir.yCoord,dir.zCoord);
        return pos;
    }

    @Override
    public boolean onServerFire(ItemStack weapon, EntityLivingBase shooter, WeaponShot shot, Vec3d position, Vec3d dir, int delay)
    {
        DrainEnergy(weapon, getShootCooldown(weapon), false);
        float newHeat =  (getHeat(weapon)+4) * 2.2f;
        setHeat(weapon, newHeat);
        manageOverheat(weapon, shooter.worldObj, shooter);
        PlasmaBolt fire = spawnProjectile(weapon,shooter,position,dir,shot);
        fire.simulateDelay(delay);
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onClientShot(ItemStack weapon, EntityLivingBase shooter, Vec3d position, Vec3d dir, WeaponShot shot)
    {
        //ClientProxy.weaponHandler.addShootDelay(this);
        MOPositionedSound sound = new MOPositionedSound(MatterOverdriveSounds.weaponsPhaserRifleShot,SoundCategory.PLAYERS,0.8f + itemRand.nextFloat()*0.2f, 0.9f + itemRand.nextFloat() * 0.2f);
        sound.setPosition((float) position.xCoord,(float)position.yCoord,(float)position.zCoord);
        Minecraft.getMinecraft().getSoundHandler().playSound(sound);
        spawnProjectile(weapon,shooter,position,dir,shot);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onProjectileHit(RayTraceResult hit, ItemStack weapon, World world, float amount)
    {

    }

    @Override
    public PlasmaBolt getDefaultProjectile(ItemStack weapon, EntityLivingBase shooter, Vec3d position, Vec3d dir, WeaponShot shot)
    {
        PlasmaBolt bolt = super.getDefaultProjectile(weapon,shooter,position,dir,shot);
        bolt.setKnockBack(0.1f);
        return bolt;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack itemStack)
    {
        return EnumAction.BOW;
    }

    @Override
    protected void addCustomDetails(ItemStack weapon, EntityPlayer player, List infos)
    {

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
        return 8;
    }

    @Override
    public boolean canFire(ItemStack weapon,World world,EntityLivingBase shooter)
    {
        return DrainEnergy(weapon, getShootCooldown(weapon), true) && !isOverheated(weapon);
    }

    @Override
    public float getShotSpeed(ItemStack weapon, EntityLivingBase shooter) {
        return 4;
    }

    @Override
    public float getWeaponBaseAccuracy(ItemStack weapon,boolean zoomed)
    {
        return 1f + getHeat(weapon) / (zoomed ? 30f : 10f);
    }
}
