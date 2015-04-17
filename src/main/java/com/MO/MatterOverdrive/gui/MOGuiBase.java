package com.MO.MatterOverdrive.gui;

import java.util.List;

import cofh.lib.gui.element.ElementBase;
import cofh.lib.gui.element.ElementButton;
import cofh.lib.util.helpers.StringHelper;
import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.container.IButtonHandler;
import com.MO.MatterOverdrive.container.MOBaseContainer;
import com.MO.MatterOverdrive.container.slot.MOSlot;
import com.MO.MatterOverdrive.container.slot.SlotPlayerInventory;
import com.MO.MatterOverdrive.data.inventory.Slot;
import com.MO.MatterOverdrive.gui.element.CloseButton;
import com.MO.MatterOverdrive.gui.element.ElementInventorySlot;
import com.MO.MatterOverdrive.gui.element.MOElementBase;
import com.MO.MatterOverdrive.gui.element.SidePannel;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import cofh.lib.gui.GuiBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;

public abstract class MOGuiBase extends GuiBase implements IButtonHandler, GuiElementList
{
    protected SidePannel sidePannel;
    protected CloseButton closeButton;

	public MOGuiBase(Container container) 
	{
		super(container);
		this.texture = new ResourceLocation(Reference.PATH_ELEMENTS + "base_gui_hotbar.png");
        this.xSize = 224;
        this.ySize = 176;
        sidePannel = new SidePannel(this,212,33);
        closeButton = new CloseButton(this,this,207,6,"close");
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

    @Override
    protected ElementBase getElementAtPosition(int mX, int mY) {

        for (int i = elements.size(); i-- > 0;)
        {
            ElementBase element = elements.get(i);
            if (element.isVisible() && element.intersectsWith(mX, mY))
            {
                return element;
            }
        }
        return null;
    }

	public void setTooltip(List<String> tooltip)
	{
		this.tooltip = tooltip;
	}

    public void handleListChange(String listName, int mouseButton,int element)
    {

    }

    protected void updateElementInformation()
    {
        for (int i = elements.size(); i-- > 0;)
        {
            if (elements.get(i) instanceof MOElementBase)
            {
                ((MOElementBase) elements.get(i)).updateInfo();
            }
        }
    }

    protected void AddPlayerSlots(Container container,GuiElementList elements,boolean main,boolean hotbar)
    {
        for (int i = 0;i < container.inventorySlots.size();i++)
        {
            if(container.inventorySlots.get(i) instanceof SlotPlayerInventory)
            {
                SlotPlayerInventory slot = (SlotPlayerInventory) container.inventorySlots.get(i);
                if (main && !slot.isHotbar())
                {
                    elements.addElement(new ElementInventorySlot(this, slot,18,18, "small"));
                }
                else if (hotbar && slot.isHotbar())
                {
                    elements.addElement(new ElementInventorySlot(this, slot,18,18, "small"));
                }
            }
        }
    }

    public MOBaseContainer getContainer()
    {
        return (MOBaseContainer)inventorySlots;
    }
}
