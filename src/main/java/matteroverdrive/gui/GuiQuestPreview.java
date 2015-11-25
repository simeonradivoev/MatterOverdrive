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

import matteroverdrive.Reference;
import matteroverdrive.container.ContainerFalse;
import matteroverdrive.data.quest.QuestStack;
import matteroverdrive.gui.element.ElementTextList;
import matteroverdrive.gui.element.MOElementButtonScaled;
import matteroverdrive.util.MOStringHelper;
import matteroverdrive.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;

import java.util.List;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Simeon on 11/22/2015.
 */
public class GuiQuestPreview extends MOGuiBase
{
    public static final ResourceLocation backgroundTexture = new ResourceLocation(Reference.PATH_GUI + "contract.png");
    ElementTextList questInfo;
    QuestStack questStack;
    MOElementButtonScaled acceptButton;

    public GuiQuestPreview(QuestStack questStack)
    {
        super(new ContainerFalse(),200,225);
        background = null;
        questInfo = new ElementTextList(this,18,68,xSize-48,100,0x505758,false);
        acceptButton = new MOElementButtonScaled(this,this,14,ySize-28,"accept_quest",68,12);
        acceptButton.setDownTexture(null);
        acceptButton.setOverTexture(null);
        acceptButton.setNormalTexture(null);
        //acceptButton.setIcon(ClientProxy.holoIcons.getIcon("tick"));
        acceptButton.setText(String.format("[ %s ]",MOStringHelper.translateToLocal("gui.label.accept")));
        acceptButton.setToolTip(MOStringHelper.translateToLocal("gui.tooltip.quest.accept"));
        acceptButton.setTextColor(0x279f33);
        this.questStack = questStack;
    }

    @Override
    public void initGui()
    {
        super.initGui();
        elements.remove(sidePannel);
        elements.remove(closeButton);
        addElement(questInfo);
        addElement(acceptButton);
        loadQuestInfo(questStack);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTick, int x, int y)
    {
        bindTexture(backgroundTexture);
        glColor3f(1,1,1);
        RenderUtils.drawPlane(guiLeft,guiTop,0,xSize,ySize);
        if (questStack != null)
        {
            String questName = questStack.getTitle(Minecraft.getMinecraft().thePlayer);
            questName = "Test me buddy if you can I'm here";
            int titleWidth = fontRendererObj.getStringWidth(questName);
            float scale = 100f/(float)titleWidth;
            glPushMatrix();
            glTranslated(guiLeft + 24,guiTop + 30,0);
            glScalef(scale,scale,scale);
            fontRendererObj.drawString(EnumChatFormatting.BOLD + questName,0,0,0x2394e3);
            glPopMatrix();
        }
        super.drawGuiContainerBackgroundLayer(partialTick,x,y);
    }

    private void loadQuestInfo(QuestStack questStack)
    {
        questInfo.clearLines();
        List<String> list = getFontRenderer().listFormattedStringToWidth(questStack.getInfo(Minecraft.getMinecraft().thePlayer),165);
        for (String s : list)
        {
            questInfo.addLine(s);
        }
        questInfo.addLine("");
        for (int i = 0;i < questStack.getObjectivesCount(Minecraft.getMinecraft().thePlayer);i++)
        {
            String objective = questStack.getObjective(Minecraft.getMinecraft().thePlayer,i);
            if (questStack.isObjectiveCompleted(Minecraft.getMinecraft().thePlayer,i))
            {
                questInfo.addLine(EnumChatFormatting.GREEN + "■ " + objective);
            }else
            {
                questInfo.addLine(EnumChatFormatting.DARK_GREEN + "□ " + objective);
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
}
