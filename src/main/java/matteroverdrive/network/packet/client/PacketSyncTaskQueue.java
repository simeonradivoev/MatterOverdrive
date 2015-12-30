package matteroverdrive.network.packet.client;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import matteroverdrive.api.network.IMatterNetworkDispatcher;
import matteroverdrive.matter_network.MatterNetworkTaskQueue;
import matteroverdrive.network.packet.TileEntityUpdatePacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

/**
 * Created by Simeon on 12/30/2015.
 */
public class PacketSyncTaskQueue extends TileEntityUpdatePacket
{
    int queueID;
    ByteBuf byteBuf;
    MatterNetworkTaskQueue taskQueue;
    public PacketSyncTaskQueue(){}
    public PacketSyncTaskQueue(IMatterNetworkDispatcher tileEntity, int taskQueue)
    {
        super(tileEntity.getPosition().x,tileEntity.getPosition().y,tileEntity.getPosition().z);
        this.taskQueue = tileEntity.getTaskQueue(taskQueue);
        this.queueID = taskQueue;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        super.fromBytes(buf);
        queueID = buf.readByte();
        byteBuf = buf;
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        super.toBytes(buf);
        buf.writeByte(queueID);
        taskQueue.writeToBuffer(buf);
    }

    public static class ClientHandler extends AbstractClientPacketHandler<PacketSyncTaskQueue>
    {

        @Override
        public IMessage handleClientMessage(EntityPlayer player, PacketSyncTaskQueue message, MessageContext ctx)
        {
            TileEntity tileEntity = message.getTileEntity(player.worldObj);
            if (tileEntity != null && tileEntity instanceof IMatterNetworkDispatcher)
            {
                ((IMatterNetworkDispatcher) tileEntity).getTaskQueue(message.queueID).readFromBuffer(message.byteBuf);
            }
            return null;
        }
    }
}
