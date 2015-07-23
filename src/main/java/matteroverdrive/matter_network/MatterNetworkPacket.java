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

package matteroverdrive.matter_network;

import cofh.lib.util.position.BlockPosition;
import matteroverdrive.api.network.IMatterNetworkConnection;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.HashSet;

/**
 * Created by Simeon on 4/26/2015.
 */
public abstract class MatterNetworkPacket
{
    protected BlockPosition senderPos;
    protected NBTTagCompound filter;
    protected HashSet<MatterNetworkPathNode> path;


    public MatterNetworkPacket(){path = new HashSet<>();}
    public MatterNetworkPacket(BlockPosition senderPos,ForgeDirection port)
    {
        this();
        this.senderPos = senderPos;
        this.senderPos.setOrientation(port);
    }
    public MatterNetworkPacket(BlockPosition senderPos,ForgeDirection port,NBTTagCompound filter)
    {
        this();
        this.senderPos = senderPos;
        this.senderPos.setOrientation(port);
        this.filter = filter;
    }

    public MatterNetworkPacket addToPath(IMatterNetworkConnection connection,ForgeDirection recivedFrom)
    {
        MatterNetworkPathNode node = new MatterNetworkPathNode(connection.getPosition(),recivedFrom.getOpposite());
        if (!path.contains(node))
        {
            path.add(node);
        }
        return this;
    }

    public boolean hasPassedTrough(IMatterNetworkConnection connection)
    {
        return path.contains(new MatterNetworkPathNode(connection.getPosition()));
    }

    public IMatterNetworkConnection getSender(World world)
    {
        if (world != null)
        {
            TileEntity tileEntity = senderPos.getTileEntity(world);
            if (tileEntity != null && tileEntity instanceof IMatterNetworkConnection)
                return (IMatterNetworkConnection) tileEntity;
        }
        return null;
    }

    public void readFromNBT(NBTTagCompound tagCompound)
    {
        if (tagCompound != null)
        {
            if (tagCompound.hasKey("Sender",10))
            {
                senderPos = new BlockPosition(tagCompound.getCompoundTag("Sender"));
            }
            if (tagCompound.hasKey("Filter",10))
            {
                filter = tagCompound.getCompoundTag("Filter");
            }
        }
    }

    public void writeToNBT(NBTTagCompound tagCompound)
    {
        if (tagCompound != null && senderPos != null)
        {
            if (senderPos != null)
            {
                NBTTagCompound sender = new NBTTagCompound();
                senderPos.writeToNBT(sender);
                tagCompound.setTag("Sender",sender);
            }
            if (filter != null)
            {
                tagCompound.setTag("Filter",filter);
            }
        }
    }

    public abstract boolean isValid(World world);

    public NBTTagCompound getFilter()
    {
        return filter;
    }

    public abstract String getName();

    public ForgeDirection getSenderPort()
    {
        return senderPos.orientation;
    }
}
