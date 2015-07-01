package matteroverdrive.handler;

import matteroverdrive.entity.AndroidPlayer;
import matteroverdrive.starmap.GalaxyClient;
import matteroverdrive.starmap.GalaxyServer;
import matteroverdrive.tile.IMOTickable;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Created by Simeon on 4/26/2015.
 */
public class TickHandler
{
    private MatterNetworkTickHandler networkTick;
    private VersionCheckerHandler versionCheckerHandler;
    private PlayerEventHandler playerEventHandler;
    private boolean worldStartFired = false;

    public TickHandler(ConfigurationHandler configurationHandler,PlayerEventHandler playerEventHandler)
    {
        networkTick = new MatterNetworkTickHandler();
        this.playerEventHandler = playerEventHandler;
        versionCheckerHandler = new VersionCheckerHandler();

        configurationHandler.subscribe(versionCheckerHandler);
        configurationHandler.subscribe(networkTick);
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event)
    {
        versionCheckerHandler.onPlayerTick(event);
        AndroidPlayer player = AndroidPlayer.get(event.player);
        if (player != null) {
            player.onPlayerTick(event);
        }
    }

    //Called when the client ticks.
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event)
    {

    }

    //Called when the server ticks. Usually 20 ticks a second.
    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event)
    {
        playerEventHandler.onServerTick(event);
    }

    public void onServerStart(FMLServerStartedEvent event)
    {

    }

    //Called when a new frame is displayed (See fps)
    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent event)
    {

    }

    //Called when the world ticks
    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent event)
    {
        if (!worldStartFired)
        {
            onWorldStart(event.side,event.world);
            worldStartFired = true;
        }
        networkTick.onWorldTick(event);

        if (event.side.isServer()) {
            for (int i = 0;i < event.world.loadedTileEntityList.size();i++) {
                TileEntity tileentity = (TileEntity) event.world.loadedTileEntityList.get(i);

                if (tileentity instanceof IMOTickable && !tileentity.isInvalid() && tileentity.hasWorldObj() && event.world.blockExists(tileentity.xCoord, tileentity.yCoord, tileentity.zCoord)) {
                    ((IMOTickable) tileentity).onServerTick(event);
                }
            }
        }
    }

    public void onWorldStart(Side side,World world)
    {

    }
}
