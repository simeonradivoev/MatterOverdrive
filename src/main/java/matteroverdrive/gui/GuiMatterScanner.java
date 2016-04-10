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

import matteroverdrive.MatterOverdrive;
import matteroverdrive.container.ContainerFalse;
import matteroverdrive.data.matter_network.ItemPattern;
import matteroverdrive.gui.element.MOElementBase;
import matteroverdrive.gui.element.MOElementButton;
import matteroverdrive.gui.pages.PageScanInfo;
import matteroverdrive.items.MatterScanner;
import matteroverdrive.network.packet.bi.PacketMatterScannerGetDatabase;
import matteroverdrive.network.packet.server.PacketMatterScannerUpdate;
import matteroverdrive.proxy.ClientProxy;
import matteroverdrive.util.MOStringHelper;
import matteroverdrive.util.TimeTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import java.util.List;

public class GuiMatterScanner extends MOGuiBase
{
	public static final String QUIDE_ELEMENTS_NAME = "GUIDE_ENTRY";
	private static final int REFRESH_DEPLAY = 200;
	public ItemStack scanner;
	public int databaseSlot;
	int lastPage = 0;
	PageScanInfo pageScanInfo;
	TimeTracker refreshTimeTracker;
	ItemPattern lastSelected;

	MOElementButton infoPageButton;
	MOElementButton scanPageButton;

	public GuiMatterScanner(ItemStack scanner, int slot)
	{
		super(new ContainerFalse(), 300, 230);
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

	private void registerPages(GuiMatterScanner gui, ItemStack matterScanner)
	{
		pageScanInfo = new PageScanInfo(this, 0, 0, "Scan Info", null, scanner);
		updateSelected(scanner);
		pageScanInfo.setSize(this.xSize, this.ySize);

		AddPage(pageScanInfo, ClientProxy.holoIcons.getIcon("page_icon_info"), MOStringHelper.translateToLocal("gui.tooltip.page.scan_info"));
	}

	@Override
	protected void updateElementInformation()
	{
		super.updateElementInformation();
		if (refreshTimeTracker.hasDelayPassed(Minecraft.getMinecraft().theWorld, REFRESH_DEPLAY))
		{
			//System.out.println("Refreshed");
			updateSelected(scanner);
		}
	}

	public void UpdatePatternList(List<ItemPattern> list)
	{
		pageScanInfo.updateList(list);
	}

	private ItemPattern getSelectedFromDatabase(ItemStack scanner)
	{
		return MatterScanner.getSelectedAsPattern(scanner);
	}

	private void updateSelected(ItemStack scanner)
	{
		lastSelected = getSelectedFromDatabase(scanner);
		pageScanInfo.setItemNBT(lastSelected);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{

		drawElements(0, true);
	}

	@Override
	public void handleElementButtonClick(MOElementBase element, String buttonName, int mouseButton)
	{
		super.handleElementButtonClick(element, buttonName, mouseButton);
		if (buttonName.equals(PageScanInfo.SCROLL_UP_BUTTON_NAME))
		{
			pageScanInfo.list.scrollUp();
		}
		else if (buttonName.equals(PageScanInfo.SCROLL_DOWN_BUTTON_NAME))
		{
			pageScanInfo.list.scrollDown();
		}
		else if (buttonName.equals(PageScanInfo.LIST_ELEMENT_NAME))
		{
			ItemPattern elementTag = (ItemPattern)pageScanInfo.list.getElement(mouseButton).getValue();
			SetSelected(elementTag);
		}
	}

	@Override
	public void handleListChange(String listName, int mouseButton, int element)
	{
		if (listName == QUIDE_ELEMENTS_NAME)
		{
			//guideDescription.OpenGuide(element);
		}
	}

	@Override
	public void setPage(int page)
	{
		super.setPage(page);
		if (scanner.hasTagCompound())
		{
			scanner.getTagCompound().setShort(MatterScanner.PAGE_TAG_NAME, (short)page);
		}
	}

	void SetSelected(ItemPattern tagCompound)
	{
		lastSelected = tagCompound;
		pageScanInfo.setItemNBT(tagCompound);
	}

	@Override
	public void onGuiClosed()
	{
		super.onGuiClosed();
		if (MatterScanner.getSelectedAsPattern(scanner).equals(lastSelected))
		{
			MatterOverdrive.packetPipeline.sendToServer(new PacketMatterScannerUpdate(scanner, (short)databaseSlot));
		}
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
