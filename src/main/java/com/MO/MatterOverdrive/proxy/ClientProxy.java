package com.MO.MatterOverdrive.proxy;

import com.MO.MatterOverdrive.client.render.*;
import com.MO.MatterOverdrive.handler.KeyHandler;
import com.MO.MatterOverdrive.init.MatterOverdriveBlocks;
import com.MO.MatterOverdrive.init.MatterOverdriveIcons;
import com.MO.MatterOverdrive.init.MatterOverdriveItems;
import com.MO.MatterOverdrive.tile.TileEntityMachinePatternStorage;
import com.MO.MatterOverdrive.tile.TileEntityMachineReplicator;
import com.MO.MatterOverdrive.tile.pipes.TileEntityMatterPipe;
import com.MO.MatterOverdrive.tile.pipes.TileEntityNetworkPipe;
import com.MO.MatterOverdrive.tile.pipes.TileEntityPipe;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy
{
    static protected boolean iconsBlocksRegistered = false;
    static protected boolean iconsItemsRegistered = false;

	public void registerProxies()
	{
        FMLCommonHandler.instance().bus().register(new KeyHandler());
        MinecraftForge.EVENT_BUS.register(new MatterOverdriveIcons());

        TileEntityRendererPipe pipeRenderer = new TileEntityRendererPipe();
        TileEntityRendererMatterPipe matter_pipeRenderer = new TileEntityRendererMatterPipe();
        TileEntityRendererNetworkPipe network_pipeRenderer = new TileEntityRendererNetworkPipe();
        TileEntityRendererReplicator replicator_renderer = new TileEntityRendererReplicator();
        TileEntityRendererPatterStorage pattern_storage_renderer = new TileEntityRendererPatterStorage();
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPipe.class, pipeRenderer);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMatterPipe.class, matter_pipeRenderer);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityNetworkPipe.class, network_pipeRenderer);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMachineReplicator.class,replicator_renderer);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMachinePatternStorage.class,pattern_storage_renderer);
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(MatterOverdriveBlocks.matter_pipe), new ItemRendererPipe(matter_pipeRenderer, new TileEntityMatterPipe(), 2));
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(MatterOverdriveBlocks.network_pipe),new ItemRendererPipe(network_pipeRenderer,new TileEntityNetworkPipe(),2));
        MinecraftForgeClient.registerItemRenderer(MatterOverdriveItems.phaser, new ItemRendererPhaser());
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(MatterOverdriveBlocks.replicator),new ItemRendererTileEntityMachine(replicator_renderer,new TileEntityMachineReplicator()));
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(MatterOverdriveBlocks.pattern_storage),new ItemRendererTileEntityMachine(pattern_storage_renderer,new TileEntityMachinePatternStorage()));
        RenderingRegistry.registerBlockHandler(new BlockRendererReplicator());
	}


}
