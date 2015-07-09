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
import matteroverdrive.client.render.tileentity.*;
import matteroverdrive.entity.*;
import matteroverdrive.handler.ConfigurationHandler;
import matteroverdrive.init.MatterOverdriveItems;
import matteroverdrive.tile.*;
import net.minecraft.client.model.*;
import net.minecraft.client.renderer.texture.TextureManager;
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
    private RendererBlockGravitationalStabilizer rendererBlockGravitationalStabilizer;
    private RendererBlockPipe rendererBlockPipe;
    private RendererBlockChargingStation rendererBlockChargingStation;
    private RendererBlockPatternStorage rendererBlockPatternStorage;
    private RendererBlockReplicator rendererBlockReplicator;
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
    private TileEntityRendererReplicator tileEntityRendererReplicator;
    private TileEntityRendererPipe tileEntityRendererPipe;
    private TileEntityRendererMatterPipe tileEntityRendererMatterPipe;
    private TileEntityRendererNetworkPipe tileEntityRendererNetworkPipe;
    private TileEntityRendererPatterStorage tileEntityRendererPatterStorage;
    private TileEntityRendererWeaponStation tileEntityRendererWeaponStation;
    private TileEntityRendererPatternMonitor tileEntityRendererPatternMonitor;
    private TileEntityRendererGravitationalAnomaly tileEntityRendererGravitationalAnomaly;
    private TileEntityRendererGravitationalStabilizer tileEntityRendererGravitationalStabilizer;
    private TileEntityRendererFusionReactorController tileEntityRendererFusionReactorController;
    private TileEntityRendererAndroidStation tileEntityRendererAndroidStation;
    private TileEntityRendererStarMap tileEntityRendererStarMap;
    private TileEntityRendererChargingStation tileEntityRendererChargingStation;
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
        tileEntityRendererReplicator = new TileEntityRendererReplicator();
        tileEntityRendererPipe = new TileEntityRendererPipe();
        tileEntityRendererMatterPipe = new TileEntityRendererMatterPipe();
        tileEntityRendererNetworkPipe = new TileEntityRendererNetworkPipe();
        tileEntityRendererPatterStorage = new TileEntityRendererPatterStorage();
        tileEntityRendererWeaponStation = new TileEntityRendererWeaponStation();
        tileEntityRendererPatternMonitor = new TileEntityRendererPatternMonitor();
        tileEntityRendererGravitationalAnomaly = new TileEntityRendererGravitationalAnomaly();
        tileEntityRendererGravitationalStabilizer = new TileEntityRendererGravitationalStabilizer();
        tileEntityRendererFusionReactorController = new TileEntityRendererFusionReactorController();
        tileEntityRendererAndroidStation = new TileEntityRendererAndroidStation();
        tileEntityRendererStarMap = new TileEntityRendererStarMap();
        tileEntityRendererChargingStation = new TileEntityRendererChargingStation();

        configHandler.subscribe(tileEntityRendererAndroidStation);
        configHandler.subscribe(tileEntityRendererWeaponStation);
    }

    public void createBlockRenderers()
    {
        blockRenderer = new MOBlockRenderer();
        rendererBlockGravitationalStabilizer = new RendererBlockGravitationalStabilizer();
        rendererBlockPipe = new RendererBlockPipe();
        rendererBlockChargingStation = new RendererBlockChargingStation();
        rendererBlockPatternStorage = new RendererBlockPatternStorage();
        rendererBlockReplicator = new RendererBlockReplicator();
    }

    public void registerBlockRenderers()
    {
        RenderingRegistry.registerBlockHandler(blockRenderer);
        RenderingRegistry.registerBlockHandler(rendererBlockGravitationalStabilizer);
        RenderingRegistry.registerBlockHandler(rendererBlockPipe);
        RenderingRegistry.registerBlockHandler(rendererBlockChargingStation);
        RenderingRegistry.registerBlockHandler(rendererBlockPatternStorage);
        RenderingRegistry.registerBlockHandler(rendererBlockReplicator);
    }

    public void registerTileEntitySpecialRenderers()
    {
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMachineReplicator.class, tileEntityRendererReplicator);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMachinePatternStorage.class, tileEntityRendererPatterStorage);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityWeaponStation.class, tileEntityRendererWeaponStation);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMachinePatternMonitor.class, tileEntityRendererPatternMonitor);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityGravitationalAnomaly.class, tileEntityRendererGravitationalAnomaly);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMachineGravitationalStabilizer.class, tileEntityRendererGravitationalStabilizer);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMachineFusionReactorController.class, tileEntityRendererFusionReactorController);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAndroidStation.class, tileEntityRendererAndroidStation);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMachineStarMap.class, tileEntityRendererStarMap);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMachineChargingStation.class, tileEntityRendererChargingStation);
    }

    public void createItemRenderers()
    {
        rendererPhaser = new ItemRendererPhaser();
    }

    public void registerItemRenderers()
    {
        MinecraftForgeClient.registerItemRenderer(MatterOverdriveItems.phaser, rendererPhaser);
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
    public TileEntityRendererStarMap getTileEntityRendererStarMap()
    {
        return tileEntityRendererStarMap;
    }
    public Random getRandom(){return random;}
    public void addCustomRenderer(IWorldLastRenderer renderer){customRenderers.add(renderer);}
}
