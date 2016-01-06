package matteroverdrive.util;

import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.machines.MOTileEntityMachine;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

/**
 * Created by Simeon on 1/3/2016.
 */
public class MachineHelper
{
    public static boolean canOpenMachine(World world, int x, int y, int z, EntityPlayer player,boolean hasGui,String errorMessage)
    {
        if (world.isRemote)
        {
            return true;
        } else if (hasGui)
        {
            TileEntity tileEntity = world.getTileEntity(x,y,z);
            if (tileEntity instanceof MOTileEntityMachine)
            {
                if (((MOTileEntityMachine) tileEntity).isUseableByPlayer(player)) {
                    FMLNetworkHandler.openGui(player, MatterOverdrive.instance, -1, world, x, y, z);
                    return true;
                } else
                {
                    ChatComponentText message = new ChatComponentText(EnumChatFormatting.GOLD + "[Matter Overdrive] " + EnumChatFormatting.RED + MOStringHelper.translateToLocal(errorMessage).replace("$0", ((MOTileEntityMachine) tileEntity).getInventoryName()));
                    message.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED));
                    player.addChatMessage(message);
                }
            }
        }

        return false;
    }

    public static boolean canRemoveMachine(World world, EntityPlayer player, int x, int y, int z, boolean willHarvest)
    {
        TileEntity tileEntity = world.getTileEntity(x,y,z);
        if (tileEntity != null && tileEntity instanceof MOTileEntityMachine)
        {
            if (!player.capabilities.isCreativeMode &&
                    ((MOTileEntityMachine) tileEntity).hasOwner() && !((MOTileEntityMachine) tileEntity).getOwner().equals(player.getGameProfile().getId()))
            {
                ChatComponentText message = new ChatComponentText(EnumChatFormatting.GOLD + "[Matter Overdrive] " + EnumChatFormatting.RED + MOStringHelper.translateToLocal("alert.no_rights.break").replace("$0",((MOTileEntityMachine) tileEntity).getInventoryName()));
                message.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED));
                player.addChatMessage(message);
                return false;
            }
        }
        return true;
    }
}
