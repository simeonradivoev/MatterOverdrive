package matteroverdrive.items.starmap;

import matteroverdrive.api.starmap.IShip;
import matteroverdrive.items.includes.MOBaseItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.List;
import java.util.UUID;

/**
 * Created by Simeon on 6/24/2015.
 */
public abstract class ItemShipAbstract extends MOBaseItem implements IShip
{
    public ItemShipAbstract(String name)
    {
        super(name);
    }

    public void addDetails(ItemStack itemstack, EntityPlayer player, List infos)
    {
        super.addDetails(itemstack,player,infos);
        infos.add("Build Time: " + getBuildTime(itemstack));
    }

    public boolean hasDetails(ItemStack stack){return true;}

    public int getBuildTime(ItemStack building)
    {
        if (building.hasTagCompound())
        {
            return building.getTagCompound().getInteger("BuildTime");
        }
        else
        {
            return 0;
        }
    }

    public void setBuildTime(ItemStack building,int buildTime)
    {
        if (!building.hasTagCompound())
        {
            building.setTagCompound(new NBTTagCompound());
        }

        building.getTagCompound().setInteger("BuildTime", buildTime);
    }

    @Override
    public boolean isOwner(ItemStack ship, EntityPlayer player)
    {
        if (ship.hasTagCompound())
        {
            if (ship.getTagCompound().hasKey("Owner") && !ship.getTagCompound().getString("Owner").isEmpty()) {
                try {
                    return UUID.fromString(ship.getTagCompound().getString("Owner")).equals(EntityPlayer.func_146094_a(player.getGameProfile()));
                }
                catch (Exception e)
                {
                    return false;
                }
            }
        }
        return false;
    }

    @Override
    public void setOwner(ItemStack ship, UUID playerId)
    {
        if (!ship.hasTagCompound())
        {
            ship.setTagCompound(new NBTTagCompound());
        }

        ship.getTagCompound().setString("Owner",playerId.toString());
    }
}
