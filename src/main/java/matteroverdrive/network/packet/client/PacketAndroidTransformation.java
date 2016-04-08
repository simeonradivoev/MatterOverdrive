package matteroverdrive.network.packet.client;

import io.netty.buffer.ByteBuf;
import matteroverdrive.entity.android_player.AndroidPlayer;
import matteroverdrive.entity.player.MOPlayerCapabilityProvider;
import matteroverdrive.network.packet.PacketAbstract;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
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
            AndroidPlayer androidPlayer = MOPlayerCapabilityProvider.GetAndroidCapability(player);
            if (androidPlayer != null)
            {
                androidPlayer.startConversion();
            }
        }
    }
}
