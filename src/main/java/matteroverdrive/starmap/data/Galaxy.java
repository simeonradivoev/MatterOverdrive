package matteroverdrive.starmap.data;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.network.packet.client.starmap.PacketUpdateTravelEvents;
import matteroverdrive.starmap.GalaxyGenerator;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;

import java.util.*;

/**
 * Created by Simeon on 6/13/2015.
 */
public class Galaxy extends SpaceBody
{
    public static float GALAXY_SIZE_TO_LY = 8000;
    public static float LY_TO_TICKS = 5;


    long seed;
    HashMap<Integer,Quadrant> quadrantHashMap;
    List<TravelEvent> travelEvents;
    World world;
    int version;

    public Galaxy()
    {
        super();
        init();
    }

    public Galaxy(String name,int id,long seed)
    {
        super(name, id);
        init();
        setSeed(seed);
    }

    private void init()
    {
        quadrantHashMap = new HashMap<Integer, Quadrant>();
        travelEvents = new ArrayList<TravelEvent>();
    }

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
        for (int i = 0; i < travelEvents.size();i++)
        {
            travelEvents.get(i).writeToBuffer(buf);
        }
    }

    public void readFromNBT(NBTTagCompound tagCompound,GalaxyGenerator generator)
    {
        super.readFromNBT(tagCompound, generator);
        quadrantHashMap.clear();
        travelEvents.clear();

        version = tagCompound.getInteger("Version");
        NBTTagList quadrantList = tagCompound.getTagList("Quadrants", 10);
        for (int i = 0;i < quadrantList.tagCount();i++)
        {
            Quadrant quadrant = new Quadrant();
            quadrant.readFromNBT(quadrantList.getCompoundTagAt(i), generator);
            addQuadrant(quadrant);
            quadrant.setGalaxy(this);
        }
        NBTTagList travelEventsList = tagCompound.getTagList("TravelEvents", 10);
        for (int i = 0;i < travelEventsList.tagCount();i++)
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
        for (int i = 0;i < size;i++)
        {
            Quadrant quadrant = new Quadrant();
            quadrant.readFromBuffer(buf);
            addQuadrant(quadrant);
            quadrant.setGalaxy(this);
        }
        int travelEventsSize = buf.readInt();
        for (int i = 0;i < travelEventsSize;i++)
        {
            TravelEvent travelEvent = new TravelEvent(buf);
            travelEvents.add(travelEvent);
        }
    }

    public void update(World world)
    {
        manageTravelEvents(world);

        for (Quadrant quadrant : getQuadrants())
        {
            quadrant.update(world);
        }
    }

    private void manageDirty(World world)
    {
        if (world.isRemote)
        {

        }
    }

    private void manageTravelEvents(World world)
    {
        Iterator<TravelEvent> travelEventIterator = travelEvents.iterator();

        while (travelEventIterator.hasNext())
        {
            TravelEvent travelEvent = travelEventIterator.next();

            if (travelEvent.isValid(this)) {

                if (travelEvent.isComplete(world))
                {
                    if (!world.isRemote)
                    {
                        Planet to = getPlanet(travelEvent.getTo());
                        Planet from = getPlanet(travelEvent.getFrom());
                        if (to != null) {
                            ItemStack ship = from.removeShip(travelEvent.getShipID());
                            if (ship != null) {
                                to.addShip(ship);
                                from.markDirty();
                                to.markDirty();
                                to.onTravelEvent(ship,travelEvent.getFrom());
                                MatterOverdrive.packetPipeline.sendToDimention(new PacketUpdateTravelEvents(this), world);
                            }
                        }
                    }

                    travelEventIterator.remove();
                }
            }else
            {
                travelEventIterator.remove();
            }
        }
    }

    //region Getters and Setters
    @Override
    public SpaceBody getParent() {
        return null;
    }

    public Map<Integer,Quadrant> getQuadrantMap()
    {
        return quadrantHashMap;
    }

    public Collection<Quadrant> getQuadrants()
    {
        return quadrantHashMap.values();
    }


    public Quadrant quadrant(int at){return quadrantHashMap.get(at);}

    public void addQuadrant(Quadrant quadrant)
    {
        quadrantHashMap.put(quadrant.getId(),quadrant);
    }

    public int getQuadrantCount(){return quadrantHashMap.size();}

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

    public Planet getPlanet(GalacticPosition position) {
        Star star = getStar(position);
        if (star != null && position.planetID >= 0) {
            if (star.hasPlanet(position.planetID)) {
                return star.planet(position.planetID);
            }
        }
        return null;
    }

    public Star getStar(GalacticPosition position)
    {
        Quadrant quadrant = getQuadrant(position);
        if (quadrant != null && position.starID >= 0) {
            if (quadrant.hasStar(position.starID)) {
                return quadrant.star(position.starID);
            }
        }
        return null;
    }

    public Quadrant getQuadrant(GalacticPosition position)
    {
        if (position.quadrantID >= 0 && quadrantHashMap.containsKey(position.quadrantID))
        {
            return quadrantHashMap.get(position.quadrantID);
        }
        return null;
    }

    public void setSeed(long seed)
    {
        this.seed = seed;
    }
    public long getSeed()
    {
        return seed;
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
                if( star.isClaimed(player) > 1)
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
                if( star.isClaimed(player) == 1)
                {
                    count++;
                }
            }
        }
        return count;
    }

    public boolean addTravelEvent(TravelEvent travelEvent)
    {
        for (TravelEvent event : travelEvents)
        {
            if (event.getFrom().equals(travelEvent.getFrom()) && event.getShipID() == travelEvent.getShipID())
            {
                return false;
            }
        }
        travelEvents.add(travelEvent);
        return true;
    }
    public List<TravelEvent> getTravelEvents(){return travelEvents;}
    public void setTravelEvents(List<TravelEvent> travelEvents){this.travelEvents = travelEvents;}
    //endregion
}
