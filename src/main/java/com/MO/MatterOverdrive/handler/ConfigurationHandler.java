package com.MO.MatterOverdrive.handler;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.init.MatterOverdriveBlocks;

import com.MO.MatterOverdrive.util.IConfigSubscriber;
import cpw.mods.fml.client.config.IConfigElement;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class ConfigurationHandler
{
    private Set<IConfigSubscriber> subscribers;
	public Configuration config;
    public static final String CATEGORY_MATTER = "matter registry";
    public static final String CATEGORY_WORLD_GEN = "world gen";
    public static final String CATEGORY_MATTER_NETWORK = "matter network";
    public static final String CATEGORY_MACHINES = "machine options";
    public static final String CATEGORY_OVERRIDE_MATTER = CATEGORY_MATTER + ".item overrides";
    public static final String CATEGORY_NEW_ITEMS = CATEGORY_MATTER + ".new items";
    public static final String CATEGORY_WORLD_SPAWN_ORES = "spawn ores";
    public static final String CATEGORY_WORLD_SPAWN_OTHER = "spawn other";
    public static final String CATEGORY_WORLD_SPAWN = "spawn";
    public static final String CATEGORY_CLIENT = "client";
    public static final String CATEGORY_STARMAP = "starmap";
    public static final String CATEGORY_SERVER = "server";

    public static final String KEY_AUTOMATIC_RECIPE_CALCULATION = "automatic matter calculation from recipe";
    public static final String KEY_MAX_BROADCASTS = "max broadcasts per tick";
    public static final String KEY_MBLACKLIST = "blacklist";
    public static final String KEY_VERSION_CHECK = "version_check";
    public static final String KEY_GRAVITATIONAL_ANOMALY_FALLING_BLOCKS = "gravitational anomaly falling blocks";
    public static final String KEY_GRAVITATIONAL_ANOMALY_BLOCK_ENTITIES = "gravitational anomaly block entities";
    public static final String KEY_GRAVITATIONAL_ANOMALY_SPAWN_CHANCE = "gravitational anomaly spawn chance";
    public static final String KEY_GRAVITATIONAL_ANOMALY_VANILLA_FLUIDS = "gravitational anomaly vanilla fluids";
    public static final String KEY_GRAVITATIONAL_ANOMALY_FORGE_FLUIDS = "gravitational anomaly forge fluids";

    public ConfigurationHandler(File file)
    {
        config = new Configuration(file,Reference.VERSION);
        subscribers = new HashSet<IConfigSubscriber>();
    }

	public void init()
	{
        config.load();
        ConfigCategory category = config.getCategory(CATEGORY_MATTER);
        category.setComment("Configuration for the Matter Network");
        updateCategoryLang(category);
        config.get(CATEGORY_MATTER,KEY_MBLACKLIST,new String[0]).comment = "Blacklist for items in the matter registry. Automatic Recipe calculation will ignore recipes with these items. Just add the unlocalized name or the ore dictionary name in the list.";
        category = config.getCategory(CATEGORY_NEW_ITEMS);
        category.setComment("Registration of new items and the amount of matter they contain. Add them like so: I:[unlocalized name or ore Dictionary name]=[matter amount]");
        updateCategoryLang(category);
        category = config.getCategory(CATEGORY_OVERRIDE_MATTER);
        category.setComment("Overriding of existing items and the amount of matter they contain. Add them like so: I:[unlocalized name or ore Dictionary name]=[matter amount]");
        updateCategoryLang(category);
        category = config.getCategory(CATEGORY_CLIENT);
        updateCategoryLang(category);
        category.setComment("Options for the Matter overdrive client");
        category = config.getCategory(CATEGORY_SERVER);
        updateCategoryLang(category);
        category.setComment("Options form the Matter Overdrive server");

        category = config.getCategory(CATEGORY_MACHINES);
        category.setComment("Machine Options.");
        updateCategoryLang(category);
        category = config.getCategory(CATEGORY_MATTER_NETWORK);
        category.setComment("Matter Network Options.");
        updateCategoryLang(category);
        category = config.getCategory(CATEGORY_WORLD_GEN);
        category.setComment("World Generation options.");
        updateCategoryLang(category);
        category = config.getCategory(CATEGORY_STARMAP);
        category.setComment("Star Map Galaxy Options");
        updateCategoryLang(category);

        config.get(CATEGORY_WORLD_GEN,CATEGORY_WORLD_SPAWN_ORES, true, "Should ores such as dilithium and tritanium ore spawn in the world. This applies for all ores !").setLanguageKey("config." + CATEGORY_WORLD_SPAWN_ORES.replace(' ', '_') + ".name");
        config.get(CATEGORY_WORLD_GEN,CATEGORY_WORLD_SPAWN + "." + MatterOverdriveBlocks.dilithium_ore.getUnlocalizedName(),true).setLanguageKey(MatterOverdriveBlocks.dilithium_ore.getUnlocalizedName() + ".name");
        config.get(CATEGORY_WORLD_GEN,CATEGORY_WORLD_SPAWN + "." + MatterOverdriveBlocks.tritaniumOre.getUnlocalizedName(),true).setLanguageKey(MatterOverdriveBlocks.tritaniumOre.getUnlocalizedName() + ".name");

        config.getBoolean(KEY_AUTOMATIC_RECIPE_CALCULATION, CATEGORY_MATTER, true, "Shoud Matter be automaticly calculated from Recipes");

        for (IConfigSubscriber subscriber : subscribers)
        {
            subscriber.onConfigChanged(this);
        }
        save();
	}

    public void updateCategoryLang(ConfigCategory category)
    {
        category.setLanguageKey("config." + category.getName().replace(' ','_') + ".name");
    }

    public boolean getBool(String key,String category,Boolean def,String comment)
    {
        return config.getBoolean(key, category, def, comment);
    }

    public boolean getBool(String key,String category,Boolean def)
    {
        return config.getBoolean(key, category, def, null);
    }

    public int getInt(String key,String category,Integer def,String comment)
    {
        Property property = config.get(category, key, def);
        property.comment = comment;
        return property.getInt(def);
    }

    public int getInt(String key,String category,Integer def)
    {
        return getInt(key,category,def,"");
    }

    public String[] getStringList(String category,String key)
    {
        return config.get(category,key,new String[0]).getStringList();
    }

    public ConfigCategory getCategory(String cat)
    {
        return config.getCategory(cat);
    }

    public double getMachineDouble(String machine,String prop,double def,String comment)
    {
        Property p = config.get(CATEGORY_MACHINES + "." + machine.replaceFirst("tile.",""),prop,def);
        p.comment = comment;
        p.setLanguageKey(machine + ".config." + prop);
        return p.getDouble(def);
    }

    public boolean getMachineBool(String machine,String prop,boolean def,String comment)
    {
        Property p = config.get(CATEGORY_MACHINES + "." + machine.replaceFirst("tile.",""),prop,def);
        p.comment = comment;
        p.setLanguageKey(machine + ".config." + prop);
        return p.getBoolean(def);
    }

    public void initMachineCategory(String machine)
    {
        config.setCategoryLanguageKey(CATEGORY_MACHINES + "." + machine.replaceFirst("tile.", ""), machine + ".name");
    }

    public void save()
    {
        if(config.hasChanged())
        {
            config.save();
        }
    }

    public void subscribe(IConfigSubscriber subscriber)
    {
        if (!subscribers.contains(subscriber)) {
            subscribers.add(subscriber);
        }
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent eventArgs)
    {
        if(eventArgs.modID.equals(Reference.MOD_ID)) {
            config.save();
        }

        for (IConfigSubscriber subscriber : subscribers)
        {
            subscriber.onConfigChanged(this);
        }
    }

    public void addCategoryToGui(List<IConfigElement> list)
    {
        list.add(new ConfigElement<ConfigCategory>(getCategory(ConfigurationHandler.CATEGORY_CLIENT)));
        list.add(new ConfigElement<ConfigCategory>(getCategory(ConfigurationHandler.CATEGORY_SERVER)));
        list.add(new ConfigElement<ConfigCategory>(getCategory(ConfigurationHandler.CATEGORY_WORLD_GEN)));
        list.add(new ConfigElement<ConfigCategory>(getCategory(ConfigurationHandler.CATEGORY_MATTER_NETWORK)));
        list.add(new ConfigElement<ConfigCategory>(getCategory(ConfigurationHandler.CATEGORY_MACHINES)));
        list.add(new ConfigElement<ConfigCategory>(getCategory(ConfigurationHandler.CATEGORY_MATTER)));
        list.add(new ConfigElement<ConfigCategory>(getCategory(ConfigurationHandler.CATEGORY_STARMAP)));
    }
}
