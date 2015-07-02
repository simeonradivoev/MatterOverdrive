package matteroverdrive.starmap;

import cpw.mods.fml.common.gameevent.TickEvent;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.handler.ConfigurationHandler;
import matteroverdrive.init.MatterOverdriveItems;
import matteroverdrive.network.packet.client.starmap.PacketUpdateGalaxy;
import matteroverdrive.network.packet.client.starmap.PacketUpdatePlanet;
import matteroverdrive.starmap.data.Galaxy;
import matteroverdrive.starmap.data.Planet;
import matteroverdrive.starmap.data.Quadrant;
import matteroverdrive.starmap.data.Star;
import matteroverdrive.util.IConfigSubscriber;
import matteroverdrive.util.MOLog;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
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
    //region Static Vars
    public static final int GALAXY_VERSION = 0;
    //endregion
    //region Private Vars
    private static GalaxyServer instance;
    private World world;
    private Galaxy theGalaxy;
    private GalaxyGenerator galaxyGenerator;
    private Random random;
    private HashMap<UUID,Planet> homePlanets;
    //endregion

    //region Constructors
    public GalaxyServer()
    {
        galaxyGenerator = new GalaxyGenerator();
        random = new Random();
        homePlanets = new HashMap();
    }
    //endregion

    //region Saving and Creation
    public void createGalaxy(File file,World world)
    {
        theGalaxy = galaxyGenerator.generateGalaxy("Galaxy",world.getWorldInfo().getVanillaDimension(),world.getWorldInfo().getSeed(),world);
        saveGalaxy(file);
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
    //endregion

    //region Loading
    public boolean loadGalaxy(File file,World world)
    {
        if (file.exists() && file.isFile()) {
            try {
                FileInputStream inputStream = new FileInputStream(file);
                NBTTagCompound tagCompound = CompressedStreamTools.readCompressed(inputStream);
                inputStream.close();
                theGalaxy = new Galaxy(world);
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
    //endregion

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
                            buildHomeworld(planet,player);
                            return planet;
                        }
                    }
                }
            }
        }
        return null;
    }

    private void buildHomeworld(Planet planet,EntityPlayer player)
    {
        planet.setOwner(player);
        planet.setHomeworld(true);
        planet.setBuildingSpaces(8);
        planet.setFleetSpaces(10);
        planet.addBuilding(new ItemStack(MatterOverdriveItems.buildingBase));
        planet.addShip(new ItemStack(MatterOverdriveItems.scoutShip));
        planet.markDirty();
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

    //region Events
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
            if (!loadGalaxy(galaxyFile,load.world)) {
                createGalaxy(galaxyFile, load.world);
                MOLog.log(Level.INFO, "Galaxy Generated and saved to '%1$s'. Took %2$s milliseconds",galaxyFile.getPath(),((System.nanoTime() - start) / 1000000));
            }else
            {
                MOLog.log(Level.INFO, "Galaxy Loaded from '%1$s'. Took %2$s milliseconds", galaxyFile.getPath(), ((System.nanoTime() - start) / 1000000));
            }
        }
    }

    @SubscribeEvent
    public void onWorldSave(WorldEvent.Save save)
    {
        if (!save.world.isRemote && save.world.provider.dimensionId == 0)
        {
            if (theGalaxy != null && theGalaxy.isDirty())
            {
                long start = System.nanoTime();
                File galaxyFile = getGalaxyFile(save.world);
                if (saveGalaxy(galaxyFile)) {
                    theGalaxy.onSave(galaxyFile,save.world);
                    MOLog.log(Level.INFO, "Galaxy saved to '%s'. Took %s milliseconds", galaxyFile.getPath(), ((System.nanoTime() - start) / 1000000));
                }
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

    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent event)
    {
        if (!event.world.isRemote && event.world.provider.dimensionId == 0)
        {
            theGalaxy.update(event.world);
        }
    }

    @Override
    public void onConfigChanged(ConfigurationHandler config)
    {

    }
    //endregion

    //region Getters and Setters
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
    //endregion
}
