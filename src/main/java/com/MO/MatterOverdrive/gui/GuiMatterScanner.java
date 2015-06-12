package com.MO.MatterOverdrive.gui;

import cofh.lib.util.TimeTracker;
import com.MO.MatterOverdrive.MatterOverdrive;
import com.MO.MatterOverdrive.container.ContainerMatterScanner;
import com.MO.MatterOverdrive.gui.element.MOElementButton;
import com.MO.MatterOverdrive.gui.pages.PageInfo;
import com.MO.MatterOverdrive.gui.pages.PageScanInfo;
import com.MO.MatterOverdrive.items.MatterScanner;
import com.MO.MatterOverdrive.network.packet.bi.PacketMatterScannerGetDatabase;
import com.MO.MatterOverdrive.network.packet.server.PacketMatterScannerUpdate;

import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.util.MatterDatabaseHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class GuiMatterScanner extends MOGuiBase
{
	public ItemStack scanner;
    public int databaseSlot;

	private static final int REFRESH_DEPLAY = 200;
	private static final String INFO_PAGE_BUTTON_NAME = "InfoPageButton";
	private static final String SCAN_PAGE_BUTTON_NAME = "ScanPageButton";
	public static final String QUIDE_ELEMENTS_NAME = "QuideList";

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
		refreshTimeTracker = new TimeTracker();
		this.databaseSlot = slot;
		lastPage = MatterScanner.getLastPage(scanner);

		pageScanInfo = new PageScanInfo(this,0,0,"Scan Process",null,scanner);
		updateSelected(scanner);
		pageScanInfo.setSize(this.xSize, this.ySize);
		pageInfo = new PageInfo(this,0,0,xSize,ySize,"Matter Overdrive");
        pages.add(pageScanInfo);
        pages.add(pageInfo);

		scanPageButton = new MOElementButton(this,this,6,8,SCAN_PAGE_BUTTON_NAME,0,0,22,0,22,0,22,22,"");
		scanPageButton.setTexture(Reference.PATH_GUI_ITEM + "search54.png", 44, 22);
		scanPageButton.setToolTip("Scan Info");
        pageButtons.add(scanPageButton);

		infoPageButton = new MOElementButton(this,this,6,28 + 8,INFO_PAGE_BUTTON_NAME,0,0,22,0,22,0,22,22,"");
		infoPageButton.setTexture(Reference.PATH_GUI_ITEM + "info20.png", 44, 22);
		scanPageButton.setToolTip("Quide Database");
        pageButtons.add(infoPageButton);

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

		this.sidePannel.addElement(scanPageButton);
		this.sidePannel.addElement(infoPageButton);
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
	public void handleElementButtonClick(String buttonName, int mouseButton) 
	{
		super.handleElementButtonClick(buttonName, mouseButton);

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
        if(MatterDatabaseHelper.areEqual(MatterScanner.getSelectedAsNBT(scanner),lastSelected))
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
