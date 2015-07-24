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

import cofh.lib.gui.element.ElementDualScaled;
import cofh.lib.util.helpers.MathHelper;
import matteroverdrive.Reference;
import matteroverdrive.container.ContainerFactory;
import matteroverdrive.container.ContainerMachine;
import matteroverdrive.gui.element.ElementInventorySlot;
import matteroverdrive.gui.element.ElementMatterStored;
import matteroverdrive.gui.element.ElementSlot;
import matteroverdrive.gui.element.MOElementEnergy;
import matteroverdrive.tile.TileEntityMachineDecomposer;
import matteroverdrive.util.MatterHelper;
import net.minecraft.entity.player.InventoryPlayer;

public class GuiDecomposer extends MOGuiMachine<TileEntityMachineDecomposer>
{
	MOElementEnergy energyElement;
	ElementMatterStored matterElement;
	ElementDualScaled decompose_progress;
    ElementSlot outputSlot;

	public GuiDecomposer(InventoryPlayer inventoryPlayer,TileEntityMachineDecomposer entity)
	{
		super(ContainerFactory.createMachineContainer(entity,inventoryPlayer),entity);
		name = "decomposer";
		matterElement = new ElementMatterStored(this,74,39,machine.getMatterStorage());
		energyElement = new MOElementEnergy(this,100,39,machine.getEnergyStorage());
		decompose_progress = new ElementDualScaled(this,32,52);
        outputSlot = new ElementInventorySlot(this,getContainer().getSlotAt(machine.OUTPUT_SLOT_ID),129,55,22,22,"big");

		decompose_progress.setMode(1);
		decompose_progress.setSize(24, 16);
		decompose_progress.setTexture(Reference.TEXTURE_ARROW_PROGRESS, 48, 16);
		energyElement.setTexture(Reference.TEXTURE_ENERGY_METER, 32, 64);
	}
	
	@Override
	public void initGui()
	{
		super.initGui();

		pages.get(0).addElement(outputSlot);
		pages.get(0).addElement(energyElement);
		pages.get(0).addElement(matterElement);
        this.addElement(decompose_progress);

        AddMainPlayerSlots(this.inventorySlots, pages.get(0));
		AddHotbarPlayerSlots(this.inventorySlots,this);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_,
			int p_146976_2_, int p_146976_3_) 
	{
		super.drawGuiContainerBackgroundLayer(p_146976_1_, p_146976_2_, p_146976_3_);
		decompose_progress.setQuantity(MathHelper.round((((ContainerMachine)getContainer()).getProgress() * 24)));
		ManageReqiremnetsTooltips();
	}

	void ManageReqiremnetsTooltips()
	{
		if(machine.getStackInSlot(machine.INPUT_SLOT_ID) != null)
		{
			int matterAmount = MatterHelper.getMatterAmountFromItem(machine.getStackInSlot(machine.INPUT_SLOT_ID));
			energyElement.setEnergyRequired(-(machine.getEnergyDrainMax()));
			matterElement.setDrain(matterAmount);
			energyElement.setEnergyRequiredPerTick(-machine.getEnergyDrainPerTick());
		}
		else
		{
			energyElement.setEnergyRequired(0);
			matterElement.setDrain(0);
			energyElement.setEnergyRequiredPerTick(0);
		}
	}
}
