package com.MO.MatterOverdrive.gui;

import java.util.List;

import cofh.lib.gui.element.ElementButton;
import cofh.lib.util.helpers.StringHelper;
import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.gui.element.CloseButton;
import com.MO.MatterOverdrive.gui.element.SidePannel;
import net.minecraft.inventory.Container;
import cofh.lib.gui.GuiBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;

public abstract class MOGuiBase extends GuiBase
{
    protected static final String SIDE_BUTTON_NAME = "SideButton";

    protected SidePannel sidePannel;
    private CloseButton closeButton;

	public MOGuiBase(Container container) 
	{
		super(container);
		this.texture = new ResourceLocation(Reference.PATH_ELEMENTS + "base_gui.png");
        this.xSize = 224;
        this.ySize = 176;
        sidePannel = new SidePannel(this,212,33,SIDE_BUTTON_NAME);
        closeButton = new CloseButton(this,207,6,"close");
        this.drawTitle =  false;
        this.drawInventory = false;
	}

    @Override
    public void initGui()
    {
        super.initGui();
        this.addElement(sidePannel);
        this.addElement(closeButton);
    }

	public void setTooltip(List<String> tooltip)
	{
		this.tooltip = tooltip;
	}

    @Override
    public void handleElementButtonClick(String buttonName, int mouseButton)
    {
        super.handleElementButtonClick(buttonName,mouseButton);
        if(buttonName == SIDE_BUTTON_NAME)
        {
            OnPanelOpen(sidePannel.IsOpen());
        }
    }

    public void handleListChange(String listName, int mouseButton,int element)
    {

    }

    public void OnPanelOpen(boolean isPanelOpen)
    {

    }

    public void setPanelOpen(boolean isOpen)
    {
        sidePannel.setOpen(isOpen);
        OnPanelOpen(isOpen);
    }
}
