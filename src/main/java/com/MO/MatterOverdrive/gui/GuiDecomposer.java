package com.MO.MatterOverdrive.gui;

import com.MO.MatterOverdrive.data.inventory.Slot;
import com.MO.MatterOverdrive.gui.element.ElementPlayerSlots;
import com.MO.MatterOverdrive.gui.element.ElementSlot;
import com.MO.MatterOverdrive.gui.element.ElementSlotsList;
import com.MO.MatterOverdrive.util.MatterHelper;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import cofh.lib.gui.element.ElementDualScaled;
import cofh.lib.gui.element.ElementEnergyStored;
import cofh.lib.util.helpers.MathHelper;

import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.container.ContainerDecomposer;
import com.MO.MatterOverdrive.gui.element.ElementMatterStored;
import com.MO.MatterOverdrive.tile.TileEntityMachineDecomposer;

import java.util.ArrayList;
import java.util.List;

public class GuiDecomposer extends MOGuiBase
{
	public TileEntityMachineDecomposer decomposer;
	ElementEnergyStored energyElement;
	ElementMatterStored matterElement;
	ElementDualScaled decompose_progress;
    ElementPlayerSlots playerSlots;
    ElementSlotsList slotsList;
    ElementSlot outputSlot;

	public GuiDecomposer(InventoryPlayer inventoryPlayer,TileEntityMachineDecomposer entity)
	{
		super(new ContainerDecomposer(inventoryPlayer,entity));

		this.decomposer = entity;
		matterElement = new ElementMatterStored(this,74,39,decomposer.getMatterStorage());
		energyElement = new ElementEnergyStored(this,100,39,decomposer.getEnergyStorage());
		decompose_progress = new ElementDualScaled(this,32,52);
        playerSlots = new ElementPlayerSlots(this,44,91);
        slotsList = new ElementSlotsList(this,5,49,new ArrayList<Slot>(0),0);
        slotsList.AddSlot(decomposer.getInventory().getSlot(decomposer.INPUT_SLOT_ID));
        slotsList.AddSlot(decomposer.getInventory().getSlot(decomposer.getEnergySlotID()));
        outputSlot = new ElementSlot(this,true,inventorySlots.getSlot(decomposer.OUTPUT_SLOT_ID));
	}
	
	@Override
	public void initGui()
	{
		super.initGui();
		decompose_progress.setMode(1);
		decompose_progress.setSize(24,16);
		decompose_progress.setTexture(Reference.TEXTURE_ARROW_PROGRESS, 48, 16);
        energyElement.setTexture(Reference.TEXTURE_ENERGY_METER,32,64);
        this.addElement(playerSlots);
        this.addElement(slotsList);
        this.addElement(outputSlot);
		this.addElement(energyElement);
		this.addElement(matterElement);
        this.addElement(decompose_progress);
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
                list.set(k, EnumChatFormatting.GRAY + info);
            }
        }

        MatterHelper.DrawMatterInfoTooltip(stack,TileEntityMachineDecomposer.DECEOPOSE_SPEED_PER_MATTER,TileEntityMachineDecomposer.DECOMPOSE_ENERGY_PER_TICK,list);
		list.add("Test");

        FontRenderer font = stack.getItem().getFontRenderer(stack);
        drawHoveringText(list, x, y, (font == null ? fontRendererObj : font));
    }
	
	@Override
	public void drawGuiContainerForegroundLayer(int part1,int part2)
	{
		
		drawElements(0, true);
		drawTabs(0, true);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_,
			int p_146976_2_, int p_146976_3_) 
	{
		super.drawGuiContainerBackgroundLayer(p_146976_1_, p_146976_2_, p_146976_3_);
		
		//if(this.replicator.isDecomposing())
		//{
		//	
		//}
		
		//docompose_progress.setQuantity(MathHelper.round(((float)this.replicator.decomposeTime / (float)this.replicator.DECEOPOSE_SPEED) * 24));
        int matterAmount = MatterHelper.getMatterAmountFromItem(decomposer.getStackInSlot(decomposer.OUTPUT_SLOT_ID));
		decompose_progress.setQuantity(MathHelper.round(((float)this.decomposer.decomposeProgress / 100f) * 24));
	}
}
