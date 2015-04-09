package com.MO.MatterOverdrive.gui;

import cofh.lib.gui.element.ElementEnergyStored;

import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.container.ContainerMatterAnalyzer;
import com.MO.MatterOverdrive.gui.element.ElementPlayerSlots;
import com.MO.MatterOverdrive.gui.element.ElementScanProgress;
import com.MO.MatterOverdrive.gui.element.ElementSlotsList;
import com.MO.MatterOverdrive.tile.TileEntityMachineMatterAnalyzer;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
/**
 * Created by Simeon on 3/16/2015.
 */
public class GuiMatterAnalyzer extends MOGuiMachine<TileEntityMachineMatterAnalyzer>
{
    ElementEnergyStored energyElement;
    ElementScanProgress scanProgress;

    public GuiMatterAnalyzer(InventoryPlayer playerInventory,TileEntityMachineMatterAnalyzer analyzer)
    {
        super(new ContainerMatterAnalyzer(playerInventory,analyzer),analyzer);
        energyElement = new ElementEnergyStored(this,176,39,analyzer.getEnergyStorage());
        scanProgress = new ElementScanProgress(this,49,36);
    }

    @Override
    public void initGui()
    {
        super.initGui();
        energyElement.setTexture(Reference.TEXTURE_ENERGY_METER, 32, 64);
        homePage.addElement(energyElement);
        homePage.addElement(scanProgress);

        AddPlayerSlots(inventorySlots, homePage, true, false);
        AddPlayerSlots(inventorySlots,this,false,true);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float p_146976_1_,
                                                   int p_146976_2_, int p_146976_3_)
    {
        super.drawGuiContainerBackgroundLayer(p_146976_1_, p_146976_2_, p_146976_3_);

        scanProgress.setProgress(((float) this.machine.analyzeTime / (float) this.machine.ANALYZE_SPEED));
        
        if(this.machine.getStackInSlot(machine.input_slot) != null)
        	scanProgress.setSeed(Item.getIdFromItem(this.machine.getStackInSlot(machine.input_slot).getItem()));
    }
}
