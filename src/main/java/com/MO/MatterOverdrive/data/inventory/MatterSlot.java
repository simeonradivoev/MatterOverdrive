package com.MO.MatterOverdrive.data.inventory;

import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.container.slot.SlotMatter;
import com.MO.MatterOverdrive.proxy.ClientProxy;
import com.MO.MatterOverdrive.util.MatterHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

/**
 * Created by Simeon on 3/16/2015.
 */
public class MatterSlot extends Slot
{
    public MatterSlot(boolean isMainSlot) {
        super(isMainSlot);
    }

    @Override
    public boolean isValidForSlot(ItemStack itemStack)
    {
        return MatterHelper.containsMatter(itemStack);
    }

    @Override
    public IIcon getTexture()
    {
        return ClientProxy.holoIcons.getIcon("decompose");
    }

    @Override
    boolean isEqual(net.minecraft.inventory.Slot slot)
    {
        return slot instanceof SlotMatter;
    }
}
