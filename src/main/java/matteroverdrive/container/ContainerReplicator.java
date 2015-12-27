package matteroverdrive.container;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import matteroverdrive.tile.TileEntityMachineReplicator;
import matteroverdrive.util.MOContainerHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;

/**
 * Created by Simeon on 12/27/2015.
 */
public class ContainerReplicator extends ContainerMachine<TileEntityMachineReplicator>
{
    int patternReplicateCount;

    public ContainerReplicator(InventoryPlayer inventory, TileEntityMachineReplicator machine)
    {
        super(inventory, machine);
    }

    @Override
    public void init(InventoryPlayer inventory)
    {
        addAllSlotsFromInventory(machine.getInventoryContainer());
        MOContainerHelper.AddPlayerSlots(inventory, this, 45, 89, true, true);
    }

    @Override
    public void addCraftingToCrafters(ICrafting icrafting)
    {
        super.addCraftingToCrafters(icrafting);
        icrafting.sendProgressBarUpdate(this, 1, this.machine.getTaskReplicateCount());
    }

    @Override
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();
        for (Object crafter : this.crafters) {
            ICrafting icrafting = (ICrafting) crafter;

            if (this.patternReplicateCount != this.machine.getTaskReplicateCount()) {
                icrafting.sendProgressBarUpdate(this, 1, this.machine.getTaskReplicateCount());
            }

            this.patternReplicateCount = this.machine.getTaskReplicateCount();
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int slot,int newValue)
    {
        super.updateProgressBar(slot,newValue);
        if (slot == 1)
        {
            patternReplicateCount = newValue;
        }
    }

    public int getPatternReplicateCount()
    {
        return patternReplicateCount;
    }
}
