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

package matteroverdrive.entity;

import matteroverdrive.entity.ai.AndoidTargetSelector;
import matteroverdrive.init.MatterOverdriveItems;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

/**
 * Created by Simeon on 5/26/2015.
 */
public class EntityRougeAndroidMob extends EntityMob
{
    public static final AndoidTargetSelector targetSelector = new AndoidTargetSelector();

    public EntityRougeAndroidMob(World world)
    {
        super(world);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.0D, false));
        this.tasks.addTask(4, new EntityAIAttackOnCollide(this, EntityVillager.class, 1.0D, true));
        this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0D));
        this.tasks.addTask(6, new EntityAIMoveThroughVillage(this, 1.0D, false));
        this.tasks.addTask(7, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));

        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true,false,targetSelector));
    }

    @Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(SharedMonsterAttributes.maxHealth.clampValue(64.0D));
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(SharedMonsterAttributes.movementSpeed.clampValue(1.6D));
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(SharedMonsterAttributes.attackDamage.clampValue(4.0D));
    }

    @Override
    protected Entity findPlayerToAttack()
    {
        EntityPlayer entityplayer = this.worldObj.getClosestVulnerablePlayerToEntity(this, 16.0D);
        if (targetSelector.isEntityApplicable(entityplayer))
        {
            return entityplayer != null && this.canEntityBeSeen(entityplayer) ? entityplayer : null;
        }
        return null;
    }

    @Override
    protected void dropRareDrop(int p_70600_1_)
    {

    }

    public void onDeath(DamageSource damage)
    {
        super.onDeath(damage);
        int i = 0;

        Entity entity = damage.getEntity();
        if (entity instanceof EntityPlayer)
        {
            i = EnchantmentHelper.getLootingModifier((EntityLivingBase) entity);
        }
        int j = 0;

        if (this.func_146066_aG() && this.worldObj.getGameRules().getGameRuleBooleanValue("doMobLoot"))
        {
            this.dropFewItems(this.recentlyHit > 0, i);
            this.dropEquipment(this.recentlyHit > 0, i);

            if (this.recentlyHit > 0)
            {
                j = this.rand.nextInt(100) - i;

                if (j < 20)
                {
                    this.entityDropItem(new ItemStack(MatterOverdriveItems.androidParts, 1, rand.nextInt(4)), 0.0F);
                }
            }
        }
    }

    @Override
    public boolean isPotionApplicable(PotionEffect p_70687_1_)
    {
        return false;
    }
}
