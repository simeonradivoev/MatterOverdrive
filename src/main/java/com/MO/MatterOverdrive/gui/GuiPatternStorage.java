package com.MO.MatterOverdrive.gui;

import cofh.lib.gui.element.ElementEnergyStored;
import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.container.ContainerPatternStorage;
import com.MO.MatterOverdrive.container.slot.SlotPatternStorage;
import com.MO.MatterOverdrive.data.inventory.Slot;
import com.MO.MatterOverdrive.gui.element.ElementPlayerSlots;
import com.MO.MatterOverdrive.gui.element.ElementSlot;
import com.MO.MatterOverdrive.gui.element.ElementSlotsList;
import com.MO.MatterOverdrive.tile.TileEntityMachinePatternStorage;
import net.minecraft.entity.player.InventoryPlayer;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simeon on 3/27/2015.
 */
public class GuiPatternStorage extends MOGuiBase
{
    private TileEntityMachinePatternStorage patternStorage;
    ElementEnergyStored energyElement;
    ElementSlotsList slotsList;
    ElementPlayerSlots playerSlots;

    public GuiPatternStorage(InventoryPlayer playerInventory,TileEntityMachinePatternStorage patternStorage)
    {
        super(new ContainerPatternStorage(playerInventory,patternStorage));
        energyElement = new ElementEnergyStored(this,176,39,patternStorage.getEnergyStorage());
        List<Slot> slots = new ArrayList<Slot>();
        slots.add(patternStorage.getInventory().getSlot(patternStorage.input_slot));
        slots.add(patternStorage.getInventory().getSlot(patternStorage.getEnergySlotID()));
        slotsList = new ElementSlotsList(this,5,49,slots,0);
        playerSlots = new ElementPlayerSlots(this,44,91);
        this.patternStorage = patternStorage;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTick, int x, int y) {

        super.drawGuiContainerBackgroundLayer(partialTick,x,y);

        DrawInputSlots();
    }

    public void DrawInputSlots()
    {
        GL11.glPushMatrix();
        GL11.glTranslatef(guiLeft, guiTop, 0.0F);

        this.bindTexture(ElementSlot.slot_big);

        for (int x = 0; x < 3;x++)
        {
            this.drawSizedTexturedModalRect(x * 24 + 74, 34, 0, 0, 22, 22, 22, 22);
        }
        for (int x = 0; x < 3;x++)
        {
            this.drawSizedTexturedModalRect(x * 24 + 74, 24 + 34, 0, 0, 22, 22, 22, 22);
        }

        GL11.glPopMatrix();
    }

    @Override
    public void initGui()
    {
        super.initGui();
        energyElement.setTexture(Reference.TEXTURE_ENERGY_METER, 32, 64);
        this.addElement(energyElement);
        this.addElement(slotsList);
        this.addElement(playerSlots);
    }

}
