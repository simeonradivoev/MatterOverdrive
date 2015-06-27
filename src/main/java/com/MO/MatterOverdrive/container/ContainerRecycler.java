package com.MO.MatterOverdrive.container;

import com.MO.MatterOverdrive.container.slot.*;
import com.MO.MatterOverdrive.tile.TileEntityMachineMatterRecycler;
import com.MO.MatterOverdrive.util.MOContainerHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;

/**
 * Created by Simeon on 5/15/2015.
 */
public class ContainerRecycler extends ContainerMachine<TileEntityMachineMatterRecycler>
{
    private int lastRecycleTime;

    public ContainerRecycler(InventoryPlayer inventory,TileEntityMachineMatterRecycler tileentity)
    {
        super(inventory, tileentity);
    }

    @Override
    public void init(InventoryPlayer inventory)
    {
        this.addSlotToContainer(new SlotInventory(machine.getInventoryContainer(),machine.getInventoryContainer().getSlot(machine.INPUT_SLOT_ID),8,55));
        this.addSlotToContainer(new SlotRemoveOnly(machine,machine.OUTPUT_SLOT_ID,64,52));
        this.addSlotToContainer(new SlotEnergy(machine,this.machine.getEnergySlotID(),8,82));

        super.init(inventory);
        MOContainerHelper.AddPlayerSlots(inventory, this, 45, 89, true, true);
    }

    public void addCraftingToCrafters(ICrafting icrafting)
    {
        super.addCraftingToCrafters(icrafting);
        icrafting.sendProgressBarUpdate(this, 0, this.machine.recycleProgress);
    }

    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();
        for(int i = 0;i < this.crafters.size();i++)
        {
            ICrafting icrafting = (ICrafting)this.crafters.get(i);

            if(this.lastRecycleTime != this.machine.recycleProgress)
            {
                icrafting.sendProgressBarUpdate(this, 0, this.machine.recycleProgress);
            }
        }

        lastRecycleTime = this.machine.recycleProgress;
    }

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int slot,int newValue)
    {
        if(slot == 0)
            this.machine.recycleProgress = newValue;
    }

    @Override
    public boolean canInteractWith(EntityPlayer player)
    {
        return true;
    }
}
