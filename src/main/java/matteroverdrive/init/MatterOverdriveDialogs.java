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

package matteroverdrive.init;

import matteroverdrive.Reference;
import matteroverdrive.api.dialog.IDialogRegistry;
import matteroverdrive.data.dialog.*;
import matteroverdrive.handler.ConfigurationHandler;
import matteroverdrive.util.MOStringHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Created by Simeon on 8/13/2015.
 */
public class MatterOverdriveDialogs
{
	public static DialogMessage backMessage;
	public static DialogMessage backHomeMessage;
	public static DialogMessage quitMessage;
	public static DialogMessage trade;
	//public static DialogMessage tradeRouteQuest;

	public static void init(ConfigurationHandler configurationHandler, IDialogRegistry registry)
	{
		backMessage = new DialogMessageBack(null, MOStringHelper.formatVariations("dialog.generic.back", "question", 3)).setUnlocalized(true);
		registry.registerMessage(new ResourceLocation(Reference.MOD_ID, "back"), backMessage);
		quitMessage = new DialogMessageQuit(null, MOStringHelper.formatVariations("dialog.generic.quit", "question", 5)).setUnlocalized(true);
		registry.registerMessage(new ResourceLocation(Reference.MOD_ID, "quit"), quitMessage);
		backHomeMessage = new DialogMessageBackToMain(null, MOStringHelper.formatVariations("dialog.generic.back", "question", 3)).setUnlocalized(true);
		registry.registerMessage(new ResourceLocation(Reference.MOD_ID, "back_home"), backHomeMessage);
		trade = new DialogMessageTrade(null, MOStringHelper.formatVariations("dialog.generic.trade", "question", 4)).setUnlocalized(true);
		registry.registerMessage(new ResourceLocation(Reference.MOD_ID, "trade"), trade);
		//tradeRouteQuest = new DialogMessage("dialog.mad_scientist.trade_route_quest.0.line","dialog.mad_scientist.trade_route_quest.0.question").setUnlocalized(true);
		//registry.registerMessage(tradeRouteQuest);
		//DialogMessage tradeRouteQuest = new DialogMessage("dialog.mad_scientist.trade_route_quest.1.line","dialog.mad_scientist.trade_route_quest.1.question").setUnlocalized(true);
		//registry.registerMessage(tradeRouteQuest);
		//MatterOverdriveDialogs.tradeRouteQuest.addOption(tradeRouteQuest);
		//tradeRouteQuest.addOption(backHomeMessage);

		if (FMLCommonHandler.instance().getSide() == Side.CLIENT)
		{
			backMessage.setHoloIcon("mini_quit");
			quitMessage.setHoloIcon("mini_quit");
			backHomeMessage.setHoloIcon("mini_quit");
			trade.setHoloIcon("trade");
		}
	}
}
