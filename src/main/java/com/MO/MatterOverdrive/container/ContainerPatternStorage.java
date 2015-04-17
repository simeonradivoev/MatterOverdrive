package com.MO.MatterOverdrive.container;

import cofh.lib.util.helpers.EnergyHelper;
import com.MO.MatterOverdrive.container.slot.SlotDatabase;
import com.MO.MatterOverdrive.container.slot.SlotEnergy;
import com.MO.MatterOverdrive.container.slot.SlotPatternStorage;
import com.MO.MatterOverdrive.tile.TileEntityMachinePatternStorage;
import com.MO.MatterOverdrive.util.MOContainerHelper;
import com.MO.MatterOverdrive.util.MatterHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by Simeon on 3/27/2015.
 */
public class ContainerPatternStorage extends ContainerMachine<TileEntityMachinePatternStorage>
{
    private int lastEnergy;

    public ContainerPatternStorage(InventoryPlayer inventoryPlayer,TileEntityMachinePatternStorage patternStorage)
    {
        super(inventoryPlayer, patternStorage);
    }

    @Override
    public void init(InventoryPlayer inventoryPlayer)
    {
        this.addSlotToContainer(new SlotDatabase(machine, machine.input_slot, 8, 52));

        for (int x = 0; x < 3;x++)
        {
            this.addSlotToContainer(new SlotPatternStorage(machine,machine.input_slot + 1 + x ,x * 24 + 77,37));
        }
        for (int x = 0; x < 3;x++)
        {
            this.addSlotToContainer(new SlotPatternStorage(machine,machine.input_slot + 1 + x + 3,x * 24 + 77,24 + 37));
        }

        this.addSlotToContainer(new SlotEnergy(machine, machine.getEnergySlotID(),8,79));

        super.init(inventoryPlayer);

        MOContainerHelper.AddPlayerSlots(inventoryPlayer, this, 45, 89,true,true);
    }

    @Override
    public boolean canInteractWith(EntityPlayer p_75145_1_)
    {
        return true;
    }
}
