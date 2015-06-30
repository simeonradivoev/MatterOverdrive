package matteroverdrive.container;

import matteroverdrive.container.slot.SlotDatabase;
import matteroverdrive.container.slot.SlotEnergy;
import matteroverdrive.container.slot.SlotPatternStorage;
import matteroverdrive.tile.TileEntityMachinePatternStorage;
import matteroverdrive.util.MOContainerHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;

/**
 * Created by Simeon on 3/27/2015.
 */
public class ContainerPatternStorage extends ContainerMachine<TileEntityMachinePatternStorage>
{
    public ContainerPatternStorage(InventoryPlayer inventoryPlayer,TileEntityMachinePatternStorage patternStorage)
    {
        super(inventoryPlayer, patternStorage);
    }

    @Override
    public void init(InventoryPlayer inventoryPlayer)
    {
        this.addSlotToContainer(new SlotDatabase(machine, machine.input_slot, 8, 55));

        for (int x = 0; x < 3;x++)
        {
            this.addSlotToContainer(new SlotPatternStorage(machine,machine.input_slot + 1 + x ,x * 24 + 77,37));
        }
        for (int x = 0; x < 3;x++)
        {
            this.addSlotToContainer(new SlotPatternStorage(machine,machine.input_slot + 1 + x + 3,x * 24 + 77,24 + 37));
        }

        this.addSlotToContainer(new SlotEnergy(machine, machine.getEnergySlotID(),8,82));

        super.init(inventoryPlayer);

        MOContainerHelper.AddPlayerSlots(inventoryPlayer, this, 45, 89,true,true);
    }

    @Override
    public boolean canInteractWith(EntityPlayer p_75145_1_)
    {
        return true;
    }
}
