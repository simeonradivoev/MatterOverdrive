package com.MO.MatterOverdrive.data;

import net.minecraft.entity.player.EntityPlayer;

/**
 * Created by Simeon on 3/16/2015.
 */
public interface IUseableCondition
{
    public boolean usableByPlayer(EntityPlayer player);
}
