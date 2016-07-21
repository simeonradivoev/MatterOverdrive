package matteroverdrive.network.packet.client;

import io.netty.buffer.ByteBuf;
import matteroverdrive.init.MatterOverdriveCapabilities;
import matteroverdrive.network.packet.TileEntityUpdatePacket;
import matteroverdrive.tile.MOTileEntityMachineMatter;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Simeon on 4/22/2015.
 */
public class PacketMatterUpdate extends TileEntityUpdatePacket
{
	private int matter = 0;

	public PacketMatterUpdate()
	{
	}

	public PacketMatterUpdate(TileEntity tileentity)
	{
		super(tileentity.getPos());
		matter = tileentity.getCapability(MatterOverdriveCapabilities.MATTER_HANDLER, null).getMatterStored();
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		super.fromBytes(buf);
		matter = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		super.toBytes(buf);
		buf.writeInt(matter);
	}

	public static class ClientHandler extends AbstractClientPacketHandler<PacketMatterUpdate>
	{
		@SideOnly(Side.CLIENT)
		@Override
		public void handleClientMessage(EntityPlayerSP player, PacketMatterUpdate message, MessageContext ctx)
		{
			if (player != null && player.worldObj != null)
			{
				TileEntity tileEntity = player.worldObj.getTileEntity(message.pos);

				if (tileEntity != null && tileEntity.hasCapability(MatterOverdriveCapabilities.MATTER_HANDLER, null))
				{
					tileEntity.getCapability(MatterOverdriveCapabilities.MATTER_HANDLER, null).setMatterStored(message.matter);
				}
			}
		}
	}
}
