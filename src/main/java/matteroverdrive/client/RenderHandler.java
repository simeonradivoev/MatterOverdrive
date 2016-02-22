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

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.api.android.IAndroidStatRenderRegistry;
import matteroverdrive.api.android.IBioticStat;
import matteroverdrive.api.inventory.IBionicPart;
import matteroverdrive.api.renderer.IBionicPartRenderer;
import matteroverdrive.api.renderer.IBioticStatRenderer;
import matteroverdrive.api.starmap.IStarmapRenderRegistry;
import matteroverdrive.client.model.ModelDrone;
import matteroverdrive.client.model.ModelHulkingScientist;
import matteroverdrive.client.model.ModelTritaniumArmor;
import matteroverdrive.client.render.*;
import matteroverdrive.client.render.biostat.BioticStatRendererShield;
import matteroverdrive.client.render.biostat.BioticStatRendererTeleporter;
import matteroverdrive.client.render.entity.*;
import matteroverdrive.client.render.tileentity.*;
import matteroverdrive.client.render.tileentity.starmap.*;
import matteroverdrive.client.render.weapons.*;
import matteroverdrive.client.render.weapons.layers.WeaponLayerAmmoRender;
import matteroverdrive.client.render.weapons.modules.ModuleHoloSightsRender;
import matteroverdrive.client.render.weapons.modules.ModuleSniperScopeRender;
import matteroverdrive.entity.*;
import matteroverdrive.entity.android_player.AndroidPlayer;
import matteroverdrive.entity.monster.EntityMeleeRougeAndroidMob;
import matteroverdrive.entity.monster.EntityMutantScientist;
import matteroverdrive.entity.monster.EntityRangedRogueAndroidMob;
import matteroverdrive.entity.weapon.PlasmaBolt;
import matteroverdrive.handler.ConfigurationHandler;
import matteroverdrive.init.MatterOverdriveBioticStats;
import matteroverdrive.init.MatterOverdriveBlocks;
import matteroverdrive.init.MatterOverdriveItems;
import matteroverdrive.items.IsolinearCircuit;
import matteroverdrive.items.ItemUpgrade;
import matteroverdrive.items.SecurityProtocol;
import matteroverdrive.items.android.RougeAndroidParts;
import matteroverdrive.items.food.AndroidPill;
import matteroverdrive.items.weapon.module.WeaponModuleBarrel;
import matteroverdrive.items.weapon.module.WeaponModuleColor;
import matteroverdrive.items.weapon.module.WeaponModuleHoloSights;
import matteroverdrive.items.weapon.module.WeaponModuleSniperScope;
import matteroverdrive.machines.fusionReactorController.TileEntityMachineFusionReactorController;
import matteroverdrive.machines.pattern_monitor.TileEntityMachinePatternMonitor;
import matteroverdrive.machines.pattern_storage.TileEntityMachinePatternStorage;
import matteroverdrive.machines.replicator.TileEntityMachineReplicator;
import matteroverdrive.starmap.data.Galaxy;
import matteroverdrive.starmap.data.Planet;
import matteroverdrive.starmap.data.Quadrant;
import matteroverdrive.starmap.data.Star;
import matteroverdrive.tile.*;
import matteroverdrive.util.MOLog;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.Attributes;
import net.minecraftforge.client.model.IFlexibleBakedModel;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.logging.log4j.Level;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

/**
 * Created by Simeon on 4/17/2015.
 */
