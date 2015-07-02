package matteroverdrive.items.starmap;

import matteroverdrive.api.starmap.BuildingType;
import matteroverdrive.starmap.data.Planet;
import net.minecraft.item.ItemStack;

/**
 * Created by Simeon on 7/2/2015.
 */
public class ItemBuildingBase extends ItemBuildingAbstract
{
    public ItemBuildingBase(String name) {
        super(name);
    }

    @Override
    public BuildingType getType(ItemStack building) {
        return BuildingType.BASE;
    }

    @Override
    public boolean canBuild(ItemStack building, Planet planet) {
        return true;
    }

    @Override
    public int maxBuildTime(ItemStack building, Planet planet) {
        return 20 * 80;
    }
}
