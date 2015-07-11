package matteroverdrive.data.inventory;

import cofh.lib.util.helpers.EnergyHelper;
import matteroverdrive.Reference;
import matteroverdrive.api.weapon.IWeaponModule;
import matteroverdrive.proxy.ClientProxy;
import matteroverdrive.util.MatterHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

/**
 * Created by Simeon on 4/13/2015.
 */
public class ModuleSlot extends Slot
{
    int type;

    public ModuleSlot(boolean isMainSlot,int type)
    {
        super(isMainSlot);
        this.type = type;
    }

    public boolean isValidForSlot(ItemStack item)
    {
        switch (type)
        {
            case Reference.MODULE_BATTERY:
                return EnergyHelper.isEnergyContainerItem(item);
            default:
                if(MatterHelper.isWeaponModule(item))
                {
                    if (((IWeaponModule)item.getItem()).getSlot(item) == type)
                    {
                        return true;
                    }
                }
                return false;
        }
    }

    public IIcon getTexture()
    {
        switch (type)
        {
            case Reference.MODULE_BATTERY:
                return ClientProxy.holoIcons.getIcon("battery");
            case Reference.MODULE_COLOR:
                return ClientProxy.holoIcons.getIcon("color");
            case Reference.MODULE_BARREL:
                return ClientProxy.holoIcons.getIcon("barrel");
            case Reference.MODULE_SIGHTS:
                return ClientProxy.holoIcons.getIcon("sights");
            default:
                return ClientProxy.holoIcons.getIcon("module");
        }
    }
}
