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

package matteroverdrive.commands;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.handler.thread.RegisterItemsFromRecipes;
import matteroverdrive.init.MatterOverdriveMatter;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simeon on 8/11/2015.
 */
public class MatterRegistryCommands extends CommandBase
{

    @Override
    public String getCommandName() {
        return "matter_registry";
    }

    public int getRequiredPermissionLevel()
    {
        return 2;
    }

    @Override
    public String getCommandUsage(ICommandSender p_71518_1_) {
        return "matter_registry <command> <value>";
    }

    @Override
    public void processCommand(ICommandSender commandSender, String[] parameters)
    {
        if (parameters.length > 0)
        {
            if (parameters[0].equalsIgnoreCase("recalculate"))
            {
                MatterOverdrive.matterRegistry.getEntries().clear();
                MatterOverdriveMatter.registerBasic(MatterOverdrive.configHandler);
                Thread registerItemsThread = new Thread(new RegisterItemsFromRecipes(MatterOverdrive.registryPath));
                registerItemsThread.run();
            }
        }
    }

    @Override
    public List addTabCompletionOptions(ICommandSender commandSender, String[] parameters)
    {
        List<String> commands = new ArrayList<>();

        if (parameters.length == 1)
        {
            commands.add("recalculate");
            commands.add("add");
            commands.add("blacklist");
        }
        return commands;
    }
}
