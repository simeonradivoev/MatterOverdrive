package com.MO.MatterOverdrive.gui.element;

import java.util.List;

import com.MO.MatterOverdrive.api.matter.IMatterDatabase;
import com.MO.MatterOverdrive.handler.IMatterEntry;
import com.MO.MatterOverdrive.items.MatterScanner;
import org.lwjgl.opengl.GL11;

import com.MO.MatterOverdrive.gui.MOGuiBase;
import com.MO.MatterOverdrive.util.MatterDatabaseHelper;
import com.MO.MatterOverdrive.util.MatterHelper;
import com.MO.MatterOverdrive.util.RenderUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;

public class MatterDatabaseListBox extends MOElementListBox
{
	public ItemStack scanner;
	public ResourceLocation background;
	public String filter = "";
	
	
	public MatterDatabaseListBox(MOGuiBase gui,int x,int y,int width,int height, ItemStack database)
	{
		super(gui,x,y,width,height);
		this.scanner = database;
		updateList(this.filter);
	}
	
	class MatterDatabaseEntry implements IMOListBoxElement
	{
		NBTTagCompound itemComp;
		int listID;
		boolean active;
		
		public MatterDatabaseEntry(NBTTagCompound itemComp,int listID,boolean active)
		{
			this.listID = listID;
			this.itemComp = itemComp;
			this.active = active;
		}
		
		@Override
		public int getHeight() {
			return 25;
		}

		@Override
		public int getWidth() {
			return 25;
		}

		@Override
		public Object getValue() 
		{
			return itemComp;
		}

		@Override
		public void draw(MOElementListBox listBox, int x, int y, int backColor,
				int textColor, boolean selected) 
		{
			ItemStack itemStack = ItemStack.loadItemStackFromNBT(itemComp);
			Minecraft.getMinecraft().getTextureManager().bindTexture(background);
			GL11.glColor4f(1.0F, 1.0F, 1.0F,1.0F);
			
			if(this.active)
			{
				listBox.drawTexturedModalRect(x, y, 0, 198, 39, 22);
				
			}
			else
			{
				listBox.drawTexturedModalRect(x, y, 0, 176, 22, 22);
			}
			
			RenderUtils.renderStack(3 + x,3 + y, itemStack);
		}

		
		public void drawToolTop(MOElementListBox listBox, int x, int y) 
		{
			ItemStack item = MatterDatabaseHelper.GetItemStackFromNBT(itemComp);
			List tooltip = item.getTooltip(Minecraft.getMinecraft().thePlayer, false);
			tooltip.add("Progress: " + MatterDatabaseHelper.GetProgressFromNBT(itemComp) + "%");
			tooltip.add("Matter: " + MatterHelper.getMatterAmountFromItem(item) + MatterHelper.MATTER_UNIT);
			((MatterDatabaseListBox)listBox).getGui().setTooltip(tooltip);
			GL11.glColor4f(1, 1, 1, 1);
		}
	}
	
	public MOGuiBase getGui() 
	{
		return (MOGuiBase)this.gui;
	}

	public void setFilter(String filter)
	{
		if(!this.filter.equals(filter))
		{
			updateList(filter);
		}
		this.filter = filter;
	}
	
	@Override
	public void drawBackground(int mouseX, int mouseY, float gameTicks) 
	{
		
	}
	
	public String getFilter()
	{
		return this.filter;
	}
	
	
	
	public void updateList(String filter)
	{
		//System.out.println("List Updated");
		this.clear();

		IMatterDatabase database = MatterScanner.getLink(Minecraft.getMinecraft().theWorld, scanner);

		if(database != null) {
			NBTTagList itemList = database.getItemsAsNBT();
			String selectedIndex = MatterScanner.getSelectedIndex(scanner);

			if (itemList != null) {
				for (int i = 0; i < itemList.tagCount(); i++) {
					int itemId = itemList.getCompoundTagAt(i).getShort("id");

					if (filter == "" || ItemStack.loadItemStackFromNBT(itemList.getCompoundTagAt(i)).getDisplayName().toLowerCase().contains(filter.toLowerCase())) {
						MatterDatabaseEntry selected = new MatterDatabaseEntry(itemList.getCompoundTagAt(i), i, selectedIndex == MatterDatabaseHelper.GetItemStackFromNBT(itemList.getCompoundTagAt(i)).getItem().getUnlocalizedName());
						this.add(selected);

						if (selectedIndex == MatterDatabaseHelper.GetItemStackFromNBT(itemList.getCompoundTagAt(i)).getItem().getUnlocalizedName())
							this.setSelectedIndex(this.getElementCount() - 1);
					}
				}
			}
		}
	}

	public void updateList(){this.updateList(this.filter);}
	
	@Override
	protected void onSelectionChanged(int newIndex, IMOListBoxElement newElement) 
	{
		MatterDatabaseEntry entry = (MatterDatabaseEntry) newElement;

		MatterScanner.setSelectedIndex(scanner,MatterDatabaseHelper.GetItemStackFromNBT(entry.itemComp).getItem().getUnlocalizedName());
		updateList(this.filter);
		gui.handleElementButtonClick(this.name,((MatterDatabaseEntry) newElement).listID);
	}
	
}
