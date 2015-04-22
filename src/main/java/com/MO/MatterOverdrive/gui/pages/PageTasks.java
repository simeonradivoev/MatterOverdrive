package com.MO.MatterOverdrive.gui.pages;

import cofh.lib.gui.GuiBase;
import com.MO.MatterOverdrive.api.network.MatterNetworkTask;
import com.MO.MatterOverdrive.data.network.MatterNetworkTaskQueue;
import com.MO.MatterOverdrive.gui.element.ElementBaseGroup;
import com.MO.MatterOverdrive.gui.element.ElementTaskList;

/**
 * Created by Simeon on 4/21/2015.
 */
public class PageTasks extends ElementBaseGroup
{
    ElementTaskList taskList;

    public PageTasks(GuiBase gui, int posX, int posY,MatterNetworkTaskQueue<? extends MatterNetworkTask> taskQueue)
    {
        this(gui, posX, posY, 0, 0, taskQueue);
    }

    public PageTasks(GuiBase gui, int posX, int posY, int width, int height,MatterNetworkTaskQueue<? extends MatterNetworkTask> taskQueue)
    {
        super(gui, posX, posY, width, height);
        taskList = new ElementTaskList(gui,48,36,width,height,taskQueue);
        addElement(taskList);
    }
}
