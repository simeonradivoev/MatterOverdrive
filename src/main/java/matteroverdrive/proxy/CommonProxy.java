package matteroverdrive.proxy;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.compat.MatterOverdriveCompat;
import matteroverdrive.starmap.GalaxyServer;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;

public class CommonProxy
{
	public void registerProxies()
	{
        MinecraftForge.EVENT_BUS.register(GalaxyServer.getInstance());
        FMLCommonHandler.instance().bus().register(GalaxyServer.getInstance());
        MatterOverdrive.configHandler.subscribe(GalaxyServer.getInstance());
        MatterOverdrive.configHandler.subscribe(GalaxyServer.getInstance().getGalaxyGenerator());
	}

    public void registerCompatModules()
    {
        MatterOverdriveCompat.registerModules();
    }

    public void registerBlockIcons(IIconRegister register)
    {

    }

    public EntityPlayer getPlayerEntity(MessageContext ctx)
    {
        return ctx.getServerHandler().playerEntity;
    }
}
