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
import matteroverdrive.MatterOverdrive;
import matteroverdrive.api.starmap.GalacticPosition;
import matteroverdrive.api.starmap.IShip;
import matteroverdrive.network.packet.client.starmap.PacketUpdateTravelEvents;
import matteroverdrive.starmap.GalaxyGenerator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;

import java.io.File;
import java.util.*;

/**
 * Created by Simeon on 6/13/2015.
 */
public class Galaxy extends SpaceBody
{
	//region Static Vars
	public static final float GALAXY_SIZE_TO_LY = 8000;
	public static final float LY_TO_TICKS = 8;
	public static final float AU_TO_TICKS = 10;
	public static final float PLANET_SYSTEM_SIZE_TO_AU = 100;
	public static float GALAXY_BUILD_TIME_MULTIPLY = 1;
	public static float GALAXY_TRAVEL_TIME_MULTIPLY = 1;
	//endregion
	//region Private Vars
	private long seed;
	private HashMap<Integer, Quadrant> quadrantHashMap;
	private List<TravelEvent> travelEvents;
	private World world;
	private int version;
	private boolean isDirty;
	private Iterator<Quadrant> quadrantUpdateIterator;
	//endregion

	//region Constructors
	public Galaxy()
	{
		this(null);
	}

	public Galaxy(World world)
	{
		super();
		init();
		this.world = world;
	}

	public Galaxy(String name, int id, long seed, World world)
	{
		super(name, id);
		init();
		setSeed(seed);
		this.world = world;
	}
	//endregion

	private void init()
	{
		quadrantHashMap = new HashMap<>();
		travelEvents = new ArrayList<>();
		quadrantUpdateIterator = getQuadrants().iterator();
	}

	//region update functions
	public void update(World world)
	{
		manageTravelEvents(world);

		try
		{
			if (quadrantUpdateIterator.hasNext())
			{
				quadrantUpdateIterator.next().update(world);
			}
			else
			{
				quadrantUpdateIterator = getQuadrants().iterator();
			}
		}
		catch (Exception e)
		{
			quadrantUpdateIterator = getQuadrants().iterator();
		}

	}

	private void manageTravelEvents(World world)
	{
		Iterator<TravelEvent> travelEventIterator = travelEvents.iterator();

		while (travelEventIterator.hasNext())
		{
			TravelEvent travelEvent = travelEventIterator.next();

			if (travelEvent.isValid(this))
			{

				if (travelEvent.isComplete(world))
				{
					if (!world.isRemote)
					{
						Planet to = getPlanet(travelEvent.getTo());
						Planet from = getPlanet(travelEvent.getFrom());
						if (to != null)
						{
							to.addShip(travelEvent.getShip());
							from.markDirty();
							to.markDirty();
							to.onTravelEvent(travelEvent.getShip(), travelEvent.getFrom(), world);
							MatterOverdrive.packetPipeline.sendToDimention(new PacketUpdateTravelEvents(this), world);
						}
					}
					travelEventIterator.remove();
				}
			}
			else
			{
				travelEventIterator.remove();
			}
		}
	}
	//endregion

	//region Events
	public void onSave(File file, World world)
	{
		isDirty = false;

		for (Quadrant quadrant : getQuadrants())
		{
			quadrant.onSave(file, world);
		}
	}
	//endregion

	//region Read - Write Funtions
	@Override
	public void writeToNBT(NBTTagCompound tagCompound)
	{
		super.writeToNBT(tagCompound);
		tagCompound.setInteger("Version", version);
		NBTTagList quadrantList = new NBTTagList();
		for (Quadrant quadrant : getQuadrants())
		{
			NBTTagCompound quadrantNBT = new NBTTagCompound();
			quadrant.writeToNBT(quadrantNBT);
			quadrantList.appendTag(quadrantNBT);
		}
		tagCompound.setTag("Quadrants", quadrantList);
		NBTTagList travelEventsList = new NBTTagList();
		for (TravelEvent travelEvent : travelEvents)
		{
			travelEventsList.appendTag(travelEvent.toNBT());
		}
		tagCompound.setTag("TravelEvents", travelEventsList);
	}

	public void writeToBuffer(ByteBuf buf)
	{
		buf.writeInt(version);
		buf.writeInt(getQuadrants().size());
		for (Quadrant quadrant : getQuadrants())
		{
			quadrant.writeToBuffer(buf);
		}
		buf.writeInt(travelEvents.size());
		for (TravelEvent travelEvent : travelEvents)
		{
			travelEvent.writeToBuffer(buf);
		}
	}

