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

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.init.MatterOverdriveItems;
import matteroverdrive.items.weapon.PhaserRifle;
import matteroverdrive.network.packet.AbstractBiPacketHandler;
import matteroverdrive.network.packet.PacketAbstract;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;

/**
 * Created by Simeon on 7/25/2015.
 */
public class PacketFirePhaserRifle extends PacketAbstract
{
    private int seed;
    private boolean isAiming;
    private int sender;
    private long timestamp;
    private Vec3 position;
    private Vec3 direction;

    public PacketFirePhaserRifle(){}
    public PacketFirePhaserRifle(boolean isAiming,int sender,int seed,Vec3 pos,Vec3 dir)
    {
        this.isAiming = isAiming;
        this.sender = sender;
        this.seed = seed;
        this.position = pos;
        this.direction = dir;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.isAiming = buf.readBoolean();
        this.sender = buf.readInt();
        this.seed = buf.readInt();
        this.timestamp = buf.readLong();
        this.position = Vec3.createVectorHelper(buf.readDouble(), buf.readDouble(), buf.readDouble());
        this.direction = Vec3.createVectorHelper(buf.readDouble(),buf.readDouble(),buf.readDouble());
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(isAiming);
        buf.writeInt(sender);
        buf.writeInt(seed);
        buf.writeLong(System.currentTimeMillis());
        buf.writeDouble(position.xCoord);
        buf.writeDouble(position.yCoord);
        buf.writeDouble(position.zCoord);
        buf.writeDouble(direction.xCoord);
        buf.writeDouble(direction.yCoord);
        buf.writeDouble(direction.zCoord);
    }

    public static class BiHandler extends AbstractBiPacketHandler<PacketFirePhaserRifle>
    {
        @Override
        public IMessage handleClientMessage(EntityPlayer player, PacketFirePhaserRifle message, MessageContext ctx)
        {
            if (player.getEntityId() != message.sender)
            {
                Entity entity = player.worldObj.getEntityByID(message.sender);
                if (entity != null && entity instanceof EntityPlayer && ((EntityPlayer)entity).getHeldItem() != null && ((EntityPlayer)entity).getHeldItem().getItem() instanceof PhaserRifle) {
                    MatterOverdriveItems.phaserRifle.onClientFire(((EntityPlayer)entity).getHeldItem(), (EntityPlayer)entity, message.isAiming,message.seed,message.position,message.direction);
                }
            }
            return null;
        }

        @Override
        public IMessage handleServerMessage(EntityPlayer player, PacketFirePhaserRifle message, MessageContext ctx)
        {
            if (player.getHeldItem() != null && player.getHeldItem().getItem() == MatterOverdriveItems.phaserRifle && MatterOverdriveItems.phaserRifle.canFire(player.getHeldItem(),player.worldObj))
            {
                MatterOverdriveItems.phaserRifle.onServerFire(player.getHeldItem(), player, message.isAiming,message.seed, (int)(System.currentTimeMillis() - message.timestamp),message.position,message.direction);
                MatterOverdrive.packetPipeline.sendToAllAround(message,player,128);
            }
            return null;
        }
    }
}
