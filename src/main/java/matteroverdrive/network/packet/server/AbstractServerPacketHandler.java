package matteroverdrive.network.packet.server;

import matteroverdrive.network.packet.AbstractPacketHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Created by Simeon on 4/22/2015.
 */
public abstract class AbstractServerPacketHandler<T extends IMessage> extends AbstractPacketHandler<T>
{
    public AbstractServerPacketHandler(){}

    public IMessage handleClientMessage(EntityPlayer player, T message, MessageContext ctx)
    {
        return null;
    }
}
