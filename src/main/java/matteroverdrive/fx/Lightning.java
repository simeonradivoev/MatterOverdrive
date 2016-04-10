package matteroverdrive.fx;

import matteroverdrive.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.vecmath.Vector3d;

/**
 * Created by Simeon on 2/11/2016.
 */
public class Lightning extends MOEntityFX
{
	private float randomness;
	private Vector3d destination;
	private float length;
	private float speed;

	public Lightning(World worldIn, Vec3d start, Vec3d destination)
	{
		this(worldIn, start, destination, 1, 1);
		setColorRGBA(Reference.COLOR_HOLO.multiplyWithoutAlpha(0.5f));
	}

	public Lightning(World worldIn, Vec3d start, Vec3d destination, float randomness, float speed)
	{
		super(worldIn, start.xCoord, start.yCoord, start.zCoord);
		this.destination = new Vector3d(destination.xCoord, destination.yCoord, destination.zCoord);
		this.length = (float)destination.distanceTo(new Vec3d(posX, posY, posZ));
		this.particleMaxAge = 10 + (int)(this.rand.nextFloat() * 10);
		this.randomness = randomness;
		this.speed = speed;
		setEntityBoundingBox(new AxisAlignedBB(start.xCoord, start.yCoord, start.zCoord, destination.xCoord, destination.yCoord, destination.zCoord));
		this.renderDistanceWeight = Minecraft.getMinecraft().gameSettings.renderDistanceChunks / 6f;
	}

	public void renderParticle(VertexBuffer worldRendererIn, Entity entityIn, float partialTicks, float rotX, float rotXZ, float rotZ, float rotYZ, float rotXY)
	{
		rand.setSeed((long)(particleAge * speed) + hashCode());
		Vector3d lastPos = new Vector3d(prevPosX, prevPosY, prevPosZ);

		for (float i = 0; i < length; i += 0.1)
		{
			float time = 1 - (i / length);
			float randMag = (float)Math.sin(time * Math.PI) * randomness;
			double tickX = (this.posX - this.prevPosX) * (double)partialTicks - interpPosX;
			double tickY = (this.posY - this.prevPosY) * (double)partialTicks - interpPosY;
			double tickZ = (this.posZ - this.prevPosZ) * (double)partialTicks - interpPosZ;

			Vector3d randomDir = new Vector3d(rand.nextGaussian(), rand.nextGaussian(), rand.nextGaussian());
			randomDir.scale(randMag);

			Vector3d toDestDir = new Vector3d(destination);
			toDestDir.sub(lastPos);
			toDestDir.normalize();

			Vector3d dir = new Vector3d(toDestDir);
			dir.add(randomDir);
			dir.scale(0.1);

			Vector3d pos = new Vector3d(lastPos);
			pos.add(dir);

			int b = this.getBrightnessForRender(partialTicks);
			int j = b >> 16 & 65535;
			int k = b & 65535;
			worldRendererIn.pos(lastPos.x + tickX, lastPos.y + tickY, lastPos.z + tickZ).tex(0, 0).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
			worldRendererIn.pos(pos.x + tickX, pos.y + tickY, pos.z + tickZ).tex(0, 0).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
			lastPos = pos;
		}
	}

	@Override
	public void onUpdate()
	{
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;

		if (this.particleAge++ >= this.particleMaxAge)
		{
			this.setExpired();
		}
	}
}
