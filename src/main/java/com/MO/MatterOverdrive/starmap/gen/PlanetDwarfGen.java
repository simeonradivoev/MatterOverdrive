package com.MO.MatterOverdrive.starmap.gen;

import cofh.lib.gui.GuiColor;
import com.MO.MatterOverdrive.starmap.data.Planet;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;

import java.util.Random;

/**
 * Created by Simeon on 6/21/2015.
 */
public class PlanetDwarfGen extends PlanetAbstractGen
{
    public PlanetDwarfGen()
    {
        super((byte)2, 4, 4);
    }

    @Override
    protected void setSize(Planet planet, Random random)
    {
        planet.setSize(0.2f + random.nextFloat() * 0.4f);
    }

    @Override
    public double getWeight(Planet planet)
    {
        if (planet.getOrbit() > 0.6f || planet.getOrbit() < 0.4f)
        {
            return 0.3f;
        }
        return 0.1f;
    }
}
