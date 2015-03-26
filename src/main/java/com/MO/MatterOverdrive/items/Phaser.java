package com.MO.MatterOverdrive.items;

import java.util.*;

import com.MO.MatterOverdrive.util.MOEnergyHelper;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.*;
import net.minecraft.world.World;

import com.MO.MatterOverdrive.handler.SoundHandler;
import com.MO.MatterOverdrive.items.includes.MOItemEnergyContainer;

public class Phaser extends MOItemEnergyContainer {

	private static final float ENERGY_MULTIPLY = 2.5f;
    private static final int COOLDOWN = 10;
    private static final int MAX_LEVEL = 6;
    private static final int KILL_MODE_LEVEL = 3;
    private static final float KILL_DAMAGE_MULTIPLY = 2.5f;
    private static final int STUN_SLEEP_MULTIPLY = 5;
	
	public Phaser(String name) {
		super(name,32000,128,128);
		this.bFull3D = true;
	}
	
	public void registerIcons(IIconRegister iconRegistry)
    {

    }

    @Override
    public void addInformation(ItemStack itemstack, EntityPlayer player, List infos, boolean moreInfo)
    {
        super.addInformation(itemstack,player,infos,moreInfo);

        infos.add(EnumChatFormatting.DARK_RED + "Power Use: " + MOEnergyHelper.formatEnergy(GetEneryUse(itemstack)));
        infos.add(EnumChatFormatting.DARK_GREEN + "Damage: " + GetPhaserDamage(itemstack));
    }

    @Override
    public int getDamage(ItemStack stack)
    {
        if(!stack.hasTagCompound())
        {
            stack.setTagCompound(new NBTTagCompound());
        }
        return MAX_LEVEL - 1 - stack.getTagCompound().getByte("power");
    }

    @Override
    public int getMaxDamage(ItemStack stack)
    {
        return MAX_LEVEL;
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack)
    {
        return true;
    }

    @Override
    public int getDisplayDamage(ItemStack stack)
    {
        return this.getDamage(stack);
    }

	@Override
	public int getItemStackLimit(ItemStack item) 
	{
		return 1;
	}
	
	@Override
	public void onCreated(ItemStack itemStack, World world, EntityPlayer player) 
	{
		super.onCreated(itemStack, world, player);
	}
	
	/**
     * How long it takes to use or consume an item
     */
	@Override
    public int getMaxItemUseDuration(ItemStack item)
    {
        return 100;
    }
	
	public static boolean isKillMode(ItemStack item)
	{
		if(!item.hasTagCompound())
		{
			item.setTagCompound(new NBTTagCompound());
		}
		return item.getTagCompound().getByte("power") >= KILL_MODE_LEVEL;
	}
	
	@Override
	public void onUpdate(ItemStack item, World w, Entity player, int slot, boolean onHotbar)
	{
        int cooldown = item.getTagCompound().getInteger("cooldown");

		if(cooldown > 0)
        {
            cooldown--;
            item.getTagCompound().setInteger("cooldown", cooldown);
        }
	}
	
	private void ManageShooting(ItemStack item, World w, EntityPlayer player)
	{
		if(!w.isRemote)
		{
            if(!inCooldown(item))
            {
                Shoot(item, w, player);
            }
		}
	}
	
