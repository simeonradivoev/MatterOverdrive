package matteroverdrive.init;

import matteroverdrive.handler.ConfigurationHandler;
import matteroverdrive.world.MOWorldGen;
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
