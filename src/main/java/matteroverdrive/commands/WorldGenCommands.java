package matteroverdrive.commands;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.world.MOImageGen;
import matteroverdrive.world.buildings.WeightedRandomMOWorldGenBuilding;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simeon on 1/5/2016.
 */
public class WorldGenCommands extends CommandBase
{

    @Override
    public String getCommandName()
    {
        return "mo_gen";
    }

    @Override
    public String getCommandUsage(ICommandSender p_71518_1_)
    {
        return "mo_gen <command> <structure name> <options>";
    }

    @Override
    public void execute(MinecraftServer server,ICommandSender commandSender, String[] parameters) throws PlayerNotFoundException
    {
        EntityPlayer entityPlayer = null;
        if (parameters.length >= 4)
        {
            entityPlayer = getPlayer(server,commandSender,parameters[3]);
        }else if (commandSender instanceof EntityPlayer)
        {
            entityPlayer = (EntityPlayer)commandSender;
        }
        boolean forceGeneration = false;
        if (parameters.length >= 3)
        {
            forceGeneration = parameters[2].contains("f");
        }

        if (parameters.length >= 1)
        {
            if (parameters[0].equalsIgnoreCase("generate"))
            {
                if (parameters.length >= 2 && entityPlayer != null)
                {
                    for (WeightedRandomMOWorldGenBuilding entry : MatterOverdrive.moWorld.worldGen.buildings)
                    {
                        if (entry.worldGenBuilding.getName().equalsIgnoreCase(parameters[1]))
                        {
                            MOImageGen.ImageGenWorker worker = MatterOverdrive.moWorld.worldGen.startBuildingGeneration(entry.worldGenBuilding,entityPlayer.getPosition(),entityPlayer.getRNG(),commandSender.getEntityWorld(),null,null,forceGeneration);
                            if (worker != null)
                            {
                                worker.setPlaceNotify(2);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos)
    {
        List<String> commands = new ArrayList<>();

        if (args.length == 1)
        {
            commands.add("generate");
        }else if (args.length == 2)
        {
            for (WeightedRandomMOWorldGenBuilding entry : MatterOverdrive.moWorld.worldGen.buildings)
            {
                commands.add(entry.worldGenBuilding.getName());
            }
        }else if (args.length == 4)
        {
            for (Object entityPlayer : sender.getEntityWorld().playerEntities)
            {
                commands.add(((EntityPlayer)entityPlayer).getName());
            }
        }
        return commands;
    }
}
