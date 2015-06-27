package com.MO.MatterOverdrive.data.inventory;

import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.api.matter.IMatterPatternStorage;
import com.MO.MatterOverdrive.proxy.ClientProxy;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

/**
 * Created by Simeon on 3/27/2015.
 */
public class PatternStorageSlot extends Slot
{
    public PatternStorageSlot(boolean isMainSlot) {
        super(isMainSlot);
    }

    @Override
    public boolean isValidForSlot(ItemStack item)
    {
        return item.getItem() instanceof IMatterPatternStorage;
    }

    @Override
    public IIcon getTexture()
    {
        return ClientProxy.holoIcons.getIcon("pattern_storage");
    }
}
