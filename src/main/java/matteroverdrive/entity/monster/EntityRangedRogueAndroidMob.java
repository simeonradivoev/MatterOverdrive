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

package matteroverdrive.entity.monster;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.api.entity.IRangedEnergyWeaponAttackMob;
import matteroverdrive.api.weapon.WeaponShot;
import matteroverdrive.entity.ai.AndroidTargetSelector;
import matteroverdrive.entity.ai.EntityAIMoveAlongPath;
import matteroverdrive.entity.ai.EntityAIPhaserBoltAttack;
import matteroverdrive.entity.ai.EntityAIRangedRunFromMelee;
import matteroverdrive.init.MatterOverdriveItems;
import matteroverdrive.items.android.RougeAndroidParts;
import matteroverdrive.items.weapon.EnergyWeapon;
import matteroverdrive.network.packet.bi.PacketFirePlasmaShot;
import matteroverdrive.util.AndroidPartsFactory;
import matteroverdrive.util.WeaponFactory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Simeon on 11/15/2015.
 */
public class EntityRangedRogueAndroidMob extends EntityRougeAndroidMob implements IRangedEnergyWeaponAttackMob
{
	public static boolean DROP_NORMAL_WEAPONS = true;
	public static boolean DROP_LEGENDARY_WEAPONS = true;
	public static boolean UNLIMITED_WEAPON_ENERGY = true;
	private final EntityAIPhaserBoltAttack aiBoltAttack = new EntityAIPhaserBoltAttack(this, 1.0D, 60, 15.0F);
	private final EntityAIRangedRunFromMelee aiRangedRunFromMelee = new EntityAIRangedRunFromMelee(this, 1.0D);

	public EntityRangedRogueAndroidMob(World world)
	{
		super(world);
		init(world);
	}

	public EntityRangedRogueAndroidMob(World world, int level, boolean legendary)
	{
		super(world, level, legendary);
		init(world);
	}

