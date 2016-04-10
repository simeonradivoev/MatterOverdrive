package matteroverdrive.network.packet.client.task_queue;

import io.netty.buffer.ByteBuf;
import matteroverdrive.api.network.IMatterNetworkDispatcher;
import matteroverdrive.matter_network.MatterNetworkTaskQueue;
import matteroverdrive.network.packet.TileEntityUpdatePacket;
import matteroverdrive.network.packet.client.AbstractClientPacketHandler;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Simeon on 12/30/2015.
 */
public class PacketSyncTaskQueue extends TileEntityUpdatePacket
{
	int queueID;
	ByteBuf byteBuf;
	MatterNetworkTaskQueue taskQueue;

	public PacketSyncTaskQueue()
	{
	}

	public PacketSyncTaskQueue(IMatterNetworkDispatcher dispatcher, int taskQueue)
	{
		super(dispatcher.getPos());
		this.taskQueue = dispatcher.getTaskQueue(taskQueue);
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
		@SideOnly(Side.CLIENT)
		@Override
		public void handleClientMessage(EntityPlayerSP player, PacketSyncTaskQueue message, MessageContext ctx)
		{
			TileEntity tileEntity = message.getTileEntity(player.worldObj);
			if (tileEntity != null && tileEntity instanceof IMatterNetworkDispatcher)
			{
				((IMatterNetworkDispatcher)tileEntity).getTaskQueue(message.queueID).readFromBuffer(message.byteBuf);
			}
		}
	}
}
