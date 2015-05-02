package com.MO.MatterOverdrive.handler;

import java.io.File;

import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.init.MatterOverdriveBlocks;

import net.minecraftforge.common.config.Configuration;

public class MOConfigurationHandler
{
	public Configuration config;
	public static final String CATEGORY_MATTER_LIST = "matterlist";
    public static final String CATEGORY_WORLD_GEN = "world_gen";
    public static final String CATEGORY_MATTER_NETWORK = "mattern_network";
    public static final String CATEGORY_MACHINES = "machines";

    public static float REPLICATOR_SOUND_VALUME;

    public MOConfigurationHandler(File file)
    {
        config = new Configuration(file);
    }

	public void init()
	{
        load();
        config.get(CATEGORY_WORLD_GEN, Reference.CONFIG_WORLD_SPAWN_ORES, true);
        config.get(CATEGORY_WORLD_GEN,Reference.CONFIG_WORLD_SPAWN + "." + MatterOverdriveBlocks.dilithiumOre.getUnlocalizedName(),true);
        config.get(CATEGORY_WORLD_GEN,Reference.CONFIG_WORLD_SPAWN + "." + MatterOverdriveBlocks.tritaniumOre.getUnlocalizedName(),true);
        save();
	}

    public float getMachineFloat(String name,float defaultValue,float min,float max,String comment)
    {
        return config.getFloat(name,CATEGORY_MACHINES,defaultValue,min,max,comment);
    }

    public boolean getMachineBool(String name,boolean defaultValue,String comment)
    {
        return config.getBoolean(name,CATEGORY_MACHINES,defaultValue,comment);
    }

    public void load()
    {
        config.load();
    }

    public void save()
    {
        if(config.hasChanged())
        {
            config.save();
        }
    }
}
