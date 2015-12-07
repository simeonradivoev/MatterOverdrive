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

import cofh.lib.gui.GuiBase;
import cofh.lib.gui.GuiColor;
import cofh.lib.gui.element.ElementBase;
import cofh.lib.util.helpers.MathHelper;
import matteroverdrive.Reference;
import matteroverdrive.client.render.HoloIcon;
import matteroverdrive.container.IButtonHandler;
import matteroverdrive.container.MOBaseContainer;
import matteroverdrive.container.slot.SlotPlayerInventory;
import matteroverdrive.data.ScaleTexture;
import matteroverdrive.gui.element.*;
import matteroverdrive.gui.events.IListHandler;
import matteroverdrive.gui.events.ITextHandler;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public abstract class MOGuiBase extends GuiBase implements IButtonHandler,ITextHandler,IListHandler, GuiElementList
{
    int currentPage = -1;
    protected SidePannel sidePannel;
    protected CloseButton closeButton;
    ScaleTexture background;
    protected List<ElementBaseGroup> pages;
    protected List<MOElementButton> pageButtons;

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
        background.setOffsets(57, 34, 42, 34);
        this.xSize = width;
        this.ySize = height;
        sidePannel = new SidePannel(this,xSize - 12,33,ySize - 34);
        closeButton = new CloseButton(this,this,xSize - 17,6,"close");
        this.drawTitle =  false;
        this.drawInventory = false;
        this.texW = 224;
        this.texH = 176;
        super.width = width;
        super.height = height;

        pages = new ArrayList<>(3);
        pageButtons = new ArrayList<>(3);

        registerPages(container);
    }

    @Override
    public void addTooltips(List<String> var1) {
        super.addTooltips(var1);
        for (int i = elements.size(); i-- > 0;)
        {
            ElementBase element = elements.get(i);
            if (element instanceof  MOElementBase && element.isVisible() && element.intersectsWith(mouseX, mouseY))
            {
                ((MOElementBase)element).addTooltip(var1,mouseX,mouseY);
            }
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTick, int x, int y) {

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        //RenderUtils.drawSizeableBackground(guiLeft, guiTop, xSize, ySize, texW, texH, texture, this.zLevel, 57);
        if (background != null) {
            background.render(guiLeft, guiTop, xSize, ySize);
        }

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
        for (ElementBaseGroup page : pages)
        {
            page.init();
            this.addElement(page);
        }

        this.addElement(sidePannel);
        sidePannel.init();
        this.addElement(closeButton);

        for (MOElementButton button : pageButtons)
        {
            sidePannel.addElement(button);
        }

        setPage(currentPage);
    }

    public void registerPages(MOBaseContainer container)
    {

    }

    @Override
    public void handleElementButtonClick(ElementBase element,String buttonName, int mouseButton)
    {
        for (int i = 0;i < pageButtons.size();i++)
        {
            if (i < pages.size())
            {
                if (pageButtons.get(i).equals(element))
                {
                    setPage(i);
                }
            }
        }
    }

    public void setPage(int page)
    {
        page = MathHelper.clampI(page, 0, pages.size() - 1);
        if (currentPage != page) {
            onPageChange(page);
        }
        currentPage = page;


        for (int i = 0; i < pages.size(); i++) {
            pages.get(i).setVisible(i == currentPage);

            if (i < pageButtons.size()) {
                pageButtons.get(i).setEnabled(i != currentPage);
            }
        }
    }

    public void onPageChange(int newPage)
    {

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
        for (int i = 0; i < pageButtons.size();i++)
        {
            pageButtons.get(i).setPosition(6,8 + (pageButtons.get(i).getHeight() + 2) * i);
        }

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
        AddPlayerSlots(45,ySize - 106,container,elements,true,false,"small",null);
    }

    protected void AddHotbarPlayerSlots(Container container,GuiElementList elements)
    {
        AddPlayerSlots(45,ySize - 27,container,elements,false,true,"small",null);
    }

    protected void AddMainPlayerSlots(Container container,GuiElementList elements,String type,GuiColor color,int x,int y)
    {
        AddPlayerSlots(x,y,container,elements,true,false,type,color);
    }

    protected void AddHotbarPlayerSlots(Container container,GuiElementList elements,String type,GuiColor color,int x,int y)
    {
        AddPlayerSlots(x,y,container,elements,false,true,type,color);
    }

    protected void AddPlayerSlots(int x,int y,Container container,GuiElementList elements,boolean main,boolean hotbar,String type,GuiColor color)
    {
        for (int i = 0;i < container.inventorySlots.size();i++)
        {
            if(container.inventorySlots.get(i) instanceof SlotPlayerInventory)
            {
                SlotPlayerInventory slot = (SlotPlayerInventory) container.inventorySlots.get(i);
                if (main && !slot.isHotbar())
                {
                    ElementInventorySlot inventorySlot = new ElementInventorySlot(this, slot,18,18, type);
                    inventorySlot.setColor(color);
                    inventorySlot.setPosition(x + (18 * (slot.getSlotIndex() % 9)),y + (18 * (slot.getSlotIndex() / 9)));
                    elements.addElement(inventorySlot);
                }
                else if (hotbar && slot.isHotbar())
                {
                    ElementInventorySlot inventorySlot = new ElementInventorySlot(this, slot,18,18, type);
                    inventorySlot.setColor(color);
                    inventorySlot.setPosition(x + (18 * (slot.getSlotIndex() % 9)), y);
                    elements.addElement(inventorySlot);
                }
            }
        }
    }

    public MOElementIconButton AddPage(ElementBaseGroup page,HoloIcon icon,String tooltip)
    {
        pages.add(page);
        MOElementIconButton button = new MOElementIconButton(this,this,0,0,page.getName(),0,0,24,0,24,0,24,24,"",icon);
        button.setTexture(Reference.PATH_ELEMENTS + "page_button.png", 48, 24);
        button.setToolTip(tooltip);
        pageButtons.add(button);
        return button;
    }

    public MOBaseContainer getContainer()
    {
        return (MOBaseContainer)inventorySlots;
    }
}
