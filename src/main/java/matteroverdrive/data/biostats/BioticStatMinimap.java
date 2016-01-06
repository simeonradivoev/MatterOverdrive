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

import com.google.common.collect.Multimap;
import matteroverdrive.entity.player.AndroidPlayer;
import net.minecraftforge.event.entity.living.LivingEvent;

/**
 * Created by Simeon on 9/12/2015.
 */
public class BioticStatMinimap extends AbstractBioticStat
{
    public BioticStatMinimap(String name, int xp)
    {
        super(name, xp);
    }

    @Override
    public void onAndroidUpdate(AndroidPlayer android, int level) {

    }

    @Override
    public void onActionKeyPress(AndroidPlayer androidPlayer, int level, boolean server) {

    }

    @Override
    public void onKeyPress(AndroidPlayer androidPlayer, int level, int keycode, boolean down) {

    }

    @Override
    public void onLivingEvent(AndroidPlayer androidPlayer, int level, LivingEvent event) {

    }

    @Override
    public void changeAndroidStats(AndroidPlayer androidPlayer, int level, boolean enabled) {

    }

    @Override
    public Multimap attributes(AndroidPlayer androidPlayer, int level) {
        return null;
    }

    @Override
    public boolean isActive(AndroidPlayer androidPlayer, int level) {
        return true;
    }

    @Override
    public int getDelay(AndroidPlayer androidPlayer, int level)
    {
        return 0;
    }
}
