package matteroverdrive.tile.pipes;

import net.minecraft.util.EnumFacing;

import java.util.Collections;
import java.util.List;

/**
 * Created by Simeon on 3/7/2015.
 */
public class WeightedDirection
{
    public float weight = 0;
    public EnumFacing dir;

    public  WeightedDirection(EnumFacing dir,float weight)
    {
        this.weight = weight;
        this.dir = dir;
    }

    public static  void Sort(List<WeightedDirection> dirs)
    {
        Collections.sort(dirs, (o1, o2) -> {
			if (o1.weight > o2.weight)
				return 1;
			else if (o1.weight < o2.weight)
				return -1;
			else
				return 0;
		});
    }
}
