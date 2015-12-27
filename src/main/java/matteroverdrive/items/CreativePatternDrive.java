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

package matteroverdrive.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import matteroverdrive.Reference;
import matteroverdrive.data.ItemPattern;
import matteroverdrive.util.MatterDatabaseHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Created by Simeon on 7/22/2015.
 */
public class CreativePatternDrive extends PatternDrive
{

    public CreativePatternDrive(String name, int capacity)
    {
        super(name, capacity);
    }

    @Override
    public ItemPattern[] getPatterns(ItemStack patternStorage)
    {
        return MatterDatabaseHelper.getPatternsFromStorage(patternStorage);
    }

    private void loadAllPatterns(ItemStack patternStorage)
    {

    }

    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer entityPlayer)
    {
        if (!world.isRemote && entityPlayer.isSneaking())
        {
            loadAllPatterns(itemStack);
        }
        return itemStack;
    }

    @SideOnly(Side.CLIENT)
    protected String getIconString()
    {
        return Reference.MOD_ID + ":" + "pattern_drive";
    }

    @Override
    public boolean addItem(ItemStack storage, ItemStack itemStack,int initialAmount,boolean simulate)
    {
        return false;
    }
}
