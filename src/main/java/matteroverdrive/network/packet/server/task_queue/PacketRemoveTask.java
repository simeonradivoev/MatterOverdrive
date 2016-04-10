package matteroverdrive.network.packet.server.task_queue;

import io.netty.buffer.ByteBuf;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.api.network.IMatterNetworkDispatcher;
import matteroverdrive.api.network.MatterNetworkTaskState;
import matteroverdrive.network.packet.TileEntityUpdatePacket;
import matteroverdrive.network.packet.client.task_queue.PacketSyncTaskQueue;
import matteroverdrive.network.packet.server.AbstractServerPacketHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created by Simeon on 4/28/2015.
 */
public class PacketRemoveTask extends TileEntityUpdatePacket
{
	int taskIndex;
	byte queueID;
	MatterNetworkTaskState task_state;

	public PacketRemoveTask()
	{
		super();
	}

	public PacketRemoveTask(TileEntity dispatcher, int taskIndex, byte queueID, MatterNetworkTaskState task_state)
	{
		super(dispatcher);
		this.taskIndex = taskIndex;
		this.queueID = queueID;
		this.task_state = task_state;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		super.fromBytes(buf);
		taskIndex = buf.readInt();
		queueID = buf.readByte();
		task_state = MatterNetworkTaskState.get(buf.readByte());
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		super.toBytes(buf);
		buf.writeInt(taskIndex);
		buf.writeByte(queueID);
		buf.writeByte(task_state.ordinal());
	}

	public static class ServerHandler extends AbstractServerPacketHandler<PacketRemoveTask>
	{

		@Override
		public void handleServerMessage(EntityPlayerMP player, PacketRemoveTask message, MessageContext ctx)
		{
			TileEntity entity = message.getTileEntity(player.worldObj);

			if (entity instanceof IMatterNetworkDispatcher)
			{
				IMatterNetworkDispatcher dispatcher = (IMatterNetworkDispatcher)entity;
				dispatcher.getTaskQueue(message.queueID).dropAt(message.taskIndex).setState(message.task_state);
				MatterOverdrive.packetPipeline.sendTo(new PacketSyncTaskQueue(dispatcher, message.queueID), player);
			}
		}
	}
}
