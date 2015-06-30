package matteroverdrive.starmap.gen;

import matteroverdrive.starmap.data.Planet;

import java.util.Random;

/**
 * Created by Simeon on 6/21/2015.
 */
public class PlanetGasGiantGen extends PlanetAbstractGen
{

    public PlanetGasGiantGen() {
        super((byte)1, 2, 8);
    }

    @Override
    protected void setSize(Planet planet, Random random) {
        planet.setSize(2 + random.nextFloat());
    }

    @Override
    public double getWeight(Planet body)
    {
        if (body.getOrbit() > 0.6f)
        {
            return 0.3f;
        }
        return 0.1f;
    }
}
