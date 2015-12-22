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

package matteroverdrive.tile.pipes;

import cpw.mods.fml.relauncher.Side;
import matteroverdrive.api.network.IMatterNetworkCable;
import matteroverdrive.api.network.IMatterNetworkConnection;
import matteroverdrive.data.BlockPos;
import matteroverdrive.machines.MachineNBTCategory;
import matteroverdrive.matter_network.MatterNetworkPacket;
import matteroverdrive.util.MatterNetworkHelper;
import matteroverdrive.util.math.MOMathHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.EnumSet;

/**
 * Created by Simeon on 3/15/2015.
 */
public class TileEntityNetworkPipe extends TileEntityPipe implements IMatterNetworkCable, IMatterNetworkConnection {

    @Override
    public boolean canConnectTo(TileEntity entity, ForgeDirection direction)
    {
        if (entity instanceof IMatterNetworkConnection)
        {
            if (entity instanceof TileEntityNetworkPipe)
            {
                TileEntityNetworkPipe networkPipe = (TileEntityNetworkPipe)entity;
                int pipeConnections = networkPipe.getConnections();
                if (MOMathHelper.getBoolean(pipeConnections,direction.ordinal())) {
                    return true;
                }
                else
                {
                    int pipeConnectionsCount = 0;
                    for (int i = 0; i < 6; i++) {
                        pipeConnectionsCount += ((pipeConnections >> i) & 1);
                    }
                    return pipeConnectionsCount < 2;
                }
            }
            else
            {
                return ((IMatterNetworkConnection) entity).canConnectFromSide(direction);
            }
        }
        return false;
    }

    @Override
    public void onAdded(World world, int x, int y, int z) {

    }

    @Override
    public void onPlaced(World world, EntityLivingBase entityLiving) {

    }

    @Override
    public void onDestroyed()
    {

    }

    @Override
    public void writeToDropItem(ItemStack itemStack) {

    }

    @Override
    public void readFromPlaceItem(ItemStack itemStack) {

    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public void broadcast(MatterNetworkPacket packet,ForgeDirection direction)
    {
        if (isValid())
        {
            for (int i = 0; i < 6; i++)
            {
                if (direction.getOpposite().ordinal() != i)
                    MatterNetworkHelper.broadcastPacketInDirection(worldObj, packet, this, ForgeDirection.getOrientation(i));
            }
        }
    }

    @Override
    public BlockPos getPosition() {
        return new BlockPos(this);
    }

    @Override
    public boolean canConnectFromSide(ForgeDirection side)
    {
        return MOMathHelper.getBoolean(getConnections(),side.ordinal());
    }

    @Override
    public void updateSides(boolean notify)
    {
        int connections = 0;
        int connectionCount = 0;

        for (int i = 0; i < 6; i++) {
            TileEntity t = this.worldObj.getTileEntity(ForgeDirection.values()[i].offsetX + this.xCoord, ForgeDirection.values()[i].offsetY + this.yCoord, ForgeDirection.values()[i].offsetZ + this.zCoord);

            if (connectionCount < 2 && canConnectTo(t, ForgeDirection.getOrientation(ForgeDirection.OPPOSITES[i])))
            {
                connections |= ForgeDirection.values()[i].flag;
                connectionCount++;
            }
        }

        this.setConnections(connections, notify);
    }

    @Override
    public void writeCustomNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories) {

    }

    @Override
    public void readCustomNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories) {

    }

    @Override
    protected void onAwake(Side side) {

    }
}
