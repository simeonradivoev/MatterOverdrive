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

package matteroverdrive.proxy;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.compat.MatterOverdriveCompat;
import matteroverdrive.starmap.GalaxyServer;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;

public class CommonProxy
{
	public void registerProxies()
	{
        MinecraftForge.EVENT_BUS.register(GalaxyServer.getInstance());
        FMLCommonHandler.instance().bus().register(GalaxyServer.getInstance());
        MatterOverdrive.configHandler.subscribe(GalaxyServer.getInstance());
        MatterOverdrive.configHandler.subscribe(GalaxyServer.getInstance().getGalaxyGenerator());
	}

    public void registerCompatModules()
    {
        MatterOverdriveCompat.registerModules();
    }

    public void registerBlockIcons(IIconRegister register) {}

    public EntityPlayer getPlayerEntity(MessageContext ctx)
    {
        return ctx.getServerHandler().playerEntity;
    }

    public void init(FMLInitializationEvent event)
    {
        registerProxies();
    }
}
