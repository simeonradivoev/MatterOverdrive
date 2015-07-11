package matteroverdrive.container;

import matteroverdrive.Reference;
import matteroverdrive.container.slot.SlotEnergy;
import matteroverdrive.container.slot.SlotInventory;
import matteroverdrive.entity.AndroidPlayer;
import matteroverdrive.tile.TileEntityAndroidStation;
import matteroverdrive.util.MOContainerHelper;
import net.minecraft.entity.player.InventoryPlayer;

/**
 * Created by Simeon on 5/27/2015.
 */
public class ContainerAndroidStation extends ContainerMachine<TileEntityAndroidStation>
{
    public ContainerAndroidStation(InventoryPlayer playerInventory, TileEntityAndroidStation machine)
    {
        this.machine = machine;
        init(playerInventory);
    }

    @Override
    protected void init(InventoryPlayer inventory)
    {
        AndroidPlayer android = AndroidPlayer.get(inventory.player);

        for (int i = 0;i < Reference.BIONIC_OTHER+1;i++)
        {
            addSlotToContainer(new SlotInventory(android,android.getInventory().getSlot(i),0,0));
        }
        addSlotToContainer(new SlotEnergy(android.getInventory(), Reference.BIONIC_BATTERY, 8, 55));

        super.init(inventory);
        MOContainerHelper.AddPlayerSlots(inventory, this, 45, 150, true, true);
    }
}