	public void readFromNBT(NBTTagCompound tagCompound, GalaxyGenerator generator)
	{
		super.readFromNBT(tagCompound, generator);
		quadrantHashMap.clear();
		travelEvents.clear();

		version = tagCompound.getInteger("Version");
		NBTTagList quadrantList = tagCompound.getTagList("Quadrants", 10);
		for (int i = 0; i < quadrantList.tagCount(); i++)
		{
			Quadrant quadrant = new Quadrant();
			quadrant.readFromNBT(quadrantList.getCompoundTagAt(i), generator);
			addQuadrant(quadrant);
			quadrant.setGalaxy(this);
		}
		NBTTagList travelEventsList = tagCompound.getTagList("TravelEvents", 10);
		for (int i = 0; i < travelEventsList.tagCount(); i++)
		{
			travelEvents.add(new TravelEvent(travelEventsList.getCompoundTagAt(i)));
		}
	}

	public void readFromBuffer(ByteBuf buf)
	{
		quadrantHashMap.clear();
		travelEvents.clear();

		version = buf.readInt();
		int size = buf.readInt();
		for (int i = 0; i < size; i++)
		{
			Quadrant quadrant = new Quadrant();
			quadrant.readFromBuffer(buf);
			addQuadrant(quadrant);
			quadrant.setGalaxy(this);
		}
		int travelEventsSize = buf.readInt();
		for (int i = 0; i < travelEventsSize; i++)
		{
			TravelEvent travelEvent = new TravelEvent(buf);
			travelEvents.add(travelEvent);
		}
	}
	//endregion

	//region Getters and Setters
	@Override
	public SpaceBody getParent()
	{
		return null;
	}

	public Map<Integer, Quadrant> getQuadrantMap()
	{
		return quadrantHashMap;
	}

	public Collection<Quadrant> getQuadrants()
	{
		return quadrantHashMap.values();
	}

	public Quadrant quadrant(int at)
	{
		return quadrantHashMap.get(at);
	}

	public void addQuadrant(Quadrant quadrant)
	{
		quadrantHashMap.put(quadrant.getId(), quadrant);
	}

	public int getQuadrantCount()
	{
		return quadrantHashMap.size();
	}

	public int getStarCount()
	{
		int count = 0;
		for (Quadrant quadrant : getQuadrants())
		{
			count += quadrant.getStars().size();
		}
		return count;
	}

	public void setWorld(World world)
	{
		this.world = world;
	}

	public Planet getPlanet(GalacticPosition position)
	{
		Star star = getStar(position);
		if (star != null && position.getPlanetID() >= 0)
		{
			if (star.hasPlanet(position.getPlanetID()))
			{
				return star.planet(position.getPlanetID());
			}
		}
		return null;
	}

	public Star getStar(GalacticPosition position)
	{
		Quadrant quadrant = getQuadrant(position);
		if (quadrant != null && position.getStarID() >= 0)
		{
			if (quadrant.hasStar(position.getStarID()))
			{
				return quadrant.star(position.getStarID());
			}
		}
		return null;
	}

	public Quadrant getQuadrant(GalacticPosition position)
	{
		if (position.getQuadrantID() >= 0 && quadrantHashMap.containsKey(position.getQuadrantID()))
		{
			return quadrantHashMap.get(position.getQuadrantID());
		}
		return null;
	}

	public long getSeed()
	{
		return seed;
	}

	public void setSeed(long seed)
	{
		this.seed = seed;
	}

	public int getVersion()
	{
		return version;
	}

	public int getOwnedSystemCount(EntityPlayer player)
	{
		int count = 0;
		for (Quadrant quadrant : getQuadrants())
		{
			for (Star star : quadrant.getStars())
			{
				if (star.isClaimed(player) > 1)
				{
					count++;
				}
			}
		}
		return count;
	}

	public int getEnemySystemCount(EntityPlayer player)
	{
		int count = 0;
		for (Quadrant quadrant : getQuadrants())
		{
			for (Star star : quadrant.getStars())
			{
				if (star.isClaimed(player) == 1)
				{
					count++;
				}
			}
		}
		return count;
	}

	public boolean addTravelEvent(TravelEvent travelEvent)
	{
		travelEvents.add(travelEvent);
		return true;
	}

	public boolean canCompleteTravelEvent(TravelEvent travelEvent)
	{
		if (travelEvent.getTo() != null)
		{
			Planet to = getPlanet(travelEvent.getTo());
			ItemStack shipStack = travelEvent.getShip();
			if (shipStack != null && to != null)
			{
				EntityPlayer owner = null;
				UUID ownerID = ((IShip)shipStack.getItem()).getOwnerID(shipStack);
				if (ownerID != null)
				{
					owner = world.getPlayerEntityByUUID(ownerID);
				}
				return to.canAddShip(shipStack, owner);
			}
		}
		return false;
	}

	public List<TravelEvent> getTravelEvents()
	{
		return travelEvents;
	}

	public void setTravelEvents(List<TravelEvent> travelEvents)
	{
		this.travelEvents = travelEvents;
	}

	public boolean isDirty()
	{
		if (isDirty)
		{
			return true;
		}
		else
		{
			for (Quadrant quadrant : getQuadrants())
			{
				if (quadrant.isDirty())
				{
					return true;
				}
			}
		}
		return false;
	}

	public void markDirty()
	{
		this.isDirty = true;
	}
	//endregion
}
