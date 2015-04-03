package com.MO.MatterOverdrive.gui;

import java.util.ArrayList;
import java.util.List;

import cofh.lib.gui.element.ElementBase;
import com.MO.MatterOverdrive.MatterOverdrive;
import com.MO.MatterOverdrive.api.matter.IMatterDatabase;
import com.MO.MatterOverdrive.gui.element.*;
import com.MO.MatterOverdrive.gui.pages.PageScanInfo;
import com.MO.MatterOverdrive.items.MatterScanner;
import com.MO.MatterOverdrive.network.packet.PacketMatterScannerUpdate;

import cofh.lib.gui.GuiColor;
import cofh.lib.gui.container.ContainerFalse;
import cofh.lib.gui.element.ElementButton;

import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.util.MatterDatabaseHelper;
import com.MO.MatterOverdrive.util.MatterHelper;
import com.MO.MatterOverdrive.util.RenderUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiMatterScanner extends MOGuiBase
{
	public ItemStack scanner;
    public int databaseSlot;
	private static final String SCROLL_DOWN_BUTTON_NAME = "scroll_down";
	private static final String SCROLL_UP_BUTTON_NAME = "scroll_up";
	private static final String LIST_ELEMENT_NAME = "list";

	public static final ResourceLocation background = new ResourceLocation(Reference.PATH_GUI + "matter_scanner.png");
	public static final String backgroundPath = Reference.PATH_GUI + "matter_scanner.png";

	int lastPage;
	private List<ElementBase> pages;
	PageScanInfo pageScanInfo;

	NBTTagCompound lastSelected;
	
	MatterDatabaseListBox list;
	MOElementTextField searchField;
	ElementButton scrollButtonUp;
	ElementButton scrollButtonDown;
	
	int xStart = 0,yStart = 0;
	
	@Override
	public void initGui() 
	{
		super.initGui();
		list.background = this.background;
		list.setName(LIST_ELEMENT_NAME);
		searchField = new MOElementTextField(this,41,26);
		list.setFilter("");
		list.updateList("");

		//set selected item in list, as active object
		if(list.getSelectedElement() != null)
			SetSelected((NBTTagCompound) list.getSelectedElement().getValue());


		this.addElement(searchField);
		this.addElement(scrollButtonUp);
		this.addElement(scrollButtonDown);
		this.addElement(list);
	}
	
	public GuiMatterScanner(ItemStack scanner,int slot)
	{
		super(new ContainerFalse());
		pages = new ArrayList<ElementBase>();
		this.scanner = scanner;
		//this.texture = background;
		//this.xSize = 216;
		//this.ySize = 176;
		list = new MatterDatabaseListBox(this,3,39,37,100,scanner);
		scrollButtonUp = new ElementButton(this,11,27,SCROLL_UP_BUTTON_NAME,22,188,32,188,10,10,backgroundPath);
		scrollButtonDown = new ElementButton(this,11,142,SCROLL_DOWN_BUTTON_NAME,42,188,52,188,10,10,backgroundPath);
        this.databaseSlot = slot;
		lastSelected = MatterScanner.getSelectedAsNBT(scanner);
		lastPage = MatterScanner.getLastPage(scanner);

		pageScanInfo = new PageScanInfo(this,0,0,"Scan Process",lastSelected);
		pages.add(pageScanInfo);
	}

	@Override
	public void drawScreen(int x, int y, float partialTick) {

		super.drawScreen(x,y,partialTick);
		pages.get(lastPage).update();
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int x, int y) {
		if(searchField != null)
		{
			this.list.setFilter(searchField.getText());
		}
		DrawSelectedInfo();
		this.pages.get(lastPage).drawForeground(mouseX,mouseY);
		drawElements(0, true);
		drawTabs(0, true);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTick, int x, int y) {

		super.drawGuiContainerBackgroundLayer(partialTick, x, y);

		GL11.glPushMatrix();
		GL11.glTranslatef(guiLeft, guiTop, 0.0F);
		this.pages.get(lastPage).drawBackground(mouseX, mouseY, partialTick);
		GL11.glPopMatrix();
	}
	
	void DrawSelectedInfo()
	{
		IMatterDatabase databaseTile = MatterScanner.getLink(Minecraft.getMinecraft().theWorld,scanner);
		
		if(databaseTile != null && lastSelected != null)
		{
			NBTTagCompound itemNBT = lastSelected;
			ItemStack item = MatterDatabaseHelper.GetItemStackFromNBT(itemNBT);
			
			if(itemNBT != null)
			{
				String Name = item.getDisplayName();
				String Matter = "Matter: " + String.valueOf(MatterHelper.getMatterAmountFromItem(item)) + MatterHelper.MATTER_UNIT;
				
				List infos = item.getTooltip(null, false);
				infos.add(Matter);
				RenderUtils.DrawMultilineInfo(infos, 50, 98, 8, 32, new GuiColor(255, 255, 255).getColor());
				pageScanInfo.setItemNBT(itemNBT);
				return;
			}
		}

		//scan_progress.setVisible(false);
		//scan_info_graph.setVisible(false);
	}

	@Override
	public void handleElementButtonClick(String buttonName, int mouseButton) 
	{
		if(buttonName == SCROLL_UP_BUTTON_NAME)
		{
			list.scrollUp();
		}
		else if(buttonName == SCROLL_DOWN_BUTTON_NAME)
		{
			list.scrollDown();
		}
		else if (buttonName == LIST_ELEMENT_NAME)
		{
			NBTTagCompound elementTag = (NBTTagCompound)list.getElement(mouseButton).getValue();
			SetSelected(elementTag);
		}
	}

	void SetSelected(NBTTagCompound tagCompound)
	{
		if(tagCompound != null)
		{
			lastSelected = tagCompound;
			pageScanInfo.setItemNBT(tagCompound);
		}
	}
	
	void DrawBlocks(ItemStack scanner,float gameTicks)
	{
		list.drawBackground(this.mouseX, this.mouseY, gameTicks);
		list.drawForeground(this.mouseX, this.mouseY);
	}

    @Override
    public void onGuiClosed()
    {
        super.onGuiClosed();
        if(MatterDatabaseHelper.areEqual(MatterScanner.getSelectedAsNBT(scanner),lastSelected))
            MatterOverdrive.packetPipeline.sendToServer(new PacketMatterScannerUpdate(scanner, (short) databaseSlot));
    }
}
