package matteroverdrive.fx;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Simeon on 5/13/2015.
 */
@SideOnly(Side.CLIENT)
public class GravitationalAnomalyParticle extends Particle
{
	float smokeParticleScale;
	Vec3d center;

	public GravitationalAnomalyParticle(World world, double x, double y, double z, Vec3d center)
	{
		this(world, x, y, z, center, 1.0F);
	}

	public GravitationalAnomalyParticle(World world, double x, double y, double z, Vec3d center, float f)
	{
		super(world, x, y, z, 0.0D, 0.0D, 0.0D);
		this.particleRed = this.particleGreen = this.particleBlue = (float)(Math.random() * 0.30000001192092896D);
		this.particleScale *= 0.75F;
		this.particleScale *= f;
		this.smokeParticleScale = this.particleScale;
		this.particleMaxAge = (int)(8.0D / (Math.random() * 0.8D + 0.2D));
		this.particleMaxAge = (int)((float)this.particleMaxAge * f);
		//this.noClip = true;
		this.center = center;
	}

	@Override
	public void renderParticle(VertexBuffer buffer, Entity entity, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ)
	{
		float f6 = ((float)this.particleAge + partialTicks) / (float)this.particleMaxAge * 32.0F;

		if (f6 < 0.0F)
		{
			f6 = 0.0F;
		}

		if (f6 > 1.0F)
		{
			f6 = 1.0F;
		}

		this.particleScale = this.smokeParticleScale * f6;
		super.renderParticle(buffer, entity, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
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

		this.setParticleTextureIndex(7 - this.particleAge * 8 / this.particleMaxAge);
		this.moveEntity(this.motionX, this.motionY, this.motionZ);

		this.motionX = (center.xCoord - posX) * 0.1;
		this.motionY = (center.yCoord - posY) * 0.1;
		this.motionZ = (center.zCoord - posZ) * 0.1;
	}
}
