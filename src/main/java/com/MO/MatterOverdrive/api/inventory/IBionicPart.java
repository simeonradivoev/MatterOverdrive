package com.MO.MatterOverdrive.api.inventory;

import com.MO.MatterOverdrive.entity.AndroidPlayer;
import net.minecraft.item.ItemStack;

/**
 * Created by Simeon on 5/26/2015.
 */
public interface IBionicPart
{
    int getType(ItemStack itemStack);
    boolean affectAndroid(AndroidPlayer player,ItemStack itemStack);
}
