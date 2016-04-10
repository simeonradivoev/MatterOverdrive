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
import matteroverdrive.container.ContainerFactory;
import matteroverdrive.container.slot.SlotInventory;
import matteroverdrive.data.inventory.SlotContract;
import matteroverdrive.gui.element.ElementInventorySlot;
import matteroverdrive.tile.TileEntityMachineContractMarket;
import matteroverdrive.util.MOStringHelper;
import net.minecraft.entity.player.InventoryPlayer;

/**
 * Created by Simeon on 11/22/2015.
 */
public class GuiContractMarket extends MOGuiMachine<TileEntityMachineContractMarket>
{
	public GuiContractMarket(InventoryPlayer inventoryPlayer, TileEntityMachineContractMarket machine)
	{
		super(ContainerFactory.createMachineContainer(machine, inventoryPlayer), machine);
		name = "contract_market";
	}

	@Override
	public void initGui()
	{
		super.initGui();
		int slotXCount = 0;
		int slotYCount = 0;
		for (Object object : inventorySlots.inventorySlots)
		{
			if (object instanceof SlotInventory && ((SlotInventory)object).getSlot() instanceof SlotContract)
			{
				ElementInventorySlot contractSlot = new ElementInventorySlot(this, (SlotInventory)object, slotXCount * 18 + 45, ySize - 124 - slotYCount * 18, 18, 18, "small");
				pages.get(0).addElement(contractSlot);
				slotXCount++;
				if (slotXCount >= 9)
				{
					slotYCount++;
					slotXCount = 0;
				}
			}
		}
		AddMainPlayerSlots(this.inventorySlots, pages.get(0));
		AddHotbarPlayerSlots(this.inventorySlots, this);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		if (pages.get(0).isVisible())
		{
			fontRendererObj.drawString(MOStringHelper.translateToLocal("gui.time_until_next_quest", MOStringHelper.formatRemainingTime(machine.getTimeUntilNextQuest() / 20f)), 64, 30, Reference.COLOR_GUI_LIGHT.getColor());
		}
	}
}
