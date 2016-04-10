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

package matteroverdrive.starmap.data;

import io.netty.buffer.ByteBuf;
import matteroverdrive.api.starmap.GalacticPosition;
import matteroverdrive.util.MOLog;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.network.ByteBufUtils;

/**
 * Created by Simeon on 6/28/2015.
 */
public class TravelEvent
{
	//region Private Vars
	private long timeStart;
	private int timeLength;
	private GalacticPosition from, to;
	private ItemStack ship;
	//endregion

	//region Constructors
	public TravelEvent()
	{

	}

	public TravelEvent(NBTTagCompound tagCompound)
	{
		readFromNBT(tagCompound);
	}

	public TravelEvent(ByteBuf buf)
	{
		readFromBuffer(buf);
	}

	public TravelEvent(World world, GalacticPosition from, GalacticPosition to, ItemStack shipStack, Galaxy galaxy)
	{
		timeStart = world.getTotalWorldTime();
		this.from = from;
		this.to = to;
		this.ship = shipStack;
		calculateTravelTime(galaxy, from, to);
	}
	//endregion

	private void calculateTravelTime(Galaxy galaxy, GalacticPosition from, GalacticPosition to)
	{
		this.timeLength = (int)(from.distanceToLY(galaxy, to) * Galaxy.LY_TO_TICKS);
		if (this.timeLength == 0)
		{
			this.timeLength = (int)(from.distanceToAU(galaxy, to) * Galaxy.AU_TO_TICKS);
		}
	}

	//region Read - Write
	public void writeToNBT(NBTTagCompound tagCompound)
	{
		NBTTagCompound shipStackNBT = new NBTTagCompound();
		if (ship != null)
		{
			ship.writeToNBT(shipStackNBT);
			tagCompound.setTag("Ship", shipStackNBT);
		}
		tagCompound.setInteger("TimeLength", timeLength);
		tagCompound.setLong("TimeStart", timeStart);
		tagCompound.setTag("From", from.toNBT());
		tagCompound.setTag("To", to.toNBT());
	}

	public void readFromNBT(NBTTagCompound tagCompound)
	{
		from = new GalacticPosition(tagCompound.getCompoundTag("From"));
		to = new GalacticPosition(tagCompound.getCompoundTag("To"));
		if (tagCompound.hasKey("Ship", Constants.NBT.TAG_COMPOUND))
		{
			try
			{
				ship = ItemStack.loadItemStackFromNBT(tagCompound.getCompoundTag("Ship"));
			}
			catch (Exception e)
			{
				MOLog.warn("Could not load ship from NBT in travel event", e);
			}
		}
		timeLength = tagCompound.getInteger("TimeLength");
		timeStart = tagCompound.getLong("TimeStart");
	}

	public void readFromBuffer(ByteBuf buf)
	{
		from = new GalacticPosition(buf);
		to = new GalacticPosition(buf);
		ship = ByteBufUtils.readItemStack(buf);
		timeLength = buf.readInt();
		timeStart = buf.readLong();
	}

	public void writeToBuffer(ByteBuf buf)
	{
		from.writeToBuffer(buf);
		to.writeToBuffer(buf);
		ByteBufUtils.writeItemStack(buf, ship);
		buf.writeInt(timeLength);
		buf.writeLong(timeStart);
	}
	//endregion

	//region Getters and Setters
	public int getTimeLength()
	{
		return (int)Math.ceil(timeLength * Galaxy.GALAXY_TRAVEL_TIME_MULTIPLY);
	}

	public void setTimeLength(int timeLength)
	{
		this.timeLength = timeLength;
	}

	public long getTimeStart()
	{
		return timeStart;
	}

	public void setTimeStart(long timeStart)
	{
		this.timeStart = timeStart;
	}

	public long getTimeRemainning(World world)
	{
		return (timeStart + getTimeLength()) - world.getTotalWorldTime();
	}

	public double getPercent(World world)
	{
		return 1d - (double)((timeStart + timeLength) - world.getTotalWorldTime()) / (double)timeLength;
	}

	public ItemStack getShip()
	{
		return ship;
	}

	public void setShip(ItemStack ship)
	{
		this.ship = ship;
	}

	public GalacticPosition getTo()
	{
		return to;
	}

	public void setTo(GalacticPosition to)
	{
		this.to = to;
	}

	public GalacticPosition getFrom()
	{
		return from;
	}

	public void setFrom(GalacticPosition from)
	{
		this.from = from;
	}

	public NBTTagCompound toNBT()
	{
		NBTTagCompound tagCompound = new NBTTagCompound();
		writeToNBT(tagCompound);
		return tagCompound;
	}

	public boolean isValid(Galaxy galaxy)
	{
		if (this.from != null && this.to != null)
		{
			Planet from = galaxy.getPlanet(this.from);
			Planet to = galaxy.getPlanet(this.to);
			if (from != null && to != null)
			{
				return ship != null;
			}
		}
		return false;
	}

	public boolean isComplete(World world)
	{
		return getTimeRemainning(world) <= 0;
	}
	//endregion
}
