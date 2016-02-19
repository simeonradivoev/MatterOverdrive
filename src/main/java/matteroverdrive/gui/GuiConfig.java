/*
 * This file is part of Matter Overdrive
 * Copyright (c) 2015., Simeon Radivoev, All rights reserved.
 *
 * Matter Overdrive is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Matter Overdrive is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Matter Overdrive.  If not, see <http://www.gnu.org/licenses>.
 */

package matteroverdrive.gui;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.IConfigElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simeon on 5/9/2015.
 */
public class GuiConfig extends net.minecraftforge.fml.client.config.GuiConfig
{

    public GuiConfig(GuiScreen parent)
    {
        super(parent,getAllGuiCategories(),Reference.MOD_ID,false,false,"Matter Overdrive Configurations");
    }

    public GuiConfig(GuiScreen parent,String category) {
        super(parent, getConfigElements(parent,category), Reference.MOD_ID, false, false, GuiConfig.getAbridgedConfigPath(MatterOverdrive.configHandler.toString()),Reference.MOD_NAME + " Configurations");
    }

    private static List<IConfigElement> getConfigElements(GuiScreen parent, String category) {

        List<IConfigElement> list = new ArrayList<>();
        list.add(new ConfigElement(MatterOverdrive.configHandler.getCategory(category)));
        return list;
    }

    private static List<IConfigElement> getAllGuiCategories()
    {
        List<IConfigElement> list = new ArrayList<>();
        MatterOverdrive.configHandler.addCategoryToGui(list);
        return list;
    }
}
