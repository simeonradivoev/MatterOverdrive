package com.MO.MatterOverdrive.gui;

import com.MO.MatterOverdrive.MatterOverdrive;
import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.handler.MOConfigurationHandler;
import cpw.mods.fml.client.config.IConfigElement;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.ConfigElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simeon on 5/9/2015.
 */
public class GuiConfig extends cpw.mods.fml.client.config.GuiConfig
{

    public GuiConfig(GuiScreen parent) {
        super(parent, getConfigElements(parent), Reference.MOD_ID, false, false, GuiConfig.getAbridgedConfigPath(MatterOverdrive.configHandler.toString()),Reference.MOD_NAME + " Configurations");
    }

    private static List<IConfigElement> getConfigElements(GuiScreen parent) {

        List<IConfigElement> list = new ArrayList<IConfigElement>();

        list.add(new ConfigElement<ConfigCategory>(MatterOverdrive.configHandler.config.getCategory(MOConfigurationHandler.CATEGORY_WORLD_GEN)));
        list.add(new ConfigElement<ConfigCategory>(MatterOverdrive.configHandler.config.getCategory(MOConfigurationHandler.CATEGORY_MATTER_NETWORK)));
        list.add(new ConfigElement<ConfigCategory>(MatterOverdrive.configHandler.config.getCategory(MOConfigurationHandler.CATEGORY_MACHINES)));
        list.add(new ConfigElement<ConfigCategory>(MatterOverdrive.configHandler.config.getCategory(MOConfigurationHandler.CATEGORY_MATTER)));
        return list;
    }
}
