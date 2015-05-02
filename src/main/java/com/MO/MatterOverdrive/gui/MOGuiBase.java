package com.MO.MatterOverdrive.gui;

import java.util.List;

import cofh.lib.gui.element.ElementBase;
import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.container.IButtonHandler;
import com.MO.MatterOverdrive.container.MOBaseContainer;
import com.MO.MatterOverdrive.container.slot.SlotPlayerInventory;
import com.MO.MatterOverdrive.data.ScaleTexture;
import com.MO.MatterOverdrive.gui.element.CloseButton;
import com.MO.MatterOverdrive.gui.element.ElementInventorySlot;
import com.MO.MatterOverdrive.gui.element.MOElementBase;
import com.MO.MatterOverdrive.gui.element.SidePannel;
import com.MO.MatterOverdrive.util.RenderUtils;
import net.minecraft.inventory.Container;
import cofh.lib.gui.GuiBase;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public abstract class MOGuiBase extends GuiBase implements IButtonHandler, GuiElementList
{
    protected SidePannel sidePannel;
    protected CloseButton closeButton;
    ScaleTexture background;

    int texW;
    int texH;

	public MOGuiBase(MOBaseContainer container)
	{
        this(container,225,186);

	}

    public MOGuiBase(MOBaseContainer container,int width,int height)
    {
        super(container);
        background = new ScaleTexture(new ResourceLocation(Reference.PATH_ELEMENTS + "base_gui_hotbar.png"),92,77);
        background.setOffsets(57,34,42,34);
        this.xSize = width;
        this.ySize = height;
        sidePannel = new SidePannel(this,xSize - 12,33,ySize - 34);
        closeButton = new CloseButton(this,this,xSize - 17,6,"close");
        this.drawTitle =  false;
        this.drawInventory = false;
        this.texW = 224;
        this.texH = 176;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTick, int x, int y) {

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        //RenderUtils.drawSizeableBackground(guiLeft, guiTop, xSize, ySize, texW, texH, texture, this.zLevel, 57);
        background.Render(guiLeft,guiTop,xSize,ySize);

        mouseX = x - guiLeft;
        mouseY = y - guiTop;

        GL11.glPushMatrix();
        GL11.glTranslatef(guiLeft, guiTop, 0.0F);
        drawElements(partialTick, false);
        drawTabs(partialTick, false);
        GL11.glPopMatrix();
    }

    @Override
    public void initGui()
    {
        super.initGui();
        sidePannel.init();
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
    public void addToTooltip(String string){this.tooltip.add(string);}
    public List<String> getTooltips(){return tooltip;}

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

    protected void AddMainPlayerSlots(Container container,GuiElementList elements)
    {
        AddPlayerSlots(45,ySize - 106,container,elements,true,false);
    }

    protected void AddHotbarPlayerSlots(Container container,GuiElementList elements)
    {
        AddPlayerSlots(45,ySize - 27,container,elements,false,true);
    }

    protected void AddPlayerSlots(int x,int y,Container container,GuiElementList elements,boolean main,boolean hotbar)
    {
        for (int i = 0;i < container.inventorySlots.size();i++)
        {
            if(container.inventorySlots.get(i) instanceof SlotPlayerInventory)
            {
                SlotPlayerInventory slot = (SlotPlayerInventory) container.inventorySlots.get(i);
                if (main && !slot.isHotbar())
                {
                    ElementInventorySlot inventorySlot = new ElementInventorySlot(this, slot,18,18, "small");
                    inventorySlot.setPosition(x + (18 * (slot.getSlotIndex() % 9)),y + (18 * (slot.getSlotIndex() / 9)));
                    elements.addElement(inventorySlot);
                }
                else if (hotbar && slot.isHotbar())
                {
                    ElementInventorySlot inventorySlot = new ElementInventorySlot(this, slot,18,18, "small");
                    inventorySlot.setPosition(x + (18 * (slot.getSlotIndex() % 9)), y);
                    elements.addElement(inventorySlot);
                }
            }
        }
    }

    public MOBaseContainer getContainer()
    {
        return (MOBaseContainer)inventorySlots;
    }
}
