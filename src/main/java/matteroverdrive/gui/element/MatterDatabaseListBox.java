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

package matteroverdrive.gui.element;

import matteroverdrive.data.matter_network.ItemPattern;
import matteroverdrive.gui.MOGuiBase;
import matteroverdrive.items.MatterScanner;
import matteroverdrive.util.MatterHelper;
import matteroverdrive.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.List;

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
		ItemPattern itemComp;
		String name;

		public MatterDatabaseEntry(ItemPattern itemComp)
		{
			this.itemComp = itemComp;
			this.name = itemComp.toItemStack(false).getDisplayName();
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
			GlStateManager.color(1.0F, 1.0F, 1.0F,1.0F);

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
				ItemStack itemStack = itemComp.toItemStack(false);
				RenderUtils.renderStack(3 + x, 3 + y, itemStack);
			}
		}


		public void drawToolTop(MOElementListBox listBox, int x, int y)
		{
			ItemStack item = itemComp.toItemStack(false);
			List tooltip = item.getTooltip(Minecraft.getMinecraft().thePlayer, false);
			tooltip.add("Progress: " + itemComp.getProgress() + "%");
			tooltip.add("Matter: " + MatterHelper.getMatterAmountFromItem(item) + MatterHelper.MATTER_UNIT);
			((MatterDatabaseListBox)listBox).getGui().setTooltip(tooltip);
			GlStateManager.color(1, 1, 1, 1);
		}
	}

	public MOGuiBase getGui()
	{
		return this.gui;
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



	public void updateList(List<ItemPattern> patternList) {
		//System.out.println("List Updated");
		this.clear();
		ItemPattern selected = MatterScanner.getSelectedAsPattern(scanner);

		if (patternList != null) {
			for (int i = 0; i < patternList.size(); i++) {
				if (selected.equals(patternList.get(i)))
					_selectedIndex = i;

				MatterDatabaseEntry selectedEntry = new MatterDatabaseEntry(patternList.get(i));
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
		(gui).handleElementButtonClick(this,this.name,newIndex);
	}

}
