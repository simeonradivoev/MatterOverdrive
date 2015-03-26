package com.MO.MatterOverdrive.data.inventory;

import cofh.lib.gui.slot.SlotRemoveOnly;
import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.gui.slot.SlotDatabase;
import com.MO.MatterOverdrive.util.MatterDatabaseHelper;
import com.MO.MatterOverdrive.util.MatterHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * Created by Simeon on 3/16/2015.
 */
public class DatabaseSlot extends Slot
{
    protected static final ResourceLocation Icon = new ResourceLocation(Reference.PATH_GUI + "items/scanner.png");

    @Override
    public boolean isValidForSlot(ItemStack itemStack)
    {
        return MatterHelper.isDatabaseItem(itemStack);
    }

    @Override
    public ResourceLocation getTexture()
    {
        return Icon;
    }

    @Override
    boolean isEqual(net.minecraft.inventory.Slot slot)
    {
        return slot instanceof SlotDatabase;
    }
}
