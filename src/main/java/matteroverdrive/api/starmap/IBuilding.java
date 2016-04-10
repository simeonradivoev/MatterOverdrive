package matteroverdrive.api.starmap;

import net.minecraft.item.ItemStack;

/**
 * Created by Simeon on 6/23/2015.
 */
public interface IBuilding extends IBuildable
{
	BuildingType getType(ItemStack building);
}
