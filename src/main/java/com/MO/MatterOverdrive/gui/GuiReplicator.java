package com.MO.MatterOverdrive.gui;

import com.MO.MatterOverdrive.data.inventory.Slot;
import com.MO.MatterOverdrive.gui.element.ElementPlayerSlots;
import com.MO.MatterOverdrive.gui.element.ElementSlot;
import com.MO.MatterOverdrive.gui.element.ElementSlotsList;
import com.MO.MatterOverdrive.util.MatterDatabaseHelper;
import com.MO.MatterOverdrive.util.MatterHelper;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;

import cofh.lib.gui.element.ElementDualScaled;
import cofh.lib.gui.element.ElementEnergyStored;
import cofh.lib.util.helpers.MathHelper;

import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.container.ContainerReplicator;
import com.MO.MatterOverdrive.gui.element.ElementMatterStored;
import com.MO.MatterOverdrive.tile.TileEntityMachineReplicator;

import net.minecraft.entity.player.InventoryPlayer;

import java.util.ArrayList;
import java.util.List;

public class GuiReplicator extends MOGuiBase
{
	public TileEntityMachineReplicator replicator;
	ElementEnergyStored energyElement;
	ElementMatterStored matterElement;
	ElementDualScaled replicate_progress;
    ElementPlayerSlots playerSlots;
    ElementSlotsList slotsList;
    ElementSlot outputSlot;
    ElementSlot seccoundOutputSlot;

	public GuiReplicator(InventoryPlayer inventoryPlayer,TileEntityMachineReplicator entity)
    {
		super(new ContainerReplicator(inventoryPlayer,entity));

		this.replicator = entity;
		matterElement = new ElementMatterStored(this,141,39,replicator.getMatterStorage());
		energyElement = new ElementEnergyStored(this,167,39,replicator.getEnergyStorage());
		replicate_progress = new ElementDualScaled(this,32,52);
        playerSlots = new ElementPlayerSlots(this,44,91);
        slotsList = new ElementSlotsList(this,5,49,new ArrayList<Slot>(0),0);
        slotsList.AddSlot(replicator.getInventory().getSlot(replicator.DATABASE_SLOT_ID));
        slotsList.AddSlot(replicator.getInventory().getSlot(replicator.getEnergySlotID()));
        outputSlot = new ElementSlot(this,true,this.inventorySlots.getSlot(replicator.OUTPUT_SLOT_ID));
        seccoundOutputSlot = new ElementSlot(this,true,this.inventorySlots.getSlot(replicator.SECOUND_OUTPUT_SLOT_ID));
	}
	
	@Override
	public void initGui()
	{
		super.initGui();
		replicate_progress.setMode(1);
		replicate_progress.setSize(24,16);
		replicate_progress.setTexture(Reference.TEXTURE_ARROW_PROGRESS, 48, 16);
        energyElement.setTexture(Reference.TEXTURE_ENERGY_METER,32,64);
        this.addElement(playerSlots);
        this.addElement(slotsList);
		this.addElement(energyElement);
		this.addElement(matterElement);
		this.addElement(replicate_progress);
        this.addElement(outputSlot);
        this.addElement(seccoundOutputSlot);
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

        ItemStack itemStack = stack;
        if(MatterHelper.isDatabaseItem(stack))
        {
            itemStack = MatterDatabaseHelper.GetSelectedItem(stack);
        }

        MatterHelper.DrawMatterInfoTooltip(itemStack,TileEntityMachineReplicator.REPLICATE_SPEED_PER_MATTER,TileEntityMachineReplicator.REPLICATE_ENERGY_PER_TICK,list);

        FontRenderer font = stack.getItem().getFontRenderer(stack);
        drawHoveringText(list, x, y, (font == null ? fontRendererObj : font));
    }
	
	@Override
	public void drawGuiContainerForegroundLayer(int part1,int part2)
	{
        ManageReqiremnetsTooltips();
		drawElements(0, true);
		drawTabs(0, true);
	}

    void ManageReqiremnetsTooltips()
    {
        ItemStack scanner = replicator.getStackInSlot(replicator.DATABASE_SLOT_ID);

        if(scanner != null)
        {
            NBTTagCompound tag = MatterDatabaseHelper.GetItemAsNBTAt(scanner,MatterDatabaseHelper.GetSelectedIndex(scanner));
            ItemStack item = MatterDatabaseHelper.GetItemStackFromNBT(tag);

            matterElement.setDrain(-MatterHelper.getMatterAmountFromItem(item));
        }
        else
        {
            matterElement.setDrain(0);
        }
    }

	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_,
			int p_146976_2_, int p_146976_3_) 
	{
		super.drawGuiContainerBackgroundLayer(p_146976_1_, p_146976_2_, p_146976_3_);

		replicate_progress.setQuantity(MathHelper.round(((float)this.replicator.replicateProgress / 100f) * 24));
	}
	
}
