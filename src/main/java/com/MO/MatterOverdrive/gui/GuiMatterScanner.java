package com.MO.MatterOverdrive.gui;

import java.util.List;

import com.MO.MatterOverdrive.MatterOverdrive;
import com.MO.MatterOverdrive.gui.element.*;
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

public class GuiMatterScanner extends MOGuiBase
{
	public ItemStack database;
    public int databaseSlot;
	private static final String SCROLL_DOWN_BUTTON_NAME = "scroll_down";
	private static final String SCROLL_UP_BUTTON_NAME = "scroll_up";
	private static final String LIST_ELEMENT_NAME = "list";
	public static final String backgroundPath = Reference.PATH_GUI + "matter_scanner.png";
	public static final ResourceLocation background = new ResourceLocation(Reference.PATH_GUI + "matter_scanner.png");
	private static FontRenderer   fontRenderer = Minecraft.getMinecraft().fontRenderer;
	private static TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();
    int lastSelectedIndex;
	
	MatterDatabaseListBox list;
	MOElementTextField searchField;
	ElementButton scrollButtonUp;
	ElementButton scrollButtonDown;
	ElementProgress scan_progress;
	ElementScanProgress scan_info_graph;
	ElementItemPreview itemPreview;
	
	int xStart = 0,yStart = 0;
	
	@Override
	public void initGui() 
	{
		super.initGui();
		list.background = this.background;
		list.setName(LIST_ELEMENT_NAME);
		searchField = new MOElementTextField(this,41,26);
		scan_progress.setTexture(backgroundPath, 256, 256);
		scan_progress.setMaxValue(MatterDatabaseHelper.MAX_ITEM_PROGRESS);
		scan_progress.SetTextPostition(18, 5);
		scan_progress.setTextColor(new GuiColor(255, 255,255).getColor());
		scan_info_graph.setProgress(1);
		list.setFilter("");
		list.updateList("");
		this.addElement(list);
		this.addElement(searchField);
		this.addElement(scrollButtonUp);
		this.addElement(scrollButtonDown);
		this.addElement(scan_progress);
		this.addElement(scan_info_graph);
		this.addElement(itemPreview);
	}
	
	public GuiMatterScanner(ItemStack database,int slot)
	{
		super(new ContainerFalse());
		this.database = database;
		//this.texture = background;
		//this.xSize = 216;
		//this.ySize = 176;
		list = new MatterDatabaseListBox(this,3,39,37,100,database);
		scan_progress = new ElementProgress(this,46 + 35,146 + 2,46,146,39,202,62,188,105,14,142,18);
		scrollButtonUp = new ElementButton(this,11,27,SCROLL_UP_BUTTON_NAME,22,188,32,188,10,10,backgroundPath);
		scrollButtonDown = new ElementButton(this,11,142,SCROLL_DOWN_BUTTON_NAME,42,188,52,188,10,10,backgroundPath);
		scan_info_graph = new ElementScanProgress(this,87,44);
		itemPreview = new ElementItemPreview(this,45,44,null);
        this.databaseSlot = slot;
        lastSelectedIndex = MatterDatabaseHelper.GetSelectedIndex(database);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int x, int y) {
		if(searchField != null)
		{
			this.list.setFilter(searchField.getText());
		}
		DrawSelectedInfo();
		drawElements(0, true);
		drawTabs(0, true);
	}
	
	void DrawSelectedInfo()
	{
		int selected = MatterDatabaseHelper.GetSelectedIndex(database);
		
		if(selected >= 0)
		{
			NBTTagCompound itemNBT = MatterDatabaseHelper.GetItemAsNBTAt(database, selected);
			ItemStack item = MatterDatabaseHelper.GetItemStackFromNBT(itemNBT);
			
			if(itemNBT != null)
			{
				String Name = item.getDisplayName();
				String Matter = "Matter: " + String.valueOf(MatterHelper.getMatterAmountFromItem(item)) + MatterHelper.MATTER_UNIT;
				
				List infos = item.getTooltip(null, false);
				infos.add(Matter);
				RenderUtils.DrawMultilineInfo(infos, 50, 98, 8, 32, new GuiColor(255,255,255).getColor());
				scan_progress.setValue(MatterDatabaseHelper.GetProgressFromNBT(itemNBT));
				scan_progress.setText(String.valueOf((int)(((float)MatterDatabaseHelper.GetProgressFromNBT(itemNBT) / (float)100) * 100)) + "%");
			}
		}
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
			itemPreview.setItemStack(ItemStack.loadItemStackFromNBT(elementTag));
			scan_info_graph.setSeed(elementTag.getShort("id"));
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
        if(lastSelectedIndex != MatterDatabaseHelper.GetSelectedIndex(database))
            MatterOverdrive.packetPipeline.sendToServer(new PacketMatterScannerUpdate(database, (short) databaseSlot));
    }
}
