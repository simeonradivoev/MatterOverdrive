package com.MO.MatterOverdrive.client;

import com.MO.MatterOverdrive.client.render.*;
import com.MO.MatterOverdrive.client.render.biostat.BiostatRendererShield;
import com.MO.MatterOverdrive.client.render.biostat.BiostatRendererTeleporter;
import com.MO.MatterOverdrive.client.render.block.MOBlockRenderer;
import com.MO.MatterOverdrive.client.render.block.RendererBlockGravitationalStabilizer;
import com.MO.MatterOverdrive.client.render.block.RendererBlockPipe;
import com.MO.MatterOverdrive.client.render.entity.*;
import com.MO.MatterOverdrive.client.render.item.ItemRendererPhaser;
import com.MO.MatterOverdrive.client.render.item.ItemRendererPipe;
import com.MO.MatterOverdrive.client.render.item.ItemRendererTileEntityMachine;
import com.MO.MatterOverdrive.client.render.tileentity.*;
import com.MO.MatterOverdrive.entity.*;
import com.MO.MatterOverdrive.init.MatterOverdriveBlocks;
import com.MO.MatterOverdrive.init.MatterOverdriveItems;
import com.MO.MatterOverdrive.tile.*;
import com.MO.MatterOverdrive.tile.pipes.TileEntityMatterPipe;
import com.MO.MatterOverdrive.tile.pipes.TileEntityNetworkPipe;
import com.MO.MatterOverdrive.tile.pipes.TileEntityPipe;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.client.model.*;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.item.Item;
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

    public void createTileEntityRenderers()
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
    }

    public void createBlockRenderers()
    {
        blockRenderer = new MOBlockRenderer();
        gravitationalStabilizerRenderer = new RendererBlockGravitationalStabilizer();
        rendererBlockPipe = new RendererBlockPipe();
    }

    public void registerBlockRenderers()
    {
        RenderingRegistry.registerBlockHandler(blockRenderer);
        RenderingRegistry.registerBlockHandler(gravitationalStabilizerRenderer);
        RenderingRegistry.registerBlockHandler(rendererBlockPipe);
    }

    public void registerTileEntitySpecialRenderers()
    {
        //ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPipe.class, pipeRenderer);
        //ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMatterPipe.class, matter_pipeRenderer);
        //ClientRegistry.bindTileEntitySpecialRenderer(TileEntityNetworkPipe.class, network_pipeRenderer);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMachineReplicator.class,replicator_renderer);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMachinePatternStorage.class,pattern_storage_renderer);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityWeaponStation.class,renderer_weapon_station);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMachinePatternMonitor.class,pattern_monitor_renderer);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityGravitationalAnomaly.class,gravitational_anomaly_renderer);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMachineGravitationalStabilizer.class,gravitational_stabilizer_renderer);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMachineFusionReactorController.class,fusion_reactor_controller_renderer);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAndroidStation.class,rendererAndroidStation);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMachineStarMap.class,rendererStarMap);
    }

    public void createItemRenderers()
    {
        rendererPhaser = new ItemRendererPhaser();
    }

    public void registerItemRenderers()
    {
        MinecraftForgeClient.registerItemRenderer(MatterOverdriveItems.phaser, rendererPhaser);
        //MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(MatterOverdriveBlocks.matter_pipe), new ItemRendererPipe(matter_pipeRenderer, new TileEntityMatterPipe(), 2));
        //MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(MatterOverdriveBlocks.network_pipe),new ItemRendererPipe(network_pipeRenderer,new TileEntityNetworkPipe(),2));
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(MatterOverdriveBlocks.replicator), new ItemRendererTileEntityMachine(replicator_renderer, new TileEntityMachineReplicator()));
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(MatterOverdriveBlocks.pattern_storage),new ItemRendererTileEntityMachine(pattern_storage_renderer,new TileEntityMachinePatternStorage()));
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
