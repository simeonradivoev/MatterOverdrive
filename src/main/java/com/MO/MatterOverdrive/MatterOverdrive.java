package com.MO.MatterOverdrive;

import java.io.File;

import com.MO.MatterOverdrive.handler.TickHandler;
import com.MO.MatterOverdrive.init.MatterOverdriveWorld;
import com.MO.MatterOverdrive.network.PacketPipeline;

import com.MO.MatterOverdrive.handler.GuiHandler;
import com.MO.MatterOverdrive.handler.MOConfigurationHandler;
import com.MO.MatterOverdrive.init.MatterOverdriveBlocks;
import com.MO.MatterOverdrive.init.MatterOverdriveItems;
import com.MO.MatterOverdrive.init.MatterOverdriveMatter;
import com.MO.MatterOverdrive.proxy.CommonProxy;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import com.MO.MatterOverdrive.matter_network.MatterNetworkRegistry;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION)
public class MatterOverdrive 
{
	@SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
	public static CommonProxy proxy;
	
	public static final MatterOverdriveTab tabMatterOverdrive = new MatterOverdriveTab("tabMatterOverdrive");
	public static final MatterOverdriveTab tabMatterOverdrive_modules = new MatterOverdriveTab("tabMatterOverdrive_modules");
	public static final MatterOverdriveTab tabMatterOverdrive_upgrades = new MatterOverdriveTab("tabMatterOverdrive_upgrades");

    public static TickHandler tickHandler;
	public static MOConfigurationHandler configHandler;
    public static GuiHandler guiHandler;
    public static PacketPipeline packetPipeline;
	@Instance(Reference.MOD_ID)
	public static MatterOverdrive instance;

	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
        guiHandler = new GuiHandler();
		packetPipeline = new PacketPipeline();
        configHandler = new MOConfigurationHandler(new File(event.getModConfigurationDirectory().getAbsolutePath() + File.separator + "MatterOverdrive" + File.separator + Reference.MOD_NAME + ".cfg"));
        tickHandler = new TickHandler(configHandler);
        FMLCommonHandler.instance().bus().register(tickHandler);
        MatterOverdriveBlocks.init(event);
		MatterOverdriveItems.init(event);
        MatterOverdriveWorld.init();
		MatterOverdriveBlocks.register(event);
		MatterOverdriveItems.register(event);
        MatterOverdriveWorld.register();
		MatterNetworkRegistry.register();
        packetPipeline.registerPackets();
		UpdateTabs();
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event)
	{
        configHandler.init();
        guiHandler.register();
		NetworkRegistry.INSTANCE.registerGuiHandler(this, guiHandler);
		MatterOverdriveItems.addToDungons();
		proxy.registerProxies();
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
        MatterOverdriveMatter.registerBasic(configHandler);
        MatterOverdriveMatter.init(configHandler);
        MatterOverdriveMatter.registerComplex(configHandler);
	}

	private void UpdateTabs()
	{
		tabMatterOverdrive.item = MatterOverdriveItems.matter_scanner;
		tabMatterOverdrive_modules.item = MatterOverdriveItems.weapon_module_color;
		tabMatterOverdrive_upgrades.item = MatterOverdriveItems.item_upgrade;
	}
	
}
