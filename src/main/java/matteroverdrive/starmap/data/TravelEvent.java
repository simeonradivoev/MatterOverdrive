package matteroverdrive.starmap.data;

import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

/**
 * Created by Simeon on 6/28/2015.
 */
public class TravelEvent
{
    long timeStart;
    int timeLength,shipID;
    GalacticPosition from,to;

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

    public TravelEvent(World world,GalacticPosition from,GalacticPosition to,int shipID,int timeLength)
    {
        timeStart = world.getTotalWorldTime();
        this.from = from;
        this.to = to;
        this.shipID = shipID;
        this.timeLength = timeLength;
    }

    public boolean isComplete(World world)
    {
       return timeStart + timeLength <= world.getTotalWorldTime();
    }

    public boolean isValid(Galaxy galaxy)
    {
        if (this.from != null && this.to != null) {
            Planet from = galaxy.getPlanet(this.from);
            Planet to = galaxy.getPlanet(this.to);
            if (from != null && to != null) {
                if (shipID >= 0 && shipID < from.fleetCount()) {
                    return from.getShip(shipID) != null;
                }
            }
        }
        return false;
    }

    public ItemStack getShip(Galaxy galaxy)
    {
        if (isValid(galaxy))
        {
            return galaxy.getPlanet(from).getShip(shipID);
        }
        return null;
    }

    public void writeToNBT(NBTTagCompound tagCompound)
    {
        tagCompound.setInteger("ShipID", shipID);
        tagCompound.setInteger("TimeLength", timeLength);
        tagCompound.setLong("TimeStart",timeStart);
        tagCompound.setTag("From", from.toNBT());
        tagCompound.setTag("To",to.toNBT());
    }

    public void readFromNBT(NBTTagCompound tagCompound)
    {
        from = new GalacticPosition(tagCompound.getCompoundTag("From"));
        to = new GalacticPosition(tagCompound.getCompoundTag("To"));
        shipID = tagCompound.getInteger("ShipID");
        timeLength = tagCompound.getInteger("TimeLength");
        timeStart = tagCompound.getLong("TimeStart");
    }

    public void readFromBuffer(ByteBuf buf)
    {
        from = new GalacticPosition(buf);
        to = new GalacticPosition(buf);
        shipID = buf.readInt();
        timeLength = buf.readInt();
        timeStart = buf.readLong();
    }

    public void writeToBuffer(ByteBuf buf)
    {
        from.writeToBuffer(buf);
        to.writeToBuffer(buf);
        buf.writeInt(shipID);
        buf.writeInt(timeLength);
        buf.writeLong(timeStart);
    }

    //region Getters and Setters
    public int getTimeLength() {
        return timeLength;
    }

    public void setTimeLength(int timeLength) {
        this.timeLength = timeLength;
    }

    public long getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(long timeStart) {
        this.timeStart = timeStart;
    }

    public int getShipID() {
        return shipID;
    }

    public void setShipID(int shipID) {
        this.shipID = shipID;
    }

    public GalacticPosition getTo() {
        return to;
    }

    public void setTo(GalacticPosition to) {
        this.to = to;
    }

    public GalacticPosition getFrom() {
        return from;
    }

    public void setFrom(GalacticPosition from) {
        this.from = from;
    }

    public NBTTagCompound toNBT()
    {
        NBTTagCompound tagCompound = new NBTTagCompound();
        writeToNBT(tagCompound);
        return tagCompound;
    }
    //endregion
}
