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

package matteroverdrive.init;

import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import matteroverdrive.enchantment.EnchantmentOverclock;
import matteroverdrive.handler.ConfigurationHandler;
import matteroverdrive.util.IConfigSubscriber;

/**
 * Created by Simeon on 8/7/2015.
 */
public class MatterOverdriveEnchantments implements IConfigSubscriber
{
    public static EnchantmentOverclock overclock;

    public static void init(FMLPreInitializationEvent event,ConfigurationHandler configurationHandler)
    {
        int id = configurationHandler.getInt("Overclock",ConfigurationHandler.CATEGORY_ENCHANTMENTS,69);
        while (id < 256)
        {
            try {
                overclock = new EnchantmentOverclock(id);
                break;
            } catch (IllegalArgumentException e) {
                id++;
            }
        }

        configurationHandler.setInt("Overclock",ConfigurationHandler.CATEGORY_ENCHANTMENTS,id);
    }

    @Override
    public void onConfigChanged(ConfigurationHandler config) {

    }
}
