package matteroverdrive.world.dimensions.alien;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Simeon on 2/24/2016.
 */
@SideOnly(Side.CLIENT)
public class ColorizerAlien
{
    /** Color buffer for foliage */
    private static int[] foliageBuffer = new int[65536];
    /** Color buffer for Grass */
    private static int[] grassBuffer = new int[65536];
    /** Color buffer for Water */
    private static int[] waterBuffer = new int[65536];
    /** Color buffer for SWky */
    private static int[] skyBuffer = new int[65536];
    /** Color buffer for Fog */
    private static int[] fogBuffer = new int[65536];
    /** Color buffer for Stone */
    private static int[] stoneBuffer = new int[65536];

    public static void setFoliageBiomeColorizer(int[] p_77467_0_)
    {
        foliageBuffer = p_77467_0_;
    }

    public static void setGrassBiomeColorizer(int[] p_77467_0_)
    {
        grassBuffer = p_77467_0_;
    }

    public static void setWaterBiomeColorizer(int[] p_77467_0_)
    {
        waterBuffer = p_77467_0_;
    }

    public static void setSkyBiomeColorizer(int[] p_77467_0_)
    {
        skyBuffer = p_77467_0_;
    }

    public static void setFogBiomeColorizer(int[] p_77467_0_)
    {
        fogBuffer = p_77467_0_;
    }

    public static void setStoneBiomeColorizer(int[] p_77467_0_)
    {
        stoneBuffer = p_77467_0_;
    }
    /**
     * Gets foliage color from temperature and humidity. Args: temperature, humidity
     */
    public static int getFoliageColor(double p_77470_0_, double p_77470_2_)
    {
        return getFromBuffer(foliageBuffer,p_77470_0_,p_77470_2_);
    }

    public static int getGrassColor(double p_77470_0_, double p_77470_2_)
    {
        return getFromBuffer(grassBuffer,p_77470_0_,p_77470_2_);
    }

    public static int getWaterColor(double p_77470_0_, double p_77470_2_)
    {
        return getFromBuffer(waterBuffer,p_77470_0_,p_77470_2_);
    }

    public static int getSkyColor(double p_77470_0_, double p_77470_2_)
    {
       return getFromBuffer(skyBuffer,p_77470_0_,p_77470_2_);
    }

    public static int getFogColor(double p_77470_0_, double p_77470_2_)
    {
        return getFromBuffer(fogBuffer,p_77470_0_,p_77470_2_);
    }

    public static int getStoneColor(double p_77470_0_, double p_77470_2_)
    {
        return getFromBuffer(stoneBuffer,p_77470_0_,p_77470_2_);
    }

    private static int getFromBuffer(int[] buffer,double d0,double d2)
    {
        d2 = d2 * d0;
        int i = (int)((1.0D - d0) * 255.0D);
        int j = (int)((1.0D - d2) * 255.0D);
        return buffer[j << 8 | i];
    }

    public static int getStoneBasicColor()
    {
        return 0xffffff;
    }
}
