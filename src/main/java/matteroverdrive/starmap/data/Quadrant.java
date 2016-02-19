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
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Simeon on 6/13/2015.
 */
public class Quadrant extends SpaceBody
{
    //region Private Vars
    private Galaxy galaxy;
    private HashMap<Integer,Star> starHashMap;
    private boolean loaded;
    private float size;
    private float x,y,z;
    private boolean isDirty;
    //endregion

    //region Constructors
    public Quadrant()
    {
        super();
        init();
    }

    public Quadrant(String name,int id)
    {
        super(name,id);
        init();
    }
    //endregion

    //region Updates
    public void update(World world)
    {
        for (Star star : getStars())
        {
            star.update(world);
        }
    }
    //endregion

    public void generateMissing(NBTTagCompound tagCompound,GalaxyGenerator galaxyGenerator)
    {
        for (Star star : getStars())
        {
            star.generateMissing(tagCompound, galaxyGenerator);
        }
    }

    //region Events
    public void onSave(File file,World world)
    {
        isDirty = false;

        for (Star star : getStars())
        {
            star.onSave(file, world);
        }
    }
    //endregion

    //region Read - Write
    @Override
    public void writeToNBT(NBTTagCompound tagCompound)
    {
        super.writeToNBT(tagCompound);
        NBTTagList starList = new NBTTagList();
        tagCompound.setFloat("X", x);
        tagCompound.setFloat("Y", y);
        tagCompound.setFloat("Z", z);
        tagCompound.setFloat("Size", size);
        for (Star star : getStars())
        {
            NBTTagCompound quadrantNBT = new NBTTagCompound();
            star.writeToNBT(quadrantNBT);
            starList.appendTag(quadrantNBT);
        }
        tagCompound.setTag("Stars", starList);
    }

    public void writeToBuffer(ByteBuf buf)
    {
        super.writeToBuffer(buf);
        buf.writeFloat(x);
        buf.writeFloat(y);
        buf.writeFloat(z);
        buf.writeFloat(size);
        buf.writeInt(getStars().size());
        for (Star star : getStars())
        {
            star.writeToBuffer(buf);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound,GalaxyGenerator generator)
    {
        super.readFromNBT(tagCompound, generator);
        if (tagCompound != null) {
            x = tagCompound.getFloat("X");
            y = tagCompound.getFloat("Y");
            z = tagCompound.getFloat("Z");
            size = tagCompound.getFloat("Size");
            NBTTagList starList = tagCompound.getTagList("Stars", 10);
            for (int i = 0; i < starList.tagCount(); i++) {
                Star star = new Star();
                star.readFromNBT(starList.getCompoundTagAt(i),generator);
                addStar(star);
                star.setQuadrant(this);
            }
        }
    }

    public void readFromBuffer(ByteBuf buf)
    {
        super.readFromBuffer(buf);
        x = buf.readFloat();
        y = buf.readFloat();
        z = buf.readFloat();
        size = buf.readFloat();
        int starCount = buf.readInt();
        for (int i = 0;i < starCount;i++)
        {
            Star star = new Star();
            star.readFromBuffer(buf);
            addStar(star);
            star.setQuadrant(this);
        }
    }
    //endregion

    //region Getters and Setters
    @Override
    public SpaceBody getParent() {
        return galaxy;
    }
    private void init()
    {
        starHashMap = new HashMap<>();
    }
    public Star star(int at){return starHashMap.get(at);}
    public boolean hasStar(int id){
        return starHashMap.containsKey(id);
    }
    private boolean isLoaded()
    {
        return loaded;
    }
    public Collection<Star> getStars()
    {
        return starHashMap.values();
    }
    public Map<Integer,Star> getStarMap()
    {
        return starHashMap;
    }
    public void addStar(Star star)
    {
        starHashMap.put(star.getId(),star);
    }
    public void setGalaxy(Galaxy galaxy)
    {
        this.galaxy = galaxy;
    }
    public Galaxy getGalaxy()
    {
        return galaxy;
    }
    public float getX(){return x;}
    public float getY(){return y;}
    public float getZ(){return z;}
    public float getSize(){return size;}
    public boolean isDirty()
    {
        if (isDirty)
        {
            return true;
        }else
        {
            for (Star star : getStars())
            {
                if (star.isDirty())
                {
                    return true;
                }
            }
        }
        return false;
    }
    public void markDirty(){this.isDirty = true;}
    public void setPosition(float x,float y,float z) {this.x = x;this.y = y;this.z = z;}
    public void setSize(float size)
    {
        this.size = size;
    }
    //endregion
}
