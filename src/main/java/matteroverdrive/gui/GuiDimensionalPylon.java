package matteroverdrive.gui;

import matteroverdrive.Reference;
import matteroverdrive.container.ContainerDimensionalPylon;
import matteroverdrive.gui.element.ElementDoubleCircleBar;
import matteroverdrive.machines.dimensional_pylon.TileEntityMachineDimensionalPylon;
import matteroverdrive.util.MOEnergyHelper;
import matteroverdrive.util.MatterHelper;
import net.minecraft.entity.player.InventoryPlayer;

import java.text.DecimalFormat;

/**
 * Created by Simeon on 2/12/2016.
 */
public class GuiDimensionalPylon extends MOGuiMachine<TileEntityMachineDimensionalPylon>
{
	ElementDoubleCircleBar powerBar;
	DecimalFormat format;

	public GuiDimensionalPylon(InventoryPlayer inventoryPlayer, TileEntityMachineDimensionalPylon machine)
	{
		super(new ContainerDimensionalPylon(inventoryPlayer, machine), machine, 256, 230);
		format = new DecimalFormat("#.###");
		name = "dimensional_pylon";
		powerBar = new ElementDoubleCircleBar(this, 70, 40, 135, 135, Reference.COLOR_GUI_ENERGY);
		powerBar.setColorRight(Reference.COLOR_HOLO);
	}

	@Override
	public void initGui()
	{
		super.initGui();
		pages.get(0).addElement(powerBar);
		AddHotbarPlayerSlots(this.inventorySlots, this, "small", null, 60, ySize - 27);
	}

	@Override
	protected void updateElementInformation()
	{
		super.updateElementInformation();

		powerBar.setProgressRight((float)machine.getMatterStorage().getMatterStored() / (float)machine.getMatterStorage().getCapacity());
		powerBar.setProgressLeft((float)machine.getEnergyStorage().getEnergyStored() / (float)machine.getEnergyStorage().getMaxEnergyStored());
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		if (pages.get(0).isVisible())
		{
			ContainerDimensionalPylon container = (ContainerDimensionalPylon)getContainer();
			String info = "Efficiency";
			int width = fontRendererObj.getStringWidth(info);
			fontRendererObj.drawString(info, 140 - width / 2, 132, Reference.COLOR_GUI_DARKER.getColor());
			info = DecimalFormat.getPercentInstance().format(machine.getDimensionalValue());
			width = fontRendererObj.getStringWidth(info);
			fontRendererObj.drawString(info, 140 - width / 2, 142, Reference.COLOR_GUI_DARKER.getColor());

			double angle = -(Math.PI * 0.87) * powerBar.getProgressLeft() - ((Math.PI * 2) * 0.03);
			int xPos = 137 + (int)Math.round(Math.sin(angle) * 76);
			int yPos = 104 + (int)Math.round(Math.cos(angle) * 74);
			drawCenteredString(fontRendererObj, format.format(powerBar.getProgressLeft() * 100) + "%", xPos, yPos, Reference.COLOR_HOLO_RED.getColor());

			angle = (Math.PI * 0.87) * powerBar.getProgressRight() + ((Math.PI * 2) * 0.03);
			xPos = 137 + (int)Math.round(Math.sin(angle) * 76);
			yPos = 104 + (int)Math.round(Math.cos(angle) * 74);
			drawCenteredString(fontRendererObj, format.format(powerBar.getProgressRight() * 100) + "%", xPos, yPos, Reference.COLOR_MATTER.getColor());

			info = "+" + container.getEnergyGenPerTick() + MOEnergyHelper.ENERGY_UNIT + "/t";
			width = fontRendererObj.getStringWidth(info);
			xPos = 138 - width / 2;
			yPos = 110;
			fontRendererObj.drawStringWithShadow(info, xPos, yPos, Reference.COLOR_HOLO_RED.getColor());

			info = "-" + format.format(container.getMatterDrainPerSec()) + MatterHelper.MATTER_UNIT + "/s";
			width = fontRendererObj.getStringWidth(info);
			xPos = 138 - width / 2;
			yPos = 98;
			fontRendererObj.drawStringWithShadow(info, xPos, yPos, Reference.COLOR_MATTER.getColor());

			info = "Charge: " + machine.getCharge();
			width = fontRendererObj.getStringWidth(info);
			xPos = 138 - width / 2;
			yPos = 86;
			fontRendererObj.drawStringWithShadow(info, xPos, yPos, Reference.COLOR_MATTER.getColor());
		}
	}
}
