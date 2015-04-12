package com.MO.MatterOverdrive.gui;

import cofh.lib.gui.GuiColor;
import cofh.lib.gui.element.ElementBase;
import cofh.lib.util.helpers.StringHelper;
import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.container.slot.MOSlot;
import com.MO.MatterOverdrive.container.slot.SlotPatternStorage;
import com.MO.MatterOverdrive.container.slot.SlotUpgrade;
import com.MO.MatterOverdrive.data.Inventory;
import com.MO.MatterOverdrive.data.inventory.UpgradeSlot;
import com.MO.MatterOverdrive.gui.element.*;
import com.MO.MatterOverdrive.gui.pages.PageUpgrades;
import com.MO.MatterOverdrive.tile.MOTileEntityMachine;
import net.minecraft.inventory.Container;
import net.minecraft.util.StatCollector;

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
    protected PageUpgrades upgradesPage;

    MOElementButton homePageButton;
    MOElementButton configPageButton;
    MOElementButton upgradesPageButton;
    ElementSlotsList slotsList;
    ElementIndicator indicator;


    public MOGuiMachine(Container container,T machine)
    {
        super(container);
        this.machine = machine;
        pages = new ArrayList<ElementBaseGroup>(2);

        homePage = new ElementBaseGroup(this,0,0,xSize,ySize);
        homePage.setName("Home");
        configPage = new ElementBaseGroup(this,0,0,xSize,ySize);
        configPage.setName("Configurations");
        upgradesPage = new PageUpgrades(this,0,0,xSize,ySize,container,machine.getInventory());
        indicator = new ElementIndicator(this,6,159);

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
        this.addElement(indicator);

        sidePannel.addElement(homePageButton);
        sidePannel.addElement(configPageButton);
        sidePannel.addElement(upgradesPageButton);
    }

    @Override
    protected void updateElementInformation()
    {
        super.updateElementInformation();

        homePageButton.setEnabled(!homePage.isVisible());
        configPageButton.setEnabled(!configPage.isVisible());
        upgradesPageButton.setEnabled(!upgradesPage.isVisible());

        if (machine.isActive())
        {
            indicator.setIndication(1);
        }else
        {
            indicator.setIndication(0);
        }
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

    @Override
    protected void drawGuiContainerForegroundLayer(int x, int y)
    {
        if(name != null && !name.isEmpty())
        {
            String n = StringHelper.localize("gui." + name + ".name");
            fontRendererObj.drawString(n, 125 - (fontRendererObj.getStringWidth(n) / 2), 7, new GuiColor(44, 54, 52).getColor());
        }
        drawElements(0, true);
        drawTabs(0, true);
    }
}
