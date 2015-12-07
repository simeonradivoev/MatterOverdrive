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
import matteroverdrive.init.MatterOverdriveItems;
import matteroverdrive.util.MOStringHelper;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.EnumDifficulty;
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
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(getIsLegendary() ? 128 : getAndroidLevel() * 10 + 32);
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
        if (EntityRogueAndroid.dimensionWhitelist.size() > 0) {
            return EntityRogueAndroid.dimensionWhitelist.contains(worldObj.provider.dimensionId) && inDimensionBlacklist();
        }
        if (inDimensionBlacklist())
        {
            return false;
        }

        return super.getCanSpawnHere();
    }

    protected void addRandomArmor()
    {
        if (this.rand.nextFloat() < 0.15F * this.worldObj.func_147462_b(this.posX, this.posY, this.posZ))
        {
            int i = this.rand.nextInt(2);
            float f = this.worldObj.difficultySetting == EnumDifficulty.HARD ? 0.1F : 0.25F;

            if (this.rand.nextFloat() < 0.095F)
            {
                ++i;
            }

            if (this.rand.nextFloat() < 0.095F)
            {
                ++i;
            }

            if (this.rand.nextFloat() < 0.095F)
            {
                ++i;
            }

            for (int j = 3; j >= 0; --j)
            {
                ItemStack itemstack = this.func_130225_q(j);

                if (j < 3 && this.rand.nextFloat() < f)
                {
                    break;
                }

                if (itemstack == null)
                {
                    Item item = null;

                    if (i == 3)
                    {
                        if (rand.nextBoolean())
                        {
                            switch (j + 1)
                            {
                                case 4:
                                    item = MatterOverdriveItems.tritaniumHelemet;
                                    break;
                                case 3:
                                    item = MatterOverdriveItems.tritaniumChestplate;
                                    break;
                                case 2:
                                    item = MatterOverdriveItems.tritaniumLeggings;
                                    break;
                                case 1:
                                    item = MatterOverdriveItems.tritaniumBoots;
                                    break;
                            }
                        }else
                        {
                            item = getArmorItemForSlot(j + 1, i);
                        }
                    }

                    if (item != null)
                    {
                        this.setCurrentItemOrArmor(j + 1, new ItemStack(item));
                    }
                }
            }
        }
    }

    private boolean inDimensionBlacklist() {
        return EntityRogueAndroid.dimensionBlacklist.contains(worldObj.provider.dimensionId);
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
