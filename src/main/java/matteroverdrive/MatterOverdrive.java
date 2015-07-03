package matteroverdrive;

import java.io.File;

import matteroverdrive.commands.AndoidCommands;
import matteroverdrive.compat.MatterOverdriveCompat;
import matteroverdrive.handler.thread.RegisterItemsFromRecipes;
import matteroverdrive.network.PacketPipeline;

import matteroverdrive.proxy.CommonProxy;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.network.NetworkRegistry;
import matteroverdrive.matter_network.MatterNetworkRegistry;
import matteroverdrive.handler.*;
import matteroverdrive.init.*;
import net.minecraftforge.common.MinecraftForge;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION, guiFactory = Reference.GUI_FACTORY_CLASS, dependencies = Reference.DEPEDNENCIES)
public class MatterOverdrive 
{
	@SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
	public static CommonProxy proxy;
	
	public static final MatterOverdriveTab tabMatterOverdrive = new MatterOverdriveTab("tabMatterOverdrive");
	public static final MatterOverdriveTab tabMatterOverdrive_modules = new MatterOverdriveTab("tabMatterOverdrive_modules");
	public static final MatterOverdriveTab tabMatterOverdrive_upgrades = new MatterOverdriveTab("tabMatterOverdrive_upgrades");

    public static TickHandler tickHandler;
    public static PlayerEventHandler playerEventHandler;
	public static ConfigurationHandler configHandler;
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
        configHandler = new ConfigurationHandler(new File(event.getModConfigurationDirectory().getAbsolutePath() + File.separator + "MatterOverdrive" + File.separator + Reference.MOD_NAME + ".cfg"));


		FMLCommonHandler.instance().bus().register(configHandler);
		tickHandler = new TickHandler(configHandler,playerEventHandler);
        FMLCommonHandler.instance().bus().register(tickHandler);
        FMLCommonHandler.instance().bus().register(playerEventHandler);
        FMLCommonHandler.instance().bus().register(craftingHandler);
        MatterOverdriveBlocks.init(event);
		MatterOverdriveItems.init(event);
		moWorld = new MatterOverdriveWorld(configHandler);
		MatterOverdriveEntities.init(event, configHandler);
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

		proxy.registerCompatModules();
		MatterOverdriveCompat.preInit(event);
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event)
	{
        guiHandler.register(event.getSide());
		NetworkRegistry.INSTANCE.registerGuiHandler(this, guiHandler);
		MinecraftForge.EVENT_BUS.register(entityHandler);
		MatterOverdriveItems.addToDungons();
		proxy.registerProxies();
		configHandler.init();

		MatterOverdriveCompat.init(event);
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		MatterOverdriveCompat.postInit(event);
	}


	@EventHandler
	public void serverStarting(FMLServerStartingEvent event)
	{
		event.registerServerCommand(new AndoidCommands());
	}

    @EventHandler
    public void serverStart(FMLServerStartedEvent event)
    {
        if (configHandler.getBool(ConfigurationHandler.KEY_AUTOMATIC_RECIPE_CALCULATION, ConfigurationHandler.CATEGORY_MATTER,true))
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
