package matteroverdrive.fx;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ReplicatorParticle extends Particle
{
	/**
	 * the scale of the flame FX
	 */
	private float flameScale;
	private double centerX, centerY, centerZ;
	private double pointGravityScale = 1.0D;

	public ReplicatorParticle(World world, double p_i1209_2_, double p_i1209_4_, double p_i1209_6_, double p_i1209_8_, double p_i1209_10_, double p_i1209_12_)
	{
		super(world, p_i1209_2_, p_i1209_4_, p_i1209_6_, p_i1209_8_, p_i1209_10_, p_i1209_12_);
		this.motionX = this.motionX * 0.009999999776482582D + p_i1209_8_;
		this.motionY = this.motionY * 0.009999999776482582D + p_i1209_10_;
		this.motionZ = this.motionZ * 0.009999999776482582D + p_i1209_12_;
		double d6 = p_i1209_2_ + (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.05F);
		d6 = p_i1209_4_ + (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.05F);
		d6 = p_i1209_6_ + (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.05F);
		this.flameScale = this.particleScale;
		this.particleRed = this.particleGreen = this.particleBlue = 1.0F;
		this.particleMaxAge = (int)(8.0D / (Math.random() * 0.8D + 0.2D)) + 4;
		//this.noClip = true;
		this.setParticleTextureIndex(1);
	}

	@Override
	public void renderParticle(VertexBuffer worldRenderer, Entity entity, float p_70539_2_, float p_70539_3_, float p_70539_4_, float p_70539_5_, float p_70539_6_, float p_70539_7_)
	{
		float f6 = ((float)this.particleAge + p_70539_2_) / (float)this.particleMaxAge;
		this.particleScale = this.flameScale * (1.0F - f6 * f6 * 0.5F);
		super.renderParticle(worldRenderer, entity, p_70539_2_, p_70539_3_, p_70539_4_, p_70539_5_, p_70539_6_, p_70539_7_);
	}

	public int getBrightnessForRender(float f)
	{
		float f1 = ((float)this.particleAge + f) / (float)this.particleMaxAge;

		if (f1 < 0.0F)
		{
			f1 = 0.0F;
		}

		if (f1 > 1.0F)
		{
			f1 = 1.0F;
		}

		int i = super.getBrightnessForRender(f);
		int j = i & 255;
		int k = i >> 16 & 255;
		j += (int)(f1 * 15.0F * 16.0F);

		if (j > 240)
		{
			j = 240;
		}

		return j | k << 16;
	}

	/**
	 * Gets how bright this entity is.
	 */
	public float getBrightness(float p_70013_1_)
	{
		float f1 = ((float)this.particleAge + p_70013_1_) / (float)this.particleMaxAge;

		if (f1 < 0.0F)
		{
			f1 = 0.0F;
		}

		if (f1 > 1.0F)
		{
			f1 = 1.0F;
		}

		float f2 = super.getBrightnessForRender(p_70013_1_);
		return f2 * f1 + (1.0F - f1);
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	public void onUpdate()
	{
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;

		if (this.particleAge++ >= this.particleMaxAge)
		{
			this.setExpired();
		}

		//this.moveEntity(this.motionX, this.motionY, this.motionZ);
		Vec3d motion = new Vec3d(this.motionX, this.motionY, this.motionZ);
		Vec3d center = new Vec3d(this.centerX, this.centerY, this.centerZ);
		Vec3d position = new Vec3d(this.posX, this.posY, this.posZ);
		Vec3d gravityCenter = center.subtract(position);
		gravityCenter = new Vec3d(gravityCenter.xCoord * pointGravityScale, gravityCenter.yCoord * pointGravityScale, gravityCenter.zCoord * pointGravityScale);
		Vec3d dir = gravityCenter.add(motion);

		this.motionX = dir.xCoord;
		this.motionY = dir.yCoord;
		this.motionZ = dir.zCoord;

		this.getEntityBoundingBox().offset(this.motionX, this.motionY, this.motionZ);
		this.posX = (this.getEntityBoundingBox().minX + this.getEntityBoundingBox().maxX) / 2.0D;
		this.posY = this.getEntityBoundingBox().minY - (double)this.height;
		this.posZ = (this.getEntityBoundingBox().minZ + this.getEntityBoundingBox().maxZ) / 2.0D;

		double speedOverTime = 1D;
		this.motionX *= speedOverTime;
		this.motionY *= speedOverTime;
		this.motionZ *= speedOverTime;
		moveEntity(this.motionZ, this.motionY, this.motionZ);
	}

	public void setCenter(double x, double y, double z)
	{
		this.centerX = x;
		this.centerY = y;
		this.centerZ = z;
	}

	public void setParticleAge(int age)
	{
		this.particleMaxAge = age;
	}

	public double getPointGravityScale()
	{
		return pointGravityScale;
	}

	public void setPointGravityScale(double pointGravityScale)
	{
		this.pointGravityScale = pointGravityScale;
	}


}
