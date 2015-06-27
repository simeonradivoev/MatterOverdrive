package com.MO.MatterOverdrive.starmap;

import com.MO.MatterOverdrive.starmap.data.Galaxy;
import com.MO.MatterOverdrive.starmap.data.Planet;
import com.MO.MatterOverdrive.starmap.data.Quadrant;
import com.MO.MatterOverdrive.starmap.data.Star;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;

import java.util.HashMap;
import java.util.UUID;

/**
 * Created by Simeon on 6/17/2015.
 */
@SideOnly(Side.CLIENT)
public class GalaxyClient
{
    private static GalaxyClient instance;
    Galaxy theGalaxy;
    HashMap<UUID,Planet> homePlanets;

    public GalaxyClient()
    {
        homePlanets = new HashMap<UUID, Planet>();
    }

    public Galaxy getTheGalaxy()
    {
        return theGalaxy;
    }

    public void setTheGalaxy(Galaxy galaxy)
    {
        theGalaxy = galaxy;
        loadClaimedPlanets();
    }

    public void loadClaimedPlanets()
    {
        homePlanets.clear();

        for (Quadrant quadrant : theGalaxy.getQuadrants())
        {
            for (Star star : quadrant.getStars())
            {
                for (Planet planet : star.getPlanets())
                {
                    if (planet.isHomeworld() && planet.hasOwner())
                    {
                        homePlanets.put(planet.getOwnerUUID(),planet);
                    }
                }
            }
        }
    }

    public Planet getHomeworld(EntityPlayer player)
    {
        return homePlanets.get(EntityPlayer.func_146094_a(player.getGameProfile()));
    }

    public static GalaxyClient getInstance()
    {
        if (instance == null)
        {
            instance = new GalaxyClient();
        }

        return instance;
    }
}
