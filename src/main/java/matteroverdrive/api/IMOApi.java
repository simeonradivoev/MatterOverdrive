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

package matteroverdrive.api;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import matteroverdrive.api.android.IAndroidStatRegistry;
import matteroverdrive.api.android.IAndroidStatRenderRegistry;
import matteroverdrive.api.dialog.IDialogRegistry;
import matteroverdrive.api.matter.IMatterRegistry;
import matteroverdrive.api.renderer.IBionicPartRenderRegistry;
import matteroverdrive.api.starmap.IStarmapRenderRegistry;

/**
 * Created by Simeon on 7/20/2015.
 */
public interface IMOApi
{
    IMatterRegistry getMatterRegistry();

    IAndroidStatRegistry getAndroidStatRegistry();

    IDialogRegistry getDialogRegistry();

    @SideOnly(Side.CLIENT)
    IAndroidStatRenderRegistry getAndroidStatRenderRegistry();

    @SideOnly(Side.CLIENT)
    IBionicPartRenderRegistry getBionicStatRenderRegistry();

    @SideOnly(Side.CLIENT)
    IStarmapRenderRegistry getStarmapRenderRegistry();

}
