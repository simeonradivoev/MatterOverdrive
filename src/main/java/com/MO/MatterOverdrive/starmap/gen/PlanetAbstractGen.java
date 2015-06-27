package com.MO.MatterOverdrive.starmap.gen;

import com.MO.MatterOverdrive.starmap.data.Planet;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Random;

/**
 * Created by Simeon on 6/26/2015.
 */
public abstract class PlanetAbstractGen implements ISpaceBodyGen<Planet>
{
    byte type;
    int buildingSpaces,fleetSpaces;

    public PlanetAbstractGen(byte type,int buildingSpaces,int fleetSpaces)
    {
        this.type = type;
        this.buildingSpaces = buildingSpaces;
        this.fleetSpaces = fleetSpaces;
    }

    @Override
    public void generateSpaceBody(Planet planet,Random random)
    {
        planet.setType((byte)2);
        setSize(planet,random);
        planet.setBuildingSpaces(buildingSpaces);
        planet.setFleetSpaces(fleetSpaces);
    }

    @Override
    public boolean generateMissing(NBTTagCompound tagCompound, Planet planet, Random random)
    {
        if (planet.getType() == type) {
            if (!tagCompound.hasKey("Type", 1)) {
                planet.setType(type);
            }
            if (!tagCompound.hasKey("Size", 5)) {
                setSize(planet, random);
            }
            if (!tagCompound.hasKey("BuildingSpaces", 3)) {
                planet.setBuildingSpaces(buildingSpaces);
            }
            if (!tagCompound.hasKey("FleetSpaces", 3)) {
                planet.setFleetSpaces(fleetSpaces);
            }
            return true;
        }
        return false;
    }

    protected abstract void setSize(Planet planet,Random random);
}
