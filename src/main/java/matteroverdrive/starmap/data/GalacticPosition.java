package matteroverdrive.starmap.data;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by Simeon on 6/17/2015.
 */
public class GalacticPosition
{
    int quadrantID = -1;
    int starID = -1;
    int planetID = -1;

    public GalacticPosition()
    {

    }

    public GalacticPosition(GalacticPosition other)
    {
        quadrantID = other.quadrantID;
        starID = other.starID;
        planetID = other.planetID;
    }

    public GalacticPosition(int quadrantID,int starID,int planetID)
    {
        setPosition(quadrantID,starID,planetID);
    }

    public GalacticPosition(Planet planet)
    {
        this.planetID = planet.getId();
        if (planet.getStar() != null)
        {
            this.starID = planet.getStar().getId();
            if (planet.getStar().getQuadrant() != null)
            {
                this.quadrantID = planet.getStar().getQuadrant().getId();
            }
        }
    }

    public GalacticPosition(Star star)
    {
        this.starID = star.getId();
        if (star.getQuadrant() != null)
        {
            quadrantID = star.getQuadrant().getId();
        }
    }

    public GalacticPosition(Quadrant quadrant)
    {
        this.quadrantID = quadrant.getId();
    }

    public GalacticPosition(NBTTagCompound tagCompound)
    {
        super();
        readFromNBT(tagCompound);
    }

    public GalacticPosition(ByteBuf buf)
    {
        super();
        readFromBuffer(buf);
    }

    public void setPosition(int quadrantID,int starID,int planetID)
    {
        this.quadrantID = quadrantID;
        this.starID = starID;
        this.planetID = planetID;
    }

    public void writeToNBT(NBTTagCompound tagCompound)
    {
        tagCompound.setInteger("GalacticPositionPlanet",planetID);
        tagCompound.setInteger("GalacticPositionStar",starID);
        tagCompound.setInteger("GalacticPositionQuadrant",quadrantID);
    }

    public void readFromNBT(NBTTagCompound tagCompound)
    {
        if (tagCompound.hasKey("GalacticPositionPlanet",3))
            planetID = tagCompound.getInteger("GalacticPositionPlanet");
        if (tagCompound.hasKey("GalacticPositionStar",3))
            starID = tagCompound.getInteger("GalacticPositionStar");
        if (tagCompound.hasKey("GalacticPositionQuadrant",3))
            quadrantID = tagCompound.getInteger("GalacticPositionQuadrant");
    }

    public void writeToBuffer(ByteBuf buf)
    {
        buf.writeInt(planetID);
        buf.writeInt(starID);
        buf.writeInt(quadrantID);
    }

    public void readFromBuffer(ByteBuf buf)
    {
        planetID = buf.readInt();
        starID = buf.readInt();
        quadrantID = buf.readInt();
    }

    public boolean equals(Star star)
    {
        if (star != null && starID == star.getId())
        {
            return quadrantID >= 0 && star.getQuadrant() != null && star.getQuadrant().getId() == quadrantID;
        }
        return false;
    }

    public boolean equals(Planet planet)
    {
        if (planetID == planet.getId())
        {
            return equals(planet.getStar());
        }
        return false;
    }

    public boolean equals(Quadrant quadrant)
    {
        return quadrantID == quadrant.getId();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
            return false;

        if (obj.hashCode() == hashCode())
        {
            return true;
        }

        if (obj instanceof GalacticPosition) {
            return planetID == ((GalacticPosition) obj).planetID && starID == ((GalacticPosition) obj).starID && quadrantID == ((GalacticPosition) obj).quadrantID;
        }
        return false;
    }

    public int getStarID()
    {
        return starID;
    }

    public int getQuadrantID()
    {
        return quadrantID;
    }

    public int getPlanetID()
    {
        return planetID;
    }

    public NBTTagCompound toNBT()
    {
        NBTTagCompound tagCompound = new NBTTagCompound();
        writeToNBT(tagCompound);
        return tagCompound;
    }
}
