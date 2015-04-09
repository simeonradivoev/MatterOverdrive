package com.MO.MatterOverdrive.data.inventory;

import com.MO.MatterOverdrive.Reference;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * Created by Simeon on 4/9/2015.
 */
public class UpgradeSlot extends Slot
{
    protected static final ResourceLocation Icon = new ResourceLocation(Reference.PATH_GUI + "items/upgrade.png");

    public UpgradeSlot(boolean isMainSlot) {
        super(isMainSlot);
    }

    public boolean isValidForSlot(ItemStack item)
    {
        return true;
    }
    public ResourceLocation getTexture()
    {
        return Icon;
    }
}
