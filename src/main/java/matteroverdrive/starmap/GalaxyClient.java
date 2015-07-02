package matteroverdrive.starmap;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import matteroverdrive.api.starmap.IShip;
import matteroverdrive.starmap.data.Galaxy;
import matteroverdrive.starmap.data.Planet;
import matteroverdrive.starmap.data.Quadrant;
import matteroverdrive.starmap.data.Star;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.UUID;

/**
 * Created by Simeon on 6/17/2015.
 */
@SideOnly(Side.CLIENT)
public class GalaxyClient
{
    //region Private Vars
    private static GalaxyClient instance;
    private Galaxy theGalaxy;
    private HashMap<UUID,Planet> homePlanets;
    //endregion

    //region Constructors
    public GalaxyClient()
    {
        homePlanets = new HashMap();
    }
    public Galaxy getTheGalaxy()
    {
        return theGalaxy;
    }
    //endregion

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

    public boolean canSeePlanetInfo(Planet planet,EntityPlayer player)
    {
        if (planet.isOwner(player))
        {
            return true;
        }

        for (ItemStack shipStack : planet.getFleet())
        {
            if (((IShip)shipStack.getItem()).isOwner(shipStack,player))
            {
                return true;
            }
        }

        return false;
    }

    public boolean canSeeStarInfo(Star star,EntityPlayer player)
    {
        for (Planet planet : star.getPlanets())
        {
            if (canSeePlanetInfo(planet,player))
            {
                return true;
            }
        }
        return false;
    }

    //region Events
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event)
    {
		if (Minecraft.getMinecraft().theWorld != null &&
				theGalaxy != null &&
				!Minecraft.getMinecraft().isGamePaused() &&
				Minecraft.getMinecraft().theWorld.isRemote &&
				Minecraft.getMinecraft().theWorld.provider.dimensionId == 0 &&
				event.phase == TickEvent.Phase.START)
		{
			theGalaxy.update(Minecraft.getMinecraft().theWorld);
		}
    }
    //endregion

    //region Getters and Setters
    public static GalaxyClient getInstance()
    {
        if (instance == null)
        {
            instance = new GalaxyClient();
        }

        return instance;
    }
    public Planet getHomeworld(EntityPlayer player)
    {
        return homePlanets.get(EntityPlayer.func_146094_a(player.getGameProfile()));
    }
    public void setTheGalaxy(Galaxy galaxy)
    {
        theGalaxy = galaxy;
        loadClaimedPlanets();
    }
    //endregion
}
