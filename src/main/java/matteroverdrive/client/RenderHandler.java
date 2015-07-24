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

package matteroverdrive.client;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.api.android.IAndroidStatRenderRegistry;
import matteroverdrive.api.android.IBionicStat;
import matteroverdrive.api.renderer.IBioticStatRenderer;
import matteroverdrive.api.starmap.IStarmapRenderRegistry;
import matteroverdrive.client.render.*;
import matteroverdrive.client.render.biostat.BioticStatRendererShield;
import matteroverdrive.client.render.biostat.BioticStatRendererTeleporter;
import matteroverdrive.client.render.block.*;
import matteroverdrive.client.render.entity.*;
import matteroverdrive.client.render.item.ItemRendererPhaser;
import matteroverdrive.client.render.tileentity.*;
import matteroverdrive.client.render.tileentity.starmap.*;
import matteroverdrive.entity.*;
import matteroverdrive.handler.ConfigurationHandler;
import matteroverdrive.init.MatterOverdriveItems;
import matteroverdrive.machines.fusionReactorController.TileEntityMachineFusionReactorController;
import matteroverdrive.starmap.data.Galaxy;
import matteroverdrive.starmap.data.Planet;
import matteroverdrive.starmap.data.Quadrant;
import matteroverdrive.starmap.data.Star;
import matteroverdrive.tile.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.*;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.RenderWorldLastEvent;

import java.util.ArrayList;
import java.util.Collection;
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
    private AndroidStatRenderRegistry statRenderRegistry;
    private StarmapRenderRegistry starmapRenderRegistry;

    //region Block Renderers
    private MOBlockRenderer blockRenderer;
    private RendererBlockGravitationalStabilizer rendererBlockGravitationalStabilizer;
    private RendererBlockPipe rendererBlockPipe;
    private RendererBlockChargingStation rendererBlockChargingStation;
    private RendererBlockPatternStorage rendererBlockPatternStorage;
    private RendererBlockReplicator rendererBlockReplicator;
    //endregion
    //region Biostat Renderers
    private BioticStatRendererTeleporter rendererTeleporter;
    private BioticStatRendererShield biostatRendererShield;
    //endregion
    //region Starmap Renderers
    private StarMapRendererPlanet starMapRendererPlanet;
    private StarMapRendererQuadrant starMapRendererQuadrant;
    private StarMapRendererStar starMapRendererStar;
    private StarMapRenderGalaxy starMapRenderGalaxy;
    private StarMapRenderPlanetStats starMapRenderPlanetStats;
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
        rendererPhaserBeam = new RendererPhaserBeam();
        statRenderRegistry = new AndroidStatRenderRegistry();
        starmapRenderRegistry = new StarmapRenderRegistry();

        addCustomRenderer(matterScannerInfoHandler);
        addCustomRenderer(renderParticlesHandler);
        addCustomRenderer(rendererPhaserBeam);
    }

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event)
    {
        for (int i = 0;i < customRenderers.size();i++)
        {
            customRenderers.get(i).onRenderWorldLast(this,event);
        }
        for (IBionicStat stat : MatterOverdrive.statRegistry.getStats())
        {
            Collection<IBioticStatRenderer> statRendererCollection = statRenderRegistry.getRendererCollection(stat.getClass());
            if (statRendererCollection != null)
            {
                for (IBioticStatRenderer renderer : statRendererCollection)
                {
                    renderer.onWorldRender(stat,AndroidPlayer.get(Minecraft.getMinecraft().thePlayer).getUnlockedLevel(stat),event);
                }
            }
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

    public void createBioticStatRenderers()
    {
        rendererTeleporter = new BioticStatRendererTeleporter();
        biostatRendererShield = new BioticStatRendererShield();
    }

    public void registerBioticStatRenderers()
    {
        statRenderRegistry.registerRenderer(MatterOverdrive.statRegistry.shield.getClass(),biostatRendererShield);
        statRenderRegistry.registerRenderer(MatterOverdrive.statRegistry.teleport.getClass(),rendererTeleporter);
    }

    public void createStarmapRenderers()
    {
        starMapRendererPlanet = new StarMapRendererPlanet();
        starMapRendererQuadrant = new StarMapRendererQuadrant();
        starMapRendererStar = new StarMapRendererStar();
        starMapRenderGalaxy = new StarMapRenderGalaxy();
        starMapRenderPlanetStats = new StarMapRenderPlanetStats();
    }

    public void registerStarmapRenderers()
    {
        starmapRenderRegistry.registerRenderer(Planet.class,starMapRendererPlanet);
        starmapRenderRegistry.registerRenderer(Quadrant.class,starMapRendererQuadrant);
        starmapRenderRegistry.registerRenderer(Star.class,starMapRendererStar);
        starmapRenderRegistry.registerRenderer(Galaxy.class,starMapRenderGalaxy);
        starmapRenderRegistry.registerRenderer(Planet.class,starMapRenderPlanetStats);
    }

    public RenderParticlesHandler getRenderParticlesHandler()
    {
        return renderParticlesHandler;
    }
    public TileEntityRendererStarMap getTileEntityRendererStarMap()
    {
        return tileEntityRendererStarMap;
    }
    public IAndroidStatRenderRegistry getStatRenderRegistry(){return statRenderRegistry;}
    public IStarmapRenderRegistry getStarmapRenderRegistry(){return starmapRenderRegistry;}
    public Random getRandom(){return random;}
    public void addCustomRenderer(IWorldLastRenderer renderer){customRenderers.add(renderer);}
}
