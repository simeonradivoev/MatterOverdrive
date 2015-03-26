package com.MO.MatterOverdrive.init;

import com.MO.MatterOverdrive.world.MOWorldGen;
import cpw.mods.fml.common.registry.GameRegistry;

/**
 * Created by Simeon on 3/23/2015.
 */
public class MatterOverdriveWorld
{
    public static MOWorldGen worldGen;

    public static void init()
    {
        worldGen = new MOWorldGen();
    }

    public static void register()
    {
        GameRegistry.registerWorldGenerator(worldGen,1);
    }
}
