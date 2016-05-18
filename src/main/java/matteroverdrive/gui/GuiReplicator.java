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

import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.api.network.MatterNetworkTaskState;
import matteroverdrive.container.ContainerMachine;
import matteroverdrive.container.ContainerReplicator;
import matteroverdrive.container.MOBaseContainer;
import matteroverdrive.data.matter_network.ItemPatternMapping;
import matteroverdrive.gui.element.*;
import matteroverdrive.gui.pages.PageTasks;
import matteroverdrive.machines.replicator.TileEntityMachineReplicator;
import matteroverdrive.matter_network.tasks.MatterNetworkTaskReplicatePattern;
import matteroverdrive.network.packet.server.task_queue.PacketRemoveTask;
import matteroverdrive.proxy.ClientProxy;
import matteroverdrive.util.MOStringHelper;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;

import java.util.List;

public class GuiReplicator extends MOGuiNetworkMachine<TileEntityMachineReplicator>
{
	MOElementEnergy energyElement;
	ElementMatterStored matterElement;
	ElementDualScaled replicate_progress;
	ElementSlot outputSlot;
	ElementSlot seccoundOutputSlot;
	PageTasks pagePackets;
	ElementItemPattern itemPattern;

	public GuiReplicator(InventoryPlayer inventoryPlayer, TileEntityMachineReplicator entity)
	{
		super(new ContainerReplicator(inventoryPlayer, entity), entity);
		name = "replicator";
		matterElement = new ElementMatterStored(this, 141, 39, machine.getMatterStorage());
		energyElement = new MOElementEnergy(this, 167, 39, machine.getEnergyStorage());
		replicate_progress = new ElementDualScaled(this, 32, 52);
		outputSlot = new ElementInventorySlot(this, this.getContainer().getSlotAt(machine.OUTPUT_SLOT_ID), 70, 52, 22, 22, "big");
		seccoundOutputSlot = new ElementInventorySlot(this, this.getContainer().getSlotAt(machine.SECOND_OUTPUT_SLOT_ID), 96, 52, 22, 22, "big");

		itemPattern = new ElementItemPattern(this, null, "big_main", 37, 22);
		slotsList.setPosition(5, 49);
		slotsList.addElementAt(0, itemPattern);

		replicate_progress.setMode(1);
		replicate_progress.setSize(24, 16);
		replicate_progress.setTexture(Reference.TEXTURE_ARROW_PROGRESS, 48, 16);
	}

	@Override
	public void initGui()
	{
		super.initGui();
		this.addElement(replicate_progress);
		pages.get(0).addElement(outputSlot);
		pages.get(0).addElement(seccoundOutputSlot);
		pages.get(0).addElement(matterElement);
		pages.get(0).addElement(energyElement);
		AddHotbarPlayerSlots(inventorySlots, this);
		AddMainPlayerSlots(inventorySlots, pages.get(0));
	}

	@Override
	public void registerPages(MOBaseContainer container, TileEntityMachineReplicator machine)
	{
		super.registerPages(container, machine);

		pagePackets = new PageTasks(this, 10, 0, xSize, ySize, machine.getTaskQueue((byte)0));
		pagePackets.setName("Tasks");
		AddPage(pagePackets, ClientProxy.holoIcons.getIcon("page_icon_tasks"), MOStringHelper.translateToLocal("gui.tooltip.page.tasks")).setIconColor(Reference.COLOR_MATTER);
	}

	@Override
	protected void renderToolTip(ItemStack stack, int x, int y)
	{
		List list = stack.getTooltip(this.mc.thePlayer, this.mc.gameSettings.advancedItemTooltips);

		for (int k = 0; k < list.size(); ++k)
		{
			String info = (String)list.get(k);

			if (k == 0)
			{
				list.set(k, stack.getRarity().rarityColor + info);
			}
			else
			{
				list.set(k, TextFormatting.GRAY + info);
			}
		}

		FontRenderer font = stack.getItem().getFontRenderer(stack);
		drawHoveringText(list, x, y, (font == null ? fontRendererObj : font));
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTick, int x, int y)
	{

		super.drawGuiContainerBackgroundLayer(partialTick, x, y);

		replicate_progress.setQuantity(Math.round(((ContainerMachine)getContainer()).getProgress() * 24));
	}

	@Override
	public void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		ManageReqiremnetsTooltips();
	}

	void ManageReqiremnetsTooltips()
	{
		energyElement.setEnergyRequiredPerTick(-machine.getEnergyDrainPerTick());
		energyElement.setEnergyRequired(-machine.getEnergyDrainMax());
	}

	@Override
	protected void updateElementInformation()
	{
		super.updateElementInformation();

		MatterNetworkTaskReplicatePattern task = (MatterNetworkTaskReplicatePattern)machine.getTaskQueue((byte)0).peek();
		if (task != null)
		{
			if (itemPattern.getPatternMapping() == null || itemPattern.getPatternMapping().getItemPattern() == null || !itemPattern.getPatternMapping().getItemPattern().equals(task.getPattern()))
			{
				itemPattern.setPatternMapping(new ItemPatternMapping(task.getPattern(), null, 0, 0));
			}
			itemPattern.setAmount(((ContainerReplicator)inventorySlots).getPatternReplicateCount());
		}
		else
		{
			if (itemPattern.getPatternMapping() != null)
			{
				itemPattern.setPatternMapping(null);
			}
			itemPattern.setAmount(0);
		}
	}

	@Override
	public void handleElementButtonClick(MOElementBase element, String buttonName, int mouseButton)
	{
		super.handleElementButtonClick(element, buttonName, mouseButton);
		if (buttonName.equals("DropTask"))
		{
			NBTTagCompound tagCompound = new NBTTagCompound();
			tagCompound.setInteger("TaskID", mouseButton);
			MatterOverdrive.packetPipeline.sendToServer(new PacketRemoveTask(machine, mouseButton, (byte)0, MatterNetworkTaskState.INVALID));
		}
	}

}