	private void Shoot(ItemStack item, World w, EntityPlayer player)
	{
		float penetration = 1F;
		float f = 1.0F;
		float f1 = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * f;
		float f2 = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * f;
		double d0 = player.prevPosX + (player.posX - player.prevPosX) * f;
		double d1 = player.prevPosY + (player.posY - player.prevPosY) * f + 1.62D - player.yOffset;
		double d2 = player.prevPosZ + (player.posZ - player.prevPosZ) * f;
		Vec3 vec3 = Vec3.createVectorHelper(d0, d1, d2);
		float f3 = MathHelper.cos(-f2 * 0.017453292F - (float) Math.PI);
		float f4 = MathHelper.sin(-f2 * 0.017453292F - (float) Math.PI);
		float f5 = -MathHelper.cos(-f1 * 0.017453292F);
		float f6 = MathHelper.sin(-f1 * 0.017453292F);
		float f7 = f4 * f5;
		float f8 = f3 * f5;
		double d3 = 32.0D;

		Vec3 vec31 = vec3.addVector(f7 * d3, f6 * d3, f8 * d3);
		Vec3 direction = Vec3.createVectorHelper(f7 * d3, f6 * d3, f8 * d3);
		direction.normalize();
		
		if(this.getEnergyStored(item) > 0)
		{
			this.shoot(item,penetration, w, player, vec3, vec31, direction, d0, d1, d2);
		}
	}

	@Override
	public ItemStack onItemRightClick(ItemStack item, World w, EntityPlayer p) 
	{
		this.TagCompountCheck(item);
		
		if(p.isSneaking())
		{
			SwitchModes(w,p,item);
		}else
		{
			ManageShooting(item,w,p);
		}
		
		return item;
	}
	
	private void SwitchModes(World world,EntityPlayer player,ItemStack item)
	{
		this.TagCompountCheck(item);
		SoundHandler.PlaySoundAt(world, "phaser_switch_mode", player);
        byte level = item.getTagCompound().getByte("power");
        level++;
        if(level >= MAX_LEVEL)
        {
            level = 0;
        }
		item.getTagCompound().setByte("power",level);
	}
	
	
	private void DrainEnergy(ItemStack item)
	{
		super.extractEnergy(item, GetEneryUse(item), false);
	}

    @Override
    public int extractEnergy(ItemStack container, int maxExtract, boolean simulate) {

        return 0;
    }

    private int GetEneryUse(ItemStack item)
    {
        this.TagCompountCheck(item);
        byte level = item.getTagCompound().getByte("power");
        return (int)Math.floor(Math.pow(ENERGY_MULTIPLY,level + 1));
    }
	
	private float GetPhaserDamage(ItemStack item)
	{
        this.TagCompountCheck(item);
        byte level = item.getTagCompound().getByte("power");
        if(level >= KILL_MODE_LEVEL)
        {
            return (float)Math.pow(KILL_DAMAGE_MULTIPLY,level - (KILL_MODE_LEVEL-1));
        }
        return 0;
	}

    private int GetSleepTime(ItemStack item)
    {
        this.TagCompountCheck(item);
        byte level = item.getTagCompound().getByte("power");
        if(level < KILL_MODE_LEVEL)
        {
            return (int)Math.pow(level+1,STUN_SLEEP_MULTIPLY);
        }
        return 0;
    }

    private boolean inCooldown(ItemStack item)
    {
        this.TagCompountCheck(item);
        return item.getTagCompound().getInteger("cooldown") > 0;
    }

    private void setCooldown(ItemStack item)
    {
        item.getTagCompound().setInteger("cooldown", COOLDOWN);
    }