public class RenderHandler
{
    public static final Function<ResourceLocation, TextureAtlasSprite> modelTextureBakeFunc = new Function<ResourceLocation, TextureAtlasSprite>()
    {
        @Nullable
        @Override
        public TextureAtlasSprite apply(@Nullable ResourceLocation input)
        {
            return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(input.toString());
        }
    };
    public static int stencilBuffer;
    private final Random random = new Random();
    private final RenderMatterScannerInfoHandler matterScannerInfoHandler;
    private final RenderParticlesHandler renderParticlesHandler;
    private final RenderWeaponsBeam renderWeaponsBeam;
    private final List<IWorldLastRenderer> customRenderers;
    private final AndroidStatRenderRegistry statRenderRegistry;
    private final StarmapRenderRegistry starmapRenderRegistry;
    private final RenderDialogSystem renderDialogSystem;
    private final AndroidBionicPartRenderRegistry bionicPartRenderRegistry;
    private final WeaponModuleModelRegistry weaponModuleModelRegistry;
    private final PipeRenderManager pipeRenderManager;
    private final DimensionalRiftsRender dimensionalRiftsRender;
    private final SpaceSkyRenderer spaceSkyRenderer;
    private final WeaponRenderHandler weaponRenderHandler = new WeaponRenderHandler();

    //region Weapon Module Renderers
    private final ModuleSniperScopeRender moduleSniperScopeRender = new ModuleSniperScopeRender(weaponRenderHandler);
    private final ModuleHoloSightsRender moduleHoloSightsRender = new ModuleHoloSightsRender(weaponRenderHandler);
    //endregion
    //region Weapon Layers
    private final WeaponLayerAmmoRender weaponLayerAmmoRender = new WeaponLayerAmmoRender();
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
    private static ItemRenderPlasmaShotgun renderPlasmaShotgun;
    private static ItemRendererIonSniper rendererIonSniper;
    //endregion
    //region Entity Renderers
    private EntityRendererRougeAndroid<EntityMeleeRougeAndroidMob> rendererRougeAndroid;
    public EntityRendererRougeAndroid rendererRougeAndroidHologram;
    private EntityRendererMadScientist rendererMadScientist;
    private EntityRendererFailedCow rendererFailedCow;
    private EntityRendererFailedChicken rendererFailedChicken;
    private EntityRendererFailedPig rendererFailedPig;
    private EntityRendererFailedSheep rendererFailedSheep;
    private EntityRendererPhaserFire rendererPhaserFire;
    private EntityRendererRangedRougeAndroid rendererRangedRougeAndroid;
    private EntityRendererMutantScientist rendererMutantScientist;
    private EntityRendererDrone rendererDrone;
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
    private TileEntityRendererContractMarket tileEntityRendererContractMarket;
    //endregion
    //region Models
    public ModelTritaniumArmor modelTritaniumArmor;
    public ModelTritaniumArmor modelTritaniumArmorFeet;
    public ModelBiped modelMeleeRogueAndroidParts;
    public ModelBiped modelRangedRogueAndroidParts;
    public IFlexibleBakedModel doubleHelixModel;
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
        bionicPartRenderRegistry = new AndroidBionicPartRenderRegistry();
        weaponModuleModelRegistry = new WeaponModuleModelRegistry();
        pipeRenderManager = new PipeRenderManager();
        dimensionalRiftsRender = new DimensionalRiftsRender();
        spaceSkyRenderer = new SpaceSkyRenderer();

        addCustomRenderer(matterScannerInfoHandler);
        addCustomRenderer(renderParticlesHandler);
        addCustomRenderer(renderWeaponsBeam);
        addCustomRenderer(renderDialogSystem);
        addCustomRenderer(dimensionalRiftsRender);

        OBJLoader.instance.addDomain(Reference.MOD_ID);

