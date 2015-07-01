package matteroverdrive.starmap.data;

import matteroverdrive.starmap.GalaxyGenerator;
import matteroverdrive.starmap.gen.ISpaceBodyGen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Simeon on 6/13/2015.
 */
public class Star extends SpaceBody
{
    Quadrant quadrant;
    HashMap<Integer,Planet> planetHashMap;
    float x,y,z,size,mass;
    byte type;
    int temperature,color,seed;
    boolean generated;
    boolean isDirty;

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
        tagCompound.setTag("Planets",planetList);
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

    public void update(World world)
    {
        for (Planet planet : getPlanets())
        {
            planet.update(world);
        }
    }

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
    public boolean isDirty(){return isDirty;}
    public void setDirty(boolean isDirty){this.isDirty = isDirty;}
}
