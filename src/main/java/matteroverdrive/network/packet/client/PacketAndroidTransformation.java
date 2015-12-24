package matteroverdrive.network.packet.client;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import matteroverdrive.entity.player.AndroidPlayer;
import matteroverdrive.network.packet.PacketAbstract;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Created by Simeon on 12/24/2015.
 */
public class PacketAndroidTransformation extends PacketAbstract
{
    public PacketAndroidTransformation(){}

    @Override
    public void fromBytes(ByteBuf buf)
    {

    }

    @Override
    public void toBytes(ByteBuf buf)
    {

    }

    public static class ClientHandler extends AbstractClientPacketHandler<PacketAndroidTransformation>
    {
        @Override
        public IMessage handleClientMessage(EntityPlayer player, PacketAndroidTransformation message, MessageContext ctx)
        {
            AndroidPlayer androidPlayer = AndroidPlayer.get(player);
            if (androidPlayer != null)
            {
                androidPlayer.startConversion();
            }
            return null;
        }
    }
}
