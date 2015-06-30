package com.MO.MatterOverdrive.starmap;

import com.MO.MatterOverdrive.handler.ConfigurationHandler;
import com.MO.MatterOverdrive.starmap.data.Galaxy;
import com.MO.MatterOverdrive.starmap.data.Planet;
import com.MO.MatterOverdrive.starmap.data.Quadrant;
import com.MO.MatterOverdrive.starmap.data.Star;
import com.MO.MatterOverdrive.starmap.gen.*;
import com.MO.MatterOverdrive.util.IConfigSubscriber;
import com.MO.MatterOverdrive.util.math.MOMathHelper;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Simeon on 6/13/2015.
 */
public class GalaxyGenerator implements IConfigSubscriber
{
    Random random,starRandom,planetRandom,starNameRandom;
    WeightedRandomSpaceGen<Planet> planetGen;
    WeightedRandomSpaceGen<Star> starGen;
    float StarPrefixChance = 1;
    float StarSufixChance = 0.8f;
    int maxStars = 2048 + 256;
    int minStars = 2048;
    int minPlanets = 1;
    int maxPlanets = 4;
    int quadrantCount = 3;

    public GalaxyGenerator()
    {
        random = new Random();
        starRandom = new Random();
        planetRandom = new Random();
        starNameRandom = new Random();
        planetGen = new WeightedRandomSpaceGen<Planet>(random);
        starGen = new WeightedRandomSpaceGen<Star>(random);
        planetGen.addGen(new PlanetGasGiantGen());
        planetGen.addGen(new PlanetDwarfGen());
        planetGen.addGen(new PlanetNormalGen());
        starGen.addGens(StarGen.getStarGens());
    }

    //region Single Generation
    public Galaxy generateGalaxy(String name,int id,long seed)
    {
        Galaxy galaxy = new Galaxy(name,id,seed);
        random.setSeed(seed);
        generateQuadrants(galaxy, quadrantCount);
        return galaxy;
    }

    public void generateStar(Star star,boolean forced,boolean generatePLanets)
    {
        if (star.isGenerated() || forced)
        {
            star.clearPlanets();
            starRandom.setSeed(star.getSeed());
            Vec3 starPos = generateStarPosition(starRandom);
            star.setPosition((float) starPos.xCoord, (float) starPos.yCoord, (float) starPos.zCoord);
            starGen.getRandomGen(star,starRandom).generateSpaceBody(star, starRandom);
            if (generatePLanets)
                generatePlanets(star, minPlanets + random.nextInt(maxPlanets - minPlanets));
        }
    }

    public void generatePlanet(Planet planet,boolean forced)
    {
        if (planet.isGenerated() || forced) {
            planetRandom.setSeed(planet.getSeed());
            float orbit = planetRandom.nextFloat();
            planet.setOrbit(orbit);
            planetGen.getRandomGen(planet, planetRandom).generateSpaceBody(planet, planetRandom);
        }
    }
    //endregion

    //region Massive Generation
    public void generateQuadrants(Galaxy galaxy,int size3d)
    {
        starNameRandom.setSeed(galaxy.getSeed());
        List<Star> stars = generateStars(minStars + random.nextInt(maxStars - minStars));
        Quadrant[] quadrants = new Quadrant[size3d * size3d * size3d];

        float quadrantPosPeace = 2f / (float)size3d;

        for (int i = 0;i < quadrants.length;i++)
        {
            float z = (i % size3d) * quadrantPosPeace - 1;
            float y = ((i / size3d) % size3d) * quadrantPosPeace - 1;
            float x = (i / (size3d * size3d)) * quadrantPosPeace - 1;

            quadrants[i] = new Quadrant("Q" + i,i);
            galaxy.addQuadrant(quadrants[i]);
            quadrants[i].setGalaxy(galaxy);
            quadrants[i].setSize(quadrantPosPeace);
            quadrants[i].setPosition(x, y, z);
        }

        for (Star star : stars)
        {
            for (int i = 0;i < quadrants.length;i++)
            {
                if (star.getX() >= quadrants[i].getX() && star.getX() < quadrants[i].getX() + quadrantPosPeace
                        && star.getY() >= quadrants[i].getY() && star.getY() < quadrants[i].getY() + quadrantPosPeace
                        && star.getZ() >= quadrants[i].getZ() && star.getZ() < quadrants[i].getZ() + quadrantPosPeace)
                {
                    star.setId(quadrants[i].getStars().size());
                    quadrants[i].addStar(star);
                    star.setQuadrant(quadrants[i]);
                }
            }
        }
    }

