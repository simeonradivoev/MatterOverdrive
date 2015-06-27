package com.MO.MatterOverdrive.gui;

import cofh.lib.gui.element.ElementEnergyStored;
import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.container.ContainerPatternStorage;
import com.MO.MatterOverdrive.container.slot.MOSlot;
import com.MO.MatterOverdrive.container.slot.SlotPatternStorage;
import com.MO.MatterOverdrive.gui.element.ElementInventorySlot;
import com.MO.MatterOverdrive.tile.TileEntityMachinePatternStorage;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;

/**
 * Created by Simeon on 3/27/2015.
 */
public class GuiPatternStorage extends MOGuiMachine<TileEntityMachinePatternStorage>
{
    ElementEnergyStored energyElement;

    public GuiPatternStorage(InventoryPlayer playerInventory,TileEntityMachinePatternStorage patternStorage)
    {
        super(new ContainerPatternStorage(playerInventory,patternStorage),patternStorage);
        name = "pattern_storage";
        energyElement = new ElementEnergyStored(this,176,39,patternStorage.getEnergyStorage());
        energyElement.setTexture(Reference.TEXTURE_ENERGY_METER, 32, 64);
    }

    @Override
    public void initGui()
    {
        super.initGui();
        pages.get(0).addElement(energyElement);
        AddPatternStorageSlots(inventorySlots, pages.get(0));
        AddMainPlayerSlots(inventorySlots, pages.get(0));
        AddHotbarPlayerSlots(inventorySlots, this);
    }

    public void AddPatternStorageSlots(Container container,GuiElementList list)
    {
        for (int i = 0;i < container.inventorySlots.size();i++)
        {
            if(container.inventorySlots.get(i) instanceof SlotPatternStorage)
            {
                list.addElement(new ElementInventorySlot(this, (MOSlot)container.inventorySlots.get(i),22,22, "big",machine.getInventoryContainer().getSlot(i).getTexture()));
            }
        }
    }

}
