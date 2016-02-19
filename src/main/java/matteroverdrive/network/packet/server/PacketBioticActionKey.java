package matteroverdrive.network.packet.server;

import io.netty.buffer.ByteBuf;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.api.android.IBioticStat;
import matteroverdrive.entity.android_player.AndroidPlayer;
import matteroverdrive.network.packet.PacketAbstract;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Simeon on 1/1/2016.
 */
public class PacketBioticActionKey extends PacketAbstract
{
    public PacketBioticActionKey(){}

    @Override
    public void fromBytes(ByteBuf buf)
    {

    }

    @Override
    public void toBytes(ByteBuf buf)
    {

    }

    public static class ServerHandler extends AbstractServerPacketHandler<PacketBioticActionKey>
    {
        @SideOnly(Side.CLIENT)
        @Override
        public void handleServerMessage(EntityPlayerMP player, PacketBioticActionKey message, MessageContext ctx)
        {
            AndroidPlayer androidPlayer = AndroidPlayer.get(player);
            if (androidPlayer.isAndroid())
            {
                for (IBioticStat stat : MatterOverdrive.statRegistry.getStats())
                {
                    int unlockedLevel = androidPlayer.getUnlockedLevel(stat);
                    if (unlockedLevel > 0 && stat.isEnabled(androidPlayer, unlockedLevel))
                    {
                        stat.onActionKeyPress(androidPlayer, unlockedLevel, true);
                    }
                }
            }
        }
    }
}
