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

import matteroverdrive.container.ContainerFactory;
import matteroverdrive.gui.element.*;
import matteroverdrive.init.MatterOverdriveItems;
import matteroverdrive.machines.transporter.TileEntityMachineTransporter;
import matteroverdrive.util.MOStringHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

/**
 * Created by Simeon on 5/3/2015.
 */
public class GuiTransporter extends MOGuiMachine<TileEntityMachineTransporter>
{
	MOElementEnergy energy;
	ElementMatterStored matterStored;
	ElementIntegerField xCoords;
	ElementIntegerField yCoords;
	ElementIntegerField zCoords;
	MOElementButtonScaled importButton;
	MOElementButtonScaled newLocationButton;
	MOElementButtonScaled resetButton;
	MOElementTextField name;
	ElementTransportList list;
	MOElementButtonScaled removeLocation;

	public GuiTransporter(InventoryPlayer inventoryPlayer, TileEntityMachineTransporter machine)
	{
		super(ContainerFactory.createMachineContainer(machine, inventoryPlayer), machine, 225, 220);
		energy = new MOElementEnergy(this, xSize - 35, 50, machine.getEnergyStorage());
		matterStored = new ElementMatterStored(this, xSize - 35, 100, machine.getMatterStorage());

		xCoords = new ElementIntegerField(this, this, 32, 24, 80, 16);
		xCoords.setName("XCoord");
		yCoords = new ElementIntegerField(this, this, 32, 24 + 18, 80, 16);
		yCoords.setName("YCoord");
		zCoords = new ElementIntegerField(this, this, 32, 24 + 18 * 2, 80, 16);
		zCoords.setName("ZCoord");

		list = new ElementTransportList(this, this, 45, 30, 140, 100, machine);
		list.setName("Locations");

		name = new MOElementTextField(this, this, 32 + 6, 4, 74, 16);
		name.setTextOffset(6, 4);
		name.setBackground(MOElementButton.HOVER_TEXTURE_DARK);
		name.setName("LocationName");

		importButton = new MOElementButtonScaled(this, this, 22, 28 + 18 * 3, "Import", 50, 18);
		importButton.setNormalTexture(MOElementButton.NORMAL_TEXTURE);
		importButton.setOverTexture(MOElementButton.HOVER_TEXTURE);
		importButton.setDisabledTexture(MOElementButton.HOVER_TEXTURE_DARK);
		importButton.setText(MOStringHelper.translateToLocal("gui.label.button.import"));

		resetButton = new MOElementButtonScaled(this, this, 22 + 52, 28 + 18 * 3, "Reset", 50, 18);
		resetButton.setNormalTexture(MOElementButton.NORMAL_TEXTURE);
		resetButton.setOverTexture(MOElementButton.HOVER_TEXTURE);
		resetButton.setText(MOStringHelper.translateToLocal("gui.label.button.reset"));

		newLocationButton = new MOElementButtonScaled(this, this, 115, ySize - 55, "New", 40, 18);
		newLocationButton.setNormalTexture(MOElementButton.NORMAL_TEXTURE);
		newLocationButton.setOverTexture(MOElementButton.HOVER_TEXTURE);
		newLocationButton.setText(MOStringHelper.translateToLocal("gui.label.button.new"));

		removeLocation = new MOElementButtonScaled(this, this, 50, ySize - 55, "Remove", 60, 18);
		removeLocation.setNormalTexture(MOElementButton.NORMAL_TEXTURE);

		removeLocation.setText(MOStringHelper.translateToLocal("gui.label.button.remove"));
	}

	@Override
	public void initGui()
	{
		super.initGui();

		pages.get(0).addElement(energy);
		pages.get(0).addElement(matterStored);
		pages.get(0).addElement(list);
		pages.get(1).addElement(xCoords);
		pages.get(1).addElement(yCoords);
		pages.get(1).addElement(zCoords);
		pages.get(1).addElement(importButton);
		pages.get(1).addElement(name);
		pages.get(1).addElement(resetButton);
		pages.get(0).addElement(removeLocation);
		pages.get(0).addElement(newLocationButton);

		pages.get(1).getElements().get(0).setPosition(32, 110);

		xCoords.init();
		yCoords.init();
		zCoords.init();

		updateInfo();

		AddHotbarPlayerSlots(inventorySlots, this);
	}

