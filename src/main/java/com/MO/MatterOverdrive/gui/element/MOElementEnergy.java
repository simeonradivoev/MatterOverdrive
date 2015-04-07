package com.MO.MatterOverdrive.gui.element;

import cofh.api.energy.IEnergyStorage;
import cofh.lib.gui.GuiBase;
import cofh.lib.gui.element.ElementEnergyStored;
import com.MO.MatterOverdrive.util.MOEnergyHelper;
import net.minecraft.util.EnumChatFormatting;

import java.util.List;

/**
 * Created by Simeon on 4/7/2015.
 */
public class MOElementEnergy extends ElementEnergyStored
{
    int energyRequired;

    public MOElementEnergy(GuiBase gui, int posX, int posY, IEnergyStorage storage)
    {
        super(gui, posX, posY, storage);
    }

    @Override
    public void addTooltip(List<String> list)
    {
        super.addTooltip(list);
        if(energyRequired > 0)
        {
            list.add(EnumChatFormatting.GREEN + "+" +String.valueOf(energyRequired) + MOEnergyHelper.ENERGY_UNIT + EnumChatFormatting.RESET);
        }
        else if(energyRequired < 0)
        {
            list.add(EnumChatFormatting.RED +String.valueOf(energyRequired) + MOEnergyHelper.ENERGY_UNIT + EnumChatFormatting.RESET);
        }
    }

    public int getEnergyRequired()
    {
        return energyRequired;
    }

    public void setEnergyRequired(int energyRequired)
    {
        this.energyRequired = energyRequired;
    }
}
