/*
 * This file is part of Matter Overdrive
 * Copyright (c) 2015., Simeon Radivoev, All rights reserved.
 *
 * Matter Overdrive is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Matter Overdrive is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Matter Overdrive.  If not, see <http://www.gnu.org/licenses>.
 */

package matteroverdrive.network;

import io.netty.channel.ChannelHandler;
import matteroverdrive.Reference;
import matteroverdrive.network.packet.AbstractBiPacketHandler;
import matteroverdrive.network.packet.bi.PacketFirePlasmaShot;
import matteroverdrive.network.packet.bi.PacketMatterScannerGetDatabase;
import matteroverdrive.network.packet.bi.PacketStarLoading;
import matteroverdrive.network.packet.bi.PacketWeaponTick;
import matteroverdrive.network.packet.client.*;
import matteroverdrive.network.packet.client.pattern_monitor.PacketClearPatterns;
import matteroverdrive.network.packet.client.pattern_monitor.PacketSendItemPattern;
import matteroverdrive.network.packet.client.quest.PacketSyncQuests;
import matteroverdrive.network.packet.client.quest.PacketUpdateQuest;
import matteroverdrive.network.packet.client.starmap.PacketUpdateGalaxy;
import matteroverdrive.network.packet.client.starmap.PacketUpdatePlanet;
import matteroverdrive.network.packet.client.starmap.PacketUpdateTravelEvents;
import matteroverdrive.network.packet.client.task_queue.PacketSyncTaskQueue;
import matteroverdrive.network.packet.server.*;
import matteroverdrive.network.packet.server.pattern_monitor.PacketPatternMonitorAddRequest;
import matteroverdrive.network.packet.server.starmap.PacketStarMapAttack;
import matteroverdrive.network.packet.server.starmap.PacketStarMapClientCommands;
import matteroverdrive.network.packet.server.task_queue.PacketRemoveTask;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Created by Simeon on 3/5/2015.
 */
@ChannelHandler.Sharable
public class PacketPipeline
{
	public final SimpleNetworkWrapper dispatcher;
	protected int packetID;

