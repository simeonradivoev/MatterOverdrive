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

package matteroverdrive.gui.pages;

import com.mojang.realmsclient.gui.ChatFormatting;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.api.quest.IQuestReward;
import matteroverdrive.api.quest.QuestStack;
import matteroverdrive.data.quest.rewards.ItemStackReward;
import matteroverdrive.entity.player.MOExtendedProperties;
import matteroverdrive.gui.GuiAndroidHud;
import matteroverdrive.gui.GuiDataPad;
import matteroverdrive.gui.element.*;
import matteroverdrive.gui.element.list.ListElementQuest;
import matteroverdrive.gui.events.IListHandler;
import matteroverdrive.items.DataPad;
import matteroverdrive.network.packet.server.PacketDataPadCommands;
import matteroverdrive.network.packet.server.PacketQuestActions;
import matteroverdrive.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simeon on 11/19/2015.
 */
public class PageActiveQuests extends ElementBaseGroup implements IListHandler
{
	EnumHand hand;
	ItemStack dataPadStack;
	MOElementListBox quests;
	ElementTextList questInfo;
	ElementBaseGroup questRewards;
	ElementScrollGroup questInfoGroup;

	public PageActiveQuests(GuiDataPad gui, int posX, int posY, int width, int height, String name, MOExtendedProperties extendedProperties)
	{
		super(gui, posX, posY, width, height);
		this.setName(name);
		quests = new MOElementListBox(gui, this, posX + 22, posY + 28, width - 44, 74);
		quests.textColor = Reference.COLOR_HOLO.multiplyWithoutAlpha(0.5f).getColor();
		quests.selectedTextColor = Reference.COLOR_HOLO.getColor();
		questInfo = new ElementTextList(gui, 0, 0, width - 15, Reference.COLOR_HOLO.getColor(), false);
		questRewards = new ElementBaseGroup(gui, 8, 8, width - 15, 24);
		questRewards.setName("Quest Rewards");
		questInfoGroup = new ElementScrollGroup(gui, 22, 120, width - 15, 80);
		questInfoGroup.addElement(questInfo);
		questInfoGroup.addElement(questRewards);
		questInfoGroup.setScrollerColor(Reference.COLOR_HOLO.getColor());
		loadQuests(extendedProperties);
	}

	@Override
	public FontRenderer getFontRenderer()
	{
		return Minecraft.getMinecraft().fontRendererObj;
	}

	@Override
	public void init()
	{
		super.init();
		addElement(quests);
		addElement(questInfoGroup);
	}

	protected void loadQuests(MOExtendedProperties extendedProperties)
	{
		quests.clear();
		for (QuestStack questStack : extendedProperties.getQuestData().getActiveQuests())
		{
			quests.add(new ListElementQuest(extendedProperties.getPlayer(), questStack, quests.getWidth()));
		}
	}

	public void refreshQuests(MOExtendedProperties extendedProperties)
	{
		loadQuests(extendedProperties);
		loadSelectedQuestInfo();
	}

	@Override
	public void drawForeground(int mouseX, int mouseY)
	{
		super.drawForeground(mouseX, mouseY);

		GlStateManager.enableBlend();
		RenderUtils.applyColorWithAlpha(Reference.COLOR_HOLO, 0.2f);
		Minecraft.getMinecraft().getTextureManager().bindTexture(GuiAndroidHud.top_element_bg);
		RenderUtils.drawPlane(60, sizeY / 2 - 10, 0, 174, 11);
	}

	@Override
	public void ListSelectionChange(String name, int selected)
	{
		if (dataPadStack.getTagCompound() == null)
		{
			dataPadStack.setTagCompound(new NBTTagCompound());
		}

		((DataPad)dataPadStack.getItem()).setSelectedActiveQuest(dataPadStack, selected);
		questInfoGroup.setScroll(0);
		loadSelectedQuestInfo();
		MatterOverdrive.packetPipeline.sendToServer(new PacketDataPadCommands(hand, dataPadStack));
	}

