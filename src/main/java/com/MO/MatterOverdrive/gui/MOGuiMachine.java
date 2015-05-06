package com.MO.MatterOverdrive.gui;

import cofh.lib.gui.GuiColor;
import cofh.lib.gui.element.ElementBase;
import cofh.lib.gui.element.ElementButtonManaged;
import cofh.lib.util.helpers.MathHelper;
import cofh.lib.util.helpers.StringHelper;
import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.container.ContainerMachine;
import com.MO.MatterOverdrive.container.MOBaseContainer;
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
    protected ElementBaseGroup homePage;
    protected ElementBaseGroup configPage;
    protected PageUpgrades upgradesPage;

    MOElementButton homePageButton;
    MOElementButton configPageButton;
    MOElementButton upgradesPageButton;
    ElementSlotsList slotsList;
    ElementIndicator indicator;


    public MOGuiMachine(ContainerMachine<T> container,T machine)
    {
        this(container,machine,225,186);
    }

    public MOGuiMachine(ContainerMachine<T> container,T machine,int width,int height)
    {
        super(container,width,height);
        this.machine = machine;

        homePage = new ElementBaseGroup(this,0,0,xSize,ySize);
        homePage.setName("Home");
        configPage = new ElementBaseGroup(this,0,0,xSize,ySize);
        configPage.setName("Configurations");
        upgradesPage = new PageUpgrades(this,0,0,xSize,ySize,container,machine);
        indicator = new ElementIndicator(this,6,ySize - 18);

        pages.add(homePage);
        pages.add(configPage);
        pages.add(upgradesPage);

        homePageButton = new MOElementButton(this,this,6,8,"Home",0,0,24,0,24,0,24,24,"");
        homePageButton.setTexture(Reference.PATH_GUI_ITEM + "home2.png", 48, 24);
        homePageButton.setToolTip("Home");
        pageButtons.add(homePageButton);

        configPageButton = new MOElementButton(this,this,6,8,"Config",0,0,24,0,24,0,24,24,"");
        configPageButton.setTexture(Reference.PATH_GUI_ITEM + "config.png", 48, 24);
        configPageButton.setToolTip("Configurations");
        pageButtons.add(configPageButton);

        upgradesPageButton = new MOElementButton(this,this,6,8,"Upgrades",0,0,24,0,24,0,24,24,"");
        upgradesPageButton.setTexture(Reference.PATH_GUI_ITEM + "upgrades.png", 48, 24);
        upgradesPageButton.setToolTip("Upgrades");
        pageButtons.add(upgradesPageButton);

        slotsList = new ElementSlotsList(this,5,52,80,200,machine.getInventory(),0,false);
        slotsList.setMargin(5);

        setPage(0);
    }

    @Override
    public void initGui()
    {
        super.initGui();
        this.addElement(slotsList);
        this.addElement(indicator);
    }

    @Override
    protected void updateElementInformation()
    {
        super.updateElementInformation();

        for (int i = 0; i < pageButtons.size();i++)
        {
            pageButtons.get(i).setPosition(6,8 + (pageButtons.get(i).getHeight() + 2) * i);
        }

        if (machine.isActive())
        {
            indicator.setIndication(1);
        }else
        {
            indicator.setIndication(0);
        }
    }

    @Override
    public void textChanged(String elementName, String text, boolean typed)
    {

    }

    @Override
    public  void ListSelectionChange(String name,int selected)
    {

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
