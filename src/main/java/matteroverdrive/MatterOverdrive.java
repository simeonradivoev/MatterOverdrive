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

package matteroverdrive;

import matteroverdrive.commands.*;
import matteroverdrive.compat.MatterOverdriveCompat;
import matteroverdrive.entity.EntityVillagerMadScientist;
import matteroverdrive.entity.android_player.AndroidPlayer;
import matteroverdrive.entity.player.MOExtendedProperties;
import matteroverdrive.handler.*;
import matteroverdrive.handler.dialog.DialogAssembler;
import matteroverdrive.handler.dialog.DialogRegistry;
import matteroverdrive.handler.matter_network.FluidNetworkHandler;
import matteroverdrive.handler.matter_network.MatterNetworkHandler;
import matteroverdrive.handler.quest.QuestAssembler;
import matteroverdrive.handler.quest.Quests;
import matteroverdrive.imc.MOIMCHandler;
import matteroverdrive.init.*;
import matteroverdrive.matter_network.MatterNetworkRegistry;
import matteroverdrive.network.PacketPipeline;
import matteroverdrive.proxy.CommonProxy;
import matteroverdrive.util.*;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION, guiFactory = Reference.GUI_FACTORY_CLASS, dependencies = Reference.DEPEDNENCIES)
public class MatterOverdrive
{
	@Mod.Instance(Reference.MOD_ID)
	public static MatterOverdrive instance;

	@SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
	public static CommonProxy proxy;

	public static final MatterOverdriveTab tabMatterOverdrive = new MatterOverdriveTab("tabMatterOverdrive");
	public static final MatterOverdriveTab tabMatterOverdrive_modules = new MatterOverdriveTab("tabMatterOverdrive_modules");
	public static final MatterOverdriveTab tabMatterOverdrive_upgrades = new MatterOverdriveTab("tabMatterOverdrive_upgrades");
	public static final MatterOverdriveTab tabMatterOverdrive_food = new MatterOverdriveTab("tabMatterOverdrive_food");
	public static final MatterOverdriveTab tabMatterOverdrive_ships = new MatterOverdriveTab("tabMatterOverdrive_ships");
	public static final MatterOverdriveTab tabMatterOverdrive_buildings = new MatterOverdriveTab("tabMatterOverdrive_buildings");
	public static final MatterOverdriveTab tabMatterOverdrive_decorative = new MatterOverdriveTab("tabMatterOverdrive_decorative");
	public static final MatterOverdriveTab tabMatterOverdrive_androidParts = new MatterOverdriveTab("tabMatterOverdrive_androidParts");


	public static final ExecutorService threadPool = Executors.newFixedThreadPool(2);
	public static TickHandler tickHandler;
	public static PlayerEventHandler playerEventHandler;
	public static ConfigurationHandler configHandler;
	public static GuiHandler guiHandler;
	public static PacketPipeline packetPipeline;
	public static BucketHandler bucketHandler;
	public static MatterOverdriveWorld moWorld;
	public static EntityHandler entityHandler;
	public static MatterRegistry matterRegistry;
	public static AndroidStatRegistry statRegistry;
	public static DialogRegistry dialogRegistry;
	public static MatterRegistrationHandler matterRegistrationHandler;
	public static WeaponFactory weaponFactory;
	public static AndroidPartsFactory androidPartsFactory;
	public static Quests quests;
	public static QuestFactory questFactory;
	public static DialogFactory dialogFactory;
	public static BlockHandler blockHandler;
	public static QuestAssembler questAssembler;
	public static DialogAssembler dialogAssembler;
	public static MatterNetworkHandler matterNetworkHandler;
	public static FluidNetworkHandler fluidNetworkHandler;

//	Content
	public static final MatterOverdriveItems items = new MatterOverdriveItems();
	public static final MatterOverdriveBlocks blocks = new MatterOverdriveBlocks();

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		checkJavaVersion();

		AndroidPlayer.register();
		MOExtendedProperties.register();
		matterRegistry = new MatterRegistry();
		statRegistry = new AndroidStatRegistry();
		dialogRegistry = new DialogRegistry();
		guiHandler = new GuiHandler();
		packetPipeline = new PacketPipeline();
		entityHandler = new EntityHandler();
		configHandler = new ConfigurationHandler(new File(event.getModConfigurationDirectory().getAbsolutePath() + File.separator + "MatterOverdrive" + File.separator + Reference.MOD_NAME + ".cfg"));
		playerEventHandler = new PlayerEventHandler(configHandler);
		bucketHandler = new BucketHandler();
		matterRegistrationHandler = new MatterRegistrationHandler();
		weaponFactory = new WeaponFactory();
		androidPartsFactory = new AndroidPartsFactory();
		quests = new Quests();
		questFactory = new QuestFactory();
		dialogFactory = new DialogFactory(dialogRegistry);
		blockHandler = new BlockHandler();
		questAssembler = new QuestAssembler();
		dialogAssembler = new DialogAssembler();
		matterNetworkHandler = new MatterNetworkHandler();
		fluidNetworkHandler = new FluidNetworkHandler();

