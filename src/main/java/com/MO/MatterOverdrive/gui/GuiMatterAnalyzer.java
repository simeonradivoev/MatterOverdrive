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
public class GuiMatterAnalyzer extends MOGuiBase
{
    private static final String ANALYZE_PROGRESS_TEXTURE_PATH = Reference.PATH_ELEMENTS + "Progress_Arrow_Right.png";
    ElementEnergyStored energyElement;
    ElementSlotsList slotsList;
    ElementPlayerSlots playerSlots;
    TileEntityMachineMatterAnalyzer analyzer;
    ElementScanProgress scanProgress;

    public GuiMatterAnalyzer(InventoryPlayer playerInventory,TileEntityMachineMatterAnalyzer analyzer)
    {
        super(new ContainerMatterAnalyzer(playerInventory,analyzer));
        energyElement = new ElementEnergyStored(this,176,39,analyzer.getEnergyStorage());
        slotsList = new ElementSlotsList(this,5,49,analyzer.getInventory(),0);
        playerSlots = new ElementPlayerSlots(this,44,91);
        scanProgress = new ElementScanProgress(this,49,36);
        this.analyzer = analyzer;
    }

    @Override
    public void initGui()
    {
        super.initGui();
        energyElement.setTexture(Reference.TEXTURE_ENERGY_METER,32,64);
        this.addElement(energyElement);
        this.addElement(slotsList);
        this.addElement(playerSlots);
        this.addElement(scanProgress);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float p_146976_1_,
                                                   int p_146976_2_, int p_146976_3_)
    {
        super.drawGuiContainerBackgroundLayer(p_146976_1_, p_146976_2_, p_146976_3_);

        scanProgress.setProgress(((float) this.analyzer.analyzeTime / (float) this.analyzer.ANALYZE_SPEED));
        
        if(this.analyzer.getStackInSlot(analyzer.input_slot) != null)
        	scanProgress.setSeed(Item.getIdFromItem(this.analyzer.getStackInSlot(analyzer.input_slot).getItem()));
    }
}
