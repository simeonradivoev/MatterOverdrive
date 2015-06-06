package com.MO.MatterOverdrive.data.inventory;

import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.api.inventory.IBionicPart;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * Created by Simeon on 5/26/2015.
 */
public class BionicSlot extends Slot
{
    int type;
    public static String[] names = {"head","arms","legs","chest","other","battery"};

    public ResourceLocation[] icons = new ResourceLocation[]{,};

    public BionicSlot(boolean isMainSlot,int type)
    {
        super(isMainSlot);
        this.type = type;
    }

    @Override
    public boolean isValidForSlot(ItemStack item)
    {
        return item.getItem() instanceof IBionicPart && ((IBionicPart) item.getItem()).getType(item) == type;
    }

    @Override
    public ResourceLocation getTexture()
    {
        if (type < names.length) {
            return new ResourceLocation(Reference.PATH_GUI_ITEM + "android_slot_" + names[type] + ".png");
        }
        return null;
    }
}
