package matteroverdrive.network.packet.client;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import matteroverdrive.network.packet.TileEntityUpdatePacket;
import matteroverdrive.tile.TileEntityMachineReplicator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

/**
 * Created by Simeon on 4/28/2015.
 */
public class PacketReplicationComplete extends TileEntityUpdatePacket
{
    public PacketReplicationComplete(){super();}
    public PacketReplicationComplete(TileEntity entity){super(entity);}

    public static class ClientHandler extends AbstractClientPacketHandler<PacketReplicationComplete>
    {
        @Override
        public IMessage handleClientMessage(EntityPlayer player, PacketReplicationComplete message, MessageContext ctx)
        {
            TileEntity entity = message.getTileEntity(player.worldObj);
            if (entity instanceof TileEntityMachineReplicator)
            {
                ((TileEntityMachineReplicator) entity).beginSpawnParticles();
            }
            return null;
        }
    }
}
