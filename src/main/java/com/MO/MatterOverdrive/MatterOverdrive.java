package com.MO.MatterOverdrive;

import java.io.File;

import com.MO.MatterOverdrive.init.MatterOverdriveWorld;
import com.MO.MatterOverdrive.network.PacketPipeline;

import com.MO.MatterOverdrive.handler.GuiHandler;
import com.MO.MatterOverdrive.handler.MOConfigurationHandler;
import com.MO.MatterOverdrive.init.MatterOverdriveBlocks;
import com.MO.MatterOverdrive.init.MatterOverdriveItems;
import com.MO.MatterOverdrive.init.MatterOverdriveMatter;
import com.MO.MatterOverdrive.proxy.CommonProxy;
import com.MO.MatterOverdrive.tile.TileEntityMachineReplicator;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION)
public class MatterOverdrive 
{
	@SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
	public static CommonProxy proxy;
	
	public static final MatterOverdriveTab tabMatterOverdrive = new MatterOverdriveTab("tabMatterOverdrive");
	public static MOConfigurationHandler configHandler;
    public static final PacketPipeline packetPipeline = new PacketPipeline();
	@Instance(Reference.MOD_ID)
	public static MatterOverdrive instance;
	
	////////////////////// GUI ///////////////////////////////
	public static final byte guiIDReplicator = 0;
	public static final byte guiIDDecomposer = 1;
    public static final byte guiIDMatterScanner = 2;
    public static final byte guiNetworkController = 3;
    public static final byte guiMatterAnalyzer = 4;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		configHandler.init(new File(event.getModConfigurationDirectory().getAbsolutePath() + File.separator + "MatterOverdrive" + File.separator + Reference.MOD_NAME + ".cfg"));
		MatterOverdriveBlocks.init();
		MatterOverdriveItems.init();
        MatterOverdriveWorld.init();
		MatterOverdriveBlocks.register();
		MatterOverdriveItems.register();
        MatterOverdriveWorld.register();
        packetPipeline.init();
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		GameRegistry.registerTileEntity(TileEntityMachineReplicator.class, "Replicator");
		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
		proxy.registerProxies();
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
        packetPipeline.postInit();
        MatterOverdriveMatter.registerBasic(configHandler);
        MatterOverdriveMatter.init(configHandler);
        MatterOverdriveMatter.registerComplex(configHandler);
	}
	
}