	private void shoot(ItemStack item,float penetration, World w, EntityPlayer p, Vec3 vec3,
			Vec3 vec31, Vec3 direction, double d0, double d1, double d2) 
	{
        Vec3 vec = Vec3.createVectorHelper(d0, d1, d2);
        AxisAlignedBB bb = AxisAlignedBB.getBoundingBox(
                Math.min(vec3.xCoord, vec31.xCoord),
                Math.min(vec3.yCoord, vec31.yCoord),
                Math.min(vec3.zCoord, vec31.zCoord),
                Math.max(vec3.xCoord, vec31.xCoord),
                Math.max(vec3.yCoord, vec31.yCoord),
                Math.max(vec3.zCoord, vec31.zCoord)).expand(16, 16, 16);


        Entity entity = null;
        List list = w.getEntitiesWithinAABBExcludingEntity(p, bb);
        double closest = 9999999.0D;

        for (int l = 0; l < list.size(); ++l)
        {
            Entity entity1 = (Entity) list.get(l);
            if (!entity1.isDead && entity1 != p
                    && !(entity1 instanceof EntityItem)) {
                if (entity1.isEntityAlive()) {
                    // prevent killing / flying of mounts.
                    if (entity1.riddenByEntity == p)
                        continue;

                    float f1 = 0.3F;

                    AxisAlignedBB boundingBox = entity1.boundingBox.expand(
                            f1, f1, f1);
                    MovingObjectPosition movingObjectPosition = boundingBox
                            .calculateIntercept(vec3, vec31);

                    if (movingObjectPosition != null) {
                        double nd = vec3
                                .squareDistanceTo(movingObjectPosition.hitVec);

                        if (nd < closest) {
                            entity = entity1;
                            closest = nd;
                        }
                    }
                }
            }
        }

        DamageSource damageInfo = DamageSource.causePlayerDamage(p);
        damageInfo.damageType = "phaser";
        float damage = GetPhaserDamage(item);

        MovingObjectPosition pos = w.rayTraceBlocks(vec3, vec31, true);
        if (entity != null && pos != null && pos.hitVec.squareDistanceTo(vec) > closest) {
            pos = new MovingObjectPosition(entity);
        }

        else if (entity != null && pos == null)
        {
            pos = new MovingObjectPosition(entity);
        }

        if(pos != null && pos.entityHit != null)
        {
            if (pos.entityHit instanceof EntityLivingBase)
            {
                EntityLivingBase el = (EntityLivingBase) pos.entityHit;
                if (el.attackEntityFrom(damageInfo, damage))
                {
                    DrainEnergy(item);
                    setCooldown(item);
                    SoundHandler.PlaySoundAt(w, "phaser_fire_burst_4", p,0.4f,0.6f);
                    el.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, GetSleepTime(item), 10));
                }
            }
        }

		/*boolean hasDestroyedSomething = true;
		
		while (penetration > 0 && hasDestroyedSomething) {
			AxisAlignedBB bb = AxisAlignedBB.getBoundingBox(
					Math.min(vec3.xCoord, vec31.xCoord),
					Math.min(vec3.yCoord, vec31.yCoord),
					Math.min(vec3.zCoord, vec31.zCoord),
					Math.max(vec3.xCoord, vec31.xCoord),
					Math.max(vec3.yCoord, vec31.yCoord),
					Math.max(vec3.zCoord, vec31.zCoord)).expand(16, 16, 16);

			Entity entity = null;
			List list = w.getEntitiesWithinAABBExcludingEntity(p, bb);
			double closest = 9999999.0D;
			int l;

			for (l = 0; l < list.size(); ++l)
            {
				Entity entity1 = (Entity) list.get(l);
				if (!entity1.isDead && entity1 != p
						&& !(entity1 instanceof EntityItem)) {
					if (entity1.isEntityAlive()) {
						// prevent killing / flying of mounts.
						if (entity1.riddenByEntity == p)
							continue;

						float f1 = 0.3F;

						AxisAlignedBB boundingBox = entity1.boundingBox.expand(
								f1, f1, f1);
						MovingObjectPosition movingObjectPosition = boundingBox
								.calculateIntercept(vec3, vec31);

						if (movingObjectPosition != null) {
							double nd = vec3
									.squareDistanceTo(movingObjectPosition.hitVec);

							if (nd < closest) {
								entity = entity1;
								closest = nd;
							}
						}
					}
				}
			}

			Vec3 vec = Vec3.createVectorHelper(d0, d1, d2);
			MovingObjectPosition pos = w.rayTraceBlocks(vec3, vec31, true);
			
			if (entity != null && pos != null
					&& pos.hitVec.squareDistanceTo(vec) > closest) {
				pos = new MovingObjectPosition(entity);
			} else if (entity != null && pos == null) {
				pos = new MovingObjectPosition(entity);
			}


		}*/
	}
}
