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

package matteroverdrive.network.packet.server;

import io.netty.buffer.ByteBuf;
import matteroverdrive.entity.android_player.AndroidPlayer;
import matteroverdrive.entity.player.MOPlayerCapabilityProvider;
import matteroverdrive.network.packet.PacketAbstract;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created by Simeon on 9/30/2015.
 */
public class PacketResetBioStats extends PacketAbstract
{
	@Override
	public void fromBytes(ByteBuf buf)
	{

	}

	@Override
	public void toBytes(ByteBuf buf)
	{

	}

	public static class ServerHandler extends AbstractServerPacketHandler<PacketResetBioStats>
	{

		@Override
		public void handleServerMessage(EntityPlayerMP player, PacketResetBioStats message, MessageContext ctx)
		{
			AndroidPlayer androidPlayer = MOPlayerCapabilityProvider.GetAndroidCapability(player);
			if (androidPlayer != null && androidPlayer.isAndroid())
			{
				player.addExperienceLevel(androidPlayer.resetUnlocked());
			}
		}
	}
}
