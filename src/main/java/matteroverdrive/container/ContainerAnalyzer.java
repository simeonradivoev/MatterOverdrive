package matteroverdrive.container;

import matteroverdrive.container.matter_network.ContainerTaskQueueMachine;
import matteroverdrive.machines.analyzer.TileEntityMachineMatterAnalyzer;
import matteroverdrive.util.MOContainerHelper;
import net.minecraft.entity.player.InventoryPlayer;

/**
 * Created by Simeon on 2/7/2016.
 */
public class ContainerAnalyzer extends ContainerTaskQueueMachine<TileEntityMachineMatterAnalyzer>
{
	public ContainerAnalyzer(InventoryPlayer inventory, TileEntityMachineMatterAnalyzer machine)
	{
		super(inventory, machine);
	}

	@Override
	public void init(InventoryPlayer inventory)
	{
		addAllSlotsFromInventory(machine.getInventoryContainer());
		MOContainerHelper.AddPlayerSlots(inventory, this, 45, 89, true, true);
	}
}
