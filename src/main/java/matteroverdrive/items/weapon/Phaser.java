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

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import matteroverdrive.Reference;
import matteroverdrive.api.weapon.IWeapon;
import matteroverdrive.api.weapon.WeaponShot;
import matteroverdrive.client.sound.WeaponSound;
import matteroverdrive.handler.SoundHandler;
import matteroverdrive.init.MatterOverdriveItems;
import matteroverdrive.util.MOPhysicsHelper;
import matteroverdrive.util.WeaponHelper;
import matteroverdrive.util.animation.MOEasing;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.util.vector.Vector2f;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Phaser extends EnergyWeapon implements IWeapon{

	private static final double ENERGY_MULTIPLY = 2.1;
    private static final int MAX_USE_TIME = 60;
    private static final int MAX_HEAT = 80;
    public static final int MAX_LEVEL = 6;
    private static final int KILL_MODE_LEVEL = 3;
    private static final float KILL_DAMAGE_MULTIPLY = 2f;
    private static final int STUN_SLEEP_MULTIPLY = 5;
    public static final int RANGE = 18;

    Map<EntityPlayer,WeaponSound> soundMap;

	public Phaser(String name) {
		super(name,32000,128,128,RANGE);
		this.bFull3D = true;
        soundMap = new HashMap<>();
	}

    @SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iconRegistry)
    {

    }

    @Override
    public int getBaseEnergyUse(ItemStack item)
    {
        this.TagCompountCheck(item);
        byte level = getPowerLevel(item);
        return (int)Math.pow(ENERGY_MULTIPLY, level + 1);
    }

    @Override
    protected int getBaseMaxHeat(ItemStack item) {
        return MAX_HEAT;
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected void addCustomDetails(ItemStack weapon,EntityPlayer player,List infos)
    {
        infos.add(EnumChatFormatting.BLUE + "Stun: " + (GetSleepTime(weapon) / 20f) + "s");
    }

	/**
     * How long it takes to use or consume an item
     */
	@Override
    public int getMaxItemUseDuration(ItemStack item)
    {
        return MAX_USE_TIME;
    }

	public static boolean isKillMode(ItemStack item)
	{
		if(!item.hasTagCompound())
		{
			item.setTagCompound(new NBTTagCompound());
		}
		return item.getTagCompound().getByte("power") >= KILL_MODE_LEVEL;
	}

	private void ManageShooting(ItemStack item, World w, EntityPlayer player,int useCount)
	{
        if (w.isRemote)
            return;

        Vec3 dir = getPlayerLook(player,item);
        MovingObjectPosition hit = MOPhysicsHelper.rayTrace(player, w, getRange(item), 0, Vec3.createVectorHelper(0, player.getEyeHeight(), 0), false, true,dir);
        if (hit != null)
        {
            Vec3 hitVector = hit.hitVec;

            if (hit.entityHit != null && hit.entityHit instanceof EntityLivingBase)
            {

				if (hit.entityHit instanceof EntityPlayer && FMLCommonHandler.instance().getSide() == Side.SERVER && !MinecraftServer.getServer().isPVPEnabled()) return;

                DamageSource damageInfo = getDamageSource(item,player);
                float damage = getWeaponScaledDamage(item,player);

                EntityLivingBase el = (EntityLivingBase) hit.entityHit;
                double motionX = el.motionX;
                double motionY = el.motionY;
                double moutionZ = el.motionZ;
                if (damage > 0)
                {
                    el.attackEntityFrom(damageInfo, damage);
                    el.motionX = motionX;
                    el.motionY = motionY;
                    el.motionZ = moutionZ;
                }

                el.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, GetSleepTime(item), 100));
                //el.addPotionEffect(new PotionEffect(Potion.moveSpeed.id, GetSleepTime(item), -10));
                el.addPotionEffect(new PotionEffect(Potion.digSlowdown.id, GetSleepTime(item), 100));
                el.addPotionEffect(new PotionEffect(Potion.jump.id, GetSleepTime(item), -10));

                if (WeaponHelper.hasStat(Reference.WS_FIRE_DAMAGE,item) && isKillMode(item))
                {
                    el.setFire(Math.round(WeaponHelper.modifyStat(Reference.WS_FIRE_DAMAGE,item,0) * item.getTagCompound().getByte("power")));
                }
                else if (WeaponHelper.hasStat(Reference.WS_HEAL,item))
                {
                    el.heal((WeaponHelper.modifyStat(Reference.WS_HEAL,item,0) * item.getTagCompound().getByte("power")));
                }
            }else
            {
                if (WeaponHelper.hasStat(Reference.WS_FIRE_DAMAGE,item))
                {
                    int x = hit.blockX;
                    int y = hit.blockY;
                    int z = hit.blockZ;

                    if (player.worldObj.getBlock(x,y,z).isFlammable(player.worldObj,x,y,z, ForgeDirection.getOrientation(hit.sideHit))) {
                        if (hit.sideHit == 0) {
                            --y;
                        }

                        if (hit.sideHit == 1) {
                            ++y;
                        }

                        if (hit.sideHit == 2) {
                            --z;
                        }

                        if (hit.sideHit == 3) {
                            ++z;
                        }

                        if (hit.sideHit == 4) {
                            --x;
                        }

                        if (hit.sideHit == 5) {
                            ++x;
                        }

                        if (player.canPlayerEdit(x, y, z, hit.sideHit, item)) {
                            if (player.worldObj.isAirBlock(x, y, z)) {
                                //player.worldObj.playSoundEffect((double) x + 0.5D, (double) y + 0.5D, (double) z + 0.5D, "fire.ignite", 1.0F, itemRand.nextFloat() * 0.4F + 0.8F);
                                player.worldObj.setBlock(x, y, z, Blocks.fire);
                            }
                        }
                    }
                }
            }

            if (isKillMode(item) && useCount % getShootCooldown(item) == getShootCooldown(item)/2)
            {
                if (WeaponHelper.hasStat(Reference.WS_EXPLOSION_DAMAGE, item))
                {
                    w.createExplosion(player, hitVector.xCoord, hitVector.yCoord, hitVector.zCoord, (float) WeaponHelper.modifyStat(Reference.WS_EXPLOSION_DAMAGE, item,0) * item.getTagCompound().getByte("power") - (MAX_LEVEL/2), true);
                }
            }
        }
	}

    public Vec3 getPlayerLook(EntityPlayer player,ItemStack weapon)
    {
        Vec3 dir = player.getLookVec();
        Vec3 rot = getBeamRotation(weapon,player);
        dir.rotateAroundX((float)rot.xCoord);
        dir.rotateAroundY((float) rot.yCoord);
        dir.rotateAroundZ((float)rot.zCoord);
        return dir;
    }

    public Vec3 getBeamRotation(ItemStack weapon,EntityPlayer entityPlayer)
    {
        double rotationY = (float)Math.toRadians(5) * MOEasing.Quart.easeIn(getAccuracy(weapon,entityPlayer,false),0,1,1);
        return Vec3.createVectorHelper(0,rotationY,0);
    }

    @Override
    public float getWeaponBaseAccuracy(ItemStack weapon,boolean zoomed)
    {
        return getHeat(weapon) / getMaxHeat(weapon);
    }

	@Override
	public ItemStack onItemRightClick(ItemStack item, World world, EntityPlayer player)
	{
		this.TagCompountCheck(item);

		if(player.isSneaking())
		{
			SwitchModes(world,player,item);
		}
        else
        {
            if (canFire(item, world,player))
            {
                player.setItemInUse(item, getMaxItemUseDuration(item));
            }
            if (needsRecharge(item))
            {
                chargeFromEnergyPack(item,player);
            }
            return item;
		}

		return item;
	}

    @Override
    public void onUsingTick(ItemStack itemStack, EntityPlayer player, int count)
    {
        if (canFire(itemStack,player.worldObj,player))
        {
            DrainEnergy(itemStack,1,false);
            int powerLevelMultiply = (getPowerLevel(itemStack)+1) / MAX_LEVEL;
            float newHeat =  (getHeat(itemStack)+1)*(1.1f + (0.05f*powerLevelMultiply));
            setHeat(itemStack, newHeat);
            ManageShooting(itemStack, player.worldObj, player,count);
            manageOverheat(itemStack, player.worldObj, player);
        }
        else {
            player.stopUsingItem();
        }
    }

	private void SwitchModes(World world,EntityPlayer player,ItemStack item)
	{
		this.TagCompountCheck(item);
		SoundHandler.PlaySoundAt(world, "phaser_switch_mode", player);
        byte level = getPowerLevel(item);
        level++;
        if(level >= MAX_LEVEL)
        {
            level = 0;
        }
		setPowerLevel(item,level);
	}

    @Override
	public float getWeaponBaseDamage(ItemStack item)
	{
        float damage = 0;
        this.TagCompountCheck(item);
        byte level = getPowerLevel(item);
        if(level >= KILL_MODE_LEVEL)
        {
            damage = (float)Math.pow(KILL_DAMAGE_MULTIPLY,level - (KILL_MODE_LEVEL-1));
        }
        return damage;
	}

    @Override
    public boolean canFire(ItemStack itemStack, World world,EntityLivingBase shooter) {
        return !isOverheated(itemStack) && DrainEnergy(itemStack,1,true);
    }

    @Override
    public float getShotSpeed(ItemStack weapon, EntityLivingBase shooter) {
        return 0;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onClientShot(ItemStack weapon, EntityLivingBase shooter, Vec3 position, Vec3 dir,WeaponShot shot)
    {

    }

    private int GetSleepTime(ItemStack item)
    {
        this.TagCompountCheck(item);
        byte level = getPowerLevel(item);
        if(level < KILL_MODE_LEVEL)
        {
            return (int)(Math.pow(level+1,STUN_SLEEP_MULTIPLY) * sleepTimeMultipy(item));
        }
        return 0;
    }

    private double sleepTimeMultipy(ItemStack phaser)
    {
        return WeaponHelper.modifyStat(Reference.WS_DAMAGE,phaser,1);
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
                return new Vector2f(200,45);
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
                return new Vector2f(165,90);
            case Reference.MODULE_COLOR:
                return new Vector2f(100,80);
            case Reference.MODULE_BARREL:
                return new Vector2f(85,90);
        }
        return getSlotPosition(slot,weapon);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onProjectileHit(MovingObjectPosition hit, ItemStack weapon, World world,float amount)
    {
        Block b = world.getBlock(hit.blockX,hit.blockY,hit.blockZ);
        if (hit.entityHit != null && hit.entityHit instanceof EntityLivingBase)
        {
            if (WeaponHelper.hasStat(Reference.WS_HEAL,weapon))
            {
                world.spawnParticle("happyVillager",hit.hitVec.xCoord,hit.hitVec.yCoord,hit.hitVec.zCoord,0,0,0);
            }
            else if (WeaponHelper.hasStat(Reference.WS_FIRE_DAMAGE,weapon) && MatterOverdriveItems.phaser.isKillMode(weapon))
            {
                world.spawnParticle("flame",hit.hitVec.xCoord,hit.hitVec.yCoord,hit.hitVec.zCoord,0,0,0);
            }
            else
            {
                if (MatterOverdriveItems.phaser.isKillMode(weapon)) {
                    world.spawnParticle("reddust", hit.hitVec.xCoord, hit.hitVec.yCoord, hit.hitVec.zCoord, 0, 0, 0);
                }else
                {
                    world.spawnParticle("magicCrit", hit.hitVec.xCoord, hit.hitVec.yCoord, hit.hitVec.zCoord, 0, 0, 0);
                }
            }

        }
        else if(b != null && b != Blocks.air)
        {
            if (WeaponHelper.hasStat(Reference.WS_FIRE_DAMAGE,weapon) && MatterOverdriveItems.phaser.isKillMode(weapon))
            {
                world.spawnParticle("flame",hit.hitVec.xCoord,hit.hitVec.yCoord,hit.hitVec.zCoord,0,0,0);
            }

            world.spawnParticle("smoke",hit.hitVec.xCoord,hit.hitVec.yCoord,hit.hitVec.zCoord,0,0,0);
        }
    }

    @Override
    public boolean supportsModule(int slot,ItemStack weapon)
    {
        return slot != Reference.MODULE_SIGHTS;
    }

    @Override
    public boolean supportsModule(ItemStack weapon, ItemStack module)
    {
        if (module != null)
        {
            return module.getItem() == MatterOverdriveItems.weapon_module_barrel || module.getItem() == MatterOverdriveItems.weapon_module_color;
        }
        return false;
    }

    @Override
    public boolean onServerFire(ItemStack weapon, EntityLivingBase shooter, WeaponShot shot,Vec3 position,Vec3 dir,int delay)
    {
        return false;
    }

    @Override
    public boolean isAlwaysEquipped(ItemStack weapon) {
        return false;
    }

    @Override
    public int getBaseShootCooldown(ItemStack weapon) {
        return 10;
    }

    @Override
    public float getBaseZoom(ItemStack weapon, EntityLivingBase shooter)
    {
        return 0;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean isWeaponZoomed(EntityPlayer entityPlayer,ItemStack weapon) {
        return false;
    }

    @Override
    public WeaponSound getFireSound(ItemStack weapon,EntityLivingBase entity)
    {
        //return Reference.MOD_ID + ":" +"phaser_beam_1";
        return new WeaponSound(new ResourceLocation(Reference.MOD_ID + ":" +"phaser_beam_1"),(float)entity.posX,(float)entity.posY,(float)entity.posZ,itemRand.nextFloat() * 0.05f + 0.2f,1);
    }

    public byte getPowerLevel(ItemStack weapon)
    {
        if (weapon.hasTagCompound())
        {
            return weapon.getTagCompound().getByte("power");
        }
        return 0;
    }

    public void setPowerLevel(ItemStack weapon,byte level)
    {
        if (weapon.hasTagCompound())
        {
            weapon.getTagCompound().setByte("power",level);
        }
    }
}
