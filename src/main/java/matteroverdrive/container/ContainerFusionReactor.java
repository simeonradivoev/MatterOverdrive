package matteroverdrive.container;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import matteroverdrive.container.slot.SlotEnergy;
import matteroverdrive.tile.TileEntityMachineFusionReactorController;
import matteroverdrive.util.MOContainerHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;

/**
 * Created by Simeon on 5/17/2015.
 */
public class ContainerFusionReactor extends ContainerMachine<TileEntityMachineFusionReactorController>
{
    int lastEnergyPerTick;

    public ContainerFusionReactor(InventoryPlayer inventory,TileEntityMachineFusionReactorController machine)
    {
        super(inventory,machine);
    }

    @Override
    public void init(InventoryPlayer inventory)
    {
        this.addSlotToContainer(new SlotEnergy(machine, this.machine.getEnergySlotID(),8,55));

        super.init(inventory);
        MOContainerHelper.AddPlayerSlots(inventory, this, 45, 89, false, true);
    }

    @Override
    public void addCraftingToCrafters(ICrafting icrafting)
    {
        super.addCraftingToCrafters(icrafting);
        icrafting.sendProgressBarUpdate(this, 0, this.machine.getEnergyPerTick());
    }

    @Override
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();
        for(int i = 0;i < this.crafters.size();i++)
        {
            ICrafting icrafting = (ICrafting)this.crafters.get(i);

            if(this.lastEnergyPerTick != this.machine.getEnergyPerTick())
            {
                icrafting.sendProgressBarUpdate(this, 0, this.machine.getEnergyPerTick());
            }

            this.lastEnergyPerTick = this.machine.getEnergyPerTick();
        }
    }

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int slot,int newValue)
    {
        if(slot == 0)
            this.machine.setEnergyPerTick(newValue);
    }
}