	private void loadSelectedQuestInfo()
	{
		questInfo.clearLines();
		questRewards.clearElements();
		IMOListBoxElement selectedElement = quests.getSelectedElement();
		if (selectedElement != null)
		{
			QuestStack selectedQuest = (QuestStack)selectedElement.getValue();
			String info = selectedQuest.getInfo(Minecraft.getMinecraft().thePlayer).replace("/n/", "\n");
			if (info != null)
			{
				List<String> list = getFontRenderer().listFormattedStringToWidth(info, sizeX - 32);
				for (String s : list)
				{
					questInfo.addLine(s);
				}
				questInfo.addLine("");
			}
			for (int i = 0; i < selectedQuest.getObjectivesCount(Minecraft.getMinecraft().thePlayer); i++)
			{
				List<String> objectiveLines = MatterOverdrive.questFactory.getFormattedQuestObjective(Minecraft.getMinecraft().thePlayer, selectedQuest, i, sizeX + 60);
				questInfo.addLines(objectiveLines);
			}
			questInfo.addLine("");
			questInfo.addLine(TextFormatting.GOLD + String.format("Rewards: +%sxp", selectedQuest.getXP(Minecraft.getMinecraft().thePlayer)));
			List<IQuestReward> rewards = new ArrayList<>();
			selectedQuest.addRewards(rewards, Minecraft.getMinecraft().thePlayer);
			questRewards.getElements().clear();
			questRewards.setSize(questRewards.getWidth(), rewards.size() > 0 ? 20 : 0);
			for (int i = 0; i < rewards.size(); i++)
			{
				if (rewards.get(i) instanceof ItemStackReward && rewards.get(i).isVisible(selectedQuest))
				{
					ElementItemPreview itemPreview = new ElementItemPreview(gui, i * 20, 1, ((ItemStackReward)rewards.get(i)).getItemStack());
					itemPreview.setItemSize(1);
					itemPreview.setRenderOverlay(true);
					itemPreview.setSize(18, 18);
					itemPreview.setDrawTooltip(true);
					itemPreview.setBackground(null);
					questRewards.addElement(itemPreview);
				}
			}

			if (selectedQuest == null)
			{
				((GuiDataPad)gui).completeQuestButton.setEnabled(false);
				((GuiDataPad)gui).abandonQuestButton.setEnabled(false);
			}
			else
			{
				((GuiDataPad)gui).completeQuestButton.setEnabled(QuestStack.canComplete(Minecraft.getMinecraft().thePlayer, selectedQuest));
				((GuiDataPad)gui).abandonQuestButton.setEnabled(true);
			}
		}
		else
		{
			((GuiDataPad)gui).completeQuestButton.setEnabled(false);
			((GuiDataPad)gui).abandonQuestButton.setEnabled(false);
		}
	}

	public void setDataPadStack(EnumHand hand, ItemStack dataPadStack)
	{
		this.dataPadStack = dataPadStack;
		this.hand = hand;
		if (dataPadStack.getTagCompound() != null)
		{
			quests.setSelectedIndex(((DataPad)dataPadStack.getItem()).getActiveSelectedQuest(dataPadStack));
			questInfoGroup.setScroll(dataPadStack.getTagCompound().getShort("QuestInfoScroll"));
			loadSelectedQuestInfo();
		}
	}

	public void onGuiClose()
	{
		if (dataPadStack.hasTagCompound())
		{
			dataPadStack.getTagCompound().setShort("QuestInfoScroll", (short)questInfoGroup.getScroll());
		}
		MatterOverdrive.packetPipeline.sendToServer(new PacketDataPadCommands(hand, dataPadStack));
	}

	@Override
	public void handleElementButtonClick(MOElementBase element, String elementName, int mouseButton)
	{
		super.handleElementButtonClick(element, elementName, mouseButton);
		if (elementName.equalsIgnoreCase("complete_quest"))
		{
			MatterOverdrive.packetPipeline.sendToServer(new PacketQuestActions(PacketQuestActions.QUEST_ACTION_COMPLETE, quests.getSelectedIndex(), Minecraft.getMinecraft().thePlayer));
		}
		else if (elementName.equalsIgnoreCase("abandon_quest"))
		{
			MatterOverdrive.packetPipeline.sendToServer(new PacketQuestActions(PacketQuestActions.QUEST_ACTION_ABONDON, quests.getSelectedIndex(), Minecraft.getMinecraft().thePlayer));
		}
	}
}
