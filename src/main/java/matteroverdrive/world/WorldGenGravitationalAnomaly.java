package matteroverdrive.world;

import matteroverdrive.handler.ConfigurationHandler;
import matteroverdrive.init.MatterOverdriveBlocks;
import matteroverdrive.tile.TileEntityGravitationalAnomaly;
import matteroverdrive.util.IConfigSubscriber;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import java.util.Random;

/**
 * Created by Simeon on 5/18/2015.
 */
public class WorldGenGravitationalAnomaly extends WorldGenerator implements IConfigSubscriber
{
    float defaultChance;
    float chance;
    int minMatter;
    int maxMatter;

    public WorldGenGravitationalAnomaly(float chance,int minMatter,int maxMatter)
    {
        this.defaultChance = chance;
        this.chance = chance;
        this.minMatter = minMatter;
        this.maxMatter = maxMatter;
    }

    @Override
    public boolean generate(World world, Random random, int x, int y, int z)
    {
        if (random.nextFloat() < chance)
        {
            if (world.setBlock(x,y,z, MatterOverdriveBlocks.gravitational_anomaly))
            {
                TileEntityGravitationalAnomaly anomaly = new TileEntityGravitationalAnomaly(minMatter + random.nextInt(maxMatter - minMatter));
                world.setTileEntity(x,y,z,anomaly);
            }
        }
        return false;
    }

    @Override
    public void onConfigChanged(ConfigurationHandler config)
    {
        chance = config.config.getFloat(ConfigurationHandler.KEY_GRAVITATIONAL_ANOMALY_SPAWN_CHANCE, ConfigurationHandler.CATEGORY_WORLD_GEN,defaultChance,0,1,"Spawn Chance of Gravity Anomaly pre chunk");
    }
}
