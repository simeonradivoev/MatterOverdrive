package com.MO.MatterOverdrive.init;

import com.MO.MatterOverdrive.handler.ConfigurationHandler;
import com.MO.MatterOverdrive.world.MOWorldGen;
import cpw.mods.fml.common.registry.GameRegistry;

/**
 * Created by Simeon on 3/23/2015.
 */
public class MatterOverdriveWorld
{
    public MOWorldGen worldGen;

    public MatterOverdriveWorld(ConfigurationHandler configurationHandler)
    {
        worldGen = new MOWorldGen(configurationHandler);
        configurationHandler.subscribe(worldGen);
    }

    public void register()
    {
        GameRegistry.registerWorldGenerator(worldGen,1);
    }
}
