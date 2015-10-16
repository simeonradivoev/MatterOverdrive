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

package matteroverdrive.tile;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.api.IMOTileEntity;
import matteroverdrive.machines.MachineNBTCategory;
import matteroverdrive.network.packet.server.PacketSendMachineNBT;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.EnumSet;

/**
 * Created by Simeon on 3/21/2015.
 */
public abstract class MOTileEntity extends TileEntity implements IMOTileEntity
{
    private boolean isAwake = false;

    public MOTileEntity(){super();}

    public MOTileEntity(World world,int meta)
    {
        super();
    }

    protected void updateBlock()
    {
        if(worldObj != null)
        {
            worldObj.markBlockForUpdate(xCoord,yCoord,zCoord);
        }
    }

    @Override
    public void updateEntity()
    {
        if (!isAwake)
        {
            onAwake(worldObj.isRemote ? Side.CLIENT : Side.SERVER);
            isAwake = true;
        }
    }

    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        readCustomNBT(nbt, MachineNBTCategory.ALL_OPTS);
    }

    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        writeCustomNBT(nbt, MachineNBTCategory.ALL_OPTS);
    }

    public abstract void writeCustomNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories);

    public abstract void readCustomNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories);

    @SideOnly(Side.CLIENT)
    public void sendNBTToServer(EnumSet<MachineNBTCategory> categories,boolean forceUpdate)
    {
        if (worldObj.isRemote)
        {
            MatterOverdrive.packetPipeline.sendToServer(new PacketSendMachineNBT(categories,this,forceUpdate));
        }
    }

    protected abstract void onAwake(Side side);
}
