package com.MO.MatterOverdrive.proxy;

import com.MO.MatterOverdrive.client.RenderHandler;
import com.MO.MatterOverdrive.client.render.HoloIcons;
import com.MO.MatterOverdrive.client.render.block.MOBlockRenderer;
import com.MO.MatterOverdrive.client.render.block.RendererBlockGravitationalStabilizer;
import com.MO.MatterOverdrive.data.HoloIcon;
import com.MO.MatterOverdrive.gui.GuiAndroidHud;
import com.MO.MatterOverdrive.gui.GuiStarMap;
import com.MO.MatterOverdrive.handler.KeyHandler;
import com.MO.MatterOverdrive.handler.TooltipHandler;
import com.MO.MatterOverdrive.init.MatterOverdriveIcons;

import com.MO.MatterOverdrive.starmap.GalaxyClient;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy
{
    public static RenderHandler renderHandler;
    public static KeyHandler keyHandler;
    public static GuiAndroidHud androidHud;
    public static HoloIcons holoIcons;

    @Override
	public void registerProxies()
	{
        super.registerProxies();

        renderHandler = new RenderHandler(Minecraft.getMinecraft().theWorld,Minecraft.getMinecraft().getTextureManager());
        androidHud = new GuiAndroidHud(Minecraft.getMinecraft());
        keyHandler = new KeyHandler();
        holoIcons = new HoloIcons();

        registerSubscribtions();

        //region Render Handler Functions
        renderHandler.createBlockRenderers();
        renderHandler.createTileEntityRenderers();
        renderHandler.createItemRenderers();
        renderHandler.createEntityRenderers();
        renderHandler.registerBlockRenderers();
        renderHandler.registerTileEntitySpecialRenderers();
        renderHandler.registerItemRenderers();
        renderHandler.registerEntityRenderers();
        //endregion
	}

    private void registerSubscribtions()
    {
        FMLCommonHandler.instance().bus().register(keyHandler);
        MinecraftForge.EVENT_BUS.register(GalaxyClient.getInstance());
        MinecraftForge.EVENT_BUS.register(new MatterOverdriveIcons());
        MinecraftForge.EVENT_BUS.register(renderHandler);
        MinecraftForge.EVENT_BUS.register(new TooltipHandler());
        MinecraftForge.EVENT_BUS.register(androidHud);
        FMLCommonHandler.instance().bus().register(renderHandler);
    }

    @Override
    public EntityPlayer getPlayerEntity(MessageContext ctx)
    {
        return (ctx.side.isClient() ? Minecraft.getMinecraft().thePlayer : super.getPlayerEntity(ctx));
    }

}