        MinecraftForge.EVENT_BUS.register(pipeRenderManager);
        MinecraftForge.EVENT_BUS.register(weaponRenderHandler);
        if(Minecraft.getMinecraft().getFramebuffer().enableStencil())
        {
            stencilBuffer = MinecraftForgeClient.reserveStencilBit();
        }
    }

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event)
    {
        for (IWorldLastRenderer renderer : customRenderers) {
            renderer.onRenderWorldLast(this, event);
        }
        for (IBioticStat stat : MatterOverdrive.statRegistry.getStats())
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
        tileEntityRendererContractMarket = new TileEntityRendererContractMarket();

        configHandler.subscribe(tileEntityRendererAndroidStation);
        configHandler.subscribe(tileEntityRendererWeaponStation);
    }

    @SubscribeEvent
    public void onPlayerRenderPost(RenderPlayerEvent.Post event)
    {
        //GL11.glEnable(GL11.GL_LIGHTING);
        //GL11.glColor3f(1, 1, 1);

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
                            GlStateManager.pushMatrix();
                            renderer.renderPart(part, androidPlayer, event.renderer, event.partialRenderTick);
                            GlStateManager.popMatrix();
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

    @SubscribeEvent
    public void onPlayerRenderPre(RenderPlayerEvent.Pre event)
    {
        //GL11.glEnable(GL11.GL_LIGHTING);
        //GL11.glColor3f(1, 1, 1);

        AndroidPlayer androidPlayer = AndroidPlayer.get(event.entityPlayer);
        if (androidPlayer != null && androidPlayer.isAndroid() && !event.entityPlayer.isInvisible()) {
            for (int i = 0; i < 5; i++)
            {
                ItemStack part = androidPlayer.getStackInSlot(i);
                if (part != null && part.getItem() instanceof IBionicPart)
                {
                    IBionicPartRenderer renderer = bionicPartRenderRegistry.getRenderer(((IBionicPart) part.getItem()).getClass());
                    if (renderer != null) {
                        renderer.affectPlayerRenderer(part, androidPlayer, event.renderer, event.partialRenderTick);
                    }
                }
            }
        }
    }

    public void registerWeaponModuleRenders()
    {
        weaponRenderHandler.addModuleRender(WeaponModuleSniperScope.class,moduleSniperScopeRender);
        weaponRenderHandler.addModuleRender(WeaponModuleHoloSights.class,moduleHoloSightsRender);
    }

    public void registerWeaponLayers()
    {
        weaponRenderHandler.addWeaponLayer(weaponLayerAmmoRender);
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
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMachineContractMarket.class,tileEntityRendererContractMarket);
    }

    public void createItemRenderers()
    {
        rendererPhaser = new ItemRendererPhaser();
        rendererPhaserRifle = new ItemRendererPhaserRifle();
        rendererOmniTool = new ItemRendererOmniTool();
        renderPlasmaShotgun = new ItemRenderPlasmaShotgun();
        rendererIonSniper = new ItemRendererIonSniper();
    }

    public void bakeItemModels()
    {
        weaponRenderHandler.onModelBake(Minecraft.getMinecraft().getTextureMapBlocks(),this);
        rendererPhaser.bakeModel();
        rendererPhaserRifle.bakeModel();
        rendererOmniTool.bakeModel();
        rendererIonSniper.bakeModel();
        renderPlasmaShotgun.bakeModel();
    }

    public void registerModelTextures(TextureMap textureMap,OBJModel model)
    {
        model.getTextures().forEach(textureMap::registerSprite);
    }

    public OBJModel getObjModel(ResourceLocation location,ImmutableMap<String,String> customOptions)
    {
        try
        {
            OBJModel model = (OBJModel)OBJLoader.instance.loadModel(location);
            model = (OBJModel)model.process(customOptions);
            return model;
        } catch (IOException e)
        {
            MOLog.log(Level.ERROR,e,"There was a problem while baking %s model",location.getResourcePath());
        }
        return null;
    }

    @SubscribeEvent
    public void onModelBake(ModelBakeEvent event)
    {
        event.modelRegistry.putObject(new ModelResourceLocation(MatterOverdriveItems.phaser.getRegistryName(),"inventory"),rendererPhaser);
        event.modelRegistry.putObject(new ModelResourceLocation(MatterOverdriveItems.phaserRifle.getRegistryName(),"inventory"),rendererPhaserRifle);
        event.modelRegistry.putObject(new ModelResourceLocation(MatterOverdriveItems.omniTool.getRegistryName(),"inventory"),rendererOmniTool);
        event.modelRegistry.putObject(new ModelResourceLocation(MatterOverdriveItems.ionSniper.getRegistryName(),"inventory"),rendererIonSniper);
        event.modelRegistry.putObject(new ModelResourceLocation(MatterOverdriveItems.plasmaShotgun.getRegistryName(),"inventory"),renderPlasmaShotgun);

        bakeItemModels();
    }

    @SubscribeEvent
    public void onTextureStich(TextureStitchEvent.Pre event)
    {
        if (event.map == Minecraft.getMinecraft().getTextureMapBlocks())
        {
            weaponRenderHandler.onTextureStich(Minecraft.getMinecraft().getTextureMapBlocks(), this);
        }
    }

    public static void registerItemRendererVarients()
    {
        regItemRenderVer(MatterOverdriveItems.item_upgrade,"upgrade",ItemUpgrade.subItemNames);
        regItemRenderVer(MatterOverdriveItems.weapon_module_barrel,"barrel",WeaponModuleBarrel.names);
        regItemRenderVer(MatterOverdriveItems.isolinear_circuit,"isolinear_circuit",IsolinearCircuit.subItemNames);
        regItemRenderVer(MatterOverdriveItems.androidPill,"android_pill", AndroidPill.names);
        regItemRenderVer(MatterOverdriveItems.security_protocol,"security_protocol",SecurityProtocol.types);
        regItemRenderVer(MatterOverdriveItems.androidParts,"rogue_android_part",RougeAndroidParts.names);
        regItemRenderVer(MatterOverdriveItems.androidParts,"weapon_module_color",WeaponModuleColor.names);
    }

    private static void regItemRenderVer(Item item,String name,String[] subNames)
    {
        for (String subName : subNames)
        {
            ModelBakery.registerItemVariants(item, new ResourceLocation(Reference.MOD_ID, name + "_" + subName));
        }
    }

    public void registerItemRenderers()
    {
        regItemRender(MatterOverdriveItems.dataPad);
        regItemRender(MatterOverdriveItems.scoutShip);
        regItemRender(MatterOverdriveItems.buildingBase);
        regItemRender(MatterOverdriveItems.buildingMatterExtractor);
        regItemRender(MatterOverdriveItems.buildingResidential);
        regItemRender(MatterOverdriveItems.buildingShipHangar);
        regItemRender(MatterOverdriveItems.contract);
        regItemRender(MatterOverdriveItems.dilithium_ctystal);
        regItemRender(MatterOverdriveItems.earl_gray_tea);
        regItemRender(MatterOverdriveItems.emergency_ration);
        regItemRender(MatterOverdriveItems.forceFieldEmitter);
        regItemRender(MatterOverdriveItems.h_compensator);
        regItemRender(MatterOverdriveItems.integration_matrix);
        regItemRender(MatterOverdriveItems.machine_casing);
        regItemRender(MatterOverdriveItems.me_conversion_matrix);
        regItemRender(MatterOverdriveItems.plasmaCore);
        regItemRender(MatterOverdriveItems.portableDecomposer);
        regItemRender(MatterOverdriveItems.romulan_ale);
        regItemRender(MatterOverdriveItems.s_magnet);
        regItemRender(MatterOverdriveItems.shipFactory);
        regItemRender(MatterOverdriveItems.sniperScope);
        regItemRender(MatterOverdriveItems.tritaniumSpine);
        regItemRender(MatterOverdriveItems.tritaniumChestplate);
        regItemRender(MatterOverdriveItems.tritanium_dust);
        regItemRender(MatterOverdriveItems.tritaniumHelemet);
        regItemRender(MatterOverdriveItems.tritaniumHoe);
        regItemRender(MatterOverdriveItems.tritanium_nugget);
        regItemRender(MatterOverdriveItems.tritaniumLeggings);
        regItemRender(MatterOverdriveItems.tritaniumPickaxe);
        regItemRender(MatterOverdriveItems.tritanium_plate);
        regItemRender(MatterOverdriveItems.tritaniumSword);
        regItemRender(MatterOverdriveItems.wrench);
        regItemRender(MatterOverdriveItems.battery);
        regItemRender(MatterOverdriveItems.hc_battery);
        regItemRender(MatterOverdriveItems.creative_battery);
        regItemRender(MatterOverdriveItems.energyPack);
        regItemRender(MatterOverdriveItems.matterContainer);
        regItemRender(MatterOverdriveItems.matterContainerFull);
        regItemRender(MatterOverdriveItems.pattern_drive);
        //regItemRender(MatterOverdriveItems.creativePatternDrive);
        regItemRender(MatterOverdriveItems.spacetime_equalizer);
        regItemRender(MatterOverdriveItems.item_upgrade,"upgrade",ItemUpgrade.subItemNames);
        regItemRender(MatterOverdriveItems.weapon_module_color,"weapon_module_color", WeaponModuleColor.names);
        regItemRender(MatterOverdriveItems.weapon_module_barrel,"barrel",WeaponModuleBarrel.names);
        regItemRender(MatterOverdriveItems.isolinear_circuit,"isolinear_circuit",IsolinearCircuit.subItemNames);
        regItemRender(MatterOverdriveItems.matter_dust);
        regItemRender(MatterOverdriveItems.matter_dust_refined);
        regItemRender(MatterOverdriveItems.androidPill,"android_pill", AndroidPill.names);
        regItemRender(MatterOverdriveItems.security_protocol,"security_protocol",SecurityProtocol.types);
        regItemRender(MatterOverdriveItems.androidParts,"rogue_android_part",RougeAndroidParts.names);
        regItemRender(MatterOverdriveItems.tritanium_ingot);
        regItemRender(MatterOverdriveItems.transportFlashDrive);
        regItemRender(MatterOverdriveItems.networkFlashDrive);
        regItemRender(MatterOverdriveItems.weaponHandle);
        regItemRender(MatterOverdriveItems.weaponReceiver);
        regItemRender(MatterOverdriveItems.matter_scanner);
        regItemRender(MatterOverdriveItems.phaser);
        regItemRender(MatterOverdriveItems.phaserRifle);
        regItemRender(MatterOverdriveItems.plasmaShotgun);
        regItemRender(MatterOverdriveItems.omniTool);
        regItemRender(MatterOverdriveItems.ionSniper);
        regItemRender(MatterOverdriveItems.omniTool);
        regItemRender(MatterOverdriveItems.plasmaShotgun);
        regItemRender(MatterOverdriveItems.colonizerShip);
        regItemRender(MatterOverdriveItems.tritaniumBoots);
        regItemRender(MatterOverdriveItems.buildingPowerGenerator);
        regItemRender(MatterOverdriveItems.weaponModuleRicochet);

        regItemRender(MatterOverdriveBlocks.weapon_station);
        regItemRender(MatterOverdriveBlocks.androidStation);
        regItemRender(MatterOverdriveBlocks.replicator);
        regItemRender(MatterOverdriveBlocks.decomposer);
        regItemRender(MatterOverdriveBlocks.recycler);
        regItemRender(MatterOverdriveBlocks.matter_analyzer);
        regItemRender(MatterOverdriveBlocks.transporter);
        regItemRender(MatterOverdriveBlocks.network_router);
        regItemRender(MatterOverdriveBlocks.network_switch);
        regItemRender(MatterOverdriveBlocks.fusion_reactor_coil);
        regItemRender(MatterOverdriveBlocks.machine_hull);
        regItemRender(MatterOverdriveBlocks.fusionReactorIO);
        regItemRender(MatterOverdriveBlocks.dilithium_ore);
        regItemRender(MatterOverdriveBlocks.tritaniumOre);
        regItemRender(MatterOverdriveBlocks.tritanium_block);
        regItemRender(MatterOverdriveBlocks.starMap);
        regItemRender(MatterOverdriveBlocks.solar_panel);
        regItemRender(MatterOverdriveBlocks.matter_pipe);
        regItemRender(MatterOverdriveBlocks.heavy_matter_pipe);
        regItemRender(MatterOverdriveBlocks.network_pipe);
        regItemRender(MatterOverdriveBlocks.spacetimeAccelerator);
        regItemRender(MatterOverdriveBlocks.forceGlass);
        regItemRender(MatterOverdriveBlocks.pattern_monitor);
        regItemRender(MatterOverdriveBlocks.holoSign);
        regItemRender(MatterOverdriveBlocks.pattern_storage);
        regItemRender(MatterOverdriveBlocks.inscriber);
        regItemRender(MatterOverdriveBlocks.gravitational_anomaly);
        regItemRender(MatterOverdriveBlocks.fusion_reactor_controller);
        regItemRender(MatterOverdriveBlocks.pylon);
        regItemRender(MatterOverdriveBlocks.tritaniumCrate);
        regItemRender(MatterOverdriveBlocks.tritaniumCrateYellow);

        regItemRender(MatterOverdriveBlocks.decorative_stripes);
        regItemRender(MatterOverdriveBlocks.decorative_coils);
        regItemRender(MatterOverdriveBlocks.decorative_clean);
        regItemRender(MatterOverdriveBlocks.decorative_vent_dark);
        regItemRender(MatterOverdriveBlocks.decorative_vent_bright);
        regItemRender(MatterOverdriveBlocks.decorative_holo_matrix);
        regItemRender(MatterOverdriveBlocks.decorative_tritanium_plate);
        regItemRender(MatterOverdriveBlocks.decorative_carbon_fiber_plate);
        regItemRender(MatterOverdriveBlocks.decorative_floor_tiles);
        regItemRender(MatterOverdriveBlocks.decorative_floor_tiles_green);
        regItemRender(MatterOverdriveBlocks.decorative_floor_noise);
        regItemRender(MatterOverdriveBlocks.decorative_tritanium_plate_stripe);
        regItemRender(MatterOverdriveBlocks.decorative_floot_tile_white);
        regItemRender(MatterOverdriveBlocks.decorative_white_plate);
        regItemRender(MatterOverdriveBlocks.decorative_tritanium_plate_colored,16);
        regItemRender(MatterOverdriveBlocks.decorative_engine_exhaust_plasma);
        regItemRender(MatterOverdriveBlocks.decorative_beams,2);
        regItemRender(MatterOverdriveBlocks.decorative_matter_tube,2);
        regItemRender(MatterOverdriveBlocks.decorative_tritanium_lamp,2);
        regItemRender(MatterOverdriveBlocks.decorative_separator,2);
    }

    private <T extends Item> void regItemRender(T item,String name,String[] subNames)
    {
        for (int i = 0;i < subNames.length;i++)
        {
            regItemRender(item,i,name+"_"+subNames[i]);
        }
    }

    private <T extends Item> void regItemRender(T item,int meta,String name)
    {
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item,meta,new ModelResourceLocation(Reference.MOD_ID + ":" + name,"inventory"));
    }

    private <T extends Item> void regItemRender(T item,int meta)
    {
        String name = item.getRegistryName();
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item,meta,new ModelResourceLocation(name,"inventory"));
    }

    private <T extends Item> void regItemRender(T item)
    {
        String name = item.getRegistryName();
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item,0,new ModelResourceLocation(name,"inventory"));
    }

    private void regItemRender(Block block,int metaCount)
    {
        String name = block.getRegistryName();
        for (int i = 0;i < metaCount;i++)
        {
            Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(block), i, new ModelResourceLocation(name, "inventory"));
        }
    }

    private void regItemRender(Block block)
    {
        String name = block.getRegistryName();
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(block),0,new ModelResourceLocation(name,"inventory"));
    }

    public void createEntityRenderers(RenderManager renderManager)
    {
        rendererRougeAndroid = new EntityRendererRougeAndroid(new ModelBiped(), 0,false);
        rendererMadScientist = new EntityRendererMadScientist(renderManager);
        rendererFailedPig = new EntityRendererFailedPig(renderManager,new ModelPig(), 0.7F);
        rendererFailedCow = new EntityRendererFailedCow(renderManager,new ModelCow(), 0.7f);
        rendererFailedChicken = new EntityRendererFailedChicken(renderManager,new ModelChicken(), 0.3f);
        rendererFailedSheep = new EntityRendererFailedSheep(renderManager,new ModelSheep2(), 0.7f);
        rendererPhaserFire = new EntityRendererPhaserFire(renderManager);
        rendererRangedRougeAndroid = new EntityRendererRangedRougeAndroid(0);
        rendererRougeAndroidHologram = new EntityRendererRougeAndroid(new ModelBiped(),0,true);
        rendererMutantScientist = new EntityRendererMutantScientist(renderManager,new ModelHulkingScientist(),0,1);
        rendererDrone = new EntityRendererDrone(renderManager,new ModelDrone(),0.5f);
    }

    public void registerEntityRenderers()
    {
        RenderingRegistry.registerEntityRenderingHandler(EntityMeleeRougeAndroidMob.class, rendererRougeAndroid);
        RenderingRegistry.registerEntityRenderingHandler(EntityFailedPig.class,rendererFailedPig);
        RenderingRegistry.registerEntityRenderingHandler(EntityFailedCow.class, rendererFailedCow);
        RenderingRegistry.registerEntityRenderingHandler(EntityFailedChicken.class,rendererFailedChicken);
        RenderingRegistry.registerEntityRenderingHandler(EntityFailedSheep.class,rendererFailedSheep);
        RenderingRegistry.registerEntityRenderingHandler(EntityVillagerMadScientist.class, rendererMadScientist);
        RenderingRegistry.registerEntityRenderingHandler(PlasmaBolt.class, rendererPhaserFire);
        RenderingRegistry.registerEntityRenderingHandler(EntityRangedRogueAndroidMob.class,rendererRangedRougeAndroid);
        RenderingRegistry.registerEntityRenderingHandler(EntityMutantScientist.class,rendererMutantScientist);
        RenderingRegistry.registerEntityRenderingHandler(EntityDrone.class,rendererDrone);
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

    public void createModels()
    {
        modelTritaniumArmor = new ModelTritaniumArmor(0);
        modelTritaniumArmorFeet = new ModelTritaniumArmor(0.5f);
        modelMeleeRogueAndroidParts = new ModelBiped(0);
        modelRangedRogueAndroidParts = new ModelBiped(0, 0, 96, 64);
        try
        {
            IModel model = OBJLoader.instance.loadModel(new ResourceLocation(Reference.PATH_MODEL + "gui/double_helix.obj"));
            doubleHelixModel = model.bake(model.getDefaultState(), Attributes.DEFAULT_BAKED_FORMAT, new Function<ResourceLocation, TextureAtlasSprite>()
            {
                @Nullable
                @Override
                public TextureAtlasSprite apply(@Nullable ResourceLocation input)
                {
                    return Minecraft.getMinecraft().getTextureMapBlocks().registerSprite(input);
                }
            });
        } catch (IOException e)
        {
            e.printStackTrace();
        }
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

    public WeaponModuleModelRegistry getWeaponModuleModelRegistry(){return weaponModuleModelRegistry;}

    public Random getRandom()
    {
        return random;
    }

    public void addCustomRenderer(IWorldLastRenderer renderer)
    {
        customRenderers.add(renderer);
    }

    public SpaceSkyRenderer getSpaceSkyRenderer()
    {
        return spaceSkyRenderer;
    }

    public WeaponRenderHandler getWeaponRenderHandler(){return weaponRenderHandler;}
}
