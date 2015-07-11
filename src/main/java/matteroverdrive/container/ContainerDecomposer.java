package matteroverdrive.container;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import matteroverdrive.container.slot.SlotEnergy;
import matteroverdrive.container.slot.SlotMatter;
import matteroverdrive.container.slot.SlotRemoveOnly;
import matteroverdrive.tile.TileEntityMachineDecomposer;
import matteroverdrive.util.MOContainerHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;

public class ContainerDecomposer extends ContainerMachine<TileEntityMachineDecomposer>
{
	private float lastDecomposeProgress;
	
	public ContainerDecomposer(InventoryPlayer inventory,TileEntityMachineDecomposer tileentity)
	{
		super(inventory,tileentity);
	}

	@Override
	public void init(InventoryPlayer inventory)
	{
		this.addSlotToContainer(new SlotMatter(machine,machine.INPUT_SLOT_ID,8,55));
		this.addSlotToContainer(new SlotRemoveOnly(machine,machine.OUTPUT_SLOT_ID,129,55));
		this.addSlotToContainer(new SlotEnergy(machine,this.machine.getEnergySlotID(),8,82));

		super.init(inventory);
		MOContainerHelper.AddPlayerSlots(inventory, this, 45, 89,true,true);
	}
	
	public void addCraftingToCrafters(ICrafting icrafting)
	{
		super.addCraftingToCrafters(icrafting);
		icrafting.sendProgressBarUpdate(this, 0, this.machine.decomposeProgress);
	}
	
	public void detectAndSendChanges()
	{
		super.detectAndSendChanges();
		for(int i = 0;i < this.crafters.size();i++)
		{
			ICrafting icrafting = (ICrafting)this.crafters.get(i);

			if(this.lastDecomposeProgress != this.machine.decomposeProgress)
			{
				icrafting.sendProgressBarUpdate(this, 0, this.machine.decomposeProgress);
			}
		}

		lastDecomposeProgress = this.machine.decomposeProgress;
	}
	
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int slot,int newValue)
	{
		if(slot == 0)
			this.machine.decomposeProgress = newValue;
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) 
	{
		return true;
	}
}
