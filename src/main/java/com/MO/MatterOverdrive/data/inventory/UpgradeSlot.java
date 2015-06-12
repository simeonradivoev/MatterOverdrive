package com.MO.MatterOverdrive.data.inventory;

import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.api.IUpgradeable;
import com.MO.MatterOverdrive.api.inventory.IUpgrade;
import com.MO.MatterOverdrive.api.inventory.UpgradeTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.Map;

/**
 * Created by Simeon on 4/9/2015.
 */
public class UpgradeSlot extends Slot
{
    private IUpgradeable upgradeable;
    protected static final ResourceLocation Icon = new ResourceLocation(Reference.PATH_GUI + "items/upgrade.png");

    public UpgradeSlot(boolean isMainSlot,IUpgradeable upgradeable) {
        super(isMainSlot);
        this.upgradeable = upgradeable;
    }


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
    public ResourceLocation getTexture()
    {
        return Icon;
    }
}
