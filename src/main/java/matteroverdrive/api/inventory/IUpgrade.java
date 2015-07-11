package matteroverdrive.api.inventory;

import net.minecraft.item.ItemStack;

import java.util.Map;

/**
 * Created by Simeon on 4/11/2015.
 */
public interface IUpgrade
{
    Map<UpgradeTypes,Double> getUpgrades(ItemStack itemStack);
}
