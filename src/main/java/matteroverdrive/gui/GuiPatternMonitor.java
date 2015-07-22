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
import matteroverdrive.api.network.MatterNetworkTaskState;
import matteroverdrive.container.ContainerPatternMonitor;
import matteroverdrive.container.MOBaseContainer;
import matteroverdrive.gui.element.ElementMonitorItemPattern;
import matteroverdrive.gui.element.ElementPatternsGrid;
import matteroverdrive.gui.element.MOElementButton;
import matteroverdrive.gui.element.MOElementTextField;
import matteroverdrive.gui.pages.PageTasks;
import matteroverdrive.network.packet.server.PacketPatternMonitorCommands;
import matteroverdrive.network.packet.server.PacketRemoveTask;
import matteroverdrive.proxy.ClientProxy;
import matteroverdrive.tile.TileEntityMachinePatternMonitor;
import matteroverdrive.util.MOStringHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;


/**
 * Created by Simeon on 4/26/2015.
 */
public class GuiPatternMonitor extends MOGuiNetworkMachine<TileEntityMachinePatternMonitor>
{
    MOElementButton refreshButton;
    MOElementButton requestButton;
    ElementPatternsGrid elementGrid;
    PageTasks pageTasks;
    MOElementTextField searchField;

    public GuiPatternMonitor(InventoryPlayer inventoryPlayer, TileEntityMachinePatternMonitor machine)
    {
        super(new ContainerPatternMonitor(inventoryPlayer, machine), machine);
        name = "pattern_monitor";
        refreshButton = new MOElementButton(this,this,6,45,"Refresh",0,0,22,0,22,22, "");
        refreshButton.setTexture(Reference.PATH_GUI_ITEM + "refresh.png", 44, 22);
        refreshButton.setToolTip(MOStringHelper.translateToLocal("gui.tooltip.button.refresh"));
        requestButton = new MOElementButton(this,this,6,75,"Request",0,0,22,0,22,22,"");
        requestButton.setTexture(Reference.PATH_GUI_ITEM + "request.png",44,22);
        requestButton.setToolTip(MOStringHelper.translateToLocal("gui.tooltip.button.request"));
        elementGrid = new ElementPatternsGrid(this,48,40,160,110);
        searchField = new MOElementTextField(this,41,26,167,14);

        elementGrid.updateStackList(machine.getDatabases());
    }

    @Override
    public void registerPages(MOBaseContainer container,TileEntityMachinePatternMonitor machine)
    {
        super.registerPages(container, machine);

        pageTasks = new PageTasks(this,0,0,xSize,ySize,machine.getTaskQueue((byte) 0));
        pageTasks.setName("Tasks");
        AddPage(pageTasks, ClientProxy.holoIcons.getIcon("page_icon_tasks"), MOStringHelper.translateToLocal("gui.tooltip.page.tasks")).setIconColor(Reference.COLOR_MATTER);
    }

    @Override
    public void initGui()
    {
        super.initGui();

        this.addElement(refreshButton);
        this.addElement(requestButton);
        pages.get(0).addElement(elementGrid);
        pages.get(0).addElement(searchField);
        AddHotbarPlayerSlots(inventorySlots, this);
    }

    public void handleElementButtonClick(String buttonName, int mouseButton)
    {
        super.handleElementButtonClick(buttonName, mouseButton);
        if (buttonName.equals("Refresh"))
        {
            MatterOverdrive.packetPipeline.sendToServer(new PacketPatternMonitorCommands(machine,0,null));
        }
        else if (buttonName.equals("Request"))
        {
            NBTTagList requestList = new NBTTagList();
            for (int i = 0;i < elementGrid.getElements().size();i++)
            {
                if (elementGrid.getElements().get(i) instanceof ElementMonitorItemPattern)
                {
                    ElementMonitorItemPattern itemPattern = (ElementMonitorItemPattern)elementGrid.getElements().get(i);

                    if (itemPattern.getAmount() > 0)
                    {
                        NBTTagCompound tagCompound = (NBTTagCompound)itemPattern.getTagCompound().copy();
                        tagCompound.setByte("Count", (byte) itemPattern.getAmount());
                        requestList.appendTag(tagCompound);
                        itemPattern.setAmount((byte)0);
                    }
                    else
                    {
                        itemPattern.setExpanded(false);
                    }
                }
            }

            if (requestList.tagCount() > 0)
            {
                NBTTagCompound tagCompound = new NBTTagCompound();
                tagCompound.setTag("Requests", requestList);
                MatterOverdrive.packetPipeline.sendToServer(new PacketPatternMonitorCommands(machine, PacketPatternMonitorCommands.COMMAND_REQUEST, tagCompound));
            }
        }
        else if (buttonName.equals("DropTask"))
        {
            NBTTagCompound tagCompound = new NBTTagCompound();
            tagCompound.setInteger("TaskID",mouseButton);
            MatterOverdrive.packetPipeline.sendToServer(new PacketRemoveTask(machine,mouseButton,(byte)0, MatterNetworkTaskState.INVALID));
        }
    }

    @Override
    protected void updateElementInformation()
    {
        super.updateElementInformation();

        if (machine.needsRefresh())
        {
            elementGrid.updateStackList(machine.getDatabases());
            machine.forceSearch(false);
        }

        elementGrid.setFilter(searchField.getText());
    }
}