		items.init();
		MatterOverdriveFluids.init(event);
		blocks.init();
		MatterOverdriveBioticStats.init();
		MatterOverdriveDialogs.init(configHandler, dialogRegistry);
		MatterOverdriveQuests.init();
		MatterOverdriveQuests.register(quests);
		MatterOverdriveSounds.register();
		EntityVillagerMadScientist.registerDialogMessages(dialogRegistry, event.getSide());
		MatterOverdriveCapabilities.init();

		MinecraftForge.EVENT_BUS.register(matterRegistrationHandler);
		MinecraftForge.EVENT_BUS.register(configHandler);
		tickHandler = new TickHandler(configHandler, playerEventHandler);
		MinecraftForge.EVENT_BUS.register(tickHandler);
		MinecraftForge.EVENT_BUS.register(playerEventHandler);
		MinecraftForge.EVENT_BUS.register(playerEventHandler);
		MinecraftForge.EVENT_BUS.register(bucketHandler);
		MinecraftForge.EVENT_BUS.register(blockHandler);
		moWorld = new MatterOverdriveWorld(configHandler);
		MatterOverdriveEntities.init(event, configHandler);
		MatterOverdriveEnchantments.init(event, configHandler);
		moWorld.register();
		MatterNetworkRegistry.register();
		packetPipeline.registerPackets();
		MatterOverdriveBioticStats.registerAll(configHandler, statRegistry);
		matterRegistry.preInit(event, configHandler);
		MinecraftForge.EVENT_BUS.register(matterNetworkHandler);
		MinecraftForge.EVENT_BUS.register(fluidNetworkHandler);
		UpdateTabs();

		proxy.preInit(event);

		MatterOverdriveCompat.preInit(event);
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event)
	{
		guiHandler.register(event.getSide());
		NetworkRegistry.INSTANCE.registerGuiHandler(this, guiHandler);
		MinecraftForge.EVENT_BUS.register(entityHandler);
		configHandler.init();
		MatterOverdriveCompat.init(event);

		proxy.init(event);

		MatterOverdriveRecipes.registerBlockRecipes(event);
		MatterOverdriveRecipes.registerItemRecipes(event);
		MatterOverdriveRecipes.registerInscriberRecipes(event);

		weaponFactory.initModules();
		weaponFactory.initWeapons();
		androidPartsFactory.initParts();

		AndroidPlayer.loadConfigs(configHandler);
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		proxy.postInit(event);
		MatterOverdriveCompat.postInit(event);
		MatterOverdriveEntities.register(event);
		items.addToDungons();

		questAssembler.loadQuests(quests);
		questAssembler.loadCustomQuests(quests);
		dialogAssembler.loadDialogs(dialogRegistry);
		dialogAssembler.loadCustomDialogs(dialogRegistry);

		MatterOverdriveMatter.registerBlacklistFromConfig(configHandler);
		MatterOverdriveMatter.registerBasic(configHandler);

		configHandler.postInit();
	}


	@Mod.EventHandler
	public void serverStarting(FMLServerStartingEvent event)
	{
		event.registerServerCommand(new AndoidCommands());
		event.registerServerCommand(new MatterRegistryCommands());
		event.registerServerCommand(new QuestCommands());
		event.registerServerCommand(new SaveWorldToImage());
		event.registerServerCommand(new WorldGenCommands());
		event.registerServerCommand(new DimesionTeleportCommends());
		proxy.getGoogleAnalytics().load();
	}

	@Mod.EventHandler
	public void serverStopping(FMLServerStoppingEvent event)
	{
		proxy.getGoogleAnalytics().unload();
	}

	@Mod.EventHandler
	public void serverStart(FMLServerStartedEvent event)
	{
		//matterRegistrationHandler.serverStart(event);
		tickHandler.onServerStart(event);
	}

	@Mod.EventHandler
	public void imcCallback(FMLInterModComms.IMCEvent event)
	{
		MOIMCHandler.imcCallback(event);
	}

	private void UpdateTabs()
	{
		tabMatterOverdrive.item = items.matter_scanner;
		tabMatterOverdrive_modules.item = items.weapon_module_color;
		tabMatterOverdrive_upgrades.item = items.item_upgrade;
		tabMatterOverdrive_food.item = items.earl_gray_tea;
		tabMatterOverdrive_ships.item = items.colonizerShip;
		tabMatterOverdrive_buildings.item = items.buildingBase;
		tabMatterOverdrive_decorative.item = new ItemBlock(MatterOverdrive.blocks.decorative_stripes);
		tabMatterOverdrive_androidParts.item = items.androidParts;
	}

	private void checkJavaVersion()
	{
		String versionString = System.getProperty("java.version");
		int pos = versionString.indexOf('.');
		pos = versionString.indexOf('.', pos + 1);
		double version = Double.parseDouble(versionString.substring(0, pos));
		if (version < 1.8)
		{
			MOLog.warn("Matter Overdrive only supports Java 8 and above. Please update your Java version. You are currently using: " + version);
			MatterOverdrive.proxy.getGoogleAnalytics().setExceptionHit("Java version lower than 8", true);
		}
	}
}
