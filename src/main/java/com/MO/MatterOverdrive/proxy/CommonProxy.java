package com.MO.MatterOverdrive.proxy;

import com.MO.MatterOverdrive.MatterOverdrive;

import com.MO.MatterOverdrive.handler.TickHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

public class CommonProxy
{
	public void registerProxies()
	{
	}

    public void registerBlockIcons(IIconRegister register)
    {

    }

    public EntityPlayer getPlayerEntity(MessageContext ctx)
    {
        return ctx.getServerHandler().playerEntity;
    }
}
