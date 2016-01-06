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
import matteroverdrive.entity.ai.AndroidTargetSelector;
import matteroverdrive.entity.ai.EntityAIAndroidAttackOnCollide;
import matteroverdrive.entity.ai.EntityAIMoveAlongPath;
import matteroverdrive.util.AndroidPartsFactory;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.world.World;

/**
 * Created by Simeon on 5/26/2015.
 */
public class EntityMeleeRougeAndroidMob extends EntityRougeAndroidMob
{
    public EntityMeleeRougeAndroidMob(World world)
    {
        super(world);
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAIAndroidAttackOnCollide(this, EntityLivingBase.class, 1.0D, false));
        this.tasks.addTask(3, new EntityAIMoveAlongPath(this, 1.0D));
        this.tasks.addTask(4, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(5, new EntityAIWatchClosest(this, EntityLivingBase.class, 8.0F));
        this.tasks.addTask(5, new EntityAILookIdle(this));

        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityLivingBase.class, 0, false, true, new AndroidTargetSelector(this)));
    }

    @Override
    protected boolean isAIEnabled()
    {
        return true;
    }

    @Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(64.0D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.3D);
        this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(24);
    }

    public void setAndroidLevel(int level)
    {
        super.setAndroidLevel(level);
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(4.0D + level);
    }

    public void setLegendary(boolean legendary)
    {
        super.setLegendary(legendary);
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(8);
    }

    @Override
    protected void dropRareDrop(int i)
    {

    }

    @Override
    protected void dropFewItems(boolean hit, int looting)
    {
        if (recentlyHit > 0)
        {
            float lootingModifier = (Math.min(looting, 10) / 10f);
            if (rand.nextFloat() < (0.1f + lootingModifier) || getIsLegendary())
            {

                this.entityDropItem(MatterOverdrive.androidPartsFactory.generateRandomDecoratedPart(new AndroidPartsFactory.AndroidPartFactoryContext(getAndroidLevel(),this,getIsLegendary())), 0.0F);
            }
        }
    }

    @Override
    public IEntityLivingData onSpawnWithEgg(IEntityLivingData entityLivingData)
    {
        entityLivingData = super.onSpawnWithEgg(entityLivingData);
        this.addRandomArmor();
        this.enchantEquipment();
        return entityLivingData;
    }
}
