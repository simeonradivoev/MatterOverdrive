package com.MO.MatterOverdrive.init;

import com.MO.MatterOverdrive.Reference;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.event.TextureStitchEvent;

/**
 * Created by Simeon on 3/17/2015.
 */
public class MatterOverdriveIcons
{
    public static boolean isRegistered;
    public static IIcon Base;
    public static IIcon Vent;
    public static IIcon Matter_tank_full;
    public static IIcon Matter_tank_empty;
    public static IIcon Test;
    public static IIcon Transperant;

    @SubscribeEvent
    public void registerTextures(TextureStitchEvent.Pre event)
    {
        System.out.println("Stiching textures");

        if (event.map.getTextureType() == 0)
        {
           MatterOverdriveIcons.init(event.map);
        }
    }

    public static void init(IIconRegister r)
    {
        Vent = register(r, "vent");
        Base = register(r, "base");
        Matter_tank_empty = register(r, "tank_empty");
        Matter_tank_full = register(r, "tank_full");
        Transperant = register(r, "transperant");

    }

    public static void initItems(IIconRegister r)
    {
        register(r, "vent");
        register(r, "base");
        register(r, "tank_empty");
        register(r, "tank_full");
    }

    public static IIcon register(IIconRegister register,String name)
    {
        return register.registerIcon(Reference.MOD_ID + ":" + name);
    }
}
