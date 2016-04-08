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

package matteroverdrive.network.packet.server;

import io.netty.buffer.ByteBuf;
import matteroverdrive.machines.MOTileEntityMachine;
import matteroverdrive.machines.MachineNBTCategory;
import matteroverdrive.network.packet.AbstractBiPacketHandler;
import matteroverdrive.network.packet.TileEntityUpdatePacket;
import matteroverdrive.tile.MOTileEntity;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.EnumSet;

/**
 * Created by Simeon on 8/15/2015.
 */
public class PacketSendMachineNBT extends TileEntityUpdatePacket
{
    NBTTagCompound data;
    int cattegories;
    boolean forceUpdate;

    public PacketSendMachineNBT(){}
    public PacketSendMachineNBT(EnumSet<MachineNBTCategory> categories,MOTileEntity tileEntity,boolean forceUpdate,boolean toDisk)
    {
        super(tileEntity);
        data = new NBTTagCompound();
        this.forceUpdate = forceUpdate;
        tileEntity.writeCustomNBT(data,categories, toDisk);
        this.cattegories = MachineNBTCategory.encode(categories);
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        super.fromBytes(buf);
        data = ByteBufUtils.readTag(buf);
        cattegories = buf.readInt();
        forceUpdate = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        super.toBytes(buf);
        ByteBufUtils.writeTag(buf, data);
        buf.writeInt(cattegories);
        buf.writeBoolean(forceUpdate);
    }

    public static class BiHandler extends AbstractBiPacketHandler<PacketSendMachineNBT>
    {
        @SideOnly(Side.CLIENT)
        @Override
        public void handleClientMessage(EntityPlayerSP player, PacketSendMachineNBT message, MessageContext ctx)
        {
            TileEntity tileEntity = message.getTileEntity(player.worldObj);
            if (tileEntity instanceof MOTileEntity)
            {
                ((MOTileEntity) tileEntity).readCustomNBT(message.data,MachineNBTCategory.decode(message.cattegories));
            }
        }

        @Override
        public void handleServerMessage(EntityPlayerMP player, PacketSendMachineNBT message, MessageContext ctx)
        {
            TileEntity tileEntity = message.getTileEntity(player.worldObj);
            if (tileEntity instanceof MOTileEntity)
            {
                ((MOTileEntity) tileEntity).readCustomNBT(message.data,MachineNBTCategory.decode(message.cattegories));
                if (message.forceUpdate)
                {
                    if (tileEntity instanceof MOTileEntityMachine) {
                        ((MOTileEntityMachine) tileEntity).forceSync();
                    }else
                    {
                        // TODO: 3/26/2016 Find how to mark block for update
                        //player.worldObj.markBlockForUpdate(tileEntity.getPos());
                    }
                }
            }
        }
    }
}
