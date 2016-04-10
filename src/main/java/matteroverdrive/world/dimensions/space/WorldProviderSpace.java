package matteroverdrive.world.dimensions.space;

import matteroverdrive.world.MOWorldGen;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.BiomeProviderSingle;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Simeon on 2/5/2016.
 */
public class WorldProviderSpace extends WorldProvider
{
	@Override
	public void createBiomeProvider()
	{
		this.biomeProvider = new BiomeProviderSingle(MOWorldGen.biomeSpace);
		this.hasNoSky = true;
	}

	@Override
	public DimensionType getDimensionType()
	{
		// TODO: 3/26/2016 Find how to register dimension type
		return null;
	}

	@SideOnly(Side.CLIENT)
	public Vec3d getFogColor(float p_76562_1_, float p_76562_2_)
	{
		return new Vec3d(0, 0, 0);
	}

	@SideOnly(Side.CLIENT)
	public double getVoidFogYFactor()
	{
		return 0;
	}

	public String getWelcomeMessage()
	{
		return "Going into space";
	}

	public String getDepartMessage()
	{
		return "Leaving Space";
	}

	public String getSaveFolder()
	{
		return "SPACE";
	}

	@SideOnly(Side.CLIENT)
	public net.minecraftforge.client.IRenderHandler getCloudRenderer()
	{
		return null;
	}

	@SideOnly(Side.CLIENT)
	public net.minecraftforge.client.IRenderHandler getSkyRenderer()
	{
		return null;
	}

    /*@Override
	public String getDimensionName()
    {
        return "Space";
    }

    @Override
    public String getInternalNameSuffix()
    {
        return "mo_space";
    }*/

	@SideOnly(Side.CLIENT)
	public boolean doesXZShowFog(int x, int z)
	{
		return false;
	}

	@Override
	public BlockPos getSpawnCoordinate()
	{
		return new BlockPos(0, 50, 0);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public float getCloudHeight()
	{
		return -10000;
	}

	@Override
	public boolean isSurfaceWorld()
	{
		return worldObj.isRemote;
	}

	@Override
	public boolean canRespawnHere()
	{
		return false;
	}

	@SideOnly(Side.CLIENT)
	public boolean isSkyColored()
	{
		return true;
	}

	@SideOnly(Side.CLIENT)
	public Vec3d getSkyColor(net.minecraft.entity.Entity cameraEntity, float partialTicks)
	{
		return new Vec3d(0, 0, 0);
	}

	@SideOnly(Side.CLIENT)
	public float getSunBrightness(float par1)
	{
		return worldObj.getSunBrightnessBody(par1);
	}

	@SideOnly(Side.CLIENT)
	public float getStarBrightness(float par1)
	{
		return 1;
	}

	@SideOnly(Side.CLIENT)
	public float getStarBrightnessBody(float partialTicks)
	{
		return 1;
	}

	public double getHorizon()
	{
		return -10000;
	}

	public boolean getHasNoSky()
	{
		return false;
	}

	@Override
	public float getSunBrightnessFactor(float par1)
	{
		return 0;
	}

	@SideOnly(Side.CLIENT)
	public Vec3d drawClouds(float partialTicks)
	{
		return new Vec3d(0, 0, 0);
	}

	@SideOnly(Side.CLIENT)
	public float[] calcSunriseSunsetColors(float celestialAngle, float partialTicks)
	{
		return null;
	}

	@Override
	public IChunkGenerator createChunkGenerator()
	{
		return new ChunkProviderSpace(this.worldObj, this.worldObj.getSeed());
	}
}
