package matteroverdrive.container;

import matteroverdrive.machines.dimensional_pylon.TileEntityMachineDimensionalPylon;
import matteroverdrive.util.MOContainerHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Simeon on 2/12/2016.
 */
public class ContainerDimensionalPylon extends ContainerMachine<TileEntityMachineDimensionalPylon>
{
    private int energyGenPerTick;
    private int matterDrainPerSec;

    public ContainerDimensionalPylon(InventoryPlayer inventory, TileEntityMachineDimensionalPylon machine)
    {
        super(inventory, machine);
    }

    @Override
    public void onCraftGuiOpened(ICrafting icrafting)
    {
        super.onCraftGuiOpened(icrafting);
        icrafting.sendProgressBarUpdate(this, 1, this.machine.getEnergyGenPerTick());
        icrafting.sendProgressBarUpdate(this,2,this.machine.getMatterDrainPerSec());
    }

    @Override
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();
        for (Object crafter : this.crafters) {
            ICrafting icrafting = (ICrafting) crafter;

            if (this.energyGenPerTick != this.machine.getEnergyGenPerTick()) {
                icrafting.sendProgressBarUpdate(this, 1, this.machine.getEnergyGenPerTick());
            }
            if (this.matterDrainPerSec != this.machine.getMatterDrainPerSec())
            {
                icrafting.sendProgressBarUpdate(this,2,this.machine.getMatterDrainPerSec());
            }
        }

        this.energyGenPerTick = this.machine.getEnergyGenPerTick();
    }

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int slot, int newValue)
    {
        super.updateProgressBar(slot,newValue);
        switch (slot)
        {
            case 1:
                energyGenPerTick = newValue;
                break;
            case 2:
                matterDrainPerSec = newValue;
        }
    }

    @Override
    public void init(InventoryPlayer inventory)
    {
        addAllSlotsFromInventory(machine.getInventoryContainer());
        MOContainerHelper.AddPlayerSlots(inventory, this, 45, 89, false, true);
    }

    public int getEnergyGenPerTick()
    {
        return energyGenPerTick;
    }

    public int getMatterDrainPerSec()
    {
        return matterDrainPerSec;
    }
}
