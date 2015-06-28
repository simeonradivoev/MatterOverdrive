package com.MO.MatterOverdrive.starmap;

import com.MO.MatterOverdrive.MatterOverdrive;
import com.MO.MatterOverdrive.handler.MOConfigurationHandler;
import com.MO.MatterOverdrive.network.packet.client.starmap.PacketUpdateGalaxy;
import com.MO.MatterOverdrive.network.packet.client.starmap.PacketUpdatePlanet;
import com.MO.MatterOverdrive.starmap.data.Galaxy;
import com.MO.MatterOverdrive.starmap.data.Planet;
import com.MO.MatterOverdrive.starmap.data.Quadrant;
import com.MO.MatterOverdrive.starmap.data.Star;
import com.MO.MatterOverdrive.util.IConfigSubscriber;
import com.MO.MatterOverdrive.util.MOLog;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;
import org.apache.logging.log4j.Level;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

/**
 * Created by Simeon on 6/17/2015.
 */
public class GalaxyServer implements IConfigSubscriber
{
    public static final int GALAXY_VERSION = 0;
    private static GalaxyServer instance;
    World world;
    Galaxy theGalaxy;
    GalaxyGenerator galaxyGenerator;
    Random random;
    HashMap<UUID,Planet> homePlanets;

    public GalaxyServer()
    {
        galaxyGenerator = new GalaxyGenerator();
        random = new Random();
        homePlanets = new HashMap<UUID, Planet>();
    }

    public void createGalaxy(File file,World world)
    {
        theGalaxy = galaxyGenerator.generateGalaxy("Galaxy",world.getWorldInfo().getVanillaDimension(),world.getWorldInfo().getSeed());
        saveGalaxy(file);
    }

    public boolean loadGalaxy(File file)
    {
        if (file.exists() && file.isFile()) {
            try {
                FileInputStream inputStream = new FileInputStream(file);
                NBTTagCompound tagCompound = CompressedStreamTools.readCompressed(inputStream);
                inputStream.close();
                theGalaxy = new Galaxy();
                theGalaxy.readFromNBT(tagCompound,galaxyGenerator);
                if (theGalaxy.getVersion() != GALAXY_VERSION)
                {
                    galaxyGenerator.regenerateQuadrants(theGalaxy);
                }
                loadClaimedPlanets();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public boolean saveGalaxy(File file)
    {
        MappedByteBuffer mappedByteBuffer;

        try {
            if (!file.exists())
                file.createNewFile();

            FileOutputStream fileOutputStream = new FileOutputStream(file);
            NBTTagCompound tagCompound = new NBTTagCompound();
            theGalaxy.writeToNBT(tagCompound);
            CompressedStreamTools.writeCompressed(tagCompound, fileOutputStream);
            fileOutputStream.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event)
    {
        if (!event.player.worldObj.isRemote)
        {
            MatterOverdrive.packetPipeline.sendTo(new PacketUpdateGalaxy(theGalaxy), (EntityPlayerMP) event.player);
            if (tryAndClaimPlanet(event.player)) {
                Planet planet = getHomeworld(event.player);
                MatterOverdrive.packetPipeline.sendToDimention(new PacketUpdatePlanet(planet), event.player.worldObj);
            }
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load load)
    {
        if (!load.world.isRemote && load.world.provider.dimensionId == 0)
        {
            world = load.world;
            File galaxyFile = getGalaxyFile(load.world);
            long start = System.nanoTime();
            if (!loadGalaxy(galaxyFile)) {
                createGalaxy(galaxyFile, load.world);
                MOLog.log(Level.INFO, "Galaxy Generated and saved to '%1$s'. Took %2$s milliseconds",galaxyFile.getPath(),((System.nanoTime() - start) / 1000000));
            }else
            {
                MOLog.log(Level.INFO,"Galaxy Loaded from '%1$s'. Took %2$s milliseconds",galaxyFile.getPath(),((System.nanoTime() - start) / 1000000));
            }
        }
    }

    @SubscribeEvent
    public void onWorldSave(WorldEvent.Save save)
    {
        if (!save.world.isRemote && save.world.provider.dimensionId == 0)
        {
            if (theGalaxy != null) {
                long start = System.nanoTime();
                File galaxyFile = getGalaxyFile(save.world);
                saveGalaxy(galaxyFile);
                MOLog.log(Level.INFO,"Galaxy saved to '%s'. Took %s milliseconds",galaxyFile.getPath(),((System.nanoTime() - start) / 1000000));
            }
        }
    }

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload unload)
    {
        if (unload.world.isRemote && unload.world.provider.dimensionId == 0) {
            this.world = null;

            if (theGalaxy != null) {
                theGalaxy = null;
                homePlanets.clear();
            }
        }
    }

    private Planet claimPlanet(EntityPlayer player)
    {
        UUID playerUUID = EntityPlayer.func_146094_a(player.getGameProfile());
        int quadrantID = random.nextInt(theGalaxy.getQuadrants().size());
        for (Quadrant quadrant : theGalaxy.getQuadrants())
        {
            if (quadrant.getId() == quadrantID)
            {
                for (Star star : quadrant.getStars()) {
                    if (star.getPlanets().size() > 0) {
                        boolean isClaimed = false;
                        for (Planet planet : star.getPlanets()) {
                            if (planet.hasOwner() && !planet.getOwnerUUID().equals(playerUUID)) {
                                isClaimed = true;
                                break;
                            }
                        }

                        if (!isClaimed) {
                            int planetID = random.nextInt(star.getPlanets().size());
                            Planet planet = (Planet) star.getPlanets().toArray()[planetID];
                            planet.setOwner(player);
                            planet.setHomeworld(true);
                            planet.setBuildingSpaces(8);
                            planet.setFleetSpaces(10);
                            planet.markDirty();
                            return planet;
                        }
                    }
                }
            }
        }
        return null;
    }

    public void loadClaimedPlanets()
    {
        if (theGalaxy != null) {
            homePlanets.clear();
            for (Quadrant quadrant : theGalaxy.getQuadrants()) {
                for (Star star : quadrant.getStars()) {
                    for (Planet planet : star.getPlanets()) {
                        if (planet.isHomeworld() && planet.hasOwner()) {
                            homePlanets.put(planet.getOwnerUUID(), planet);
                        }
                    }
                }
            }
        }
    }

    private boolean tryAndClaimPlanet(EntityPlayer player)
    {
        UUID playerUUID = EntityPlayer.func_146094_a(player.getGameProfile());
        if (!homePlanets.containsKey(playerUUID))
        {
            Planet claimedPlanet = claimPlanet(player);
            if (claimedPlanet != null)
            {
                homePlanets.put(playerUUID,claimedPlanet);
                return true;
            }
        }
        return false;
    }

    public Planet getHomeworld(EntityPlayer player)
    {
        return homePlanets.get(EntityPlayer.func_146094_a(player.getGameProfile()));
    }

    public Galaxy getTheGalaxy()
    {
        return theGalaxy;
    }

    public void setTheGalaxy(Galaxy galaxy)
    {
        theGalaxy = galaxy;
    }

    private File getGalaxyFile(World world)
    {
        File worldDirectory = world.getSaveHandler().getWorldDirectory();
        return new File(worldDirectory.getPath() + "/galaxy.dat");
    }

    public GalaxyGenerator getGalaxyGenerator(){return galaxyGenerator;}

    public static GalaxyServer getInstance()
    {
        if (instance == null)
        {
            instance = new GalaxyServer();
        }
        return instance;
    }

    @Override
    public void onConfigChanged(MOConfigurationHandler config)
    {

    }
}
