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
import matteroverdrive.starmap.GalaxyGenerator;
import matteroverdrive.starmap.gen.ISpaceBodyGen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Simeon on 6/13/2015.
 */
public class Star extends SpaceBody
{
    //region Private Vars
    private Quadrant quadrant;
    private HashMap<Integer,Planet> planetHashMap;
    private float x,y,z,size,mass;
    private byte type;
    private int temperature,color,seed;
    private boolean generated;
    private boolean isDirty;
    //endregion

    //region Constructors
    public Star()
    {
        super();
        init();
    }

    public Star(String name,int id)
    {
        super(name,id);
        init();
    }
    //endregion

    public void generateMissing(NBTTagCompound tagCompound,GalaxyGenerator galaxyGenerator)
    {
        if (galaxyGenerator != null) {
            for (ISpaceBodyGen<Star> starGen : galaxyGenerator.getStarGen().getGens()) {
                galaxyGenerator.getStarRandom().setSeed(seed);
                if (starGen.generateMissing(tagCompound, this, galaxyGenerator.getStarRandom())) {
                    break;
                }
            }

            for (Planet planet : getPlanets()) {
                planet.generateMissing(tagCompound, galaxyGenerator);
            }
        }
    }

    //region Updates
    public void update(World world)
    {
        for (Planet planet : getPlanets())
        {
            planet.update(world);
        }
    }
    //endregion

    //region Events
    public void onSave(File file,World world)
    {
        isDirty = false;

        for (Planet planet : getPlanets())
        {
            planet.onSave(file, world);
        }
    }
    //endregion

    //region Read - Write
    @Override
    public void writeToNBT(NBTTagCompound tagCompound)
    {
        super.writeToNBT(tagCompound);
        tagCompound.setFloat("X", x);
        tagCompound.setFloat("Y", y);
        tagCompound.setFloat("Z", z);
        tagCompound.setFloat("Size", size);
        tagCompound.setFloat("Mass",mass);
        tagCompound.setByte("Type", type);
        tagCompound.setInteger("Temperature", temperature);
        tagCompound.setInteger("Color",color);
        tagCompound.setInteger("Seed",seed);
        tagCompound.setBoolean("Generated",generated);
        NBTTagList planetList = new NBTTagList();
        for (Planet planet : getPlanets())
        {
            NBTTagCompound quadrantNBT = new NBTTagCompound();
            planet.writeToNBT(quadrantNBT);
            planetList.appendTag(quadrantNBT);
        }
        tagCompound.setTag("Planets", planetList);
    }

    @Override
    public void writeToBuffer(ByteBuf byteBuf)
    {
        super.writeToBuffer(byteBuf);
        byteBuf.writeFloat(x);
        byteBuf.writeFloat(y);
        byteBuf.writeFloat(z);
        byteBuf.writeFloat(size);
        byteBuf.writeFloat(mass);
        byteBuf.writeByte(type);
        byteBuf.writeInt(temperature);
        byteBuf.writeInt(color);
        /*int planetCount = getPlanets().size();
        byteBuf.writeInt(planetCount);
        for (Planet planet : getPlanets())
        {
            planet.writeToBuffer(byteBuf);
        }*/
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound,GalaxyGenerator generator)
    {
        super.readFromNBT(tagCompound, generator);
        x = tagCompound.getFloat("X");
        y = tagCompound.getFloat("Y");
        z = tagCompound.getFloat("Z");
        size = tagCompound.getFloat("Size");
        mass = tagCompound.getFloat("Mass");
        type = tagCompound.getByte("Type");
        temperature = tagCompound.getInteger("Temperature");
        color = tagCompound.getInteger("Color");
        seed = tagCompound.getInteger("Seed");
        generated = tagCompound.getBoolean("Generated");
        NBTTagList planetList = tagCompound.getTagList("Planets", 10);
        for (int i = 0;i < planetList.tagCount();i++)
        {
            Planet planet = new Planet();
            planet.readFromNBT(planetList.getCompoundTagAt(i),generator);
            addPlanet(planet);
            planet.setStar(this);
        }

        generateMissing(tagCompound, generator);
    }

