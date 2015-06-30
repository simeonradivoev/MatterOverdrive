package matteroverdrive.network.packet.client;

import matteroverdrive.network.packet.AbstractPacketHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
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