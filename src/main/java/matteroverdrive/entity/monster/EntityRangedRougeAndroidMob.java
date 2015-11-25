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
import matteroverdrive.entity.ai.EntityAIPhaserBoltAttack;
import matteroverdrive.init.MatterOverdriveItems;
import matteroverdrive.items.weapon.EnergyWeapon;
import matteroverdrive.network.packet.bi.PacketFirePlasmaShot;
import matteroverdrive.util.WeaponFactory;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

/**
 * Created by Simeon on 11/15/2015.
 */
public class EntityRangedRougeAndroidMob extends EntityRougeAndroidMob implements IRangedEnergyWeaponAttackMob
{
    public static boolean UNLIMITED_WEAPON_ENERGY = true;
    private EntityAIPhaserBoltAttack aiBoltAttack = new EntityAIPhaserBoltAttack(this, 1.0D, 60, 15.0F);

    public EntityRangedRougeAndroidMob(World world)
    {
        super(world);
        init(world);
    }

    public EntityRangedRougeAndroidMob(World world,int level,boolean legendary)
    {
        super(world,level,legendary);
        init(world);
    }

    protected void init(World world)
    {
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(5, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(6, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true,false,targetSelector));

        if (world != null && !world.isRemote)
        {
            this.setCombatTask();
        }
    }

    @Override
    protected void dropEquipment(boolean recentlyHit, int lootingLevel)
    {
        if (this.recentlyHit > 0)
        {
            int j = this.rand.nextInt(200) - lootingLevel;

            if (j < 5 || getIsLegendary())
            {
                this.entityDropItem(getEquipmentInSlot(0).copy(),0);
            }
        }
    }

    /**
     * Returns true if the newer Entity AI code should be run
     */
    public boolean isAIEnabled()
    {
        return true;
    }

    @Override
    protected Item getDropItem()
    {
        return MatterOverdriveItems.energyPack;
    }

    @Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25D);
        this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(24);
    }

    @Override
    public void onLivingUpdate()
    {
        super.onLivingUpdate();
        if (getHeldItem() != null)
        {
            getHeldItem().getItem().onUpdate(getHeldItem(),worldObj,this,0,true);
        }
    }

    @Override
    protected void dropFewItems(boolean recentlyHit, int lootingLevel)
    {
        int j;
        int k;

        j = this.rand.nextInt(2 + lootingLevel);

        for (k = 0; k < j; ++k)
        {
            this.dropItem(MatterOverdriveItems.energyPack, 1);
        }
    }

    @Override
    public void addRandomArmor()
    {
        super.addRandomArmor();
        int androidLevel = getAndroidLevel();
        ItemStack gun = MatterOverdrive.weaponFactory.getRandomDecoratedEnergyWeapon(new WeaponFactory.WeaponGenerationContext(androidLevel,this,getIsLegendary()));
        setCurrentItemOrArmor(0,gun);
        this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(((EnergyWeapon)gun.getItem()).getRange(gun));
    }

    public void setCombatTask()
    {
        this.tasks.removeTask(this.aiBoltAttack);
        ItemStack itemstack = this.getHeldItem();

        if (itemstack != null && itemstack.getItem() instanceof EnergyWeapon)
        {
            this.aiBoltAttack.setMaxRangedAttackDelay(((EnergyWeapon) itemstack.getItem()).getShootCooldown(itemstack));
            this.aiBoltAttack.setMaxChaseDistance(((EnergyWeapon) itemstack.getItem()).getRange(itemstack));
            this.tasks.addTask(4, this.aiBoltAttack);
        }
    }

    @Override
    public IEntityLivingData onSpawnWithEgg(IEntityLivingData entityLivingData)
    {
        entityLivingData = super.onSpawnWithEgg(entityLivingData);
        this.tasks.addTask(4, this.aiBoltAttack);
        this.addRandomArmor();
        return entityLivingData;
    }

    @Override
    public void attackEntityWithRangedAttack(EntityLivingBase target, Vec3 lastSeenPosition, boolean canSee) {
        ItemStack weapon = this.getHeldItem();
        if (!worldObj.isRemote)
        {
            if (lastSeenPosition == null)
            {
                lastSeenPosition = Vec3.createVectorHelper(target.posX,target.posY,target.posZ);
            }
            if (weapon.getItem() instanceof EnergyWeapon) {
                EnergyWeapon energyWeapon = (EnergyWeapon) weapon.getItem();
                //magic number from MC
                Vec3 pos = Vec3.createVectorHelper(this.posX, this.posY + this.getEyeHeight(), this.posZ);
                Vec3 dir = Vec3.createVectorHelper(lastSeenPosition.xCoord - this.posX, lastSeenPosition.yCoord + (double) (target.height / 3.0F) - this.posY, lastSeenPosition.zCoord - this.posZ);
                WeaponShot shot = energyWeapon.createShot(weapon, this, true);
                shot.setDamage(shot.getDamage()* MathHelper.clamp_float(0.3f*worldObj.difficultySetting.getDifficultyId(),0,1));
                shot.setAccuracy(shot.getAccuracy() + 0.3f);
                energyWeapon.onServerFire(weapon, this, shot, pos, dir);
                if (UNLIMITED_WEAPON_ENERGY)
                    energyWeapon.rechargeFully(weapon);
                MatterOverdrive.packetPipeline.sendToAllAround(new PacketFirePlasmaShot(this.getEntityId(),pos,dir,shot),worldObj.provider.dimensionId,posX,posY,posZ,64);
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
    @Override
    public void setCurrentItemOrArmor(int slot, ItemStack itemStack)
    {
        super.setCurrentItemOrArmor(slot, itemStack);

        if (!this.worldObj.isRemote && slot == 0)
        {
            this.setCombatTask();
        }
    }

    @Override
    public ItemStack getWeapon() {
        return getHeldItem();
    }
}
