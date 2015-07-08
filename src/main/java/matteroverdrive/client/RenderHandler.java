package matteroverdrive.client;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import matteroverdrive.client.render.IWorldLastRenderer;
import matteroverdrive.client.render.RenderMatterScannerInfoHandler;
import matteroverdrive.client.render.RenderParticlesHandler;
import matteroverdrive.client.render.RendererPhaserBeam;
import matteroverdrive.client.render.biostat.BiostatRendererShield;
import matteroverdrive.client.render.biostat.BiostatRendererTeleporter;
import matteroverdrive.client.render.block.*;
import matteroverdrive.client.render.entity.*;
import matteroverdrive.client.render.item.ItemRendererPhaser;
import matteroverdrive.client.render.item.ItemRendererTileEntityMachine;
import matteroverdrive.client.render.tileentity.*;
import matteroverdrive.entity.*;
import matteroverdrive.handler.ConfigurationHandler;
import matteroverdrive.init.MatterOverdriveBlocks;
import matteroverdrive.init.MatterOverdriveItems;
import matteroverdrive.tile.*;
import net.minecraft.client.model.*;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.item.Item;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.RenderWorldLastEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Simeon on 4/17/2015.
 */
public class RenderHandler
{
    private Random random = new Random();
    private RenderMatterScannerInfoHandler matterScannerInfoHandler;
    private RenderParticlesHandler renderParticlesHandler;
    private RendererPhaserBeam rendererPhaserBeam;
    private List<IWorldLastRenderer> customRenderers;

    //region Block Renderers
    private MOBlockRenderer blockRenderer;
    private RendererBlockGravitationalStabilizer gravitationalStabilizerRenderer;
    private RendererBlockPipe rendererBlockPipe;
    private RendererBlockChargingStation rendererBlockChargingStation;
    private RendererBlockPatternStorage rendererBlockPatternStorage;
    //endregion
    //region Biostat Renderers
    private BiostatRendererTeleporter rendererTeleporter;
    private BiostatRendererShield biostatRendererShield;
    //endregion
    //region Item Renderers
    private static ItemRendererPhaser rendererPhaser;
    //endregion
    //region Entity Renderers
    private EntityRendererRougeAndroid rendererRougeAndroid;
    private EntityRendererMadScientist rendererMadScientist;
    private EntityRendererFaildCow rendererFaildCow;
    private EntityRendererFailedChicken rendererFailedChicken;
    private EntityRendererFailedPig rendererFailedPig;
    private EntityRendererFailedSheep rendererFailedSheep;
    //endregion
    //region Tile Entity Renderers
    private TileEntityRendererPipe pipeRenderer;
    private TileEntityRendererMatterPipe matter_pipeRenderer;
    private TileEntityRendererNetworkPipe network_pipeRenderer;
    private TileEntityRendererReplicator replicator_renderer;
    private TileEntityRendererPatterStorage pattern_storage_renderer;
    private TileEntityRendererWeaponStation renderer_weapon_station;
    private TileEntityRendererPatternMonitor pattern_monitor_renderer;
    private TileEntityRendererGravitationalAnomaly gravitational_anomaly_renderer;
    private TileEntityRendererGravitationalStabilizer gravitational_stabilizer_renderer;
    private TileEntityRendererFusionReactorController fusion_reactor_controller_renderer;
    private TileEntityRendererAndroidStation rendererAndroidStation;
    private TileEntityRendererStarMap rendererStarMap;
    private TileEntityRendererChargingStation rendererChargingStation;
    //endregion

