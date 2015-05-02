package com.MO.MatterOverdrive.data.inventory;

import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.api.matter.IMatterPatternStorage;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * Created by Simeon on 3/27/2015.
 */
public class PatternStorageSlot extends Slot
{
    public static ResourceLocation ICON = new ResourceLocation(Reference.PATH_GUI_ITEM + "pattern_storage.png");

    public PatternStorageSlot(boolean isMainSlot) {
        super(isMainSlot);
    }

    public boolean isValidForSlot(ItemStack item)
    {
        return item.getItem() instanceof IMatterPatternStorage;
    }

    public ResourceLocation getTexture()
    {
        return ICON;
    }
}
