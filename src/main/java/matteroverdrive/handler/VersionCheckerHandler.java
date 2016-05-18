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

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.handler.thread.VersionCheckThread;
import matteroverdrive.util.IConfigSubscriber;
import matteroverdrive.util.MOLog;
import matteroverdrive.util.MOStringHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.*;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.Level;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by Simeon on 5/7/2015.
 */
public class VersionCheckerHandler implements IConfigSubscriber
{
	public static final String[] mirrors = new String[] {Reference.VERSIONS_CHECK_URL};
	public Future<String> download;
	int lastPoll = 400;
	private boolean updateInfoDisplayed = false;
	private int currentMirror = 0;
	private boolean checkForUpdates;

	//Called when a player ticks.
	public void onPlayerTick(TickEvent.PlayerTickEvent event)
	{

		if (event.phase != TickEvent.Phase.START || !checkForUpdates)
		{
			return;
		}

		if (FMLCommonHandler.instance().getSide() == Side.SERVER && FMLCommonHandler.instance().getMinecraftServerInstance().isServerRunning())
		{
			// TODO: 3/25/2016 Find how to acccess the configuration manager
			/*if (!event.player.getServer().getConfigurationManager().canSendCommands(event.player.getGameProfile())) {
				return;
            }*/
		}

		if (lastPoll > 0)
		{
			--lastPoll;
			return;
		}
		lastPoll = 400;

		if (updateInfoDisplayed)
		{
			return;
		}

		if (currentMirror < mirrors.length)
		{
			if (download == null)
			{
				download = MatterOverdrive.threadPool.submit(new VersionCheckThread(mirrors[currentMirror]));
				currentMirror++;
			}
		}

		if (download != null && download.isDone())
		{
			String result = null;

			try
			{
				result = download.get();
			}
			catch (InterruptedException e)
			{
				MOLog.log(Level.ERROR, e, "Version checking from '%1$s' was interrupted", mirrors[currentMirror - 1]);
				MatterOverdrive.proxy.getGoogleAnalytics().setExceptionHit("Version Checking interrupted");
			}
			catch (ExecutionException e)
			{
				MOLog.log(Level.ERROR, e, "Version checking from '%1$s' has failed", mirrors[currentMirror - 1]);
				MatterOverdrive.proxy.getGoogleAnalytics().setExceptionHit("Version Checking failed");
			}
			finally
			{
				if (result != null)
				{
					try
					{
						updateInfoDisplayed = constructVersionAndCheck(result, event.player);
					}
					catch (Exception e)
					{
						MOLog.log(Level.ERROR, e, "There was a problem while decoding the update info from website.");
					}
				}

				download.cancel(false);
				download = null;
			}
		}
	}

	private boolean constructVersionAndCheck(String jsonText, EntityPlayer player)
	{
		JsonParser parser = new JsonParser();
		JsonObject root = parser.parse(jsonText).getAsJsonArray().get(0).getAsJsonObject();
		SimpleDateFormat websiteDatePraser = new SimpleDateFormat("y-M-d");
		SimpleDateFormat modDateFormat = new SimpleDateFormat("d.M.y");
		String websiteDateString = root.get("date").getAsString();
		websiteDateString = websiteDateString.substring(0, websiteDateString.indexOf('T'));
		Date websiteDate = null;
		Date modDate = null;
		try
		{
			websiteDate = websiteDatePraser.parse(websiteDateString);
		}
		catch (ParseException e)
		{
			MOLog.warn("Website date was incorrect", e);
		}
		try
		{
			modDate = modDateFormat.parse(Reference.VERSION_DATE);
		}
		catch (ParseException e)
		{
			MOLog.warn("Mod version date was incorrect", e);
		}

		if (modDate != null && websiteDate != null)
		{

			if (modDate.before(websiteDate))
			{
				TextComponentString chat = new TextComponentString(TextFormatting.GOLD + "[Matter Overdrive] " + TextFormatting.WHITE + MOStringHelper.translateToLocal("alert.new_update"));
				Style style = new Style();
				player.addChatMessage(chat);

				chat = new TextComponentString("");
				ITextComponent versionName = new TextComponentString(root.get("title").getAsString() + " ").setChatStyle(new Style().setColor(TextFormatting.AQUA));
				chat.appendSibling(versionName);
				chat.appendText(TextFormatting.WHITE + "[");
				style.setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, Reference.DOWNLOAD_URL));
				style.setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentTranslation("info." + Reference.MOD_ID + ".updater.hover").setChatStyle(new Style().setColor(TextFormatting.YELLOW))));
				style.setColor(TextFormatting.GREEN);
				chat.appendSibling(new TextComponentTranslation("info." + Reference.MOD_ID + ".updater.download")).setChatStyle(style);
				chat.appendText(TextFormatting.WHITE + "]");
				player.addChatMessage(chat);

				chat = new TextComponentString(root.get("excerpt").getAsString().replaceAll("<.*?>", ""));
				style = new Style();
				style.setColor(TextFormatting.GRAY);
				chat.setChatStyle(style);
				player.addChatMessage(chat);
				return true;

			}
			else
			{
				MOLog.info("Matter Overdrive Version %1$s is up to date. From '%2$s'", root.get("title").getAsString(), mirrors[currentMirror - 1]);
			}
		}
		return false;
	}

	@Override
	public void onConfigChanged(ConfigurationHandler config)
	{
		String comment = "Should Matter Overdrive check for newer versions, every time the world starts";
		checkForUpdates = config.getBool(ConfigurationHandler.KEY_VERSION_CHECK, ConfigurationHandler.CATEGORY_CLIENT, true, comment);
		config.config.get(ConfigurationHandler.CATEGORY_CLIENT, ConfigurationHandler.KEY_VERSION_CHECK, true).setComment(comment);
	}
}
