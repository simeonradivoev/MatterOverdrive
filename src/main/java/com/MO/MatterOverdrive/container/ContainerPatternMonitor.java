package com.MO.MatterOverdrive.container;

import com.MO.MatterOverdrive.tile.TileEntitiyMachinePatternMonitor;
import com.MO.MatterOverdrive.util.MOContainerHelper;
import net.minecraft.entity.player.InventoryPlayer;

/**
 * Created by Simeon on 4/26/2015.
 */
public class ContainerPatternMonitor extends ContainerMachine<TileEntitiyMachinePatternMonitor>
{
    public ContainerPatternMonitor(InventoryPlayer inventoryPlayer,TileEntitiyMachinePatternMonitor patternStorage)
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