	protected void init(World world)
	{
		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(2, aiRangedRunFromMelee);
		this.tasks.addTask(3, aiBoltAttack);
		this.tasks.addTask(4, new EntityAIMoveAlongPath(this, 1.0D));
		this.tasks.addTask(5, new EntityAIWander(this, 1.0D));
		this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityLivingBase.class, 8.0F));
		this.tasks.addTask(6, new EntityAILookIdle(this));

		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityLivingBase.class, 0, true, true, new AndroidTargetSelector(this)));

		if (world != null && !world.isRemote)
		{
			this.setCombatTask();
		}
	}

	@Override
	protected void dropEquipment(boolean recentlyHit, int lootingLevel)
	{
		if (this.recentlyHit > 0 && DROP_NORMAL_WEAPONS)
		{
			int j = this.rand.nextInt(400) - lootingLevel;

			if (j < 5 || getIsLegendary())
			{
				this.entityDropItem(getItemStackFromSlot(EntityEquipmentSlot.MAINHAND).copy(), 0);
			}
		}
	}

	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(24);
	}

	@Override
	protected void dropFewItems(boolean recentlyHit, int lootingLevel)
	{
		if (!hasTeam() || recentlyHit)
		{
			int j;
			int k;

			j = this.rand.nextInt(2 + lootingLevel);

			for (k = 0; k < j; ++k)
			{
				this.dropItem(MatterOverdrive.items.energyPack, 1);
			}

			float lootingModifier = (Math.min(lootingLevel, 10) / 10f);
			if (rand.nextFloat() < (0.15f + lootingModifier) || getIsLegendary())
			{
				ItemStack part = MatterOverdrive.androidPartsFactory.generateRandomDecoratedPart(new AndroidPartsFactory.AndroidPartFactoryContext(getAndroidLevel(), this, getIsLegendary()));
				if (part.getItem() instanceof RougeAndroidParts)
				{
					part.setTagCompound(new NBTTagCompound());
					part.getTagCompound().setByte("Type", (byte)1);
				}
				this.entityDropItem(part, 0.0F);
			}
		}
	}

	@Override
	public void addRandomArmor()
	{
		super.addRandomArmor();
		int androidLevel = getAndroidLevel();
		ItemStack gun = MatterOverdrive.weaponFactory.getRandomDecoratedEnergyWeapon(new WeaponFactory.WeaponGenerationContext(androidLevel, this, getIsLegendary()));
		setItemStackToSlot(EntityEquipmentSlot.MAINHAND, gun);
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(((EnergyWeapon)gun.getItem()).getRange(gun) - 2);
	}

	public void setCombatTask()
	{
		ItemStack itemstack = this.getHeldItem(EnumHand.MAIN_HAND);

		if (itemstack != null && itemstack.getItem() instanceof EnergyWeapon)
		{
			this.aiBoltAttack.setMaxChaseDistance(((EnergyWeapon)itemstack.getItem()).getRange(itemstack) - 2);
			if (itemstack.getItem() == MatterOverdrive.items.ionSniper)
			{
				aiRangedRunFromMelee.setMinDistance(16f);
			}
			else if (itemstack.getItem() != MatterOverdrive.items.plasmaShotgun)
			{
				aiRangedRunFromMelee.setMinDistance(3f);
			}
		}
	}

	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata)
	{
		livingdata = super.onInitialSpawn(difficulty, livingdata);
		this.addRandomArmor();
		this.setEnchantmentBasedOnDifficulty(difficulty);
		return livingdata;
	}

	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase target, Vec3d lastSeenPosition, boolean canSee)
	{
		ItemStack weapon = this.getHeldItem(EnumHand.MAIN_HAND);
		if (!worldObj.isRemote)
		{
			if (lastSeenPosition == null)
			{
				lastSeenPosition = target.getPositionVector();
			}
			if (weapon.getItem() instanceof EnergyWeapon)
			{
				EnergyWeapon energyWeapon = (EnergyWeapon)weapon.getItem();
				//magic number from MC
				Vec3d pos = new Vec3d(this.posX, this.posY + getEyeHeight(), this.posZ);
				Vec3d dir = lastSeenPosition.subtract(this.getPositionVector());
				WeaponShot shot = energyWeapon.createServerShot(weapon, this, true);
				float difficulty = MathHelper.clamp_float((0.6f / 3f) * worldObj.getDifficulty().getDifficultyId(), 0, 0.6f) + getAndroidLevel() * (0.4f / 3f) + (getIsLegendary() ? 0.3f : 0);
				shot.setDamage(shot.getDamage() * difficulty);
				difficulty = (3 - worldObj.getDifficulty().getDifficultyId()) * 4f;
				shot.setAccuracy(shot.getAccuracy() + difficulty);
				energyWeapon.onServerFire(weapon, this, shot, pos, dir, 0);
				energyWeapon.setHeat(weapon, 0);
				if (UNLIMITED_WEAPON_ENERGY)
				{
					energyWeapon.rechargeFully(weapon);
				}
				MatterOverdrive.packetPipeline.sendToAllAround(new PacketFirePlasmaShot(this.getEntityId(), pos, dir, shot), worldObj.provider.getDimension(), posX, posY, posZ, 64);

				difficulty = 1 + (3 - worldObj.getDifficulty().getDifficultyId()) * 0.5f;
				this.aiBoltAttack.setMaxRangedAttackDelay((int)(((EnergyWeapon)weapon.getItem()).getShootCooldown(weapon) * difficulty));
			}
		}
	}

	public void readEntityFromNBT(NBTTagCompound p_70037_1_)
	{
		super.readEntityFromNBT(p_70037_1_);
		this.setCombatTask();
	}

	/**
	 * Sets the held item, or an armor slot. Slot 0 is held item. Slot 1-4 is armor. Params: Item, slot
	 */
	/*@Override
	public void setCurrentItemOrArmor(int slot, ItemStack itemStack)
    {
        super.setCurrentItemOrArmor(slot, itemStack);

        if (!this.worldObj.isRemote && slot == 0)
        {
            this.setCombatTask();
        }
    }*/
	@Override
	public ItemStack getWeapon()
	{
		return getHeldItem(EnumHand.MAIN_HAND);
	}

	@SideOnly(Side.CLIENT)
	public boolean isInRangeToRenderDist(double p_70112_1_)
	{
		double d1 = this.getEntityBoundingBox().getAverageEdgeLength();
		d1 *= 64.0D * Entity.getRenderDistanceWeight();
		d1 += getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).getAttributeValue();
		return p_70112_1_ < d1 * d1;
	}
}
