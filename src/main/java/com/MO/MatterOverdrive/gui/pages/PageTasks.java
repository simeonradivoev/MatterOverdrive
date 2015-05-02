package com.MO.MatterOverdrive.gui.pages;

import com.MO.MatterOverdrive.api.network.MatterNetworkTask;
import com.MO.MatterOverdrive.gui.MOGuiBase;
import com.MO.MatterOverdrive.matter_network.MatterNetworkTaskQueue;
import com.MO.MatterOverdrive.gui.element.ElementBaseGroup;
import com.MO.MatterOverdrive.gui.element.ElementTaskList;

/**
 * Created by Simeon on 4/21/2015.
 */
public class PageTasks extends ElementBaseGroup
{
    ElementTaskList taskList;

    public PageTasks(MOGuiBase gui, int posX, int posY,MatterNetworkTaskQueue<? extends MatterNetworkTask> taskQueue)
    {
        this(gui, posX, posY, 0, 0, taskQueue);
    }

    public PageTasks(MOGuiBase gui, int posX, int posY, int width, int height,MatterNetworkTaskQueue<? extends MatterNetworkTask> taskQueue)
    {
        super(gui, posX, posY, width, height);
        taskList = new ElementTaskList(gui,gui,48,36,150,120,taskQueue);
    }

    @Override
    public void init()
    {
        super.init();
        addElement(taskList);
    }
}
