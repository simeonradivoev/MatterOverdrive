package com.MO.MatterOverdrive.container.slot;

import com.MO.MatterOverdrive.tile.TileEntityMachineStarMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * Created by Simeon on 6/23/2015.
 */
public class SlotStarMap extends MOSlot
{
    EntityPlayer player;
    TileEntityMachineStarMap starMap;

    public SlotStarMap(TileEntityMachineStarMap starMap, int slot,EntityPlayer player) {
        super(starMap, slot, 0, 0);
        this.player = player;
        this.starMap = starMap;
    }

    public boolean isValid(ItemStack itemStack)
    {
        return starMap.isItemValidForSlot(getSlotIndex(),itemStack,player);
    }

    public void onPickupFromSlot(EntityPlayer player, ItemStack itemStack)
    {
        starMap.onItemPickup(player,itemStack);
        super.onPickupFromSlot(player,itemStack);
    }

    public boolean canTakeStack(EntityPlayer player)
    {
        return starMap.getPlanet() == null || starMap.getPlanet().isOwner(player);
    }

    public void putStack(ItemStack itemStack)
    {
        starMap.onItemPlaced(itemStack);
        super.putStack(itemStack);
    }
}
