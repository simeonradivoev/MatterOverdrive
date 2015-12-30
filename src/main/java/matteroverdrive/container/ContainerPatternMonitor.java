package matteroverdrive.container;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.network.packet.client.PacketSyncTaskQueue;
import matteroverdrive.tile.TileEntityMachinePatternMonitor;
import matteroverdrive.util.MOContainerHelper;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;

/**
 * Created by Simeon on 12/30/2015.
 */
public class ContainerPatternMonitor extends ContainerMachine<TileEntityMachinePatternMonitor>
{
    public ContainerPatternMonitor(InventoryPlayer inventory, TileEntityMachinePatternMonitor machine)
    {
        super(inventory, machine);
    }

    @Override
    public void init(InventoryPlayer inventory)
    {
        addAllSlotsFromInventory(machine.getInventoryContainer());
        MOContainerHelper.AddPlayerSlots(inventory, this, 45, 89, false, true);
    }

    @Override
    public void addCraftingToCrafters(ICrafting icrafting)
    {
        super.addCraftingToCrafters(icrafting);
        if (icrafting instanceof EntityPlayerMP)
        {
            MatterOverdrive.packetPipeline.sendTo(new PacketSyncTaskQueue(machine,0),(EntityPlayerMP)icrafting);
        }
    }

    @Override
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();
        for (Object crafter : this.crafters) {
            ICrafting icrafting = (ICrafting) crafter;


        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int slot,int newValue)
    {
        super.updateProgressBar(slot,newValue);
    }
}
