package matteroverdrive.items.starmap;

import matteroverdrive.api.starmap.*;
import matteroverdrive.init.MatterOverdriveItems;
import matteroverdrive.starmap.data.Planet;
import net.minecraft.item.ItemStack;

import java.util.UUID;

/**
 * Created by Simeon on 7/2/2015.
 */
public class ItemColonizerShip  extends ItemShipAbstract
{

    public ItemColonizerShip(String name) {
        super(name);
    }

    @Override
    public ShipType getType(ItemStack ship) {
        return ShipType.COLONIZER;
    }

    @Override
    public void onTravel(ItemStack shipStack, Planet to)
    {
        UUID owner = getOwnerID(shipStack);
        if (owner != null) {
            ItemStack base = new ItemStack(MatterOverdriveItems.buildingBase);
            if (to.canBuild((IBuilding) base.getItem(), base)) {
                shipStack.stackSize = 0;
                to.addBuilding(base);
                to.setOwnerUUID(owner);
            }
        }
    }

    @Override
    public boolean canBuild(ItemStack building, Planet planet)
    {
        for (ItemStack buildingStack : planet.getFleet())
        {
            if (buildingStack.getItem() instanceof IBuilding && ((IBuilding)buildingStack.getItem()).getType(buildingStack) == BuildingType.BASE)
            {
                return false;
            }
        }
        return true;
    }

    @Override
    public int maxBuildTime(ItemStack building, Planet planet) {
        return 20 * 40;
    }
}
