package matteroverdrive.api;

import matteroverdrive.api.inventory.UpgradeTypes;

/**
 * Created by Simeon on 6/12/2015.
 */
public interface IUpgradeable
{
    boolean isAffectedByUpgrade(UpgradeTypes type);
}
