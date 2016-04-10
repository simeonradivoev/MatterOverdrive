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

package matteroverdrive.gui;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.api.dialog.IDialogMessage;
import matteroverdrive.api.dialog.IDialogMessageSeedable;
import matteroverdrive.api.dialog.IDialogNpc;
import matteroverdrive.api.dialog.IDialogOption;
import matteroverdrive.network.packet.server.PacketConversationInteract;
import matteroverdrive.network.packet.server.PacketManageConversation;
import matteroverdrive.proxy.ClientProxy;
import matteroverdrive.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.lwjgl.opengl.GL11.GL_ONE;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;

/**
 * Created by Simeon on 8/9/2015.
 */
public class GuiDialog extends GuiScreen
{
	public static final ResourceLocation separator_texture = new ResourceLocation(Reference.PATH_ELEMENTS + "dialog_separator.png");
	private static final int INTERACTION_DELAY = 20;
	private static Random random = new Random();
	private IDialogNpc npc;
	private EntityPlayer player;
	private long seed;
	private IDialogMessage currentMessage;
	private float lastInteractionTime;

	public GuiDialog(IDialogNpc npc, EntityPlayer player)
	{
		this.npc = npc;
		this.player = player;
		seed = random.nextLong();
		currentMessage = npc.getStartDialogMessage(player);
	}

	public void initGui()
	{

	}

	public void drawScreen(int mouseX, int mouseY, float ticks)
	{
		super.drawScreen(mouseX, mouseY, ticks);

		if (npc == null || npc.getEntity().isDead)
		{
			player.closeScreen();
			return;
		}

		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.disableTexture2D();
		//GlStateManager.enableAlpha();
		GlStateManager.color(0, 0, 0, 1);
		drawTexturedModalRect(0, 0, 0, 0, width, height / 8);
		drawTexturedModalRect(0, height - height / 8, 0, 0, width, height / 8);
		GlStateManager.color(0, 0, 0, 0.4f);
		drawTexturedModalRect(0, height - height / 8 - 128, 0, 0, width, 128);
		//GlStateManager.disableAlpha();
		GlStateManager.enableTexture2D();

		drawDialog(mouseX, mouseY, ticks);

		if (lastInteractionTime > 0)
		{
			lastInteractionTime = Math.max(0, lastInteractionTime - ticks);
		}

		GlStateManager.disableBlend();
	}

