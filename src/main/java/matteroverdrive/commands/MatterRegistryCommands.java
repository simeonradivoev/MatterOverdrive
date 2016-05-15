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
import matteroverdrive.data.matter.DamageAwareStackHandler;
import matteroverdrive.data.matter.ItemHandler;
import matteroverdrive.data.matter.OreHandler;
import matteroverdrive.handler.ConfigurationHandler;
import matteroverdrive.init.MatterOverdriveMatter;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simeon on 8/11/2015.
 */
public class MatterRegistryCommands extends CommandBase
{

	@Override
	public String getCommandName()
	{
		return "matter_registry";
	}

	public int getRequiredPermissionLevel()
	{
		return 2;
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_)
	{
		return "matter_registry <command> <value>";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender commandSender, String[] args) throws CommandException
	{
		if (args.length == 1)
		{
			if (args[0].equalsIgnoreCase("recalculate"))
			{
				MatterOverdrive.matterRegistry.getItemEntires().clear();
				MatterOverdriveMatter.registerBasic(MatterOverdrive.configHandler);
				MatterOverdrive.matterRegistrationHandler.runCalculationThread(commandSender.getEntityWorld());
			}
		}
		else if (args.length == 2)
		{
			if (args[0].equalsIgnoreCase("blacklist"))
			{
				ItemStack stack;
				if (args.length >= 4)
				{
					stack = getPlayer(server, commandSender, args[3]).getHeldItem(EnumHand.MAIN_HAND);
				}
				else
				{
					stack = getPlayer(server, commandSender, commandSender.getName()).getHeldItem(EnumHand.MAIN_HAND);
				}

				String key;
				if (stack != null)
				{
					if (args[1].equalsIgnoreCase("itemstack"))
					{
						key = stack.getItem().getRegistryName() + "/" + stack.getItemDamage();
						MatterOverdrive.matterRegistry.register(stack.getItem(), new DamageAwareStackHandler(stack.getItemDamage(), 0, true));
					}
					else if (args[1].equalsIgnoreCase("item"))
					{
						key = stack.getItem().getRegistryName().toString();
						MatterOverdrive.matterRegistry.register(stack.getItem(), new ItemHandler(0, true));
					}
					else if (args[1].equalsIgnoreCase("ore"))
					{
						int[] orenames = OreDictionary.getOreIDs(stack);
						if (orenames != null && orenames.length > 0)
						{
							key = OreDictionary.getOreName(orenames[0]);
							MatterOverdrive.matterRegistry.registerOre(key, new OreHandler(0, true));
						}
						else
						{
							throw new CommandException("Could not find an ore dictionary entry", args[1]);
						}
					}
					else
					{
						throw new CommandException("Invalid type of item. Use either item, itemstack or ore.");
					}

					String[] oldBlacklist = MatterOverdrive.configHandler.getStringList(ConfigurationHandler.CATEGORY_MATTER, ConfigurationHandler.KEY_MBLACKLIST);
					String[] newBlacklist = new String[oldBlacklist != null ? oldBlacklist.length + 1 : 1];
					newBlacklist[oldBlacklist.length] = key;
					MatterOverdrive.configHandler.config.get(ConfigurationHandler.CATEGORY_MATTER, ConfigurationHandler.KEY_MBLACKLIST, new String[] {}, "").set(newBlacklist);
					MatterOverdrive.configHandler.save();
					commandSender.addChatMessage(new TextComponentString(TextFormatting.GOLD + "[" + key + "]" + TextFormatting.RESET + " Added $s to matter blacklist and config.\nYou must recalculate the registry for changes to take effect.\nUse /matter_registry recalculate."));
				}
				else
				{
					throw new CommandException("Player not holding any item", args[1]);
				}
			}
		}
		else if (args.length >= 3)
		{
			if (args[0].equalsIgnoreCase("register"))
			{
				int matter = parseInt(args[2]);
				ItemStack stack;
				if (args.length >= 4)
				{
					stack = getPlayer(server, commandSender, args[3]).getHeldItem(EnumHand.MAIN_HAND);
				}
				else
				{
					stack = getPlayer(server, commandSender, commandSender.getName()).getHeldItem(EnumHand.MAIN_HAND);
				}

				if (stack != null)
				{
					String key;
					if (args[1].equalsIgnoreCase("itemstack"))
					{
						key = stack.getItem().getRegistryName() + "/" + stack.getItemDamage();
						MatterOverdrive.matterRegistry.register(stack.getItem(), new DamageAwareStackHandler(stack.getItemDamage(), matter));

					}
					else if (args[1].equalsIgnoreCase("item"))
					{
						key = stack.getItem().getRegistryName().toString();
						MatterOverdrive.matterRegistry.register(stack.getItem(), new ItemHandler(matter));
					}
					else if (args[1].equalsIgnoreCase("ore"))
					{
						int[] oreNames = OreDictionary.getOreIDs(stack);
						if (oreNames != null && oreNames.length > 0)
						{
							key = OreDictionary.getOreName(oreNames[0]);
						}
						else
						{
							throw new CommandException("Could not find an ore dictionary entry!");
						}
					}
					else
					{
						throw new CommandException("Invalid type of item. Use either item,itemstack or ore.");
					}

					MatterOverdrive.configHandler.setInt(key, ConfigurationHandler.CATEGORY_NEW_ITEMS, matter);
					MatterOverdrive.configHandler.save();
					commandSender.addChatMessage(new TextComponentString(TextFormatting.GOLD + "[" + key + "]" + TextFormatting.RESET + " Added $s to matter registry and config.\nYou can now recalculated the registry.\nUse /matter_registry recalculate."));
				}
				else
				{
					throw new CommandException("player not holding any item", args[1]);
				}
			}
		}
	}

	@Override
	public List getTabCompletionOptions(MinecraftServer server, ICommandSender commandSender, String[] parameters, BlockPos pos)
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
