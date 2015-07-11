/*
 * This file is part of Matter Overdrive
 * Copyright (c) 2015., Simeon Radivoev, All rights reserved.
 *
 * Matter Overdrive is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Matter Overdrive is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Matter Overdrive.  If not, see <http://www.gnu.org/licenses>.
 */

package matteroverdrive.data.biostats;

import matteroverdrive.entity.AndroidPlayer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import java.text.DecimalFormat;

/**
 * Created by Simeon on 5/29/2015.
 */
public class BioticStatNanoArmor extends AbstractBioticStat
{
    public BioticStatNanoArmor(String name, int xp)
    {
        super(name, xp);
        setMaxLevel(4);
        setShowOnHud(true);
    }

    @Override
    public void onAndroidUpdate(AndroidPlayer android, int level)
    {

    }

    @Override
    public void onKeyPress(AndroidPlayer androidPlayer, int level, int keycode, boolean down)
    {

    }

    @Override
    public String getDetails(int level)
    {
        return String.format(super.getDetails(level), EnumChatFormatting.GREEN + DecimalFormat.getPercentInstance().format(getDamageNegate(level)) + EnumChatFormatting.GRAY);
    }

    @Override
    public void onLivingEvent(AndroidPlayer androidPlayer, int level, LivingEvent event)
    {
        if (event instanceof LivingHurtEvent)
        {
            ((LivingHurtEvent) event).ammount *= getDamageNegate(level);
        }
    }

    @Override
    public void changeAndroidStats(AndroidPlayer androidPlayer, int level, boolean enabled) {

    }

    @Override
    public boolean isEnabled(AndroidPlayer android, int level) {
        return super.isEnabled(android,level) && android.getEnergyStored() > 0;
    }

    @Override
    public boolean isActive(AndroidPlayer androidPlayer, int level)
    {
        return false;
    }

    public float getDamageNegate(int level)
    {
        return (1 + level) * 0.06f;
    }
}
