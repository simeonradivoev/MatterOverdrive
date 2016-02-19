package matteroverdrive.network.packet.client;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import matteroverdrive.network.packet.TileEntityUpdatePacket;
import matteroverdrive.machines.replicator.TileEntityMachineReplicator;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Simeon on 4/28/2015.
 */
public class PacketReplicationComplete extends TileEntityUpdatePacket
{
    public PacketReplicationComplete(){super();}
    public PacketReplicationComplete(TileEntity entity){super(entity);}

    public static class ClientHandler extends AbstractClientPacketHandler<PacketReplicationComplete>
    {
        @SideOnly(Side.CLIENT)
        @Override
        public void handleClientMessage(EntityPlayerSP player, PacketReplicationComplete message, MessageContext ctx)
        {
            TileEntity entity = message.getTileEntity(player.worldObj);
            if (entity instanceof TileEntityMachineReplicator)
            {
                ((TileEntityMachineReplicator) entity).beginSpawnParticles();
            }
        }
    }
}
