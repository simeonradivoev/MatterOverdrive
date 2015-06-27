package com.MO.MatterOverdrive.gui;

import com.MO.MatterOverdrive.MatterOverdrive;
import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.container.ContainerPatternMonitor;
import com.MO.MatterOverdrive.container.MOBaseContainer;
import com.MO.MatterOverdrive.gui.element.*;
import com.MO.MatterOverdrive.gui.pages.PageTasks;
import com.MO.MatterOverdrive.network.packet.server.PacketPatternMonitorCommands;
import com.MO.MatterOverdrive.network.packet.server.PacketRemoveTask;
import com.MO.MatterOverdrive.proxy.ClientProxy;
import com.MO.MatterOverdrive.tile.TileEntityMachinePatternMonitor;
import com.MO.MatterOverdrive.util.MOStringHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Simeon on 4/26/2015.
 */
public class GuiPatternMonitor extends MOGuiMachine<TileEntityMachinePatternMonitor>
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
        refreshButton = new MOElementButton(this,this,6,75,"Refresh",0,0,22,0,22,22, "");
        refreshButton.setTexture(Reference.PATH_GUI_ITEM + "refresh.png", 44, 22);
        refreshButton.setToolTip(MOStringHelper.translateToLocal("gui.tooltip.button.refresh"));
        requestButton = new MOElementButton(this,this,6,100,"Request",0,0,22,0,22,22,"");
        requestButton.setTexture(Reference.PATH_GUI_ITEM + "request.png",44,22);
        requestButton.setToolTip(MOStringHelper.translateToLocal("gui.tooltip.button.request"));
        elementGrid = new ElementPatternsGrid(this,48,40,160,114);
        searchField = new MOElementTextField(this,41,26,167,14);

        elementGrid.updateStackList(machine.getDatabases());
    }

    @Override
    public void registerPages(MOBaseContainer container,TileEntityMachinePatternMonitor machine)
    {
        super.registerPages(container,machine);

        pageTasks = new PageTasks(this,0,0,xSize,ySize,machine.getQueue((byte)0));
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
        AddHotbarPlayerSlots(inventorySlots,this);
    }

    public void handleElementButtonClick(String buttonName, int mouseButton)
    {
        super.handleElementButtonClick(buttonName,mouseButton);
        if (buttonName.equals("Refresh"))
        {
            MatterOverdrive.packetPipeline.sendToServer(new PacketPatternMonitorCommands(machine,0,null));
        }
        else if (buttonName.equals("Request"))
        {
            List<Integer> list = new ArrayList<Integer>();
            for (int i = 0;i < elementGrid.getElements().size();i++)
            {
                if (elementGrid.getElements().get(i) instanceof ElementMonitorItemPattern )
                {
                    ElementMonitorItemPattern itemPattern = (ElementMonitorItemPattern)elementGrid.getElements().get(i);

                    if (itemPattern.getAmount() > 0)
                    {
                        list.add((int) itemPattern.getTagCompound().getShort("id"));
                        list.add((int) itemPattern.getTagCompound().getShort("Damage"));
                        list.add(itemPattern.getAmount());
                        itemPattern.setAmount(0);
                    }
                    else
                    {
                        itemPattern.setExpanded(false);
                    }
                }
            }

            if (list.size() > 0)
            {
                int[] array = new int[list.size()];
                for (int i = 0;i < list.size();i++)
                {
                    array[i] = list.get(i);
                }
                NBTTagCompound tagCompound = new NBTTagCompound();
                tagCompound.setIntArray("Requests", array);
                MatterOverdrive.packetPipeline.sendToServer(new PacketPatternMonitorCommands(machine, PacketPatternMonitorCommands.COMMAND_REQUEST, tagCompound));
            }
        }
        else if (buttonName == "DropTask")
        {
            NBTTagCompound tagCompound = new NBTTagCompound();
            tagCompound.setInteger("TaskID",mouseButton);
            MatterOverdrive.packetPipeline.sendToServer(new PacketRemoveTask(machine,mouseButton,(byte)0,Reference.TASK_STATE_INVALID));
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
