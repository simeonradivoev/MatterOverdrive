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
import matteroverdrive.api.inventory.IBionicPart;
import matteroverdrive.api.renderer.IBionicPartRenderer;
import matteroverdrive.api.renderer.IBioticStatRenderer;
import matteroverdrive.api.starmap.IStarmapRenderRegistry;
import matteroverdrive.client.render.*;
import matteroverdrive.client.render.biostat.BioticStatRendererShield;
import matteroverdrive.client.render.biostat.BioticStatRendererTeleporter;
import matteroverdrive.client.render.block.*;
import matteroverdrive.client.render.entity.*;
import matteroverdrive.client.render.item.ItemRendererOmniTool;
import matteroverdrive.client.render.item.ItemRendererPhaser;
import matteroverdrive.client.render.item.ItemRendererPhaserRifle;
import matteroverdrive.client.render.parts.RougeAndroidPartsRender;
import matteroverdrive.client.render.parts.TritaniumSpineRenderer;
import matteroverdrive.client.render.tileentity.*;
import matteroverdrive.client.render.tileentity.starmap.*;
import matteroverdrive.entity.*;
import matteroverdrive.entity.weapon.PlasmaBolt;
import matteroverdrive.handler.ConfigurationHandler;
import matteroverdrive.init.MatterOverdriveBioticStats;
import matteroverdrive.init.MatterOverdriveItems;
import matteroverdrive.items.RougeAndroidParts;
import matteroverdrive.items.android.TritaniumSpine;
import matteroverdrive.machines.fusionReactorController.TileEntityMachineFusionReactorController;
import matteroverdrive.starmap.data.Galaxy;
import matteroverdrive.starmap.data.Planet;
import matteroverdrive.starmap.data.Quadrant;
import matteroverdrive.starmap.data.Star;
import matteroverdrive.tile.*;
import matteroverdrive.util.MOLog;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.*;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import org.apache.logging.log4j.Level;
import org.lwjgl.opengl.GL11;

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
    private RenderWeaponsBeam renderWeaponsBeam;
    private List<IWorldLastRenderer> customRenderers;
    private AndroidStatRenderRegistry statRenderRegistry;
    private StarmapRenderRegistry starmapRenderRegistry;
    private RenderDialogSystem renderDialogSystem;
    private RenderStarmap renderStarmap;
    private AndroidBionicPartRenderRegistry bionicPartRenderRegistry;

    //region Block Renderers
    private MOBlockRenderer blockRenderer;
    private RendererBlockGravitationalStabilizer rendererBlockGravitationalStabilizer;
    private RendererBlockPipe rendererBlockPipe;
    private RendererBlockChargingStation rendererBlockChargingStation;
    private RendererBlockPatternStorage rendererBlockPatternStorage;
    private RendererBlockReplicator rendererBlockReplicator;
    private RendererBlockTritaniumCrate rendererBlockTritaniumCrate;
    private RendererBlockInscriber rendererBlockInscriber;
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
    private static ItemRendererPhaserRifle rendererPhaserRifle;
    private static ItemRendererOmniTool rendererOmniTool;
    //endregion
    //region Entity Renderers
    private EntityRendererRougeAndroid rendererRougeAndroid;
    private EntityRendererMadScientist rendererMadScientist;
    private EntityRendererFailedCow rendererFailedCow;
    private EntityRendererFailedChicken rendererFailedChicken;
    private EntityRendererFailedPig rendererFailedPig;
    private EntityRendererFailedSheep rendererFailedSheep;
    private EntityRendererPhaserFire rendererPhaserFire;
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
    private TileEntityRendererHoloSign tileEntityRendererHoloSign;
    private TileEntityRendererPacketQueue tileEntityRendererPacketQueue;
    private TileEntityRendererInscriber tileEntityRendererInscriber;
    //endregion

    public RenderHandler(World world, TextureManager textureManager)
    {
        customRenderers = new ArrayList<>();
        matterScannerInfoHandler = new RenderMatterScannerInfoHandler();
        renderParticlesHandler = new RenderParticlesHandler(world, textureManager);
        renderWeaponsBeam = new RenderWeaponsBeam();
        statRenderRegistry = new AndroidStatRenderRegistry();
        starmapRenderRegistry = new StarmapRenderRegistry();
        renderDialogSystem = new RenderDialogSystem();
        renderStarmap = new RenderStarmap();
        bionicPartRenderRegistry = new AndroidBionicPartRenderRegistry();

        addCustomRenderer(matterScannerInfoHandler);
        addCustomRenderer(renderParticlesHandler);
        addCustomRenderer(renderWeaponsBeam);
        addCustomRenderer(renderDialogSystem);
        addCustomRenderer(renderStarmap);
    }

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event)
    {
        for (IWorldLastRenderer renderer : customRenderers) {
            renderer.onRenderWorldLast(this, event);
        }
        for (IBionicStat stat : MatterOverdrive.statRegistry.getStats())
        {
            Collection<IBioticStatRenderer> statRendererCollection = statRenderRegistry.getRendererCollection(stat.getClass());
            if (statRendererCollection != null)
            {
                for (IBioticStatRenderer renderer : statRendererCollection)
                {
                    renderer.onWorldRender(stat, AndroidPlayer.get(Minecraft.getMinecraft().thePlayer).getUnlockedLevel(stat), event);
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
        tileEntityRendererHoloSign = new TileEntityRendererHoloSign();
        tileEntityRendererPacketQueue = new TileEntityRendererPacketQueue();
        tileEntityRendererInscriber = new TileEntityRendererInscriber();

        configHandler.subscribe(tileEntityRendererAndroidStation);
        configHandler.subscribe(tileEntityRendererWeaponStation);
    }

    @SubscribeEvent
    public void onPlayerRender(RenderPlayerEvent.Post event)
    {
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glColor3f(1, 1, 1);

        AndroidPlayer androidPlayer = AndroidPlayer.get(event.entityPlayer);
        if (androidPlayer != null && androidPlayer.isAndroid() && !event.entityPlayer.isInvisible()) {
            for (int i = 0; i < 5; i++)
            {
                ItemStack part = androidPlayer.getStackInSlot(i);
                if (part != null && part.getItem() instanceof IBionicPart)
                {
                    IBionicPartRenderer renderer = bionicPartRenderRegistry.getRenderer(((IBionicPart) part.getItem()).getClass());
                    if (renderer != null) {
                        try {
                            GL11.glPushMatrix();
                            renderer.renderPart(part, androidPlayer, event.renderer, event.partialRenderTick);
                            GL11.glPopMatrix();
                        }
                        catch (Exception e)
                        {
                            MOLog.log(Level.ERROR,e,"An Error occurred while rendering bionic part");
                        }
                    }
                }
            }
        }
    }

    public void createBlockRenderers()
    {
        blockRenderer = new MOBlockRenderer();
        rendererBlockGravitationalStabilizer = new RendererBlockGravitationalStabilizer();
        rendererBlockPipe = new RendererBlockPipe();
        rendererBlockChargingStation = new RendererBlockChargingStation();
        rendererBlockPatternStorage = new RendererBlockPatternStorage();
        rendererBlockReplicator = new RendererBlockReplicator();
        rendererBlockTritaniumCrate = new RendererBlockTritaniumCrate();
        rendererBlockInscriber = new RendererBlockInscriber();
    }

    public void registerBlockRenderers()
    {
        RenderingRegistry.registerBlockHandler(blockRenderer);
        RenderingRegistry.registerBlockHandler(rendererBlockGravitationalStabilizer);
        RenderingRegistry.registerBlockHandler(rendererBlockPipe);
        RenderingRegistry.registerBlockHandler(rendererBlockChargingStation);
        RenderingRegistry.registerBlockHandler(rendererBlockPatternStorage);
        RenderingRegistry.registerBlockHandler(rendererBlockReplicator);
        RenderingRegistry.registerBlockHandler(rendererBlockTritaniumCrate);
        RenderingRegistry.registerBlockHandler(rendererBlockInscriber);
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
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityHoloSign.class, tileEntityRendererHoloSign);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMachinePacketQueue.class, tileEntityRendererPacketQueue);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityInscriber.class,tileEntityRendererInscriber);
    }

    public void createItemRenderers()
    {
        rendererPhaser = new ItemRendererPhaser();
        rendererPhaserRifle = new ItemRendererPhaserRifle();
        rendererOmniTool = new ItemRendererOmniTool();
    }

    public void registerItemRenderers()
    {
        MinecraftForgeClient.registerItemRenderer(MatterOverdriveItems.phaser, rendererPhaser);
        MinecraftForgeClient.registerItemRenderer(MatterOverdriveItems.phaserRifle,rendererPhaserRifle);
        MinecraftForgeClient.registerItemRenderer(MatterOverdriveItems.omniTool,rendererOmniTool);
    }

    public void createEntityRenderers()
    {
        rendererRougeAndroid = new EntityRendererRougeAndroid(new ModelBiped(), 0);
        rendererMadScientist = new EntityRendererMadScientist();
        rendererFailedPig = new EntityRendererFailedPig(new ModelPig(),new ModelPig(0.5f), 0.7F);
        rendererFailedCow = new EntityRendererFailedCow(new ModelCow(), 0.7f);
        rendererFailedChicken = new EntityRendererFailedChicken(new ModelChicken(), 0.3f);
        rendererFailedSheep = new EntityRendererFailedSheep(new ModelSheep2(), new ModelSheep1(), 0.7f);
        rendererPhaserFire = new EntityRendererPhaserFire();
    }

    public void registerEntityRenderers()
    {
        RenderingRegistry.registerEntityRenderingHandler(EntityRougeAndroidMob.class, rendererRougeAndroid);
        RenderingRegistry.registerEntityRenderingHandler(EntityFailedPig.class,rendererFailedPig);
        RenderingRegistry.registerEntityRenderingHandler(EntityFailedCow.class, rendererFailedCow);
        RenderingRegistry.registerEntityRenderingHandler(EntityFailedChicken.class,rendererFailedChicken);
        RenderingRegistry.registerEntityRenderingHandler(EntityFailedSheep.class,rendererFailedSheep);
        RenderingRegistry.registerEntityRenderingHandler(EntityVillagerMadScientist.class, rendererMadScientist);
        RenderingRegistry.registerEntityRenderingHandler(PlasmaBolt.class, rendererPhaserFire);
    }

    public void createBioticStatRenderers()
    {
        rendererTeleporter = new BioticStatRendererTeleporter();
        biostatRendererShield = new BioticStatRendererShield();
    }

    public void registerBioticStatRenderers()
    {
        statRenderRegistry.registerRenderer(MatterOverdriveBioticStats.shield.getClass(),biostatRendererShield);
        statRenderRegistry.registerRenderer(MatterOverdriveBioticStats.teleport.getClass(),rendererTeleporter);
    }

    public void registerBionicPartRenderers()
    {
        bionicPartRenderRegistry.register(TritaniumSpine.class,new TritaniumSpineRenderer());
        bionicPartRenderRegistry.register(RougeAndroidParts.class, new RougeAndroidPartsRender());
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
        starmapRenderRegistry.registerRenderer(Planet.class, starMapRendererPlanet);
        starmapRenderRegistry.registerRenderer(Quadrant.class, starMapRendererQuadrant);
        starmapRenderRegistry.registerRenderer(Star.class, starMapRendererStar);
        starmapRenderRegistry.registerRenderer(Galaxy.class, starMapRenderGalaxy);
        starmapRenderRegistry.registerRenderer(Planet.class, starMapRenderPlanetStats);
    }

    public RenderParticlesHandler getRenderParticlesHandler()
    {
        return renderParticlesHandler;
    }

    public TileEntityRendererStarMap getTileEntityRendererStarMap()
    {
        return tileEntityRendererStarMap;
    }

    public IAndroidStatRenderRegistry getStatRenderRegistry()
    {
        return statRenderRegistry;
    }

    public IStarmapRenderRegistry getStarmapRenderRegistry()
    {
        return starmapRenderRegistry;
    }

    public ItemRendererOmniTool getRendererOmniTool(){return rendererOmniTool;}

    public AndroidBionicPartRenderRegistry getBionicPartRenderRegistry()
    {
        return bionicPartRenderRegistry;
    }

    public Random getRandom()
    {
        return random;
    }

    public void addCustomRenderer(IWorldLastRenderer renderer)
    {
        customRenderers.add(renderer);
    }
}
