package matteroverdrive.data;

import matteroverdrive.data.inventory.Slot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Simeon on 5/26/2015.
 */
public class TileEntityInventory extends Inventory
{
	final TileEntity entity;

	public TileEntityInventory(TileEntity entity, String name)
	{
		this(entity, name, new ArrayList<>());
	}

	public TileEntityInventory(TileEntity entity, String name, Collection<Slot> slots)
	{
		this(entity, name, slots, null);
	}

	public TileEntityInventory(TileEntity entity, String name, Collection<Slot> slots, IUsableCondition usableCondition)
	{
		super(name, slots, usableCondition);
		this.entity = entity;
	}


	@Override
	public void markDirty()
	{
		if (this.entity != null)
		{
			this.entity.markDirty();
		}
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player)
	{
		if (usableCondition != null)
		{
			return usableCondition.usableByPlayer(player);
		}
		return entity.getWorld().getTileEntity(entity.getPos()) != entity ? false : player.getDistanceSq((double)entity.getPos().getX() + 0.5D, (double)entity.getPos().getY() + 0.5D, (double)entity.getPos().getZ() + 0.5D) <= 64.0D;
	}
}
