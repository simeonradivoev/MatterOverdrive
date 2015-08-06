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
import cofh.lib.util.position.BlockPosition;
import matteroverdrive.api.network.IMatterNetworkConnection;
import matteroverdrive.api.network.IMatterNetworkFilter;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import java.util.List;

/**
 * Created by Simeon on 7/21/2015.
 */
public class NetworkFlashDrive extends FlashDrive implements IMatterNetworkFilter {

    public NetworkFlashDrive(String name, GuiColor color)
    {
        super(name,color);
        setMaxStackSize(1);
    }

    public void addDetails(ItemStack itemstack, EntityPlayer player, List infos) {
        super.addDetails(itemstack,player,infos);
        if (itemstack.hasTagCompound())
        {
            NBTTagList list = itemstack.getTagCompound().getTagList(IMatterNetworkFilter.CONNECTIONS_TAG, Constants.NBT.TAG_COMPOUND);
            for (int i = 0;i < list.tagCount();i++) {
                BlockPosition pos = new BlockPosition(list.getCompoundTagAt(i));
                Block block = pos.getBlock(player.worldObj);
                if (block != null)
                {
                    infos.add(String.format("[%s,%s,%s] %s", pos.x, pos.y, pos.z,block != Blocks.air ? block.getLocalizedName() : "Unknown"));
                }
            }
        }
    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer entityPlayer, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
        TileEntity tileEntity = world.getTileEntity(x,y,z);
        if (tileEntity instanceof IMatterNetworkConnection)
        {
            BlockPosition connectionPosition = ((IMatterNetworkConnection) tileEntity).getPosition();
            if (!itemStack.hasTagCompound())
            {
                itemStack.setTagCompound(new NBTTagCompound());
            }

            boolean hasPos = false;
            NBTTagList list = itemStack.getTagCompound().getTagList(IMatterNetworkFilter.CONNECTIONS_TAG, Constants.NBT.TAG_COMPOUND);
            for (int i = 0;i < list.tagCount();i++)
            {
                BlockPosition pos = new BlockPosition(list.getCompoundTagAt(i));
                if (pos.equals(connectionPosition))
                {
                    hasPos = true;
                    list.removeTag(i);
                    break;
                }
            }

            if (!hasPos)
            {
                NBTTagCompound posNBT = new NBTTagCompound();
                connectionPosition.writeToNBT(posNBT);
                list.appendTag(posNBT);
            }

            itemStack.getTagCompound().setTag(IMatterNetworkFilter.CONNECTIONS_TAG, list);

            return true;
        }
        return false;
    }

    @Override
    public NBTTagCompound getFilter(ItemStack stack)
    {
        return stack.getTagCompound();
    }
}
