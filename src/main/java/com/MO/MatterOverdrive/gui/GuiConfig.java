package com.MO.MatterOverdrive.gui;

import com.MO.MatterOverdrive.MatterOverdrive;
import com.MO.MatterOverdrive.Reference;
import cpw.mods.fml.client.config.IConfigElement;
import net.minecraft.client.gui.GuiScreen;

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
        MatterOverdrive.configHandler.addCategoryToGui(list);
        return list;
    }
}
