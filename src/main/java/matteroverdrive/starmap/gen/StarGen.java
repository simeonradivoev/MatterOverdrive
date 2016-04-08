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

package matteroverdrive.starmap.gen;

import matteroverdrive.Reference;
import matteroverdrive.client.data.Color;
import matteroverdrive.starmap.data.Star;
import matteroverdrive.util.MOStringHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by Simeon on 6/24/2015.
 */
public class StarGen implements ISpaceBodyGen<Star>
{
    public static final ResourceLocation starNamesFile = new ResourceLocation(Reference.PATH_INFO + "star_names.txt");
    public static final ResourceLocation starPrefixesFile = new ResourceLocation(Reference.PATH_INFO + "star_prefixes.txt");
    public static final ResourceLocation starSuffixesFile = new ResourceLocation(Reference.PATH_INFO + "star_suffixes.txt");
    final float[] radiuses = new float[]{8.8f,6.6f,6.6f,1.8f,1.8f,1.4f,1.4f,1.15f,1.15f,0.96f,0.96f,0.7f,0.7f,0.2f};
    final int[] temperatures = new int[]{60000,30000,30000,10000,10000,7500,7500,6000,6000,5200,5200,3700,3700,2400};
    final float[] masses = new float[]{32,16,16,2.1f,2.1f,1.4f,1.4f,1.04f,1.04f,0.8f,0.8f,0.45f,0.45f,0.08f};
    final double[] weights = new double[]{0.00003,0.13,0.6,3,7.6,12.1,76.45};
    private static String[] prefixes,starNames, suffixes;
    int type;

    public StarGen(int type)
    {
        this.type = Math.min(type, weights.length - 1);
        starNames = MOStringHelper.readTextFile(starNamesFile).split(",");
        prefixes = MOStringHelper.readTextFile(starPrefixesFile).split(",");
        suffixes = MOStringHelper.readTextFile(starSuffixesFile).split(",");
    }

    public static List<StarGen> getStarGens()
    {
        List<StarGen> gens = new ArrayList<>(7);
        for (int i = 0;i < 7;i++)
        {
            gens.add(new StarGen(i));
        }
        return gens;
    }

    @Override
    public void generateSpaceBody(Star star, Random random)
    {
        setType(star);
        setSize(star,random);
        setMass(star,random);
        setTemperature(star,random);
        setColor(star);
    }

    @Override
    public boolean generateMissing(NBTTagCompound tagCompound, Star star, Random random)
    {
        if (!tagCompound.hasKey("Type",1))
        {
            setType(star);
        }
        if (!tagCompound.hasKey("Size",5))
        {
            setSize(star,random);
        }
        if (!tagCompound.hasKey("Temperature",3))
        {
            setTemperature(star,random);
        }
        if (!tagCompound.hasKey("Color",3))
        {
            setColor(star);
        }
        return true;
    }

    private void setType(Star star)
    {
        star.setType((byte) type);
    }

    private void setSize(Star star,Random random)
    {
        star.setSize(radiuses[type * 2 + 1] + (random.nextFloat() * (radiuses[type * 2] - radiuses[type * 2 + 1])));
    }

    private void setMass(Star star,Random random)
    {
        star.setMass(masses[type * 2 + 1] + (random.nextFloat() * (masses[type * 2] - masses[type * 2 + 1])));
    }

    private void setTemperature(Star star,Random random)
    {
        star.setTemperature((short) (temperatures[type * 2 + 1] + random.nextInt(temperatures[type * 2] - temperatures[type * 2 + 1])));
    }

    private void setColor(Star star)
    {
        star.setColor(getColorFromTemperature(star.getTemperature()).getColor());
    }

    @Override
    public double getWeight(Star star)
    {
        return weights[type];
    }

    public static Color getColorFromTemperature(int temperature)
    {
        temperature /= 100;
        int red,green,blue;

        if (temperature <= 66)
            red = 255;
        else
        {
            red = temperature - 60;
            red = (int)(329.698727446 * Math.pow(red,-0.1332047592));
            red = MathHelper.clamp_int(red, 0, 255);
        }

        if (temperature <= 66)
        {
            green = temperature;
            green = (int)(99.4708025861 * Math.log(green) - 161.1195681661);
            green = MathHelper.clamp_int(green,0,255);
        }
        else
        {
            green = temperature - 60;
            green = (int)(288.1221695283 * Math.pow(green,-0.0755148492));
            green = MathHelper.clamp_int(green,0,255);
        }

        if (temperature >= 66)
            blue = 255;
        else
        {
            if (temperature <= 19)
                blue = 0;
            else
            {
                blue = temperature - 10;
                blue = (int)(138.5177312231 * Math.log(blue) - 305.0447927307);
                blue = MathHelper.clamp_int(blue,0,255);
            }
        }

        return new Color(red,green,blue);
    }

    public static List<String> generateAvailableNames(Random random, int maxLength,float prefixChance,float sufixChance)
    {
        List<String> names = new ArrayList<>();
        String n;

        for (String name : starNames)
        {
            if (name.length() <= maxLength)
                names.add(name.replace("*",""));

            for (String prefix : prefixes)
            {
                if (!name.startsWith("*") && random.nextFloat() < prefixChance)
                {
                    n = MOStringHelper.addPrefix(name, prefix);
                    if (n.length() <= maxLength)
                        names.add(n.replace("*",""));
                }

                for (String sufix : suffixes)
                {
                    if (!name.endsWith("*") && random.nextFloat() < sufixChance) {
                        n = MOStringHelper.addSuffix(name, sufix);
                        if (n.length() <= maxLength)
                            names.add(n.replace("*",""));
                    }
                    if (!name.startsWith("*") && !name.endsWith("*") && random.nextFloat() < prefixChance && random.nextFloat() < sufixChance) {
                        n = MOStringHelper.addPrefix(MOStringHelper.addSuffix(name, sufix), prefix);
                        if (n.length() <= maxLength)
                            names.add(n);
                    }
                }
            }
        }

        Collections.shuffle(names,random);
        return names;
    }
}
