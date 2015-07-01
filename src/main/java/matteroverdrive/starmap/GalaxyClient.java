package matteroverdrive.starmap;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import matteroverdrive.starmap.data.Galaxy;
import matteroverdrive.starmap.data.Planet;
import matteroverdrive.starmap.data.Quadrant;
import matteroverdrive.starmap.data.Star;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
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

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event)
    {
        if (!Minecraft.getMinecraft().isGamePaused() && Minecraft.getMinecraft().theWorld != null && Minecraft.getMinecraft().theWorld.isRemote && Minecraft.getMinecraft().theWorld.provider.dimensionId == 0 && event.phase == TickEvent.Phase.START)
        {
            theGalaxy.update(Minecraft.getMinecraft().theWorld);
        }
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
