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

package matteroverdrive.network.packet.client;

import io.netty.buffer.ByteBuf;
import matteroverdrive.data.matter.MatterEntryItem;
import matteroverdrive.handler.MatterRegistry;
import matteroverdrive.network.packet.PacketAbstract;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Map;

/**
 * Created by Simeon on 5/9/2015.
 */
public class PacketUpdateMatterRegistry extends PacketAbstract
{
	private static Map<String, MatterEntryItem> entries;

	public PacketUpdateMatterRegistry()
	{
		super();
	}

	public PacketUpdateMatterRegistry(MatterRegistry matterRegistry)
	{
		super();
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{

	}

	@Override
	public void toBytes(ByteBuf buf)
	{

	}

	public static class ClientHandler extends AbstractClientPacketHandler<PacketUpdateMatterRegistry>
	{
		@SideOnly(Side.CLIENT)
		@Override
		public void handleClientMessage(EntityPlayerSP player, PacketUpdateMatterRegistry message, MessageContext ctx)
		{
			//MatterOverdrive.matterRegistry.hasComplitedRegistration = true;
		}
	}
}
