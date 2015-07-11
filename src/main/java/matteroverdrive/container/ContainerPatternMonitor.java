package matteroverdrive.container;

import matteroverdrive.tile.TileEntityMachinePatternMonitor;
import matteroverdrive.util.MOContainerHelper;
import net.minecraft.entity.player.InventoryPlayer;

/**
 * Created by Simeon on 4/26/2015.
 */
public class ContainerPatternMonitor extends ContainerMachine<TileEntityMachinePatternMonitor>
{
    public ContainerPatternMonitor(InventoryPlayer inventoryPlayer,TileEntityMachinePatternMonitor patternStorage)
    {
        super(inventoryPlayer, patternStorage);
    }

    @Override
    public void init(InventoryPlayer inventoryPlayer)
    {
        super.init(inventoryPlayer);
        MOContainerHelper.AddPlayerSlots(inventoryPlayer, this, 45, 89, false, true);
    }
}
