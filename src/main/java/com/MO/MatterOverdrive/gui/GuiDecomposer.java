package com.MO.MatterOverdrive.gui;

import com.MO.MatterOverdrive.data.inventory.Slot;
import com.MO.MatterOverdrive.gui.element.*;
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
import com.MO.MatterOverdrive.tile.TileEntityMachineDecomposer;

import java.util.ArrayList;
import java.util.List;

public class GuiDecomposer extends MOGuiMachine<TileEntityMachineDecomposer>
{
	MOElementEnergy energyElement;
	ElementMatterStored matterElement;
	ElementDualScaled decompose_progress;
    ElementSlot outputSlot;

	public GuiDecomposer(InventoryPlayer inventoryPlayer,TileEntityMachineDecomposer entity)
	{
		super(new ContainerDecomposer(inventoryPlayer,entity),entity);
		name = "decomposer";
		matterElement = new ElementMatterStored(this,74,39,machine.getMatterStorage());
		energyElement = new MOElementEnergy(this,100,39,machine.getEnergyStorage());
		decompose_progress = new ElementDualScaled(this,32,52);
        outputSlot = new ElementInventorySlot(this,getContainer().getSlotAt(machine.OUTPUT_SLOT_ID),22,22,"big");
	}
	
	@Override
	public void initGui()
	{
		super.initGui();
		decompose_progress.setMode(1);
		decompose_progress.setSize(24, 16);
		decompose_progress.setTexture(Reference.TEXTURE_ARROW_PROGRESS, 48, 16);
		energyElement.setTexture(Reference.TEXTURE_ENERGY_METER, 32, 64);
		homePage.addElement(outputSlot);
		homePage.addElement(energyElement);
		homePage.addElement(matterElement);
		this.addElement(decompose_progress);

		AddPlayerSlots(this.inventorySlots, homePage, true, false);
		AddPlayerSlots(this.inventorySlots,this,false,true);

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

        //MatterHelper.DrawMatterInfoTooltip(stack,TileEntityMachineDecomposer.DECEOPOSE_SPEED_PER_MATTER,machine.getEnergyDrainMax()		,list);

        FontRenderer font = stack.getItem().getFontRenderer(stack);
        drawHoveringText(list, x, y, (font == null ? fontRendererObj : font));
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


		decompose_progress.setQuantity(MathHelper.round(((float)this.machine.decomposeProgress / 100f) * 24));
		ManageReqiremnetsTooltips();
	}

	void ManageReqiremnetsTooltips()
	{
		if(machine.getStackInSlot(machine.INPUT_SLOT_ID) != null)
		{
			int matterAmount = MatterHelper.getMatterAmountFromItem(machine.getStackInSlot(machine.INPUT_SLOT_ID));
			energyElement.setEnergyRequired(-(machine.getEnergyDrainMax()));
			matterElement.setDrain(matterAmount);
			energyElement.setEnergyRequiredPerTick(-machine.getEnergyDrainPerTick());
		}
		else
		{
			energyElement.setEnergyRequired(0);
			matterElement.setDrain(0);
			energyElement.setEnergyRequiredPerTick(0);
		}
	}
}
