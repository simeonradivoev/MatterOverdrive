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

package matteroverdrive.core;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.api.IMOApi;
import matteroverdrive.api.android.IAndroidStatRegistry;
import matteroverdrive.api.android.IAndroidStatRenderRegistry;
import matteroverdrive.api.dialog.IDialogRegistry;
import matteroverdrive.api.matter.IMatterRegistry;
import matteroverdrive.api.starmap.IStarmapRenderRegistry;
import matteroverdrive.proxy.ClientProxy;

/**
 * Created by Simeon on 7/20/2015.
 */
public class Api implements IMOApi
{
    public static final Api INSTANCE = new Api();

    @Override
    public IMatterRegistry getMatterRegistry()
    {
        return MatterOverdrive.matterRegistry;
    }
    @Override
    public IAndroidStatRegistry getAndroidStatRegistry() {return MatterOverdrive.statRegistry;}

    @Override
    public IDialogRegistry getDialogRegistry()
    {
        return MatterOverdrive.dialogRegistry;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IAndroidStatRenderRegistry getAndroidStatRenderRegistry() {
        return ClientProxy.renderHandler.getStatRenderRegistry();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IStarmapRenderRegistry getStarmapRenrerRegistry() {
        return ClientProxy.renderHandler.getStarmapRenderRegistry();
    }
}
