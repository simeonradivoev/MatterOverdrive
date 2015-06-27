package com.MO.MatterOverdrive.gui;

import cofh.lib.gui.GuiColor;
import cofh.lib.util.helpers.StringHelper;
import com.MO.MatterOverdrive.MatterOverdrive;
import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.container.ContainerMachine;
import com.MO.MatterOverdrive.container.MOBaseContainer;
import com.MO.MatterOverdrive.gui.element.*;
import com.MO.MatterOverdrive.gui.pages.ConfigPage;
import com.MO.MatterOverdrive.gui.pages.PageUpgrades;
import com.MO.MatterOverdrive.network.packet.server.PacketChangeRedstoneMode;
import com.MO.MatterOverdrive.proxy.ClientProxy;
import com.MO.MatterOverdrive.tile.MOTileEntityMachine;
import com.MO.MatterOverdrive.util.MOStringHelper;

/**
 * Created by Simeon on 4/8/2015.
 */
public class MOGuiMachine<T extends MOTileEntityMachine> extends MOGuiBase
{
    T machine;
    ElementSlotsList slotsList;
    ElementIndicator indicator;


    public MOGuiMachine(ContainerMachine<T> container,T machine)
    {
        this(container,machine,225,186);
    }

    public MOGuiMachine(ContainerMachine<T> container,T machine,int width,int height)
    {
        super(container, width, height);
        this.machine = machine;

        indicator = new ElementIndicator(this,6,ySize - 18);

        slotsList = new ElementSlotsList(this,5,52,80,200,machine.getInventoryContainer(),0);
        slotsList.setMargin(5);

        registerPages(container,machine);
    }

    public void registerPages(MOBaseContainer container,T machine)
    {
        ElementBaseGroup homePage = new ElementBaseGroup(this,0,0,xSize,ySize);
        homePage.setName("Home");
        ConfigPage configPage = new ConfigPage(this,0,0,xSize,ySize);
        configPage.setName("Configurations");
        PageUpgrades upgradesPage = new PageUpgrades(this,0,0,xSize,ySize,container);
        upgradesPage.setName("Upgrades");

        AddPage(homePage, ClientProxy.holoIcons.getIcon("page_icon_home"),MOStringHelper.translateToLocal("gui.tooltip.page.home")).setIconColor(Reference.COLOR_MATTER);
        AddPage(configPage, ClientProxy.holoIcons.getIcon("page_icon_config"), MOStringHelper.translateToLocal("gui.tooltip.page.configurations"));
        AddPage(upgradesPage, ClientProxy.holoIcons.getIcon("page_icon_upgrades"), MOStringHelper.translateToLocal("gui.tooltip.page.upgrades"));

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
    public void handleElementButtonClick(String buttonName, int mouseButton)
    {
        super.handleElementButtonClick(buttonName, mouseButton);
        if (buttonName == "RedstoneMode")
        {
            machine.setRedstoneMode((byte)mouseButton);
            MatterOverdrive.packetPipeline.sendToServer(new PacketChangeRedstoneMode(machine, (byte) mouseButton));
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

    public T getMachine()
    {
        return machine;
    }
}
