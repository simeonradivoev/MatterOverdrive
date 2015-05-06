package com.MO.MatterOverdrive.gui.element;

import java.util.List;

import com.MO.MatterOverdrive.api.matter.IMatterDatabase;
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
	private static final ResourceLocation ACTIVE_SLOT_BG = ElementSlot.getTexture("big_main_active");
	private static final ResourceLocation SLOT_BG = ElementSlot.getTexture("big");
	public ItemStack scanner;
	public String filter = "";
	
	
	public MatterDatabaseListBox(MOGuiBase gui,int x,int y,int width,int height, ItemStack scanner)
	{
		super(gui,x,y,width,height);
		this.scanner = scanner;
	}
	
	class MatterDatabaseEntry implements IMOListBoxElement
	{
		NBTTagCompound itemComp;
		String name;
		
		public MatterDatabaseEntry(NBTTagCompound itemComp)
		{
			this.itemComp = itemComp;
			this.name = ItemStack.loadItemStackFromNBT(itemComp).getDisplayName();
		}

		@Override
		public String getName()
		{
			return this.name;
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
		public void draw(MOElementListBox listBox, int x, int y, int backColor,int textColor, boolean selected,boolean BG)
		{
			GL11.glColor4f(1.0F, 1.0F, 1.0F,1.0F);

			if (BG) {
				if (selected) {
					gui.bindTexture(ACTIVE_SLOT_BG);
					gui.drawSizedTexturedModalRect(x, y, 0, 0, 38, 22, 38, 22);
				} else {
					gui.bindTexture(SLOT_BG);
					gui.drawSizedTexturedModalRect(x, y, 0, 0, 22, 22, 22, 22);
				}
			}else
			{
				ItemStack itemStack = ItemStack.loadItemStackFromNBT(itemComp);
				RenderUtils.renderStack(3 + x, 3 + y, itemStack);
			}
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
		this.filter = filter;
	}

	@Override
	protected boolean shouldBeDisplayed(IMOListBoxElement element)
	{
		return element.getName().toLowerCase().contains(filter.toLowerCase());
	}
	
	public String getFilter()
	{
		return this.filter;
	}
	
	
	
	public void updateList(NBTTagList itemList) {
		//System.out.println("List Updated");
		this.clear();
		NBTTagCompound selected = MatterScanner.getSelectedAsNBT(scanner);

		if (itemList != null) {
			for (int i = 0; i < itemList.tagCount(); i++) {
				if (MatterDatabaseHelper.areEqual(selected, itemList.getCompoundTagAt(i)))
					_selectedIndex = i;

				MatterDatabaseEntry selectedEntry = new MatterDatabaseEntry(itemList.getCompoundTagAt(i));
				this.add(selectedEntry);
			}
		}
	}

	@Override
	protected void onSelectionChanged(int newIndex, IMOListBoxElement newElement) 
	{
		MatterDatabaseEntry entry = (MatterDatabaseEntry) newElement;
		MatterScanner.setSelected(scanner, entry.itemComp);
		//updateList(this.filter);
		gui.handleElementButtonClick(this.name,newIndex);
	}
	
}
