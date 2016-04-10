package matteroverdrive.network.packet.client;

import io.netty.buffer.ByteBuf;
import matteroverdrive.entity.android_player.AndroidEffects;
import matteroverdrive.entity.android_player.AndroidPlayer;
import matteroverdrive.entity.player.MOPlayerCapabilityProvider;
import matteroverdrive.network.packet.PacketAbstract;
import matteroverdrive.util.MOLog;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.apache.logging.log4j.Level;

import java.io.IOException;
import java.util.List;

/**
 * Created by Simeon on 2/8/2016.
 */
public class PacketSendAndroidEffects extends PacketAbstract
{
	int androidId;
	List<AndroidEffects.Effect> effects;

	public PacketSendAndroidEffects()
	{
	}

	public PacketSendAndroidEffects(int androidId, List<AndroidEffects.Effect> effects)
	{
		this.androidId = androidId;
		this.effects = effects;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		androidId = buf.readInt();
		try
		{
			effects = AndroidEffects.readEffectsListFromBuffer(buf);
		}
		catch (IOException e)
		{
			MOLog.log(Level.ERROR, e, "There was a problem while receiving android effects for player");
		}

	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(androidId);
		try
		{
			AndroidEffects.writeEffectsListToPacketBuffer(effects, buf);
		}
		catch (IOException e)
		{
			MOLog.log(Level.ERROR, e, "There was a problem while sending android effects to player");
		}
	}

	public static class ClientHandler extends AbstractClientPacketHandler<PacketSendAndroidEffects>
	{
		@Override
		public void handleClientMessage(EntityPlayerSP player, PacketSendAndroidEffects message, MessageContext ctx)
		{
			if (message.effects != null)
			{
				Entity entity = player.worldObj.getEntityByID(message.androidId);
				if (entity instanceof EntityPlayer)
				{
					AndroidPlayer androidPlayer = MOPlayerCapabilityProvider.GetAndroidCapability(entity);
					androidPlayer.getAndroidEffects().updateEffectsFromList(message.effects);
				}
			}
		}
	}
}
