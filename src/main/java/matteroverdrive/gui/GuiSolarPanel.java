package matteroverdrive.gui;

import matteroverdrive.container.ContainerSolarPanel;
import matteroverdrive.gui.element.MOElementEnergy;
import matteroverdrive.tile.TileEntityMachineSolarPanel;
import net.minecraft.entity.player.InventoryPlayer;

/**
 * Created by Simeon on 4/9/2015.
 */
public class GuiSolarPanel extends MOGuiMachine<TileEntityMachineSolarPanel>
{
	MOElementEnergy energy;

	public GuiSolarPanel(InventoryPlayer inventoryPlayer, TileEntityMachineSolarPanel solarPanel)
	{
		super(new ContainerSolarPanel(inventoryPlayer, solarPanel), solarPanel);
		name = "solar_panel";
		energy = new MOElementEnergy(this, 117, 35, solarPanel.getEnergyStorage());
	}

	@Override
	public void initGui()
	{
		super.initGui();
		AddMainPlayerSlots(inventorySlots, pages.get(0));
		AddHotbarPlayerSlots(inventorySlots, this);
		elements.remove(slotsList);
		pages.get(0).addElement(energy);
	}

	@Override
	protected void updateElementInformation()
	{
		super.updateElementInformation();
		energy.setEnergyRequired(machine.getChargeAmount());
	}
}
