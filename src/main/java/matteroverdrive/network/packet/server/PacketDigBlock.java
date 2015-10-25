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

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import matteroverdrive.network.packet.PacketAbstract;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.S23PacketBlockChange;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;

public class PacketDigBlock  extends PacketAbstract
{
    int typeOfDig;
    int x,y,z,side;

    public PacketDigBlock(){}

    public PacketDigBlock(int x,int y,int z,int typeOfDig,int side)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.side = side;
        this.typeOfDig = typeOfDig;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        x = buf.readInt();
        y = buf.readInt();
        z = buf.readInt();
        side = buf.readByte();
        typeOfDig = buf.readByte();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
        buf.writeByte(side);
        buf.writeByte(typeOfDig);
    }

    public static class ServerHandler extends AbstractServerPacketHandler<PacketDigBlock>
    {
        @Override
        public IMessage handleServerMessage(EntityPlayer player, PacketDigBlock message, MessageContext ctx) {
            WorldServer worldserver = MinecraftServer.getServer().worldServerForDimension(player.dimension);
            EntityPlayerMP playerMP = (EntityPlayerMP)player;

            if (message.typeOfDig == 0)
            {
                if (!MinecraftServer.getServer().isBlockProtected(worldserver, message.x, message.y, message.z, player))
                {
                    playerMP.theItemInWorldManager.onBlockClicked(message.x, message.y,message.z, message.side);
                }
                else
                {
                    playerMP.playerNetServerHandler.sendPacket(new S23PacketBlockChange(message.x, message.y, message.z, worldserver));
                }
            }
            else if (message.typeOfDig == 2)
            {
                playerMP.theItemInWorldManager.uncheckedTryHarvestBlock(message.x, message.y, message.z);

                if (worldserver.getBlock(message.x, message.y, message.z).getMaterial() != Material.air)
                {
                    playerMP.playerNetServerHandler.sendPacket(new S23PacketBlockChange(message.x,message.y, message.z, worldserver));
                }
            }
            else if (message.typeOfDig == 1)
            {
                playerMP.theItemInWorldManager.cancelDestroyingBlock(message.x, message.y, message.z);

                if (worldserver.getBlock(message.x, message.y, message.z).getMaterial() != Material.air)
                {
                    playerMP.playerNetServerHandler.sendPacket(new S23PacketBlockChange(message.x, message.y, message.z, worldserver));
                }
            }
            return null;
        }
    }
}
