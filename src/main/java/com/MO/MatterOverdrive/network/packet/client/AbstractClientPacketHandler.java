package com.MO.MatterOverdrive.network.packet.client;

import com.MO.MatterOverdrive.network.packet.AbstractPacketHandler;
import com.MO.MatterOverdrive.network.packet.PacketAbstract;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Created by Simeon on 4/22/2015.
 */
public abstract class AbstractClientPacketHandler<T extends IMessage> extends AbstractPacketHandler<T>
{
    public final IMessage handleServerMessage(EntityPlayer player, T message, MessageContext ctx)
    {
        return null;
    }
}