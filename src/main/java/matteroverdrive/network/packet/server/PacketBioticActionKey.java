package matteroverdrive.network.packet.server;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.api.android.IBionicStat;
import matteroverdrive.entity.player.AndroidPlayer;
import matteroverdrive.network.packet.PacketAbstract;
import net.minecraft.entity.player.EntityPlayer;

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

        @Override
        public IMessage handleServerMessage(EntityPlayer player, PacketBioticActionKey message, MessageContext ctx)
        {
            AndroidPlayer androidPlayer = AndroidPlayer.get(player);
            if (androidPlayer.isAndroid())
            {
                for (IBionicStat stat : MatterOverdrive.statRegistry.getStats())
                {
                    int unlockedLevel = androidPlayer.getUnlockedLevel(stat);
                    if (unlockedLevel > 0 && stat.isEnabled(androidPlayer, unlockedLevel))
                    {
                        stat.onActionKeyPress(androidPlayer, unlockedLevel, true);
                    }
                }
            }
            return null;
        }
    }
}