    public RenderHandler(World world,TextureManager textureManager)
    {
        customRenderers = new ArrayList<IWorldLastRenderer>();
        matterScannerInfoHandler = new RenderMatterScannerInfoHandler();
        renderParticlesHandler = new RenderParticlesHandler(world,textureManager);
        rendererTeleporter = new BiostatRendererTeleporter();
        biostatRendererShield = new BiostatRendererShield();
        rendererPhaserBeam = new RendererPhaserBeam();

        addCustomRenderer(matterScannerInfoHandler);
        addCustomRenderer(renderParticlesHandler);
        addCustomRenderer(rendererTeleporter);
        addCustomRenderer(biostatRendererShield);
        addCustomRenderer(rendererPhaserBeam);
    }

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event)
    {
        for (int i = 0;i < customRenderers.size();i++)
        {
            customRenderers.get(i).onRenderWorldLast(this,event);
        }
    }

    //Called when the client ticks.
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event)
    {
        renderParticlesHandler.onClientTick(event);
    }

    public void createTileEntityRenderers(ConfigurationHandler configHandler)
    {
        pipeRenderer = new TileEntityRendererPipe();
        matter_pipeRenderer = new TileEntityRendererMatterPipe();
        network_pipeRenderer = new TileEntityRendererNetworkPipe();
        replicator_renderer = new TileEntityRendererReplicator();
        pattern_storage_renderer = new TileEntityRendererPatterStorage();
        renderer_weapon_station = new TileEntityRendererWeaponStation();
        pattern_monitor_renderer = new TileEntityRendererPatternMonitor();
        gravitational_anomaly_renderer = new TileEntityRendererGravitationalAnomaly();
        gravitational_stabilizer_renderer = new TileEntityRendererGravitationalStabilizer();
        fusion_reactor_controller_renderer = new TileEntityRendererFusionReactorController();
        rendererAndroidStation = new TileEntityRendererAndroidStation();
        rendererStarMap = new TileEntityRendererStarMap();
        rendererChargingStation = new TileEntityRendererChargingStation();

        configHandler.subscribe(rendererAndroidStation);
        configHandler.subscribe(renderer_weapon_station);
    }

    public void createBlockRenderers()
    {
        blockRenderer = new MOBlockRenderer();
        gravitationalStabilizerRenderer = new RendererBlockGravitationalStabilizer();
        rendererBlockPipe = new RendererBlockPipe();
        rendererBlockChargingStation = new RendererBlockChargingStation();
        rendererBlockPatternStorage = new RendererBlockPatternStorage();
    }

    public void registerBlockRenderers()
    {
        RenderingRegistry.registerBlockHandler(blockRenderer);
        RenderingRegistry.registerBlockHandler(gravitationalStabilizerRenderer);
        RenderingRegistry.registerBlockHandler(rendererBlockPipe);
        RenderingRegistry.registerBlockHandler(rendererBlockChargingStation);
        RenderingRegistry.registerBlockHandler(rendererBlockPatternStorage);
    }

    public void registerTileEntitySpecialRenderers()
    {
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMachineReplicator.class,replicator_renderer);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMachinePatternStorage.class,pattern_storage_renderer);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityWeaponStation.class,renderer_weapon_station);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMachinePatternMonitor.class,pattern_monitor_renderer);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityGravitationalAnomaly.class,gravitational_anomaly_renderer);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMachineGravitationalStabilizer.class,gravitational_stabilizer_renderer);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMachineFusionReactorController.class,fusion_reactor_controller_renderer);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAndroidStation.class,rendererAndroidStation);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMachineStarMap.class,rendererStarMap);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMachineChargingStation.class,rendererChargingStation);
    }

    public void createItemRenderers()
    {
        rendererPhaser = new ItemRendererPhaser();
    }

    public void registerItemRenderers()
    {
        MinecraftForgeClient.registerItemRenderer(MatterOverdriveItems.phaser, rendererPhaser);
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(MatterOverdriveBlocks.replicator), new ItemRendererTileEntityMachine(replicator_renderer, new TileEntityMachineReplicator()));
    }

    public void createEntityRenderers()
    {
        rendererRougeAndroid = new EntityRendererRougeAndroid(new ModelBiped(),0);
        rendererMadScientist = new EntityRendererMadScientist();
        rendererFailedPig = new EntityRendererFailedPig(new ModelPig(),new ModelPig(0.5f),0.7F);
        rendererFaildCow = new EntityRendererFaildCow(new ModelCow(),0.7f);
        rendererFailedChicken = new EntityRendererFailedChicken(new ModelChicken(),0.3f);
        rendererFailedSheep = new EntityRendererFailedSheep(new ModelSheep2(),new ModelSheep1(),0.7f);
    }

    public void registerEntityRenderers()
    {
        RenderingRegistry.registerEntityRenderingHandler(EntityRougeAndroidMob.class, rendererRougeAndroid);
        RenderingRegistry.registerEntityRenderingHandler(EntityFailedPig.class,rendererFailedPig);
        RenderingRegistry.registerEntityRenderingHandler(EntityFailedCow.class,rendererFaildCow);
        RenderingRegistry.registerEntityRenderingHandler(EntityFailedChicken.class,rendererFailedChicken);
        RenderingRegistry.registerEntityRenderingHandler(EntityFailedSheep.class,rendererFailedSheep);
        RenderingRegistry.registerEntityRenderingHandler(EntityVillagerMadScientist.class,rendererMadScientist);
    }

    public RenderParticlesHandler getRenderParticlesHandler()
    {
        return renderParticlesHandler;
    }
    public TileEntityRendererStarMap getRendererStarMap()
    {
        return rendererStarMap;
    }
    public Random getRandom(){return random;}
    public void addCustomRenderer(IWorldLastRenderer renderer){customRenderers.add(renderer);}
}