    public List<Star> generateStars(int amount)
    {
        List<Star> stars = new ArrayList<Star>(amount);
        List<String> names = StarGen.generateAvailableNames(starNameRandom, 18,StarPrefixChance,StarSufixChance);

        for (int i = 0;i < amount;i++)
        {
            Star star = new Star(names.get(i),i);
            int seed = random.nextInt();
            star.setSeed(seed);
            stars.add(star);
            generateStar(star, true, true);
        }
        return stars;
    }

    public void generatePlanets(Star star,int amount)
    {
        for (int i = 0;i < amount;i++)
        {
            Planet planet = new Planet(star.getName() + " " + i,i);
            planet.setSeed(random.nextInt());
            star.addPlanet(planet);
            planet.setStar(star);
            generatePlanet(planet,true);
        }
    }
    //endregion

    //region Regeneration
    public void regenerateQuadrants(Galaxy galaxy)
    {
        for (Quadrant quadrant : galaxy.getQuadrants())
        {
            regenerateStars(quadrant);
        }
    }

    public void regenerateStars(Quadrant quadrant)
    {
        for (Star star : quadrant.getStars())
        {
            if (!star.isClaimed())
            {
                starRandom.setSeed(star.getSeed());
                starGen.getRandomGen(star).generateSpaceBody(star, starRandom);
                regeneratePlanets(star);
            }
        }
    }

    public void regeneratePlanets(Star star)
    {
        for (Planet planet : star.getPlanets())
        {
            generatePlanet(planet,true);
        }
    }
    //endregion

    public Vec3 generateStarPosition(Random random)
    {
        double x = MathHelper.clamp_double(MOMathHelper.nextGaussian(random, 0, 1d / 3d), -1, 1);
        double y = MathHelper.clamp_double(MOMathHelper.nextGaussian(random, 0, 1d / 3d), -1, 1);
        double z = MathHelper.clamp_double(MOMathHelper.nextGaussian(random, 0, 1d / 3d), -1, 1);
        return Vec3.createVectorHelper(x,y,z);
    }

    public WeightedRandomSpaceGen<Planet> getPlanetGen(){return planetGen;}
    public WeightedRandomSpaceGen<Star> getStarGen(){return starGen;}
    public Random getPlanetRandom(){return planetRandom;}
    public Random getStarRandom(){return starRandom;}

    @Override
    public void onConfigChanged(ConfigurationHandler config)
    {
        StarPrefixChance = config.config.getFloat("name_prefix_chance", ConfigurationHandler.CATEGORY_STARMAP, 1, 0, 1, "The chance of adding a prefix to a Star System's name");
        StarSufixChance = config.config.getFloat("name_suffix_chance", ConfigurationHandler.CATEGORY_STARMAP, 0.8f, 0, 1, "The chance of adding a suffix to a Star System's name");
        minStars = config.config.getInt("min_star_count", ConfigurationHandler.CATEGORY_STARMAP, 2048, 0, 512000, "The minimum amount of stars in a galaxy");
        maxStars = config.config.getInt("max_star_count", ConfigurationHandler.CATEGORY_STARMAP, 2048 + 256, 0, 512000, "The maximum amount of stars in a galaxy");
        minPlanets = config.config.getInt("min_planet_count", ConfigurationHandler.CATEGORY_STARMAP, 1, 0, 8, "The minimum amount of planets per star system");
        maxPlanets = config.config.getInt("max_planet_count", ConfigurationHandler.CATEGORY_STARMAP,4,0,8,"The maximum amount of planets pre star system");
        quadrantCount = config.config.getInt("quadrant_count", ConfigurationHandler.CATEGORY_STARMAP,3,1,6,"The amount of quadrants the galaxy should be divided into. The amount is cubed. x ^ 3. For example 3 ^ 3 = 27 quadrants.");
    }
}
