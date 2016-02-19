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

package matteroverdrive.network.packet.server;/* Created by Simeon on 10/17/2015. */

import matteroverdrive.util.MOLog;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import matteroverdrive.network.packet.PacketAbstract;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.S23PacketBlockChange;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;

public class PacketDigBlock  extends PacketAbstract
{
    int typeOfDig;
    BlockPos pos;
    EnumFacing side;

    public PacketDigBlock(){pos = new BlockPos(0,0,0);}

    public PacketDigBlock(BlockPos pos,int typeOfDig,EnumFacing side)
    {
        if (pos == null)
        {
            MOLog.error("Empty Pos");
        }
        this.pos = pos;
        this.side = side;
        this.typeOfDig = typeOfDig;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        pos = BlockPos.fromLong(buf.readLong());
        side = EnumFacing.VALUES[buf.readByte()];
        typeOfDig = buf.readByte();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(pos.toLong());
        buf.writeByte(side.ordinal());
        buf.writeByte(typeOfDig);
    }

    public static class ServerHandler extends AbstractServerPacketHandler<PacketDigBlock>
    {
        @Override
        public void handleServerMessage(EntityPlayerMP player, PacketDigBlock message, MessageContext ctx) {
            WorldServer worldserver = MinecraftServer.getServer().worldServerForDimension(player.dimension);
            EntityPlayerMP playerMP = player;

            if (message.typeOfDig == 0)
            {
                if (!MinecraftServer.getServer().isBlockProtected(worldserver, message.pos, player))
                {
                    playerMP.theItemInWorldManager.onBlockClicked(message.pos, message.side);
                }
                else
                {
                    playerMP.playerNetServerHandler.sendPacket(new S23PacketBlockChange(worldserver,message.pos));
                }
            }
            else if (message.typeOfDig == 2)
            {
                playerMP.theItemInWorldManager.tryHarvestBlock(message.pos);

                if (worldserver.getBlockState(message.pos).getBlock().getMaterial() != Material.air)
                {
                    playerMP.playerNetServerHandler.sendPacket(new S23PacketBlockChange(worldserver,message.pos));
                }
            }
            else if (message.typeOfDig == 1)
            {
                playerMP.theItemInWorldManager.cancelDestroyingBlock();

                if (worldserver.getBlockState(message.pos).getBlock().getMaterial() != Material.air)
                {
                    playerMP.playerNetServerHandler.sendPacket(new S23PacketBlockChange(worldserver,message.pos));
                }
            }
        }
    }
}
