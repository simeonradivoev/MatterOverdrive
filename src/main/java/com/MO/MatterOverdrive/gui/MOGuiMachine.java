package com.MO.MatterOverdrive.gui;

import cofh.lib.gui.element.ElementBase;
import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.container.slot.MOSlot;
import com.MO.MatterOverdrive.container.slot.SlotPatternStorage;
import com.MO.MatterOverdrive.container.slot.SlotUpgrade;
import com.MO.MatterOverdrive.data.Inventory;
import com.MO.MatterOverdrive.data.inventory.UpgradeSlot;
import com.MO.MatterOverdrive.gui.element.ElementBaseGroup;
import com.MO.MatterOverdrive.gui.element.ElementInventorySlot;
import com.MO.MatterOverdrive.gui.element.ElementSlotsList;
import com.MO.MatterOverdrive.gui.element.MOElementButton;
import com.MO.MatterOverdrive.tile.MOTileEntityMachine;
import net.minecraft.inventory.Container;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simeon on 4/8/2015.
 */
public class MOGuiMachine<T extends MOTileEntityMachine> extends MOGuiBase
{
    T machine;
    protected List<ElementBaseGroup> pages;
    protected ElementBaseGroup homePage;
    protected ElementBaseGroup configPage;
    protected ElementBaseGroup upgradesPage;

    MOElementButton homePageButton;
    MOElementButton configPageButton;
    MOElementButton upgradesPageButton;
    ElementSlotsList slotsList;

    public MOGuiMachine(Container container,T machine)
    {
        super(container);
        this.machine = machine;
        pages = new ArrayList<ElementBaseGroup>(2);

        homePage = new ElementBaseGroup(this,0,0,xSize,ySize);
        homePage.setName("Home");
        configPage = new ElementBaseGroup(this,0,0,xSize,ySize);
        configPage.setName("Configurations");
        upgradesPage = new ElementBaseGroup(this,0,0,xSize,ySize);

        pages.add(homePage);
        pages.add(configPage);
        pages.add(upgradesPage);

        homePageButton = new MOElementButton(this,this,6,8,"Home",0,0,22,0,22,0,22,22,"");
        homePageButton.setTexture(Reference.PATH_GUI_ITEM + "home2.png", 44, 22);
        homePageButton.setToolTip("Home");

        configPageButton = new MOElementButton(this,this,6,28 + 8,"Config",0,0,22,0,22,0,22,22,"");
        configPageButton.setTexture(Reference.PATH_GUI_ITEM + "config.png", 44, 22);
        configPageButton.setToolTip("Configurations");

        upgradesPageButton = new MOElementButton(this,this,6,28 + 28 + 8,"Upgrades",0,0,22,0,22,0,22,22,"");
        upgradesPageButton.setTexture(Reference.PATH_GUI_ITEM + "upgrades.png", 44, 22);
        upgradesPageButton.setToolTip("Upgrades");

        slotsList = new ElementSlotsList(this,5,49,machine.getInventory(),0);

        setPage(0);
    }

    @Override
    public void initGui()
    {
        super.initGui();

        this.addElement(homePage);
        this.addElement(configPage);
        this.addElement(upgradesPage);
        this.addElement(slotsList);

        sidePannel.addElement(homePageButton);
        sidePannel.addElement(configPageButton);
        sidePannel.addElement(upgradesPageButton);

        AddUpgradeSlots(inventorySlots,machine.getInventory(),upgradesPage);
    }

    @Override
    protected void updateElementInformation()
    {
        super.updateElementInformation();

        homePageButton.setEnabled(!homePage.isVisible());
        configPageButton.setEnabled(!configPage.isVisible());
        upgradesPageButton.setEnabled(!upgradesPage.isVisible());
    }

    @Override
    public void handleElementButtonClick(String buttonName, int mouseButton)
    {
        if(buttonName == "Home")
        {
            setPage(0);
        }
        else if (buttonName == "Config")
        {
            setPage(1);
        }
        else if (buttonName == "Upgrades")
        {
            setPage(2);
        }

        super.handleElementButtonClick(buttonName, mouseButton);
    }

    public void setPage(int page)
    {
        hideAllPages();

        if (page < pages.size())
        {
            pages.get(page).setVisible(true);
        }
    }

    void hideAllPages()
    {
        for (int i = 0;i < pages.size();i++)
        {
            pages.get(i).setVisible(false);
        }
    }

    public void AddUpgradeSlots(Container container,Inventory inventory,GuiElementList list)
    {
        for (int i = 0;i < container.inventorySlots.size();i++)
        {
            if(container.inventorySlots.get(i) instanceof SlotUpgrade)
            {
                ElementInventorySlot slotElement = new ElementInventorySlot(this, (MOSlot) container.inventorySlots.get(i), true);
                slotElement.setInventorySlot(inventory.getSlot(i));
                list.addElement(slotElement);
            }
        }
    }
}
