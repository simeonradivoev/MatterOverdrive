package matteroverdrive.network.packet.server;

import matteroverdrive.api.inventory.IBionicStat;
import matteroverdrive.entity.AndroidPlayer;
import matteroverdrive.handler.AndroidStatRegistry;
import matteroverdrive.network.packet.PacketAbstract;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Created by Simeon on 5/29/2015.
 */
public class PacketUnlockBioticStat extends PacketAbstract
{
    String name;
    int level;

    public PacketUnlockBioticStat()
    {

    }

    public PacketUnlockBioticStat(String name, int level)
    {
        this.name = name;
        this.level = level;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        name = ByteBufUtils.readUTF8String(buf);
        level = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf,name);
        buf.writeInt(level);
    }

    public static class ServerHandler extends AbstractServerPacketHandler<PacketUnlockBioticStat>
    {
        @Override
        public IMessage handleServerMessage(EntityPlayer player, PacketUnlockBioticStat message, MessageContext ctx)
        {
            IBionicStat stat = AndroidStatRegistry.getStat(message.name);
            AndroidPlayer androidPlayer = AndroidPlayer.get(player);
            if (stat != null && androidPlayer != null && androidPlayer.isAndroid())
            {
                androidPlayer.tryUnlock(stat, message.level);
                stat.onUnlock(androidPlayer,message.level);
            }
            return null;
        }
    }
}
