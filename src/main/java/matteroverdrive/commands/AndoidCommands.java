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

import com.mojang.realmsclient.gui.ChatFormatting;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.api.android.IBioticStat;
import matteroverdrive.entity.android_player.AndroidPlayer;
import matteroverdrive.entity.player.MOPlayerCapabilityProvider;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Simeon on 5/26/2015.
 */
public class AndoidCommands extends CommandBase
{
    @Override
    public String getCommandName() {
        return "android";
    }

    public int getRequiredPermissionLevel()
    {
        return 2;
    }

    @Override
    public String getCommandUsage(ICommandSender sender)
    {
        return "android <command> <value> <player>";
    }

    @Override
    public List getCommandAliases()
    {
        return new ArrayList();
    }

    @Override
    public void execute(MinecraftServer server,ICommandSender sender, String[] parameters) throws CommandException
    {
        if (parameters.length == 0)
        {
            sender.addChatMessage(new TextComponentString("Invalid Parameters"));
            return;
        }

        if (parameters.length >= 2) {
            EntityPlayer player;
            if (parameters.length >= 3) {
                player = getPlayer(server,sender, parameters[2]);
            } else {
                player = getCommandSenderAsPlayer(sender);
            }

            if (player != null)
            {
                AndroidPlayer androidPlayer = MOPlayerCapabilityProvider.GetAndroidCapability(player);
                if (androidPlayer != null) {

                    boolean validCommand = false;
                    String commandInfo = "";

                    if (parameters[0].equalsIgnoreCase("set")) {
                        boolean android = parseBoolean(parameters[1]);
                        androidPlayer.setAndroid(android);
                        validCommand = true;
                        if (android)
                        {
                            commandInfo = sender.getName() + " is now an Android";
                        }else
                        {
                            commandInfo = sender.getName() + " is no longer an Android";
                        }
                    }
                    else if (parameters[0].equalsIgnoreCase("stats"))
                    {
                        if (parameters[1].equalsIgnoreCase("reset"))
                        {
                            androidPlayer.resetUnlocked();
                            validCommand = true;
                            commandInfo = sender.getName() + " stats are now Reset";
                        }
                    }else if (parameters[0].equalsIgnoreCase("unlock"))
                    {
                        if (MatterOverdrive.statRegistry.hasStat(parameters[1]))
                        {
                            IBioticStat stat = MatterOverdrive.statRegistry.getStat(parameters[1]);
                            androidPlayer.unlock(stat, stat.maxLevel());
                            validCommand = true;
                            commandInfo = sender.getName() + " now has the ability " + ChatFormatting.GREEN + "[" + stat.getDisplayName(androidPlayer,stat.maxLevel()) + "]";
                        }
                    }else if (parameters[0].equalsIgnoreCase("forget"))
                    {
                        if (MatterOverdrive.statRegistry.hasStat(parameters[1]))
                        {
                            IBioticStat stat = MatterOverdrive.statRegistry.getStat(parameters[1]);
                            androidPlayer.reset(stat);
                            validCommand = true;
                            commandInfo = ChatFormatting.GREEN + "[" + stat.getDisplayName(androidPlayer,stat.maxLevel()) + "]" + ChatFormatting.RESET + " removed from " + sender.getName();
                        }
                    }

                    if (validCommand)
                    {
                        androidPlayer.sync(EnumSet.allOf(AndroidPlayer.DataType.class), false);
                        sender.addChatMessage(new TextComponentString(ChatFormatting.GOLD + "["+ Reference.MOD_NAME+"] " + ChatFormatting.RESET + commandInfo));
                        return;
                    }
                }
            }
        }

        sender.addChatMessage(new TextComponentString("Invalid Android Command. Use /help to learn more."));
    }

    @Override
    public List getTabCompletionOptions(MinecraftServer server,ICommandSender commandSender, String[] parameters, BlockPos pos)
    {
        List<String> commands = new ArrayList<>();

        if (parameters.length == 2)
        {
            if (parameters[0].equalsIgnoreCase("set"))
            {
                commands.add("true");
                commands.add("false");
            }
            else if (parameters[0].equalsIgnoreCase("stats"))
            {
                commands.add("reset");
            }
            else if (parameters[0].equalsIgnoreCase("unlock"))
            {
                commands.addAll(MatterOverdrive.statRegistry.getStats().stream().map(IBioticStat::getUnlocalizedName).collect(Collectors.toList()));
            }
            else if (parameters[0].equalsIgnoreCase("forget"))
            {
                commands.addAll(MatterOverdrive.statRegistry.getStats().stream().map(IBioticStat::getUnlocalizedName).collect(Collectors.toList()));
            }
        }else
        {
            commands.add("set");
            commands.add("stats");
            commands.add("unlock");
            commands.add("forget");
        }
        return commands;
    }

    @Override
    public boolean isUsernameIndex(String[] params, int index)
    {
        return index == 2;
    }
}