	private void updateInfo()
	{
		list.setSelectedIndex(machine.selectedLocation);

		name.setText(machine.getSelectedLocation().name);

		xCoords.setBounds(machine.getPos().getX() - machine.getTransportRange(), machine.getPos().getX() + machine.getTransportRange());
		yCoords.setBounds(machine.getPos().getY() - machine.getTransportRange(), machine.getPos().getY() + machine.getTransportRange());
		zCoords.setBounds(machine.getPos().getZ() - machine.getTransportRange(), machine.getPos().getZ() + machine.getTransportRange());

		if (machine.getSelectedLocation() != null)
		{
			xCoords.setNumber(machine.getSelectedLocation().pos.getX());
			yCoords.setNumber(machine.getSelectedLocation().pos.getY());
			zCoords.setNumber(machine.getSelectedLocation().pos.getZ());
		}
		else
		{
			xCoords.setNumber(0);
			yCoords.setNumber(0);
			zCoords.setNumber(0);
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);

		if (pages.get(1).isVisible())
		{
			getFontRenderer().drawString("X:", pages.get(1).getPosX() + xCoords.getPosX() - 10, pages.get(1).getPosY() + xCoords.getPosY() + 4, 0xFFFFFF);
			getFontRenderer().drawString(Integer.toString(xCoords.getNumber() - machine.getPos().getX()), pages.get(1).getPosX() + xCoords.getPosX() + xCoords.getWidth() + 4, pages.get(1).getPosY() + xCoords.getPosY() + 4, 0xFFFFFF);

			getFontRenderer().drawString("Y:", pages.get(1).getPosX() + yCoords.getPosX() - 10, pages.get(1).getPosY() + yCoords.getPosY() + 4, 0xFFFFFF);
			getFontRenderer().drawString(Integer.toString(yCoords.getNumber() - machine.getPos().getY()), pages.get(1).getPosX() + yCoords.getPosX() + yCoords.getWidth() + 4, pages.get(1).getPosY() + yCoords.getPosY() + 4, 0xFFFFFF);

			getFontRenderer().drawString("Z:", pages.get(1).getPosX() + zCoords.getPosX() - 10, pages.get(1).getPosY() + zCoords.getPosY() + 4, 0xFFFFFF);
			getFontRenderer().drawString(Integer.toString(zCoords.getNumber() - machine.getPos().getZ()), pages.get(1).getPosX() + zCoords.getPosX() + zCoords.getWidth() + 4, pages.get(1).getPosY() + zCoords.getPosY() + 4, 0xFFFFFF);
		}
	}

	@Override
	public void handleElementButtonClick(MOElementBase element, String buttonName, int mouseButton)
	{
		super.handleElementButtonClick(element, buttonName, mouseButton);
		if (buttonName.equals("Import"))
		{
			ItemStack usb = machine.getStackInSlot(machine.usbSlotID);
			if (usb != null)
			{
				if (MatterOverdriveItems.transportFlashDrive.hasTarget(usb))
				{
					machine.setSelectedLocation(MatterOverdriveItems.transportFlashDrive.getTarget(usb).add(0, 1, 0), name.getText());
					machine.sendConfigsToServer(true);
					updateInfo();
				}
			}
		}
		else if (buttonName.equals("New"))
		{
			machine.addNewLocation(machine.getPos(), name.getText());
			machine.sendConfigsToServer(true);
			updateInfo();
			list.setSelectedIndex(list.getElementCount() - 1);
		}
		else if (buttonName.equals("Reset"))
		{
			machine.setSelectedLocation(machine.getPos(), machine.getSelectedLocation().name);
			machine.sendConfigsToServer(true);
			updateInfo();
		}
		else if (buttonName.equals("Remove"))
		{
			machine.removeLocation(list.getSelectedIndex());
			machine.sendConfigsToServer(true);
			updateInfo();
		}
		else if (buttonName.equals("XCoord") || buttonName.equals("YCoord") || buttonName.equals("ZCoord"))
		{
			machine.setSelectedLocation(new BlockPos(xCoords.getNumber(), yCoords.getNumber(), zCoords.getNumber()), name.getText());
			machine.sendConfigsToServer(true);
		}
	}

	@Override
	protected void updateElementInformation()
	{
		super.updateElementInformation();
		ItemStack usb = machine.getStackInSlot(machine.usbSlotID);
		if (usb != null)
		{
			BlockPos target = MatterOverdriveItems.transportFlashDrive.getTarget(usb);
			int targetDestination = target != null ? (int)Math.sqrt(target.distanceSq(machine.getPos())) : 0;
			int transportRange = machine.getTransportRange();
			boolean hasTarget = MatterOverdriveItems.transportFlashDrive.hasTarget(usb);
			if (!hasTarget || targetDestination > transportRange)
			{
				importButton.setEnabled(false);
				importButton.setToolTip(MOStringHelper.translateToLocal("gui.label.button.import.too_far"));
			}
			else
			{
				importButton.setEnabled(true);
				importButton.setToolTip(null);
			}
		}
		else
		{
			importButton.setEnabled(false);
			importButton.setToolTip(null);
		}
	}

	public void onPageChange(int newPage)
	{
		if (newPage == 1)
		{
			updateInfo();
		}
	}

	@Override
	public void ListSelectionChange(String name, int selected)
	{
		if (name.equals("Locations"))
		{
			updateInfo();
			machine.selectedLocation = selected;
			machine.sendConfigsToServer(true);
			list.setSelectedIndex(machine.selectedLocation);
		}
	}

	@Override
	public void textChanged(String elementName, String text, boolean typed)
	{
		if (typed)
		{
			if (elementName.equals("LocationName"))
			{
				machine.setSelectedLocation(new BlockPos(xCoords.getNumber(), yCoords.getNumber(), zCoords.getNumber()), name.getText());
				machine.sendConfigsToServer(true);
				//updateInfo();
			}
		}
	}
}
