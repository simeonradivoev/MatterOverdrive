package com.MO.MatterOverdrive.tile.pipes;

import net.minecraftforge.common.util.ForgeDirection;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Simeon on 3/7/2015.
 */
public class WeightedDirection
{
    public float weight = 0;
    public ForgeDirection dir;

    public  WeightedDirection(ForgeDirection dir,float weight)
    {
        this.weight = weight;
        this.dir = dir;
    }

    public static  void Sort(List<WeightedDirection> dirs)
    {
        Collections.sort(dirs, new Comparator<WeightedDirection>() {
            @Override
            public int compare(WeightedDirection o1, WeightedDirection o2) {
                if (o1.weight > o2.weight)
                    return 1;
                else if (o1.weight < o2.weight)
                    return -1;
                else
                    return 0;
            }
        });
    }
}
