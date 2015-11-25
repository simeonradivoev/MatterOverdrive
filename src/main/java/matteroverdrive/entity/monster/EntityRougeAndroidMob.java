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

import matteroverdrive.Reference;
import matteroverdrive.entity.ai.AndroidTargetSelector;
import matteroverdrive.util.MOStringHelper;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

/**
 * Created by Simeon on 11/15/2015.
 */
public class EntityRougeAndroidMob extends EntityMob
{
    protected static final AndroidTargetSelector targetSelector = new AndroidTargetSelector();
    private static ResourceLocation androidNames = new ResourceLocation(Reference.PATH_INFO + "android_names.txt");
    private static String[] names = MOStringHelper.readTextFile(androidNames).split(",");

    public EntityRougeAndroidMob(World world)
    {
        super(world);
        setAndroidLevel((int)(MathHelper.clamp_double(Math.abs(rand.nextGaussian()),0,3)));
        boolean isLegendary = rand.nextDouble() < 0.05 * getAndroidLevel();
        setLegendary(isLegendary);
        init();
    }

    public EntityRougeAndroidMob(World world,int level,boolean legendary)
    {
        super(world);
        setAndroidLevel(level);
        setLegendary(legendary);
        init();
    }

    private void init()
    {
        String name = getIsLegendary() ? String.format("★ %s ",MOStringHelper.translateToLocal("rarity.legendary")) : "";
        name += String.format("[%s] ",getAndroidLevel());
        name += names[rand.nextInt(names.length)];
        setCustomNameTag(name);
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(getAndroidLevel() * 10 + 32);
        this.setHealth(this.getMaxHealth());
    }

    @Override
    protected void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(13, new Byte((byte) 0));
        this.dataWatcher.addObject(14,new Byte((byte)0));
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbtTagCompound)
    {
        super.readEntityFromNBT(nbtTagCompound);
        setLegendary(nbtTagCompound.getBoolean("Legendary"));
        setAndroidLevel(nbtTagCompound.getByte("Level"));
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbtTagCompound)
    {
        super.writeEntityToNBT(nbtTagCompound);
        nbtTagCompound.setByte("Level",this.dataWatcher.getWatchableObjectByte(13));
        nbtTagCompound.setBoolean("Legendary",this.dataWatcher.getWatchableObjectByte(14) == (byte)1);
    }

    @Override
    public boolean isPotionApplicable(PotionEffect potion)
    {
        return false;
    }

    @Override
    public boolean getCanSpawnHere()
    {
        if (EntityRogueAndroid.dimentionWhitelist.size() > 0) {
            return EntityRogueAndroid.dimentionWhitelist.contains(worldObj.provider.dimensionId) && inDimensionBlacklist();
        }
        if (inDimensionBlacklist())
        {
            return false;
        }

        return super.getCanSpawnHere();
    }

    private boolean inDimensionBlacklist() {
        return EntityRogueAndroid.dimentionBlacklist.contains(worldObj.provider.dimensionId);
    }

    public void setAndroidLevel(int level)
    {
        this.dataWatcher.updateObject(13, Byte.valueOf((byte) level));
    }

    public int getAndroidLevel()
    {
        return this.dataWatcher.getWatchableObjectByte(13);
    }

    public void setLegendary(boolean legendary)
    {
        this.dataWatcher.updateObject(14, legendary ? (byte)1 : (byte)0);
    }

    public boolean getIsLegendary()
    {
        return this.dataWatcher.getWatchableObjectByte(14) == (byte)1;
    }
}
