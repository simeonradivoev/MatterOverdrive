package matteroverdrive.api.starmap;

import matteroverdrive.starmap.data.Planet;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.UUID;

/**
 * Created by Simeon on 6/24/2015.
 */
public interface IShip extends IBuildable
{
    boolean isOwner(ItemStack ship,EntityPlayer player);
    UUID getOwnerID(ItemStack stack);
    void setOwner(ItemStack ship,UUID ownerID);
    ShipType getType(ItemStack ship);
    void onTravel(ItemStack stack,Planet to);
}
