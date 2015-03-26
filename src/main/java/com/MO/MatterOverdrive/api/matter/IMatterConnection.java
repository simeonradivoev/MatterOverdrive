package com.MO.MatterOverdrive.api.matter;

import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by Simeon on 3/7/2015.
 */
public interface IMatterConnection
{
    boolean canConnectFrom(ForgeDirection dir);
}
