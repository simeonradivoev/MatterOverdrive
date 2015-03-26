package com.MO.MatterOverdrive.handler;

import java.io.File;
import java.util.logging.Level;

import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.init.MatterOverdriveMatter;

import cpw.mods.fml.common.FMLLog;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;

public class MOConfigurationHandler 
{
	public static Configuration config;
	public static final String MATTER_LIST_CATEGORY = "matterlist";
	
	public static void init(File file)
	{
		config = new Configuration(file);
	}

    public static void load()
    {
        config.load();
    }

    public static void save()
    {
        if(config.hasChanged())
        {
            config.save();
        }
    }
}
