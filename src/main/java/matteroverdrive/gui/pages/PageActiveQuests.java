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

import cofh.lib.gui.element.ElementBase;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.data.quest.QuestStack;
import matteroverdrive.entity.player.MOExtendedProperties;
import matteroverdrive.gui.GuiAndroidHud;
import matteroverdrive.gui.GuiDataPad;
import matteroverdrive.gui.element.ElementBaseGroup;
import matteroverdrive.gui.element.ElementTextList;
import matteroverdrive.gui.element.IMOListBoxElement;
import matteroverdrive.gui.element.MOElementListBox;
import matteroverdrive.gui.element.list.ListElementQuest;
import matteroverdrive.gui.events.IListHandler;
import matteroverdrive.items.DataPad;
import matteroverdrive.network.packet.server.PacketDataPadCommands;
import matteroverdrive.network.packet.server.PacketQuestActions;
import matteroverdrive.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;

import java.util.List;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.glEnable;

/**
 * Created by Simeon on 11/19/2015.
 */
public class PageActiveQuests extends ElementBaseGroup implements IListHandler
{
    ItemStack dataPadStack;
    MOElementListBox quests;
    ElementTextList questInfo;

    public PageActiveQuests(GuiDataPad gui, int posX, int posY, int width, int height, String name, MOExtendedProperties extendedProperties)
    {
        super(gui, posX, posY, width, height);
        this.setName(name);
        quests = new MOElementListBox(gui,this,posX+22,posY+28,width-44,90);
        quests.textColor = Reference.COLOR_HOLO.multiply(0.5f).getColor();
        quests.selectedTextColor = Reference.COLOR_HOLO.getColor();
        questInfo = new ElementTextList(gui,posX+22,posY+120,width-15,80,Reference.COLOR_HOLO.getColor(),true);
        loadQuests(extendedProperties);
    }

    @Override
    public FontRenderer getFontRenderer()
    {
        return Minecraft.getMinecraft().fontRenderer;
    }

    @Override
    public void init()
    {
        super.init();
        addElement(quests);
        addElement(questInfo);
        loadSelectedQuestInfo();
    }

    protected void loadQuests(MOExtendedProperties extendedProperties)
    {
        quests.clear();
        for (QuestStack questStack : extendedProperties.getQuestData().getActiveQuests())
        {
            quests.add(new ListElementQuest(extendedProperties.getPlayer(),questStack,quests.getWidth()));
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
        super.drawForeground(mouseX,mouseY);

        glEnable(GL_BLEND);
        RenderUtils.applyColorWithAlpha(Reference.COLOR_HOLO,0.2f);
        Minecraft.getMinecraft().getTextureManager().bindTexture(GuiAndroidHud.top_element_bg);
        RenderUtils.drawPlane(60,sizeY/2 - 10,0,174,11);
    }

    @Override
    public void ListSelectionChange(String name, int selected)
    {
        if (dataPadStack.getTagCompound() == null)
        {
            dataPadStack.setTagCompound(new NBTTagCompound());
        }

        ((DataPad)dataPadStack.getItem()).setSelectedActiveQuest(dataPadStack,selected);
        questInfo.setScroll(0);
        loadSelectedQuestInfo();
        MatterOverdrive.packetPipeline.sendToServer(new PacketDataPadCommands(dataPadStack));
    }

    private void loadSelectedQuestInfo()
    {
        questInfo.clearLines();
        IMOListBoxElement selectedElement = quests.getSelectedElement();
        if (selectedElement != null) {
            QuestStack selectedQuest = (QuestStack) selectedElement.getValue();
            List<String> list = getFontRenderer().listFormattedStringToWidth(selectedQuest.getInfo(Minecraft.getMinecraft().thePlayer),sizeX+70);
            for (String s : list)
            {
                questInfo.addLine(s);
            }
            questInfo.addLine("");
            for (int i = 0;i < selectedQuest.getObjectivesCount(Minecraft.getMinecraft().thePlayer);i++)
            {
                String objective = selectedQuest.getObjective(Minecraft.getMinecraft().thePlayer,i);
                if (selectedQuest.isObjectiveCompleted(Minecraft.getMinecraft().thePlayer,i))
                {
                    questInfo.addLine(EnumChatFormatting.GREEN + "■ " + objective);
                }else
                {
                    questInfo.addLine(EnumChatFormatting.DARK_GREEN + "□ " + objective);
                }
            }
            if (selectedQuest == null)
            {
                ((GuiDataPad)gui).completeQuestButton.setEnabled(false);
                ((GuiDataPad)gui).abandonQuestButton.setEnabled(false);
            }else
            {
                ((GuiDataPad)gui).completeQuestButton.setEnabled(QuestStack.canComplete(Minecraft.getMinecraft().thePlayer,selectedQuest));
                ((GuiDataPad)gui).abandonQuestButton.setEnabled(true);
            }
        }else
        {
            ((GuiDataPad)gui).completeQuestButton.setEnabled(false);
            ((GuiDataPad)gui).abandonQuestButton.setEnabled(false);
        }
    }

    public void setDataPadStack(ItemStack dataPadStack)
    {
        this.dataPadStack = dataPadStack;
        if (dataPadStack.getTagCompound() != null)
        {
            quests.setSelectedIndex(((DataPad)dataPadStack.getItem()).getActiveSelectedQuest(dataPadStack));
            questInfo.setScroll(dataPadStack.getTagCompound().getShort("QuestInfoScroll"));
        }
    }

    public void onGuiClose()
    {
        if (dataPadStack.hasTagCompound())
        {
            dataPadStack.getTagCompound().setShort("QuestInfoScroll",(short) questInfo.getScroll());
        }
        MatterOverdrive.packetPipeline.sendToServer(new PacketDataPadCommands(dataPadStack));
    }

    @Override
    public void handleElementButtonClick(ElementBase element, String elementName, int mouseButton)
    {
        super.handleElementButtonClick(element,elementName,mouseButton);
        if (elementName.equalsIgnoreCase("complete_quest"))
        {
            MatterOverdrive.packetPipeline.sendToServer(new PacketQuestActions(PacketQuestActions.QUEST_ACTION_COMPLETE,quests.getSelectedIndex(),Minecraft.getMinecraft().thePlayer));
        }
        else if (elementName.equalsIgnoreCase("abandon_quest"))
        {
            MatterOverdrive.packetPipeline.sendToServer(new PacketQuestActions(PacketQuestActions.QUEST_ACTION_ABONDON,quests.getSelectedIndex(),Minecraft.getMinecraft().thePlayer));
        }
    }
}
