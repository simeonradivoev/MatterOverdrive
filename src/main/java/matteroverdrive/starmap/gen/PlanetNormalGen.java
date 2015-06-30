package matteroverdrive.starmap.gen;

import matteroverdrive.starmap.data.Planet;

import java.util.Random;

/**
 * Created by Simeon on 6/21/2015.
 */
public class PlanetNormalGen extends PlanetAbstractGen
{
    public PlanetNormalGen()
    {
        super((byte)0, 6, 6);
    }

    @Override
    protected void setSize(Planet planet, Random random) {
        planet.setSize(0.7f + random.nextFloat() * 0.6f);
    }

    @Override
    public double getWeight(Planet body) {
        if (body.getOrbit() < 0.6f && body.getOrbit() > 0.4f)
        {
            return 0.3f;
        }
        return 0.1f;
    }
}
