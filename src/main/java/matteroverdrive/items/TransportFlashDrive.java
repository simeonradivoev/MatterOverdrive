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

import matteroverdrive.client.data.Color;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import java.util.List;

/**
 * Created by Simeon on 8/5/2015.
 */
public class TransportFlashDrive extends FlashDrive
{
    public TransportFlashDrive(String name,Color color)
    {
        super(name,color);
    }

    public void addDetails(ItemStack itemstack, EntityPlayer player, List infos) {
        super.addDetails(itemstack, player, infos);
        if (hasTarget(itemstack))
        {
            BlockPos target = getTarget(itemstack);
            Block block = player.worldObj.getBlockState(target).getBlock();
            infos.add(EnumChatFormatting.YELLOW + String.format("[%s] %s", target.toString(),block.getMaterial() != Material.air ? block.getLocalizedName() : "Unknown"));
        }
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (worldIn.getBlockState(pos).getBlock().getMaterial() != Material.air)
        {
            setTarget(stack,pos);
            return true;
        }
        removeTarget(stack);
        return false;
    }

    public void setTarget(ItemStack itemStack,BlockPos pos)
    {
        if (!itemStack.hasTagCompound())
            itemStack.setTagCompound(new NBTTagCompound());

        itemStack.getTagCompound().setLong("target",pos.toLong());
    }

    public void removeTarget(ItemStack itemStack)
    {
        if (itemStack.hasTagCompound())
        {
            itemStack.setTagCompound(null);
        }
    }

    public BlockPos getTarget(ItemStack itemStack)
    {
        if (itemStack.getTagCompound() != null)
        {
            return BlockPos.fromLong(itemStack.getTagCompound().getLong("target"));
        }
        return null;
    }

    public boolean hasTarget(ItemStack itemStack)
    {
        return itemStack.hasTagCompound() && itemStack.getTagCompound().hasKey("taget", Constants.NBT.TAG_LONG);
    }
}
