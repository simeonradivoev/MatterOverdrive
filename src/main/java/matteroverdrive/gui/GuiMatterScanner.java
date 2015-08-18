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

import cofh.lib.gui.element.ElementBase;
import cofh.lib.util.TimeTracker;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.container.ContainerMatterScanner;
import matteroverdrive.gui.element.MOElementButton;
import matteroverdrive.gui.pages.PageInfo;
import matteroverdrive.gui.pages.PageScanInfo;
import matteroverdrive.items.MatterScanner;
import matteroverdrive.network.packet.bi.PacketMatterScannerGetDatabase;
import matteroverdrive.network.packet.server.PacketMatterScannerUpdate;
import matteroverdrive.proxy.ClientProxy;
import matteroverdrive.util.MOStringHelper;
import matteroverdrive.util.MatterDatabaseHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class GuiMatterScanner extends MOGuiBase
{
	public ItemStack scanner;
    public int databaseSlot;

	private static final int REFRESH_DEPLAY = 200;
    public static final String QUIDE_ELEMENTS_NAME = "GUIDE_ENTRY";

	int lastPage = 0;
	PageScanInfo pageScanInfo;
	PageInfo pageInfo;
	TimeTracker refreshTimeTracker;
	NBTTagCompound lastSelected;

	MOElementButton infoPageButton;
	MOElementButton scanPageButton;

	public GuiMatterScanner(ItemStack scanner,int slot)
	{
		super(new ContainerMatterScanner(),300,230);
		this.scanner = scanner;
		registerPages(this, scanner);
		refreshTimeTracker = new TimeTracker();
		this.databaseSlot = slot;
		lastPage = MatterScanner.getLastPage(scanner);

		MatterOverdrive.packetPipeline.sendToServer(new PacketMatterScannerGetDatabase(MatterScanner.getLinkPosition(scanner)));
	}
	
	@Override
	public void initGui() 
	{
		currentPage = MatterScanner.getLastPage(scanner);
		super.initGui();

		//set selected item in list, as active object
		//if(pageScanInfo.list.getSelectedElement() != null)
			//SetSelected((NBTTagCompound) pageScanInfo.list.getSelectedElement().getValue());
		//else
			//SetSelected(null);

        updateSelected(scanner);
	}

	private void registerPages(GuiMatterScanner gui,ItemStack matterScanner)
	{
		pageScanInfo = new PageScanInfo(this,0,0,"Scan Info",null,scanner);
		updateSelected(scanner);
		pageScanInfo.setSize(this.xSize, this.ySize);
		pageInfo = new PageInfo(this,0,0,xSize,ySize,"Info Database");

		AddPage(pageScanInfo, ClientProxy.holoIcons.getIcon("page_icon_info"), MOStringHelper.translateToLocal("gui.tooltip.page.scan_info"));
		AddPage(pageInfo,ClientProxy.holoIcons.getIcon("page_icon_search"),MOStringHelper.translateToLocal("gui.tooltip.page.info_database"));
	}

	@Override
	protected void updateElementInformation()
	{
		super.updateElementInformation();
		if(refreshTimeTracker.hasDelayPassed(Minecraft.getMinecraft().theWorld,REFRESH_DEPLAY))
		{
			System.out.println("Refreshed");
			updateSelected(scanner);
		}
	}

    public void UpdatePatternList(NBTTagList list)
    {
        pageScanInfo.updateList(list);
    }

	private NBTTagCompound getSelectedFromDatabase(ItemStack scanner)
	{
		return MatterScanner.getSelectedAsNBT(scanner);
	}

	private void updateSelected(ItemStack scanner)
	{
		lastSelected = getSelectedFromDatabase(scanner);
		pageScanInfo.setItemNBT(lastSelected);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int x, int y) {

		drawElements(0, true);
		drawTabs(0, true);
	}

	@Override
	public void handleElementButtonClick(ElementBase element,String buttonName, int mouseButton)
	{
		super.handleElementButtonClick(element,buttonName,mouseButton);
		if(buttonName == PageScanInfo.SCROLL_UP_BUTTON_NAME)
		{
			pageScanInfo.list.scrollUp();
		}
		else if(buttonName == PageScanInfo.SCROLL_DOWN_BUTTON_NAME)
		{
			pageScanInfo.list.scrollDown();
		}
		else if (buttonName == PageScanInfo.LIST_ELEMENT_NAME)
		{
			NBTTagCompound elementTag = (NBTTagCompound)pageScanInfo.list.getElement(mouseButton).getValue();
			SetSelected(elementTag);
		}
	}

	@Override
	public void handleListChange(String listName, int mouseButton,int element)
	{
		if(listName == QUIDE_ELEMENTS_NAME)
		{
			pageInfo.OpenQuide(element);
		}
	}

	@Override
    public void setPage(int page)
	{
        super.setPage(page);
		if(scanner.hasTagCompound())
		{
			scanner.getTagCompound().setShort(MatterScanner.PAGE_TAG_NAME, (short) page);
		}
	}

	void SetSelected(NBTTagCompound tagCompound)
	{
		lastSelected = tagCompound;
		pageScanInfo.setItemNBT(tagCompound);
	}

    @Override
    public void onGuiClosed()
    {
        super.onGuiClosed();
        if(MatterDatabaseHelper.areEqual(MatterScanner.getSelectedAsNBT(scanner), lastSelected))
            MatterOverdrive.packetPipeline.sendToServer(new PacketMatterScannerUpdate(scanner, (short) databaseSlot));
    }

	@Override
	public void textChanged(String elementName, String text, boolean typed)
	{

	}

    @Override
    public void ListSelectionChange(String name, int selected)
    {

    }
}
