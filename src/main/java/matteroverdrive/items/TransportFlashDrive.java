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

import cofh.lib.gui.GuiColor;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import java.util.List;

/**
 * Created by Simeon on 8/5/2015.
 */
public class TransportFlashDrive extends FlashDrive
{
    public TransportFlashDrive(String name,GuiColor color)
    {
        super(name,color);
    }

    public void addDetails(ItemStack itemstack, EntityPlayer player, List infos) {
        super.addDetails(itemstack, player, infos);
        if (hasTarget(itemstack))
        {
            Block block = player.worldObj.getBlock(getTargetX(itemstack), getTargetY(itemstack), getTargetZ(itemstack));
            infos.add(EnumChatFormatting.YELLOW + String.format("[%s,%s,%s] %s", getTargetX(itemstack), getTargetY(itemstack), getTargetZ(itemstack),block != Blocks.air ? block.getLocalizedName() : "Unknown"));
        }
    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer entityPlayer, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
        if (world.getBlock(x,y,z) != null && world.getBlock(x,y,z) != Blocks.air)
        {
            setTarget(itemStack,x,y,z);
            return true;
        }
        removeTarget(itemStack);
        return false;
    }

    public void setTarget(ItemStack itemStack,int x,int y,int z)
    {
        if (!itemStack.hasTagCompound())
            itemStack.setTagCompound(new NBTTagCompound());

        itemStack.getTagCompound().setInteger("TargetX",x);
        itemStack.getTagCompound().setInteger("TargetY",y);
        itemStack.getTagCompound().setInteger("TargetZ", z);
    }

    public int getTargetDistance(ItemStack itemStack)
    {
        if (itemStack.hasTagCompound())
        {
            return (int)Math.sqrt(getTargetX(itemStack)*getTargetX(itemStack) + getTargetY(itemStack)*getTargetY(itemStack) + getTargetZ(itemStack)*getTargetZ(itemStack));
        }
        return 0;
    }

    public void removeTarget(ItemStack itemStack)
    {
        if (itemStack.hasTagCompound())
        {
            itemStack.setTagCompound(null);
        }
    }

    public int getTargetX(ItemStack itemStack)
    {
        if (itemStack.hasTagCompound())
        {
            return itemStack.getTagCompound().getInteger("TargetX");
        }
        return 0;
    }

    public int getTargetY(ItemStack itemStack)
    {
        if (itemStack.hasTagCompound())
        {
            return itemStack.getTagCompound().getInteger("TargetY");
        }
        return 0;
    }

    public int getTargetZ(ItemStack itemStack)
    {
        if (itemStack.hasTagCompound())
        {
            return itemStack.getTagCompound().getInteger("TargetZ");
        }
        return 0;
    }

    public boolean hasTarget(ItemStack itemStack)
    {
        return itemStack.hasTagCompound() && itemStack.getTagCompound().hasKey("TargetX", Constants.NBT.TAG_INT) && itemStack.getTagCompound().hasKey("TargetY", Constants.NBT.TAG_INT) && itemStack.getTagCompound().hasKey("TargetZ", Constants.NBT.TAG_INT);
    }
}