	public PacketPipeline()
	{
		dispatcher = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.CHANNEL_NAME);
		packetID = 0;
	}

	public void registerPackets()
	{
		registerPacket(PacketMatterScannerUpdate.ServerHandler.class, PacketMatterScannerUpdate.class);
		registerPacket(PacketPowerUpdate.ClientHandler.class, PacketPowerUpdate.class);
		registerPacket(PacketMatterUpdate.ClientHandler.class, PacketMatterUpdate.class);
		registerPacket(PacketSendItemPattern.ClientHandler.class, PacketSendItemPattern.class);
		registerPacket(PacketPatternMonitorAddRequest.ServerHandler.class, PacketPatternMonitorAddRequest.class);
		registerPacket(PacketReplicationComplete.ClientHandler.class, PacketReplicationComplete.class);
		registerPacket(PacketRemoveTask.ServerHandler.class, PacketRemoveTask.class);
		registerPacket(PacketSyncTransportProgress.ClientHandler.class, PacketSyncTransportProgress.class);
		registerBiPacket(PacketMatterScannerGetDatabase.Handler.class, PacketMatterScannerGetDatabase.class);
		registerPacket(PacketUpdateMatterRegistry.ClientHandler.class, PacketUpdateMatterRegistry.class);
		registerPacket(PacketSyncAndroid.ClientHandler.class, PacketSyncAndroid.class);
		registerPacket(PacketUnlockBioticStat.ServerHandler.class, PacketUnlockBioticStat.class);
		registerPacket(PacketTeleportPlayer.ServerHandler.class, PacketTeleportPlayer.class);
		registerPacket(PacketSpawnParticle.ClientHandler.class, PacketSpawnParticle.class);
		registerPacket(PacketUpdatePlanet.ClientHandler.class, PacketUpdatePlanet.class);
		registerPacket(PacketUpdateGalaxy.ClientHandler.class, PacketUpdateGalaxy.class);
		registerPacket(PacketStarMapClientCommands.ServerHandler.class, PacketStarMapClientCommands.class);
		registerPacket(PacketStarMapAttack.ServerHandler.class, PacketStarMapAttack.class);
		registerPacket(PacketUpdateTravelEvents.ClientHandler.class, PacketUpdateTravelEvents.class);
		registerPacket(PacketAndroidChangeAbility.ServerHandler.class, PacketAndroidChangeAbility.class);
		registerBiPacket(PacketFirePlasmaShot.BiHandler.class, PacketFirePlasmaShot.class);
		registerPacket(PacketReloadEnergyWeapon.ServerHandler.class, PacketReloadEnergyWeapon.class);
		registerBiPacket(PacketManageConversation.BiHandler.class, PacketManageConversation.class);
		registerPacket(PacketConversationInteract.ServerHandler.class, PacketConversationInteract.class);
		registerBiPacket(PacketSendMachineNBT.BiHandler.class, PacketSendMachineNBT.class);
		registerPacket(PacketSendQueueFlash.ClientHandler.class, PacketSendQueueFlash.class);
		registerPacket(PacketDataPadCommands.ServerHandler.class, PacketDataPadCommands.class);
		registerPacket(PacketSendMinimapInfo.ClientHandler.class, PacketSendMinimapInfo.class);
		registerPacket(PacketResetBioStats.ServerHandler.class, PacketResetBioStats.class);
		registerPacket(PacketDigBlock.ServerHandler.class, PacketDigBlock.class);
		registerPacket(PacketUpdateQuest.ClientHandler.class, PacketUpdateQuest.class);
		registerPacket(PacketSyncQuests.ClientHandler.class, PacketSyncQuests.class);
		registerPacket(PacketQuestActions.ServerHandler.class, PacketQuestActions.class);
		registerPacket(PacketWeaponTick.ServerHandler.class, PacketWeaponTick.class);
		registerBiPacket(PacketStarLoading.BiHandler.class, PacketStarLoading.class);
		registerPacket(PacketAndroidTransformation.ClientHandler.class, PacketAndroidTransformation.class);
		registerPacket(PacketSyncTaskQueue.ClientHandler.class, PacketSyncTaskQueue.class);
		registerPacket(PacketBioticActionKey.ServerHandler.class, PacketBioticActionKey.class);
		registerPacket(PacketUpdatePlasmaBolt.ClientHandler.class, PacketUpdatePlasmaBolt.class);
		registerPacket(PacketClearPatterns.ClientHandler.class, PacketClearPatterns.class);
		registerPacket(PacketSendAndroidEffects.ClientHandler.class, PacketSendAndroidEffects.class);
	}

	public <REQ extends IMessage, REPLY extends IMessage> void registerPacket(Class<? extends IMessageHandler<REQ, REPLY>> messageHandler, Class<REQ> requestMessageType)
	{
		try
		{
			Side side = AbstractClientPacketHandler.class.isAssignableFrom(messageHandler) ? Side.CLIENT : Side.SERVER;
			dispatcher.registerMessage(messageHandler, requestMessageType, packetID++, side);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public <REQ extends IMessage, REPLY extends IMessage> void registerBiPacket(Class<? extends IMessageHandler<REQ, REPLY>> messageHandler, Class<REQ> requestMessageType)
	{
		if (AbstractBiPacketHandler.class.isAssignableFrom(messageHandler))
		{
			dispatcher.registerMessage(messageHandler, requestMessageType, packetID, Side.CLIENT);
			dispatcher.registerMessage(messageHandler, requestMessageType, packetID++, Side.SERVER);
		}
		else
		{
			throw new IllegalArgumentException("Cannot register " + messageHandler.getName() + " on both sides - must extend AbstractBiMessageHandler!");
		}
	}

	//region Util functions
	public void sendToServer(IMessage message)
	{
		dispatcher.sendToServer(message);
	}

	public void sendToAllAround(IMessage message, NetworkRegistry.TargetPoint point)
	{
		dispatcher.sendToAllAround(message, point);
	}

	public void sendToAllAround(IMessage message, int dimention, double x, double y, double z, double range)
	{
		dispatcher.sendToAllAround(message, new NetworkRegistry.TargetPoint(dimention, x, y, z, range));
	}

	public void sendToAllAround(IMessage message, EntityPlayer player, double range)
	{
		dispatcher.sendToAllAround(message, new NetworkRegistry.TargetPoint(player.worldObj.provider.getDimension(), player.posX, player.posY, player.posZ, range));
	}

	public void sendToAllAround(IMessage message, TileEntity tileEntity, double range)
	{
		dispatcher.sendToAllAround(message, new NetworkRegistry.TargetPoint(tileEntity.getWorld().provider.getDimension(), tileEntity.getPos().getX(), tileEntity.getPos().getY(), tileEntity.getPos().getZ(), range));
	}

	public void sendTo(IMessage message, EntityPlayerMP player)
	{
		dispatcher.sendTo(message, player);
	}

	public void sendToDimention(IMessage message, int dimention)
	{
		dispatcher.sendToDimension(message, dimention);
	}

	public void sendToDimention(IMessage message, World world)
	{
		sendToDimention(message, world.provider);
	}

	public void sendToDimention(IMessage message, WorldProvider worldProvider)
	{
		dispatcher.sendToDimension(message, worldProvider.getDimension());
	}
	//endregion
}
