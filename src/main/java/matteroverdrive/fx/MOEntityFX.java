package matteroverdrive.fx;

import matteroverdrive.client.data.Color;
import net.minecraft.client.particle.Particle;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Simeon on 1/18/2016.
 */
@SideOnly(Side.CLIENT)
public abstract class MOEntityFX extends Particle
{
	protected float renderDistanceWeight;

	public MOEntityFX(World world, double posX, double posY, double posZ, double xSpeed, double ySpeed, double zSpeed)
	{
		super(world, posX, posY, posZ, xSpeed, ySpeed, zSpeed);
	}

	protected MOEntityFX(World world, double posX, double posY, double posZ)
	{
		super(world, posX, posY, posZ);
	}

	public void setColorRGBA(Color color)
	{
		this.particleRed = color.getFloatR();
		this.particleGreen = color.getFloatG();
		this.particleBlue = color.getFloatB();
		this.particleAlpha = color.getFloatA();
	}

	public void setParticleMaxAge(int maxAge)
	{
		this.particleMaxAge = maxAge;
	}

	public void setRenderDistanceWeight(float renderDistanceWeight)
	{
		this.renderDistanceWeight = renderDistanceWeight;
	}

	@SideOnly(Side.CLIENT)
	public boolean isInRangeToRender3d(double x, double y, double z)
	{
		double d0 = this.posX - x;
		double d1 = this.posY - y;
		double d2 = this.posZ - z;
		double d3 = d0 * d0 + d1 * d1 + d2 * d2;
		return this.isInRangeToRenderDist(d3);
	}

	/**
	 * Checks if the entity is in range to render.
	 */
	@SideOnly(Side.CLIENT)
	public boolean isInRangeToRenderDist(double distance)
	{
		double d0 = this.getEntityBoundingBox().getAverageEdgeLength();

		if (Double.isNaN(d0))
		{
			d0 = 1.0D;
		}

		d0 = d0 * 64.0D * renderDistanceWeight;
		return distance < d0 * d0;
	}
}
