package com.MO.MatterOverdrive.data.inventory;

import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.container.slot.SlotShielding;
import com.MO.MatterOverdrive.init.MatterOverdriveItems;
import com.MO.MatterOverdrive.proxy.ClientProxy;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

/**
 * Created by Simeon on 4/6/2015.
 */
public class ShieldingSlot extends Slot
{
    public ShieldingSlot(boolean isMainSlot) {
        super(isMainSlot);
    }

    @Override
    public boolean isValidForSlot(ItemStack itemStack)
    {
        if(this.getItem() != null && this.getItem().stackSize < 4) {

            if (itemStack != null && itemStack.getItem() != null) {
                return itemStack.getItem() == MatterOverdriveItems.tritanium_plate;
            }
        }
        return false;
    }

    @Override
    public IIcon getTexture()
    {
        return ClientProxy.holoIcons.getIcon("shielding");
    }

    @Override
    boolean isEqual(net.minecraft.inventory.Slot slot)
    {
        return slot instanceof SlotShielding;
    }
}
