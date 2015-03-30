package com.MO.MatterOverdrive.data.inventory;

import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.container.slot.SlotMatter;
import com.MO.MatterOverdrive.util.MatterHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * Created by Simeon on 3/16/2015.
 */
public class MatterSlot extends Slot
{
    protected static final ResourceLocation Icon = new ResourceLocation(Reference.PATH_GUI + "items/decompose.png");

    @Override
    public boolean isValidForSlot(ItemStack itemStack)
    {
        return MatterHelper.containsMatter(itemStack);
    }

    @Override
    public ResourceLocation getTexture()
    {
        return Icon;
    }

    @Override
    boolean isEqual(net.minecraft.inventory.Slot slot)
    {
        return slot instanceof SlotMatter;
    }
}
