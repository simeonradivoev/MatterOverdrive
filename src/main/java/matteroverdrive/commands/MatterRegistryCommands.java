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
import matteroverdrive.handler.ConfigurationHandler;
import matteroverdrive.init.MatterOverdriveMatter;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.oredict.OreDictionary;

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
        if (parameters.length == 1)
        {
            if (parameters[0].equalsIgnoreCase("recalculate"))
            {
                MatterOverdrive.matterRegistry.getEntries().clear();
                MatterOverdriveMatter.registerBasic(MatterOverdrive.configHandler);
                MatterOverdrive.matterRegistrationHandler.runCalculationThread();
            }
        }
        else if (parameters.length == 2)
        {
            if (parameters[0].equalsIgnoreCase("blacklist"))
            {
                ItemStack stack;
                if (parameters.length >= 4)
				{
                    stack = getPlayer(commandSender, parameters[3]).getCurrentEquippedItem();
                }
				else
                {
                    stack = getPlayer(commandSender,commandSender.getCommandSenderName()).getCurrentEquippedItem();
                }

                String key;
                if (stack != null)
				{
                    if (parameters[1].equalsIgnoreCase("itemstack"))
					{
                        key = MatterOverdrive.matterRegistry.getKey(stack);

                    }
					else if (parameters[1].equalsIgnoreCase("item")) {
                        key = MatterOverdrive.matterRegistry.getKey(stack.getItem());
                    }
					else if (parameters[1].equalsIgnoreCase("ore"))
                    {
                        int[] orenames = OreDictionary.getOreIDs(stack);
                        if (orenames != null && orenames.length > 0)
                        {
                            key = OreDictionary.getOreName(orenames[0]);
                        }
						else
                        {
                            throw new CommandException("Could not find an ore dictionary entry", parameters[1]);
                        }
                    }
                    else
					{
                        throw new CommandException("Invalid type of item. Use either item, itemstack or ore.");
                    }

                    MatterOverdrive.matterRegistry.addToBlacklist(key);
                    String[] oldBlacklist = MatterOverdrive.configHandler.getStringList(ConfigurationHandler.CATEGORY_MATTER, ConfigurationHandler.KEY_MBLACKLIST);
                    String[] newBlacklist = new String[oldBlacklist != null ? oldBlacklist.length + 1 : 1];
                    newBlacklist[oldBlacklist.length] = key;
                    MatterOverdrive.configHandler.config.get(ConfigurationHandler.CATEGORY_MATTER, ConfigurationHandler.KEY_MBLACKLIST, new String[]{}, "").set(newBlacklist);
                    MatterOverdrive.configHandler.save();
                    commandSender.addChatMessage(new ChatComponentText(EnumChatFormatting.GOLD + "[" + key + "]" + EnumChatFormatting.RESET + " Added $s to matter blacklist and config.\nYou must recalculate the registry for changes to take effect.\nUse /matter_registry recalculate."));
                }
				else
                {
                    throw new CommandException("Player not holding any item", parameters[1]);
                }
            }
        }
        else if (parameters.length >= 3)
        {
            if (parameters[0].equalsIgnoreCase("register"))
            {
                int matter = parseInt(commandSender, parameters[2]);
                ItemStack stack;
                if (parameters.length >= 4)
				{
                    stack = getPlayer(commandSender, parameters[3]).getCurrentEquippedItem();
                }
				else
                {
                    stack = getPlayer(commandSender,commandSender.getCommandSenderName()).getCurrentEquippedItem();
                }

                if (stack != null)
				{
                    String key;
                    if (parameters[1].equalsIgnoreCase("itemstack"))
                    {
                        key = MatterOverdrive.matterRegistry.getKey(stack);


                    } else if (parameters[1].equalsIgnoreCase("item")) {
                        key = MatterOverdrive.matterRegistry.getKey(stack.getItem());
                    }
                    else if (parameters[1].equalsIgnoreCase("ore"))
                    {
                        int[] oreNames = OreDictionary.getOreIDs(stack);
                        if (oreNames != null && oreNames.length > 0)
                        {
                            key = OreDictionary.getOreName(oreNames[0]);
                        }else
                        {
                            throw new CommandException("Could not find an ore dictionary entry!");
                        }
                    }
                    else
                    {
                        throw new CommandException("Invalid type of item. Use either item,itemstack or ore.");
                    }

                    MatterOverdrive.matterRegistry.register(key, matter);
                    MatterOverdrive.configHandler.setInt(key, ConfigurationHandler.CATEGORY_NEW_ITEMS,matter);
                    MatterOverdrive.configHandler.save();
                    commandSender.addChatMessage(new ChatComponentText(EnumChatFormatting.GOLD + "[" + key + "]"+ EnumChatFormatting.RESET +" Added $s to matter registry and config.\nYou can now recalculated the registry.\nUse /matter_registry recalculate."));
                }
				else
                {
                    throw new CommandException("player not holding any item", parameters[1]);
                }
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
            commands.add("register");
            commands.add("blacklist");
        }
		else if (parameters.length == 2 && (parameters[0].equalsIgnoreCase("register") || parameters[0].equalsIgnoreCase("blacklist")))
        {
            commands.add("itemstack");
            commands.add("item");
            commands.add("ore");
        }
        return commands;
    }
}
