package com.MO.MatterOverdrive.gui;

import com.MO.MatterOverdrive.api.matter.IMatterDatabase;
import com.MO.MatterOverdrive.data.inventory.Slot;
import com.MO.MatterOverdrive.gui.element.*;
import com.MO.MatterOverdrive.items.MatterScanner;
import com.MO.MatterOverdrive.util.MatterDatabaseHelper;
import com.MO.MatterOverdrive.util.MatterHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;

import cofh.lib.gui.element.ElementDualScaled;
import cofh.lib.util.helpers.MathHelper;

import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.container.ContainerReplicator;
import com.MO.MatterOverdrive.tile.TileEntityMachineReplicator;

import net.minecraft.entity.player.InventoryPlayer;

import java.util.ArrayList;
import java.util.List;

public class GuiReplicator extends MOGuiMachine<TileEntityMachineReplicator>
{
	MOElementEnergy energyElement;
	ElementMatterStored matterElement;
	ElementDualScaled replicate_progress;
    ElementSlot outputSlot;
    ElementSlot seccoundOutputSlot;

	public GuiReplicator(InventoryPlayer inventoryPlayer,TileEntityMachineReplicator entity)
    {
		super(new ContainerReplicator(inventoryPlayer, entity),entity);
        name = "replicator";
		matterElement = new ElementMatterStored(this,141,39,machine.getMatterStorage());
		energyElement = new MOElementEnergy(this,167,39,machine.getEnergyStorage());
		replicate_progress = new ElementDualScaled(this,32,52);
        outputSlot = new ElementInventorySlot(this,this.getContainer().getSlotAt(machine.OUTPUT_SLOT_ID),22,22,"big");
        seccoundOutputSlot = new ElementInventorySlot(this,this.getContainer().getSlotAt(machine.SECOUND_OUTPUT_SLOT_ID),22,22,"big");
	}
	
	@Override
	public void initGui()
	{
		super.initGui();

		replicate_progress.setMode(1);
		replicate_progress.setSize(24, 16);
		replicate_progress.setTexture(Reference.TEXTURE_ARROW_PROGRESS, 48, 16);
        energyElement.setTexture(Reference.TEXTURE_ENERGY_METER, 32, 64);
		this.addElement(replicate_progress);

        homePage.addElement(outputSlot);
        homePage.addElement(seccoundOutputSlot);
        homePage.addElement(matterElement);
        homePage.addElement(energyElement);

        AddMainPlayerSlots(inventorySlots, homePage);
        AddHotbarPlayerSlots(inventorySlots, this);
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
        if(MatterHelper.isMatterScanner(stack))
        {
            IMatterDatabase database = MatterScanner.getLink(Minecraft.getMinecraft().theWorld,stack);
            if(database != null)
            {
                itemStack = MatterDatabaseHelper.GetItemStackFromNBT(database.getItemAsNBT(MatterScanner.getSelectedAsItem(stack)));
            }
        }

        //MatterHelper.DrawMatterInfoTooltip(itemStack, TileEntityMachineReplicator.REPLICATE_SPEED_PER_MATTER, TileEntityMachineReplicator.REPLICATE_ENERGY_PER_TICK, list);

        FontRenderer font = stack.getItem().getFontRenderer(stack);
        drawHoveringText(list, x, y, (font == null ? fontRendererObj : font));
    }
	
	@Override
	public void drawGuiContainerForegroundLayer(int part1,int part2)
	{
        super.drawGuiContainerForegroundLayer(part1,part2);
        ManageReqiremnetsTooltips();
	}

    void ManageReqiremnetsTooltips()
    {
        ItemStack scanner = machine.getStackInSlot(machine.DATABASE_SLOT_ID);

        if(scanner != null)
        {
            NBTTagCompound itemAsNBT = machine.GetNewItemNBT();

            if(itemAsNBT != null)
            {
                ItemStack item = MatterDatabaseHelper.GetItemStackFromNBT(itemAsNBT);
                int matterAmount = MatterHelper.getMatterAmountFromItem(item);
                matterElement.setDrain(-matterAmount);
                energyElement.setEnergyRequired(-(machine.getEnergyDrainMax(item)));
                energyElement.setEnergyRequiredPerTick(-machine.getEnergyDrainPerTick(item));
            }
        }
        else
        {
            matterElement.setDrain(0);
            energyElement.setEnergyRequired(0);
            energyElement.setEnergyRequiredPerTick(0);
        }
    }

	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_,
			int p_146976_2_, int p_146976_3_) 
	{
		super.drawGuiContainerBackgroundLayer(p_146976_1_, p_146976_2_, p_146976_3_);

		replicate_progress.setQuantity(MathHelper.round(((float)this.machine.replicateProgress / 100f) * 24));
	}
	
}