	public void drawDialog(int mouseX, int mouseY, float ticks)
	{
		if (currentMessage instanceof IDialogMessageSeedable)
		{
			((IDialogMessageSeedable)currentMessage).setSeed(seed);
		}

		int messageWidth;
		String message = currentMessage.getMessageText(npc, player);
		if (message != null && !message.isEmpty())
		{
			List<String> splitMessage = new ArrayList<>();
			String[] list = message.split("<br>");
			for (String aList : list)
			{
				splitMessage.addAll(fontRendererObj.listFormattedStringToWidth(aList, width / 3));
			}
			for (int i = 0; i < splitMessage.size(); i++)
			{
				String m = splitMessage.get(i);
				messageWidth = fontRendererObj.getStringWidth(m);
				fontRendererObj.drawString(m, width / 2 - messageWidth - 16, height - height / 8 - 64 - (splitMessage.size() * fontRendererObj.FONT_HEIGHT / 2) + fontRendererObj.FONT_HEIGHT * i, Reference.COLOR_HOLO.getColor());
			}
		}

		GlStateManager.blendFunc(GL_ONE, GL_ONE);
		Minecraft.getMinecraft().renderEngine.bindTexture(separator_texture);
		RenderUtils.applyColorWithMultipy(Reference.COLOR_HOLO, 0.5f);
		drawModalRectWithCustomSizedTexture(width / 2 - 5, height - height / 8 - 128, 0, 0, 11, 128, 11, 128);

		List<IDialogOption> options = currentMessage.getOptions(npc, player);
		int visibleOptionsCount = 0;
		for (IDialogOption option : options)
		{
			if (option.isVisible(npc, player))
			{
				visibleOptionsCount++;
			}
		}
		int optionPos = 0;
		for (IDialogOption option : options)
		{
			if (option instanceof IDialogMessageSeedable)
			{
				((IDialogMessageSeedable)option).setSeed(seed);
			}

			if (option.isVisible(npc, player))
			{
				int messageX = width / 2 + 26;
				int messageY = height - height / 8 - 60 + (18 * optionPos) - ((visibleOptionsCount * 18) / 2);
				String optionText = option.getQuestionText(npc, player);
				messageWidth = fontRendererObj.getStringWidth(optionText);


				boolean canInteract = option.canInteract(npc, player);
				if (mouseX > messageX && mouseX <= messageX + messageWidth && mouseY > messageY && mouseY <= messageY + fontRendererObj.FONT_HEIGHT)
				{
					GlStateManager.disableTexture2D();
					GlStateManager.blendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
					GlStateManager.color(0, 0, 0, 0.4f);
					drawModalRectWithCustomSizedTexture(messageX - 2, messageY - 4, 0, 0, messageWidth + 10, fontRendererObj.FONT_HEIGHT + 8, messageWidth + 8, fontRendererObj.FONT_HEIGHT + 8);
					if (canInteract)
					{
						GlStateManager.color(1, 1, 1, 0.6f);
					}
					else
					{
						RenderUtils.applyColor(Reference.COLOR_HOLO_RED);
					}
					drawModalRectWithCustomSizedTexture(messageX - 2 + messageWidth + 10, messageY - 4, 0, 0, 2, fontRendererObj.FONT_HEIGHT + 8, 2, fontRendererObj.FONT_HEIGHT + 8);
					GlStateManager.enableTexture2D();

					if (option.getHoloIcon(npc, player) != null)
					{
						GlStateManager.color(1, 1, 1);
						ClientProxy.holoIcons.renderIcon(option.getHoloIcon(npc, player), messageX - 18, messageY + fontRendererObj.FONT_HEIGHT / 2 - 8, 16, 16);
					}
					fontRendererObj.drawString(optionText, messageX + 2, messageY, canInteract ? 0xFFFFFF : Reference.COLOR_HOLO_RED.getColor());
				}
				else
				{
					if (option.getHoloIcon(npc, player) != null)
					{
						RenderUtils.applyColor(Reference.COLOR_HOLO);
						ClientProxy.holoIcons.renderIcon(option.getHoloIcon(npc, player), messageX - 18, messageY + fontRendererObj.FONT_HEIGHT / 2 - 8, 16, 16);
					}

					fontRendererObj.drawString(optionText, messageX, messageY, canInteract ? Reference.COLOR_HOLO.getColor() : Reference.COLOR_HOLO_RED.getColor());
				}
				optionPos++;
			}
		}
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int button) throws IOException
	{
		super.mouseClicked(mouseX, mouseY, button);

		List<IDialogOption> options = currentMessage.getOptions(npc, player);
		int visibleOptionsCount = 0;
		for (IDialogOption option : options)
		{
			if (option.isVisible(npc, player))
			{
				visibleOptionsCount++;
			}
		}
		int optionPos = 0;
		int optionIndex = 0;
		for (IDialogOption option : options)
		{
			if (option.isVisible(npc, player))
			{
				if (option.canInteract(npc, player))
				{
					if (option instanceof IDialogMessageSeedable)
					{
						((IDialogMessageSeedable)option).setSeed(seed);
					}

					int messageX = width / 2 + 26;
					int messageY = height - height / 8 - 60 + (18 * optionPos) - ((visibleOptionsCount * 18) / 2);
					int messageWidth = fontRendererObj.getStringWidth(option.getQuestionText(npc, player));

					if (mouseX > messageX && mouseX <= messageX + messageWidth && mouseY > messageY && mouseY <= messageY + fontRendererObj.FONT_HEIGHT)
					{
						onQuestionClick(currentMessage, optionIndex);
						return;
					}
				}
				optionPos++;
			}
			optionIndex++;
		}
	}

	protected void onQuestionClick(IDialogMessage message, int option)
	{
		if (lastInteractionTime <= 0)
		{
			lastInteractionTime = INTERACTION_DELAY;
			message.onOptionsInteract(npc, player, option);
			MatterOverdrive.packetPipeline.sendToServer(new PacketConversationInteract(npc, message, option));
		}
	}

	public void onGuiClosed()
	{
		npc.setDialogPlayer(null);
		MatterOverdrive.packetPipeline.sendToServer(new PacketManageConversation(npc, false));
	}

	public IDialogNpc getNpc()
	{
		return npc;
	}

	public EntityPlayer getPlayer()
	{
		return player;
	}

	public EntityLivingBase getCharacterInView()
	{
		return npc.getEntity();
	}

	public boolean doesGuiPauseGame()
	{
		return false;
	}

	public long getSeed()
	{
		return seed;
	}

	public IDialogMessage getCurrentMessage()
	{
		return currentMessage;
	}

	public void setCurrentMessage(IDialogMessage dialogMessage)
	{
		seed = random.nextLong();
		this.currentMessage = dialogMessage;
	}
}
