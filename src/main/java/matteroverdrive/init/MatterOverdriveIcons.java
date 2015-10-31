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

package matteroverdrive.init;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import matteroverdrive.Reference;
import matteroverdrive.client.render.IconConnectedTexture;
import matteroverdrive.proxy.ClientProxy;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.event.TextureStitchEvent;

/**
 * Created by Simeon on 3/17/2015.
 */
public class MatterOverdriveIcons
{
    public static IIcon Base;
    public static IIcon Recycler;
    public static IIcon Vent;
    public static IIcon Vent2;
    public static IIcon matter_tank_full;
    public static IIcon matter_tank_empty;
    public static IIcon Transparent;
    public static IIcon Network_port_square;
    public static IconConnectedTexture Monitor_back;
    public static IIcon YellowStripes;
    public static IIcon Coil;
    public static IIcon charging_station;
    public static IIcon pattern_storage;
    public static IIcon replicator;
    public static IIcon matter_plasma_still;
    public static IIcon matter_plasma_flowing;
    public static IIcon packet_queue_active;

    public static IIcon particle_steam;

    @SubscribeEvent
    public void registerTextures(TextureStitchEvent event)
    {
        switch (event.map.getTextureType())
        {
            case 0:
                initBlockIcons(event.map);
                break;
            case 4:
                ClientProxy.holoIcons.registerIcons(event.map);
                break;
        }
    }

    private void initBlockIcons(IIconRegister r)
    {
        Vent = register(r, "vent");
        Base = register(r, "base");
        matter_tank_empty = register(r, "tank_empty");
        matter_tank_full = register(r, "tank_full");
        Transparent = register(r, "transperant");
        Network_port_square = register(r,"network_port");
        Vent2 = register(r,"vent2");
        Monitor_back = new IconConnectedTexture(register(r,"holo_monitor"));
        YellowStripes = register(r,"base_stripes");
        Recycler = register(r,"recycler_side");
        Coil = register(r,"base_coil");
        charging_station = register(r,"charging_station");
        pattern_storage = register(r,"pattern_storage");
        replicator = register(r,"replicator");
        matter_plasma_still = register(r,"matter_plasma_still");
        matter_plasma_flowing = register(r,"matter_plasma_flowing");
        packet_queue_active = register(r,"packet_queue_active");
    }

    private void initParticleIcons(IIconRegister r)
    {
        particle_steam = register(r,"particle_steam");

    }

    private void initItems(IIconRegister r)
    {
        register(r, "vent");
        register(r, "base");
        register(r, "tank_empty");
        register(r, "tank_full");
    }

    public static IIcon register(IIconRegister register,String name)
    {
        return register.registerIcon(Reference.MOD_ID + ":" + name);
    }
}
