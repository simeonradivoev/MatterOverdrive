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

import matteroverdrive.Reference;
import matteroverdrive.container.ContainerAnalyzer;
import matteroverdrive.container.ContainerMachine;
import matteroverdrive.container.MOBaseContainer;
import matteroverdrive.gui.element.ElementScanProgress;
import matteroverdrive.gui.element.MOElementEnergy;
import matteroverdrive.gui.pages.PageTasks;
import matteroverdrive.machines.analyzer.TileEntityMachineMatterAnalyzer;
import matteroverdrive.proxy.ClientProxy;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;

/**
 * Created by Simeon on 3/16/2015.
 */
public class GuiMatterAnalyzer extends MOGuiNetworkMachine<TileEntityMachineMatterAnalyzer>
{
	MOElementEnergy energyElement;
	ElementScanProgress scanProgress;
	PageTasks pageTasks;

	public GuiMatterAnalyzer(InventoryPlayer playerInventory, TileEntityMachineMatterAnalyzer analyzer)
	{
		super(new ContainerAnalyzer(playerInventory, analyzer), analyzer);
		name = "matter_analyzer";
		energyElement = new MOElementEnergy(this, 176, 39, analyzer.getEnergyStorage());
		energyElement.setTexture(Reference.TEXTURE_ENERGY_METER, 32, 64);
		scanProgress = new ElementScanProgress(this, 49, 36);
	}

	@Override
	public void registerPages(MOBaseContainer container, TileEntityMachineMatterAnalyzer machine)
	{
		super.registerPages(container, machine);
		pageTasks = new PageTasks(this, 0, 0, xSize, ySize, machine.getTaskQueue((byte)0));
		pageTasks.setName("Tasks");
		AddPage(pageTasks, ClientProxy.holoIcons.getIcon("page_icon_tasks"), "gui.tooltip.page.tasks").setIconColor(Reference.COLOR_MATTER);
	}

	@Override
	public void initGui()
	{
		super.initGui();

		pages.get(0).addElement(energyElement);
		pages.get(0).addElement(scanProgress);

		AddMainPlayerSlots(inventorySlots, pages.get(0));
		AddHotbarPlayerSlots(inventorySlots, this);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_,
												   int p_146976_2_, int p_146976_3_)
	{
		super.drawGuiContainerBackgroundLayer(p_146976_1_, p_146976_2_, p_146976_3_);

		scanProgress.setProgress(((ContainerMachine)getContainer()).getProgress());

		if (this.machine.getStackInSlot(machine.input_slot) != null)
		{
			scanProgress.setSeed(Item.getIdFromItem(this.machine.getStackInSlot(machine.input_slot).getItem()));
			energyElement.setEnergyRequired(-machine.getEnergyDrainMax());
			energyElement.setEnergyRequiredPerTick(-machine.getEnergyDrainPerTick());
		}
		else
		{
			energyElement.setEnergyRequired(0);
			energyElement.setEnergyRequiredPerTick(0);
		}
	}
}
