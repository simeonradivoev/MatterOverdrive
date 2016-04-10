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

package matteroverdrive.data.dialog;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import matteroverdrive.api.dialog.IDialogMessage;
import matteroverdrive.api.dialog.IDialogMessageSeedable;
import matteroverdrive.api.dialog.IDialogNpc;
import matteroverdrive.api.dialog.IDialogOption;
import matteroverdrive.api.events.MOEventDialogInteract;
import matteroverdrive.api.exceptions.MORuntimeException;
import matteroverdrive.api.renderer.IDialogShot;
import matteroverdrive.gui.GuiDialog;
import matteroverdrive.util.MOJsonHelper;
import matteroverdrive.util.MOStringHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Simeon on 8/9/2015.
 */
public class DialogMessage implements IDialogMessage, IDialogMessageSeedable
{
	protected static Random random = new Random();
	protected String[] messages;
	protected String[] questions;
	protected boolean unlocalized;
	protected IDialogMessage parent;
	protected List<IDialogOption> options;
	@SideOnly(Side.CLIENT)
	protected IDialogShot[] shots;
	@SideOnly(Side.CLIENT)
	protected String holoIcon;
	protected long seed;

	public DialogMessage(JsonObject object)
	{
		init();
		this.unlocalized = MOJsonHelper.getBool(object, "unlocalized", false);
		if (object.has("message"))
		{
			JsonElement messageElement = object.get("message");
			if (messageElement.isJsonArray())
			{
				messages = MOJsonHelper.getStringArray(object, "message");
			}
			else if (messageElement.isJsonPrimitive() && messageElement.getAsJsonPrimitive().isString())
			{
				messages = new String[] {messageElement.getAsString()};
			}
		}
		else
		{
			throw new MORuntimeException(String.format("Cannot find Message for dialog"));
		}
		if (object.has("question"))
		{
			JsonElement questionElement = object.get("question");
			if (questionElement.isJsonArray())
			{
				questions = MOJsonHelper.getStringArray(object, "message");
			}
			else if (questionElement.isJsonPrimitive() && questionElement.getAsJsonPrimitive().isString())
			{
				questions = new String[] {questionElement.getAsString()};
			}
		}
	}

	public DialogMessage()
	{
		init();
	}

	public DialogMessage(String message)
	{
		this(message, message);
	}

	public DialogMessage(String message, String question)
	{
		this(message != null ? new String[] {message} : null, question != null ? new String[] {question} : null);
	}

	public DialogMessage(String[] messages, String[] questions)
	{
		this.messages = messages;
		this.questions = questions;
		init();
	}

	private void init()
	{
		options = new ArrayList<>();
	}

	@Override
	public IDialogMessage getParent(IDialogNpc npc, EntityPlayer player)
	{
		return parent;
	}

	@Override
	public List<IDialogOption> getOptions(IDialogNpc npc, EntityPlayer player)
	{
		return options;
	}

	@Override
	public String getMessageText(IDialogNpc npc, EntityPlayer player)
	{
		if (messages != null && messages.length > 0)
		{
			int messageIndex = 0;
			if (messages.length > 1)
			{
				messageIndex = random.nextInt(messages.length);
			}
			return formatMessage(messages[messageIndex], npc, player);
		}
		return "";
	}

	@Override
	public String getQuestionText(IDialogNpc npc, EntityPlayer player)
	{
		if (questions != null && questions.length > 0)
		{
			int questionIndex = 0;
			if (questions.length > 1)
			{
				questionIndex = random.nextInt(questions.length);
			}
			return formatQuestion(questions[questionIndex], npc, player);
		}
		return "";
	}

	@Override
	public void onOptionsInteract(IDialogNpc npc, EntityPlayer player, int option)
	{
		if (option >= 0 && option < options.size())
		{
			options.get(option).onInteract(npc, player);
		}
	}

	@Override
	public void onInteract(IDialogNpc npc, EntityPlayer player)
	{
		if (npc != null && player != null)
		{
			if (player.worldObj.isRemote)
			{
				if (!MinecraftForge.EVENT_BUS.post(new MOEventDialogInteract(player, npc, this, Side.CLIENT)))
				{
					setAsGuiActiveMessage(npc, player);
				}
			}
			else
			{
				if (!MinecraftForge.EVENT_BUS.post(new MOEventDialogInteract(player, npc, this, Side.SERVER)))
				{
					npc.onPlayerInteract(player, this);
				}
			}
		}
	}

	@SideOnly(Side.CLIENT)
	protected void setAsGuiActiveMessage(IDialogNpc npc, EntityPlayer player)
	{
		if (Minecraft.getMinecraft().currentScreen instanceof GuiDialog)
		{
			((GuiDialog)Minecraft.getMinecraft().currentScreen).setCurrentMessage(this);
		}
	}

	@Override
	public boolean canInteract(IDialogNpc npc, EntityPlayer player)
	{
		return true;
	}

	@Override
	public boolean isVisible(IDialogNpc npc, EntityPlayer player)
	{
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IDialogShot[] getShots(IDialogNpc npc, EntityPlayer player)
	{
		return shots;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public String getHoloIcon(IDialogNpc npc, EntityPlayer player)
	{
		return holoIcon;
	}

	@Override
	public boolean equalsOption(IDialogOption other)
	{
		return this.equals(other);
	}

	@SideOnly(Side.CLIENT)
	public void setShots(IDialogShot... shot)
	{
		this.shots = shot;
	}

	public void setParent(IDialogMessage parent)
	{
		this.parent = parent;
	}

	public void addOption(IDialogOption message)
	{
		this.options.add(message);
	}

	public IDialogOption getOption(int id)
	{
		return this.options.get(id);
	}

	public List<IDialogOption> getOptions()
	{
		return options;
	}

	@SideOnly(Side.CLIENT)
	public DialogMessage setHoloIcon(String holoIcon)
	{
		this.holoIcon = holoIcon;
		return this;
	}

	protected String formatMessage(String text, IDialogNpc npc, EntityPlayer player)
	{
		if (text != null)
		{
			return String.format(unlocalized ? MOStringHelper.translateToLocal(text) : text, player.getDisplayName().getFormattedText(), npc.getEntity().getDisplayName().getFormattedText());
		}
		return null;
	}

	protected String formatQuestion(String text, IDialogNpc npc, EntityPlayer player)
	{
		if (text != null)
		{
			return String.format(unlocalized ? MOStringHelper.translateToLocal(text) : text, player.getDisplayName().getFormattedText(), npc.getEntity().getDisplayName().getFormattedText());
		}
		return null;
	}

	public DialogMessage setUnlocalized(boolean unlocalized)
	{
		this.unlocalized = unlocalized;
		return this;
	}

	@Override
	public void setSeed(long seed)
	{
		this.seed = seed;
		random.setSeed(seed);
	}

	public void setQuestions(String[] questions)
	{
		this.questions = questions;
	}

	public void setMessages(String[] messages)
	{
		this.messages = messages;
	}
}
