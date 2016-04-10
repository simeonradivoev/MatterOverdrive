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

package matteroverdrive.handler;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.api.android.IBioticStat;
import matteroverdrive.entity.android_player.AndroidPlayer;
import matteroverdrive.entity.player.MOPlayerCapabilityProvider;
import matteroverdrive.network.packet.server.PacketBioticActionKey;
import matteroverdrive.proxy.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

/**
 * Created by Simeon on 3/6/2015.
 */
@SideOnly(Side.CLIENT)
public class KeyHandler
{
	public static final int ABILITY_USE_KEY = 0;
	public static final int ABILITY_SWITCH_KEY = 1;
	private static final String[] keyDesc = {"Open Matter Scanner GUI", "Android Ability key", "Android Switch Ability key"};
	private static final int[] keyValues = {Keyboard.KEY_X, Keyboard.KEY_R};
	private final KeyBinding[] keys;

	public KeyHandler()
	{
		keys = new KeyBinding[keyValues.length];
		for (int i = 0; i < keys.length; i++)
		{
			keys[i] = new KeyBinding(keyDesc[i], keyValues[i], "Matter Overdrive");
			ClientRegistry.registerKeyBinding(keys[i]);
		}
	}

	@SubscribeEvent
	public void onClientTick(TickEvent.ClientTickEvent event)
	{
		if (Minecraft.getMinecraft().thePlayer == null || Minecraft.getMinecraft().theWorld == null || Minecraft.getMinecraft().isGamePaused() || Minecraft.getMinecraft().currentScreen != null)
		{
			return;
		}

		AndroidPlayer androidPlayer = MOPlayerCapabilityProvider.GetAndroidCapability(FMLClientHandler.instance().getClientPlayerEntity());
		if (androidPlayer.isAndroid() && ClientProxy.keyHandler.getBinding(KeyHandler.ABILITY_USE_KEY).isPressed())
		{
			for (IBioticStat stat : MatterOverdrive.statRegistry.getStats())
			{
				int level = androidPlayer.getUnlockedLevel(stat);
				if (level > 0 && stat.isEnabled(androidPlayer, level))
				{
					stat.onActionKeyPress(androidPlayer, androidPlayer.getUnlockedLevel(stat), false);
				}
			}
			MatterOverdrive.packetPipeline.sendToServer(new PacketBioticActionKey());
		}
	}

	public void manageBiostats(int keyCode, boolean state)
	{
		AndroidPlayer androidPlayer = MOPlayerCapabilityProvider.GetAndroidCapability(FMLClientHandler.instance().getClientPlayerEntity());
		if (androidPlayer.isAndroid())
		{
			for (IBioticStat stat : MatterOverdrive.statRegistry.getStats())
			{
				int level = androidPlayer.getUnlockedLevel(stat);
				if (level > 0 && stat.isEnabled(androidPlayer, level))
				{
					stat.onKeyPress(androidPlayer, androidPlayer.getUnlockedLevel(stat), keyCode, state);
				}
			}
		}
	}

	public KeyBinding getBinding(int id)
	{
		return keys[id];
	}
}
