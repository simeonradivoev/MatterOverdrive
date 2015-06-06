package com.MO.MatterOverdrive.data;

import com.MO.MatterOverdrive.data.inventory.Slot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Simeon on 5/26/2015.
 */
public class TileEntityInventory extends Inventory
{
    TileEntity entity;

    public TileEntityInventory(TileEntity entity,String name) {
        this(entity,name,new ArrayList<Slot>());
    }

    public TileEntityInventory(TileEntity entity,String name, Collection<Slot> slots) {
        this(entity,name, slots,null);
    }

    public TileEntityInventory(TileEntity entity,String name, Collection<Slot> slots, IUseableCondition useableCondition) {
        super(name, slots, useableCondition);
        this.entity = entity;
    }


    @Override
    public void markDirty()
    {
        if(this.entity != null)
        {
            this.entity.markDirty();
        }
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player)
    {
        if(useableCondition != null)
        {
            return useableCondition.usableByPlayer(player);
        }
        return entity.getWorldObj().getTileEntity(entity.xCoord, entity.yCoord, entity.zCoord) != entity ? false : player.getDistanceSq((double)entity.xCoord + 0.5D, (double)entity.yCoord + 0.5D, (double)entity.zCoord + 0.5D) <= 64.0D;
    }
}
