package com.MO.MatterOverdrive.handler;

import java.io.File;
import java.util.logging.Level;

import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.init.MatterOverdriveBlocks;
import com.MO.MatterOverdrive.init.MatterOverdriveMatter;

import cpw.mods.fml.common.FMLLog;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;

public class MOConfigurationHandler
{
	public Configuration config;
	public static final String MATTER_LIST_CATEGORY = "matterlist";
    public static final String WORLD_GEN_CATEGORY = "world_gen";

    public MOConfigurationHandler(File file)
    {
        config = new Configuration(file);
    }

	public void init()
	{
        load();
        config.get(WORLD_GEN_CATEGORY,Reference.CONFIG_WORLD_SPAWN_ORES,true);
        config.get(WORLD_GEN_CATEGORY,Reference.CONFIG_WORLD_SPAWN + "." + MatterOverdriveBlocks.dilithiumOre.getUnlocalizedName(),true);
        config.get(WORLD_GEN_CATEGORY,Reference.CONFIG_WORLD_SPAWN + "." + MatterOverdriveBlocks.tritaniumOre.getUnlocalizedName(),true);
        save();
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
