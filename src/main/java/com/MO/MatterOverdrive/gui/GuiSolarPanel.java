package com.MO.MatterOverdrive.gui;

import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.container.ContainerSolarPanel;
import com.MO.MatterOverdrive.gui.element.MOElementEnergy;
import com.MO.MatterOverdrive.tile.TileEntityMachineSolarPanel;
import net.minecraft.entity.player.InventoryPlayer;

/**
 * Created by Simeon on 4/9/2015.
 */
public class GuiSolarPanel extends MOGuiMachine<TileEntityMachineSolarPanel>
{
    MOElementEnergy energy;

    public GuiSolarPanel(InventoryPlayer inventoryPlayer,TileEntityMachineSolarPanel solarPanel)
    {
        super(new ContainerSolarPanel(inventoryPlayer,solarPanel),solarPanel);
        name = "solar_panel";
        energy = new MOElementEnergy(this,117,35,solarPanel.getEnergyStorage());
        energy.setTexture(Reference.TEXTURE_ENERGY_METER, 32, 64);
    }

    @Override
    public void initGui()
    {
        super.initGui();
        AddMainPlayerSlots(inventorySlots, homePage);
        AddHotbarPlayerSlots(inventorySlots, this);
        elements.remove(slotsList);
        homePage.addElement(energy);
    }

    @Override
    protected void updateElementInformation()
    {
        super.updateElementInformation();
        energy.setEnergyRequired(machine.getChargeAmount());
    }
}
