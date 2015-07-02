package matteroverdrive.items.starmap;

import matteroverdrive.api.starmap.BuildingType;
import matteroverdrive.starmap.data.Planet;
import net.minecraft.item.ItemStack;

/**
 * Created by Simeon on 6/23/2015.
 */
public class ShipFactory extends ItemBuildingAbstract {

    public ShipFactory(String name) {
        super(name);
        setMaxStackSize(1);
    }

    @Override
    public boolean canBuild(ItemStack building, Planet planet) {
        return true;
    }

    @Override
    public int maxBuildTime(ItemStack building, Planet planet)
    {
        return 20 * 10;
    }

    @Override
    public BuildingType getType(ItemStack building) {
        return BuildingType.SHIP_FACTORY;
    }
}
