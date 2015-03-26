package com.MO.MatterOverdrive.data.inventory;

import cofh.lib.gui.slot.SlotEnergy;
import cofh.lib.gui.slot.SlotRemoveOnly;
import cofh.lib.util.helpers.EnergyHelper;
import com.MO.MatterOverdrive.Reference;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * Created by Simeon on 3/16/2015.
 */
public class EnergySlot extends Slot
{
    protected static final ResourceLocation Icon = new ResourceLocation(Reference.PATH_GUI + "items/energy.png");

    @Override
    public boolean isValidForSlot(ItemStack itemStack)
    {
        return EnergyHelper.isEnergyContainerItem(itemStack);
    }

    @Override
    public ResourceLocation getTexture()
    {
        return Icon;
    }

    @Override
    boolean isEqual(net.minecraft.inventory.Slot slot)
    {
        return slot instanceof SlotEnergy;
    }
}
