package com.MO.MatterOverdrive.gui;

import cofh.lib.gui.element.ElementDualScaled;
import cofh.lib.util.helpers.MathHelper;
import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.container.ContainerMachine;
import com.MO.MatterOverdrive.container.ContainerRecycler;
import com.MO.MatterOverdrive.gui.element.ElementInventorySlot;
import com.MO.MatterOverdrive.gui.element.ElementSlot;
import com.MO.MatterOverdrive.gui.element.MOElementEnergy;
import com.MO.MatterOverdrive.tile.TileEntityMachineMatterRecycler;
import com.MO.MatterOverdrive.util.MatterHelper;
import net.minecraft.entity.player.InventoryPlayer;

/**
 * Created by Simeon on 5/15/2015.
 */
public class GuiRecycler extends MOGuiMachine<TileEntityMachineMatterRecycler>
{
    MOElementEnergy energyElement;
    ElementDualScaled recycle_progress;
    ElementSlot outputSlot;

    public GuiRecycler(InventoryPlayer inventoryPlayer, TileEntityMachineMatterRecycler machine) {
        super(new ContainerRecycler(inventoryPlayer,machine), machine);

        name = "recycler";
        energyElement = new MOElementEnergy(this,100,39,machine.getEnergyStorage());
        recycle_progress = new ElementDualScaled(this,32,54);
        outputSlot = new ElementInventorySlot(this,getContainer().getSlotAt(machine.OUTPUT_SLOT_ID),22,22,"big");

        recycle_progress.setMode(1);
        recycle_progress.setSize(24, 16);
        recycle_progress.setTexture(Reference.TEXTURE_ARROW_PROGRESS, 48, 16);
        energyElement.setTexture(Reference.TEXTURE_ENERGY_METER, 32, 64);
    }

    @Override
    public void initGui()
    {
        super.initGui();

        homePage.addElement(outputSlot);
        homePage.addElement(energyElement);
        this.addElement(recycle_progress);

        AddMainPlayerSlots(this.inventorySlots, homePage);
        AddHotbarPlayerSlots(this.inventorySlots, this);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float p_146976_1_,
                                                   int p_146976_2_, int p_146976_3_) {
        super.drawGuiContainerBackgroundLayer(p_146976_1_, p_146976_2_, p_146976_3_);
        recycle_progress.setQuantity(MathHelper.round(((float) this.machine.recycleProgress / 100f) * 24));
        ManageReqiremnetsTooltips();
    }

    void ManageReqiremnetsTooltips()
    {
        if(machine.getStackInSlot(machine.INPUT_SLOT_ID) != null)
        {
            int matterAmount = MatterHelper.getMatterAmountFromItem(machine.getStackInSlot(machine.INPUT_SLOT_ID));
            energyElement.setEnergyRequired(-(machine.getEnergyDrainMax()));
            energyElement.setEnergyRequiredPerTick(-machine.getEnergyDrainPerTick());
        }
        else
        {
            energyElement.setEnergyRequired(0);
            energyElement.setEnergyRequiredPerTick(0);
        }
    }
}
