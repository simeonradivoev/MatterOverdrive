package com.MO.MatterOverdrive.data.inventory;

import cofh.lib.util.helpers.EnergyHelper;
import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.api.weapon.IWeaponModule;
import com.MO.MatterOverdrive.util.MatterHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * Created by Simeon on 4/13/2015.
 */
public class ModuleSlot extends Slot
{
    public static final ResourceLocation battery_module = new ResourceLocation(Reference.PATH_GUI_ITEM + "battery.png");
    public static final ResourceLocation color_module = new ResourceLocation(Reference.PATH_GUI_ITEM + "color.png");
    public static final ResourceLocation barrel_module = new ResourceLocation(Reference.PATH_GUI_ITEM + "barrel.png");
    public static final ResourceLocation sights_module = new ResourceLocation(Reference.PATH_GUI_ITEM + "sights.png");
    public static final ResourceLocation other_module = new ResourceLocation(Reference.PATH_GUI_ITEM + "module.png");

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

    public ResourceLocation getTexture()
    {
        switch (type)
        {
            case Reference.MODULE_BATTERY:
                return battery_module;
            case Reference.MODULE_COLOR:
                return color_module;
            case Reference.MODULE_BARREL:
                return barrel_module;
            case Reference.MODULE_SIGHTS:
                return sights_module;
            default:
                return other_module;
        }
    }
}