    @Override
    public void readFromBuffer(ByteBuf byteBuf)
    {
        super.readFromBuffer(byteBuf);
        x = byteBuf.readFloat();
        y = byteBuf.readFloat();
        z = byteBuf.readFloat();
        size = byteBuf.readFloat();
        mass = byteBuf.readFloat();
        type = byteBuf.readByte();
        temperature = byteBuf.readInt();
        color = byteBuf.readInt();
        /*int planetCount = byteBuf.readInt();
        for (int i = 0;i < planetCount;i++)
        {
            Planet planet = new Planet();
            planet.readFromBuffer(byteBuf);
            addPlanet(planet);
            planet.setStar(this);
        }*/
    }
    //endregion

    //region Getters and Setters
    @Override
    public SpaceBody getParent() {
        return quadrant;
    }
    public Planet planet(int at){return planetHashMap.get(at);}
    public boolean hasPlanet(int id){return planetHashMap.containsKey(id);}
    private void init()
    {
        planetHashMap = new HashMap<Integer, Planet>();
    }
    public Collection<Planet> getPlanets()
    {
        return planetHashMap.values();
    }
    public Map<Integer,Planet> getPlanetMap()
    {
        return planetHashMap;
    }
    public void addPlanet(Planet planet)
    {
        getPlanetMap().put(planet.getId(), planet);
    }
    public void setPosition(float x,float y,float z){this.x = x;this.y = y;this.z = z;}
    public Vec3 getPosition(){return getPosition(1);}
    public Vec3 getPosition(double multipy){return Vec3.createVectorHelper(x * multipy, y * multipy, z * multipy);}
    public float getX(){return x;}
    public float getY(){return y;}
    public float getZ(){return z;}
    public void setQuadrant(Quadrant quadrant) {this.quadrant = quadrant;}
    public Quadrant getQuadrant(){return quadrant;}
    public void setSize(float size){this.size = size;}
    public float getSize(){return size;}
    public void setType(byte type){this.type = type;}
    public byte getType(){return this.type;}
    public void setTemperature(int temperature){this.temperature = temperature;}
    public int getTemperature(){return temperature;}
    public int getColor(){return color;}
    public void setColor(int color){this.color = color;}
    public void clearPlanets(){planetHashMap.clear();}
    public int getSeed(){return seed;}
    public void setSeed(int seed){this.seed = seed;}
    public boolean isGenerated(){return generated;}
    public void setGenerated(boolean generated){this.generated = generated;}
    public void setMass(float mass){this.mass = mass;}
    public float getMass(){return mass;}
    public int isClaimed(EntityPlayer player)
    {
        int ownType = 0;
        for (Planet planet : getPlanets())
        {
            if (planet.hasOwner())
            {
                if (planet.getOwnerUUID().equals(EntityPlayer.func_146094_a(player.getGameProfile())))
                {
                    if (planet.isHomeworld())
                        if (ownType < 3)
                        {
                            ownType = 3;
                        }
                    else
                        if (ownType < 2)
                        {
                            ownType = 2;
                        }
                }

                if (ownType < 1)
                {
                    ownType = 1;
                }
            }
        }
        return ownType;
    }
    public boolean isClaimed()
    {
        for (Planet planet : getPlanets()) {
            if (planet.hasOwner()) {
                return true;
            }
        }
        return false;
    }
    public boolean isDirty()
    {
        if (isDirty)
        {
            return true;
        }else
        {
            for (Planet planet : getPlanets())
            {
                if (planet.isDirty())
                {
                    return true;
                }
            }
        }
        return false;
    }
    public void markDirty(){this.isDirty = true;}
    //endregion
}
