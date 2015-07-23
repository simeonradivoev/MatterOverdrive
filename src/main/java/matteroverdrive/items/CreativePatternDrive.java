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
import matteroverdrive.util.MatterDatabaseHelper;
import matteroverdrive.util.MatterHelper;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.RegistryNamespaced;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

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
    public NBTTagList getItemsAsNBT(ItemStack patternStorage)
    {
        TagCompountCheck(patternStorage);
        return patternStorage.getTagCompound().getTagList(MatterDatabaseHelper.ITEMS_TAG_NAME, Constants.NBT.TAG_COMPOUND);
    }

    private void loadAllPatterns(ItemStack patternStorage)
    {
        patternStorage.setTagCompound(null);
        RegistryNamespaced itemRegistry = Item.itemRegistry;
        RegistryNamespaced blockRegistry = Block.blockRegistry;
        for (Object entryObject : itemRegistry)
        {
            if (entryObject instanceof Item) {
                Item item = (Item)entryObject;
                int maxDMG = 1;

                if (item.getHasSubtypes())
                {
                    maxDMG = item.getMaxDamage();
                }

                for (int dmg = 0; dmg < maxDMG; dmg++) {
                    ItemStack stack = new ItemStack(item, 1, dmg);
                    if (MatterHelper.containsMatter(stack)) {
                        if (stack != null) {
                            MatterDatabaseHelper.writeToNBT(patternStorage, stack, 100);
                        }
                    }
                }
            }
        }

        for (Block block : (Iterable<Block>)blockRegistry)
        {
            try {
                ItemStack stack = new ItemStack(block);
                if (MatterHelper.containsMatter(stack)) {
                    if (stack != null) {
                        MatterDatabaseHelper.writeToNBT(patternStorage, stack, 100);
                    }
                }
            }catch (Exception e)
            {

            }
        }
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
