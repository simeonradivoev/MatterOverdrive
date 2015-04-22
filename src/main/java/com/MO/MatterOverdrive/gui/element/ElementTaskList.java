package com.MO.MatterOverdrive.gui.element;

import cofh.lib.gui.GuiBase;
import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.api.network.MatterNetworkTask;
import com.MO.MatterOverdrive.data.network.MatterNetworkTaskQueue;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simeon on 4/21/2015.
 */
public class ElementTaskList extends MOElementBase
{
    public static ResourceLocation TASK_ELEMENT_BG = new ResourceLocation(Reference.PATH_ELEMENTS + "task_bg.png");
    public static ResourceLocation TASK_ELEMENT_SECCOUNDARY_BG = new ResourceLocation(Reference.PATH_ELEMENTS + "task_dark_bg.png");
    MatterNetworkTaskQueue<? extends MatterNetworkTask> taskQueue;
    int scroll = 0;
    List<String> lastTooltip;

    public ElementTaskList(GuiBase gui, int posX, int posY,MatterNetworkTaskQueue<? extends MatterNetworkTask> taskQueue)
    {
        this(gui,posX,posY,0,0,taskQueue);
    }

    public ElementTaskList(GuiBase gui, int posX, int posY, int width, int height,MatterNetworkTaskQueue<? extends MatterNetworkTask> taskQueue) {
        super(gui, posX, posY, width, height);
        this.taskQueue = taskQueue;
        lastTooltip = new ArrayList<String>();
    }

    @Override
    public void drawBackground(int mouseX, int mouseY, float gameTicks)
    {
        lastTooltip.clear();

        for (int i = taskQueue.size(); i-- > 0;)
        {
            if (i == taskQueue.size()-1)
                gui.bindTexture(TASK_ELEMENT_BG);
            else
                gui.bindTexture(TASK_ELEMENT_SECCOUNDARY_BG);

            int height = 22;
            int width = 132;
            int x = posX;
            int y = posY + (28 * (i - scroll));
            if (mouseX > posX && mouseY > posY && mouseY < posY + height && mouseX < posX + width)
            {
                gui.drawSizedTexturedModalRect(x, y, 0, height, width, height, width, height * 2);
                taskQueue.getAt(i).addInfo(lastTooltip);
                lastTooltip.add("");
                lastTooltip.add(EnumChatFormatting.GOLD + EnumChatFormatting.BOLD.toString() + "Shift" + EnumChatFormatting.GOLD + " Click to Deque");
            }
            else
            {
                gui.drawSizedTexturedModalRect(x,y,0,0,width,height,width,height*2);
            }
            MatterNetworkTask task = taskQueue.getAt(i);
            Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(task.getName(), x + 8, y + 7, 0xFFFFFF);
        }
    }

    @Override
    public void addTooltip(List<String> list)
    {
        if (!lastTooltip.isEmpty())
        {
            list.addAll(lastTooltip);
        }
    }

    @Override
    public void drawForeground(int mouseX, int mouseY) {

    }
}
