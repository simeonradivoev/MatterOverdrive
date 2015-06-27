package com.MO.MatterOverdrive.starmap.gen;

import com.MO.MatterOverdrive.starmap.data.SpaceBody;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Random;

/**
 * Created by Simeon on 6/21/2015.
 */
public interface ISpaceBodyGen<T extends SpaceBody>
{
    void generateSpaceBody(T body,Random random);
    boolean generateMissing(NBTTagCompound tagCompound,T body,Random random);
    double getWeight(T body);
}
