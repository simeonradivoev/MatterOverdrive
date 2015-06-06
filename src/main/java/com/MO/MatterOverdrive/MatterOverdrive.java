package com.MO.MatterOverdrive;

import java.io.File;
import java.io.IOException;

import com.MO.MatterOverdrive.commands.AndoidCommands;
import com.MO.MatterOverdrive.handler.*;
import com.MO.MatterOverdrive.handler.thread.RegisterItemsFromRecipes;
import com.MO.MatterOverdrive.init.*;
import com.MO.MatterOverdrive.network.PacketPipeline;

import com.MO.MatterOverdrive.proxy.CommonProxy;

import cpw.mods.fml.client.IModGuiFactory;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.network.NetworkRegistry;
import com.MO.MatterOverdrive.matter_network.MatterNetworkRegistry;
import net.minecraftforge.common.MinecraftForge;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION, guiFactory  = Reference.GUI_FACTORY_CLASS)
public class MatterOverdrive 
{
	@SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
	public static CommonProxy proxy;
	
	public static final MatterOverdriveTab tabMatterOverdrive = new MatterOverdriveTab("tabMatterOverdrive");
	public static final MatterOverdriveTab tabMatterOverdrive_modules = new MatterOverdriveTab("tabMatterOverdrive_modules");
	public static final MatterOverdriveTab tabMatterOverdrive_upgrades = new MatterOverdriveTab("tabMatterOverdrive_upgrades");

    public static TickHandler tickHandler;
    public static PlayerEventHandler playerEventHandler;
	public static MOConfigurationHandler configHandler;
    public static CraftingHandler craftingHandler;
    public static GuiHandler guiHandler;
    public static PacketPipeline packetPipeline;
	@Instance(Reference.MOD_ID)
	public static MatterOverdrive instance;
	public static String registryPath;
	public static MatterOverdriveWorld moWorld;
	public static EntityHandler entityHandler;

	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		registryPath = event.getModConfigurationDirectory().getAbsolutePath() + File.separator + "MatterOverdrive" + File.separator + "Registry" + ".reg";
        guiHandler = new GuiHandler();
        playerEventHandler = new PlayerEventHandler();
        craftingHandler = new CraftingHandler();
		packetPipeline = new PacketPipeline();
		entityHandler = new EntityHandler();
        configHandler = new MOConfigurationHandler(new File(event.getModConfigurationDirectory().getAbsolutePath() + File.separator + "MatterOverdrive" + File.separator + Reference.MOD_NAME + ".cfg"));
		FMLCommonHandler.instance().bus().register(configHandler);
		tickHandler = new TickHandler(configHandler,playerEventHandler);
        FMLCommonHandler.instance().bus().register(tickHandler);
        FMLCommonHandler.instance().bus().register(playerEventHandler);
        FMLCommonHandler.instance().bus().register(craftingHandler);
        MatterOverdriveBlocks.init(event);
		MatterOverdriveItems.init(event);
		moWorld = new MatterOverdriveWorld(configHandler);
		MatterOverdriveEntities.init(event);
		MatterOverdriveBlocks.register(event);
		MatterOverdriveItems.register(event);
		MatterOverdriveEntities.register(event);
		moWorld.register();
		MatterNetworkRegistry.register();
        packetPipeline.registerPackets();
		AndroidStatRegistry.init();
        AndroidStatRegistry.registerAll();

		MatterOverdriveMatter.init(configHandler);
		MatterOverdriveMatter.registerBlacklistFromConfig(configHandler);
		MatterOverdriveMatter.registerFromConfig(configHandler);
		MatterOverdriveMatter.registerBasic(configHandler);
		UpdateTabs();
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event)
	{
        configHandler.init();
        guiHandler.register(event.getSide());
		NetworkRegistry.INSTANCE.registerGuiHandler(this, guiHandler);
		MinecraftForge.EVENT_BUS.register(entityHandler);
		MatterOverdriveItems.addToDungons();
		proxy.registerProxies();
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{

	}


	@EventHandler
	public void serverStarting(FMLServerStartingEvent event)
	{
		event.registerServerCommand(new AndoidCommands());
	}

    @EventHandler
    public void serverStart(FMLServerStartedEvent event)
    {
        if (configHandler.getBool(MOConfigurationHandler.KEY_AUTOMATIC_RECIPE_CALCULATION,MOConfigurationHandler.CATEGORY_MATTER,true))
		{
            try {
                if (MatterRegistry.needsCalculation(registryPath))
                {
                    Thread registerItemsThread = new Thread(new RegisterItemsFromRecipes(registryPath));
                    registerItemsThread.run();
                }else
                {
                    MatterRegistry.loadFromFile(registryPath);
                }
            } catch (Exception e)
            {
                e.printStackTrace();
                Thread registerItemsThread = new Thread(new RegisterItemsFromRecipes(registryPath));
                registerItemsThread.run();
            }

			tickHandler.onServerStart(event);
        }
    }

	private void UpdateTabs()
	{
		tabMatterOverdrive.item = MatterOverdriveItems.matter_scanner;
		tabMatterOverdrive_modules.item = MatterOverdriveItems.weapon_module_color;
		tabMatterOverdrive_upgrades.item = MatterOverdriveItems.item_upgrade;
	}
	
}
