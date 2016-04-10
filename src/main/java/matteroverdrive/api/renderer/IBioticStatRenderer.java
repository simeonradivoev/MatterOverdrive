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

package matteroverdrive.api.renderer;

import matteroverdrive.api.android.IBioticStat;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Simeon on 7/24/2015.
 * Used by bionic stats (android abilities) to render special stats.
 * One example is the Shield Ability {@link matteroverdrive.data.biostats.BioticStatShield} uses a renderer to render it's shield.
 * This is used in the {@link matteroverdrive.api.android.IAndroidStatRenderRegistry}.
 */
@SideOnly(Side.CLIENT)
public interface IBioticStatRenderer<T extends IBioticStat>
{
	/**
	 * This method is called to render the stat.
	 * It is called when rendering the world.
	 * @param stat the bionic stat (android ability) being rendered.
	 * @param level the unlocked level of the stat/ability.
	 * @param event the world render event. This event holds useful information such as the partial render ticks.
	 */
	void onWorldRender(T stat, int level, RenderWorldLastEvent event);
}
