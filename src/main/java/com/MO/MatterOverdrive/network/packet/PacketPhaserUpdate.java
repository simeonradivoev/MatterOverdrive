package com.MO.MatterOverdrive.network.packet;

import com.MO.MatterOverdrive.items.Phaser;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * Created by Simeon on 4/16/2015.
 */
public class PacketPhaserUpdate extends AbstractPacket
{
    int slot;
    int entity;
    boolean isFiring;

    public PacketPhaserUpdate()
    {

    }

    public PacketPhaserUpdate(int slot,int entity,boolean isFiring)
    {
        this.slot = slot;
        this.entity = entity;
        this.isFiring = isFiring;
    }

    @Override
    public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
    {
        buffer.writeInt(slot);
        buffer.writeInt(entity);
        buffer.writeBoolean(isFiring);
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
    {
        slot = buffer.readInt();
        entity = buffer.readInt();
        isFiring = buffer.readBoolean();
    }

    @Override
    public void handleClientSide(EntityPlayer player)
    {
       Entity e = player.worldObj.getEntityByID(entity);
        if (e instanceof EntityOtherPlayerMP)
        {
            EntityPlayer other_player = (EntityPlayer)e;
            ItemStack phaser = other_player.inventory.getStackInSlot(0);
            if (phaser != null)
            {
                Phaser.setFiring(phaser, isFiring);
            }
        }
    }

    @Override
    public void handleServerSide(EntityPlayer player) {

    }
}
