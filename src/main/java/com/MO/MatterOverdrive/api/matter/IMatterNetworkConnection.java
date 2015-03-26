package com.MO.MatterOverdrive.api.matter;

import cofh.lib.util.position.BlockPosition;
import com.MO.MatterOverdrive.data.MatterNetwork;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by Simeon on 3/11/2015.
 */
public interface IMatterNetworkConnection
{
    boolean canConnectToNetwork(ForgeDirection direction);
    BlockPosition getPosition();
    MatterNetwork getNetwork();
    boolean setNetwork(MatterNetwork network);
    int getID();
}
