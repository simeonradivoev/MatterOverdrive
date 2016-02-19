package matteroverdrive.network.packet.client;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import matteroverdrive.entity.android_player.AndroidPlayer;
import matteroverdrive.network.packet.PacketAbstract;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
        @SideOnly(Side.CLIENT)
        @Override
        public void handleClientMessage(EntityPlayerSP player, PacketAndroidTransformation message, MessageContext ctx)
        {
            AndroidPlayer androidPlayer = AndroidPlayer.get(player);
            if (androidPlayer != null)
            {
                androidPlayer.startConversion();
            }
        }
    }
}
