package matteroverdrive.data.inventory;

import matteroverdrive.api.IUpgradeable;
import matteroverdrive.api.inventory.IUpgrade;
import matteroverdrive.api.inventory.UpgradeTypes;
import matteroverdrive.proxy.ClientProxy;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.Map;

/**
 * Created by Simeon on 4/9/2015.
 */
public class UpgradeSlot extends Slot
{
    private IUpgradeable upgradeable;

    public UpgradeSlot(boolean isMainSlot,IUpgradeable upgradeable) {
        super(isMainSlot);
        this.upgradeable = upgradeable;
    }

    @Override
    public boolean isValidForSlot(ItemStack item)
    {
        if (item.getItem() instanceof IUpgrade)
        {
            IUpgrade upgrade = (IUpgrade)item.getItem();
            Map<UpgradeTypes,Double> upgradeMap = upgrade.getUpgrades(item);
            for (final Map.Entry<UpgradeTypes, Double> entry : upgradeMap.entrySet())
            {
                if (upgradeable.isAffectedByUpgrade(entry.getKey()))
                {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public IIcon getTexture()
    {
        return ClientProxy.holoIcons.getIcon("upgrade");
    }
}
