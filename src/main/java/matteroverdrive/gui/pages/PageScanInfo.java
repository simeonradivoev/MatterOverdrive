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

package matteroverdrive.gui.pages;

import matteroverdrive.Reference;
import matteroverdrive.client.data.Color;
import matteroverdrive.data.matter_network.ItemPattern;
import matteroverdrive.gui.MOGuiBase;
import matteroverdrive.gui.element.*;
import matteroverdrive.util.MatterDatabaseHelper;
import matteroverdrive.util.MatterHelper;
import matteroverdrive.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

import java.util.List;

/**
 * Created by Simeon on 4/3/2015.
 */
public class PageScanInfo extends ElementBaseGroup
{
	public static final String backgroundPath = Reference.PATH_GUI + "matter_scanner.png";
	public static final String LIST_ELEMENT_NAME = "list";
	public static final String SCROLL_UP_BUTTON_NAME = "scroll_up";
	public static final String SCROLL_DOWN_BUTTON_NAME = "scroll_down";
	public MatterDatabaseListBox list;
	ItemPattern pattern;
	ElementProgress scan_progress;
	ElementScanProgress scan_info_graph;
	MOElementTextField searchField;
	MOElementButton scrollButtonUp;
	MOElementButton scrollButtonDown;

	ElementItemPreview itemPreview;

	public PageScanInfo(MOGuiBase gui, int posX, int posY, String name, ItemPattern pattern, ItemStack scanner)
	{
		super(gui, posX, posY);
		this.setName(name);
		this.pattern = pattern;

		scan_info_graph = new ElementScanProgress(gui, 94, 44);
		itemPreview = new ElementItemPreview(gui, 45, 44, null);
		scan_progress = new ElementProgress(gui, 44 + 35, 202 + 2, 44, 202, 39, 202, 62, 188, 105, 14, 142, 18);
		searchField = new MOElementTextField(gui, 41, 26, 242, 14);
		list = new MatterDatabaseListBox(gui, 3, 39, 42, 152, scanner);
		scrollButtonUp = new MOElementButton(gui, this, 11, 27, SCROLL_UP_BUTTON_NAME, 22, 188, 32, 188, 10, 10, backgroundPath);
		scrollButtonDown = new MOElementButton(gui, this, 11, 190, SCROLL_DOWN_BUTTON_NAME, 42, 188, 52, 188, 10, 10, backgroundPath);

		list.setName(LIST_ELEMENT_NAME);

		list.setFilter("");

		scan_progress.setTexture(backgroundPath, 256, 256);
		scan_progress.setMaxValue(MatterDatabaseHelper.MAX_ITEM_PROGRESS);
		scan_progress.SetTextPostition(18, 5);
		scan_progress.setTextColor(new Color(255, 255, 255).getColor());
		scan_info_graph.setProgress(1);
	}

	@Override
	public void init()
	{
		super.init();

		elements.add(scan_info_graph);
		elements.add(itemPreview);
		elements.add(scan_progress);
		elements.add(searchField);
		elements.add(scrollButtonUp);
		elements.add(scrollButtonDown);
		elements.add(list);
	}

	@Override
	public void drawForeground(int mouseX, int mouseY)
	{
		super.drawForeground(mouseX, mouseY);


		ItemStack item = pattern.toItemStack(false);

		if (item != null)
		{
			//GL11.glEnable(GL11.GL_BLEND);
			GlStateManager.enableAlpha();
			GlStateManager.alphaFunc(GL11.GL_GREATER, 0.5f);
			List infos = item.getTooltip(null, false);

			if (MatterHelper.CanScan(item))
			{
				String Matter = "Matter: " + String.valueOf(MatterHelper.getMatterAmountFromItem(item)) + MatterHelper.MATTER_UNIT;
				infos.add(Matter);
			}

			RenderUtils.DrawMultilineInfo(infos, 50, 98, 8, 200, Color.WHITE.getColor());
		}
		else
		{
			String Matter = "Matter: " + String.valueOf(MatterHelper.getMatterAmountFromItem(item)) + MatterHelper.MATTER_UNIT;
			Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow("No Item Selected!", 80, 90, new Color(255, 150, 50).getColor());
		}
	}

	@Override
	public void update(int mouseX, int mouseY, float partialTicks)
	{
		super.update(mouseX, mouseY, partialTicks);

		if (searchField != null)
		{
			this.list.setFilter(searchField.getText());
		}
	}

	public void updateList(List<ItemPattern> list)
	{
		this.list.updateList(list);
	}

	public void setItemNBT(ItemPattern pattern)
	{
		this.pattern = pattern;
		ItemStack itemStack = pattern.toItemStack(false);
		scan_progress.setVisible(pattern != null && MatterHelper.CanScan(itemStack));
		scan_info_graph.setVisible(pattern != null);
		itemPreview.setVisible(pattern != null);

		if (pattern != null)
		{
			scan_progress.setValue(pattern.getProgress());
			scan_progress.setText(String.valueOf((int)(((float)pattern.getProgress() / (float)100) * 100)) + "%");


			scan_info_graph.setSeed(pattern.getItemID());
			itemPreview.setItemStack(itemStack);
		}
	}
}
