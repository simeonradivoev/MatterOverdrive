package matteroverdrive.items.starmap;

import matteroverdrive.api.starmap.ShipType;
import matteroverdrive.starmap.data.Planet;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * Created by Simeon on 6/24/2015.
 */
public class ItemScoutShip extends ItemShipAbstract
{
    public ItemScoutShip(String name) {
        super(name);
    }

    @Override
    public boolean canBuild(ItemStack building, Planet planet) {
        return true;
    }

    @Override
    public int maxBuildTime(ItemStack building, Planet planet) {
        return 20 * 30;
    }

    @Override
    public ShipType getType(ItemStack ship) {
        return ShipType.SCOUT;
    }
}
