package matteroverdrive.fx;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Created by Simeon on 2/11/2016.
 */
public class LightningCircle extends MOEntityFX
{
	private float randomness;
	private float speed;
	private float scale;
	private float growth;

	public LightningCircle(World world, double posX, double posY, double posZ, float randomness, float speed, float scale, float growth)
	{
		super(world, posX, posY, posZ);
		this.particleMaxAge = 10 + (int)(this.rand.nextFloat() * 10);
		this.randomness = randomness;
		this.speed = speed;
		this.scale = scale;
		this.growth = growth;
		this.renderDistanceWeight = Minecraft.getMinecraft().gameSettings.renderDistanceChunks / 6f;
		setEntityBoundingBox(new AxisAlignedBB(posX - scale - growth, posY - 2 * randomness, posZ - scale - growth, posX + scale + growth, posY + 2 * randomness, posZ + scale + growth));
	}

	public void renderParticle(VertexBuffer worldRendererIn, Entity entityIn, float partialTicks, float rotX, float rotXZ, float rotZ, float rotYZ, float rotXY)
	{
		particleScale = scale + ((float)particleAge / (float)particleMaxAge) * growth;
		rand.setSeed((long)(particleAge * speed) + hashCode());
		Vec3d randomDir = new Vec3d(rand.nextGaussian() * randomness, rand.nextGaussian() * randomness, rand.nextGaussian() * randomness);

		for (double i = 0; i < Math.PI * 2; i += 0.1)
		{
			double tickX = prevPosX + ((this.posX - this.prevPosX) * (double)partialTicks - interpPosX);
			double tickY = prevPosY + ((this.posY - this.prevPosY) * (double)partialTicks - interpPosY);
			double tickZ = prevPosZ + ((this.posZ - this.prevPosZ) * (double)partialTicks - interpPosZ);

			int b = this.getBrightnessForRender(partialTicks);
			int j = b >> 16 & 65535;
			int k = b & 65535;

			Vec3d pos = new Vec3d(Math.sin(i) * particleScale, 0, Math.cos(i) * particleScale).add(randomDir);
			worldRendererIn.pos(pos.xCoord + tickX, pos.yCoord + tickY, pos.zCoord + tickZ).tex(0, 0).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();

			randomDir = randomDir.addVector(rand.nextGaussian() * randomness, rand.nextGaussian() * randomness, rand.nextGaussian() * randomness);
			pos = new Vec3d(Math.sin(i + 0.1) * particleScale, 0, Math.cos(i + 0.1) * particleScale).add(randomDir);
			worldRendererIn.pos(pos.xCoord + tickX, pos.yCoord + tickY, pos.zCoord + tickZ).tex(0, 0).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
		}
	}
}
