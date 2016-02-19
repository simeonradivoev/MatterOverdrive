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
import matteroverdrive.api.quest.IQuestReward;
import matteroverdrive.api.quest.QuestStack;
import matteroverdrive.container.ContainerFalse;
import matteroverdrive.data.quest.rewards.ItemStackReward;
import matteroverdrive.gui.element.*;
import matteroverdrive.network.packet.server.PacketQuestActions;
import matteroverdrive.util.MOStringHelper;
import matteroverdrive.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simeon on 11/22/2015.
 */
public class GuiQuestPreview extends MOGuiBase
{
    public static final ResourceLocation backgroundTexture = new ResourceLocation(Reference.PATH_GUI + "contract.png");
    ElementTextList questInfo;
    ElementScrollGroup questInfoGroup;
    QuestStack questStack;
    ElementBaseGroup questRewards;
    MOElementButtonScaled acceptButton;

    public GuiQuestPreview(QuestStack questStack)
    {
        super(new ContainerFalse(),200,225);
        background = null;
        questInfo = new ElementTextList(this,0,0,xSize-18-14,0xff505758,false);
        questInfoGroup = new ElementScrollGroup(this,18,68,xSize-18-14,120);
        questInfoGroup.setScrollerColor(0xff505758);
        questRewards = new ElementBaseGroup(this,8,8,width-15,24);
        questRewards.setName("Quest Rewards");
        questInfoGroup.addElement(questInfo);
        questInfoGroup.addElement(questRewards);
        acceptButton = new MOElementButtonScaled(this,this,14,ySize-28,"accept_quest",68,12);
        acceptButton.setDownTexture(null);
        acceptButton.setOverTexture(null);
        acceptButton.setNormalTexture(null);
        //acceptButton.setIcon(ClientProxy.holoIcons.getIcon("tick"));
        acceptButton.setText(String.format("[ %s ]",MOStringHelper.translateToLocal("gui.label.accept")));
        acceptButton.setToolTip(MOStringHelper.translateToLocal("gui.tooltip.quest.accept"));
        acceptButton.setTextColor(0x5656ff);
        if (!questStack.canAccept(Minecraft.getMinecraft().thePlayer,questStack))
        {
            acceptButton.setEnabled(false);
            acceptButton.setTextColor(Reference.COLOR_HOLO_RED.getColor());
        }
        this.questStack = questStack;
    }

    @Override
    public void initGui()
    {
        super.initGui();
        elements.remove(sidePannel);
        elements.remove(closeButton);
        addElement(questInfoGroup);
        addElement(acceptButton);
        loadQuestInfo(questStack);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTick, int x, int y)
    {
        bindTexture(backgroundTexture);
        GlStateManager.color(1,1,1);
        RenderUtils.drawPlane(guiLeft,guiTop,0,xSize,ySize);
        if (questStack != null)
        {
            String questName = questStack.getTitle(Minecraft.getMinecraft().thePlayer);
            int titleWidth = fontRendererObj.getStringWidth(questName);
            float scale = Math.min(100f/(float)titleWidth,1.8f);
            GlStateManager.pushMatrix();
            GlStateManager.translate(guiLeft + 24,guiTop + 30,0);
            GlStateManager.scale(scale,scale,scale);
            fontRendererObj.drawString(EnumChatFormatting.BOLD + questName,0,0,0x2394e3);
            GlStateManager.popMatrix();
        }
        super.drawGuiContainerBackgroundLayer(partialTick,x,y);
    }

    private void loadQuestInfo(QuestStack questStack)
    {
        int width = 165;
        questInfo.clearLines();
        String info = questStack.getInfo(Minecraft.getMinecraft().thePlayer).replace("/n/","\n");
        List<String> list = getFontRenderer().listFormattedStringToWidth(info,width);
        for (String s : list)
        {
            questInfo.addLine(s);
        }
        questInfo.addLine("");
        for (int i = 0;i < questStack.getObjectivesCount(Minecraft.getMinecraft().thePlayer);i++)
        {
            List<String> objectiveLines = MatterOverdrive.questFactory.getFormattedQuestObjective(Minecraft.getMinecraft().thePlayer,questStack,i,width,EnumChatFormatting.BLUE.toString(),EnumChatFormatting.BLUE.toString());
            questInfo.addLines(objectiveLines);
        }
        questInfo.addLine("");
        questInfo.addLine(EnumChatFormatting.DARK_PURPLE + "Rewards:");
        questInfo.addLine(String.format(EnumChatFormatting.DARK_PURPLE + "   +%sxp",questStack.getXP(Minecraft.getMinecraft().thePlayer)));
        List<IQuestReward> rewards = new ArrayList<>();
        questStack.addRewards(rewards,Minecraft.getMinecraft().thePlayer);
        questRewards.getElements().clear();
        questRewards.setSize(questRewards.getWidth(),rewards.size() > 0 ? 20 : 0);
        for (int i = 0;i < rewards.size();i++)
        {
            if (rewards.get(i) instanceof ItemStackReward && rewards.get(i).isVisible(questStack))
            {
                ElementItemPreview itemPreview = new ElementItemPreview(this, i * 20, 1, ((ItemStackReward) rewards.get(i)).getItemStack());
                itemPreview.setItemSize(1);
                itemPreview.setRenderOverlay(true);
                itemPreview.setSize(18, 18);
                itemPreview.setDrawTooltip(true);
                itemPreview.setBackground(null);
                questRewards.addElement(itemPreview);
            }
        }
    }

    @Override
    public void ListSelectionChange(String name, int selected)
    {

    }

    @Override
    public void textChanged(String elementName, String text, boolean typed) {

    }

    public void handleElementButtonClick(MOElementBase element, String elementName, int mouseButton)
    {
        super.handleElementButtonClick(element,elementName,mouseButton);
        if (element == acceptButton)
        {
            MatterOverdrive.packetPipeline.sendToServer(new PacketQuestActions(PacketQuestActions.QUEST_ACTION_ADD,mc.thePlayer.inventory.currentItem,mc.thePlayer));
            mc.thePlayer.closeScreen();
        }
    }
}
