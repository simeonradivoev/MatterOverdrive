/*
 * This file is part of Matter Overdrive
 * Copyright (c) 2015., Simeon Radivoev, All rights reserved.
 *
 * Matter Overdrive is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Matter Overdrive is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Matter Overdrive.  If not, see <http://www.gnu.org/licenses>.
 */

package matteroverdrive.fx;

import matteroverdrive.client.data.Color;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

/**
 * Created by Simeon on 10/18/2015
 */
public class PhaserBoltRecoil extends Particle
{
	private float lavaParticleScale;

	public PhaserBoltRecoil(World world, double x, double y, double z, Color color, double dirX, double dirY, double dirZ)
	{
		super(world, x, y, z, dirX, dirY, dirZ);
		this.motionX += (double)((this.rand.nextFloat() - 0.5f) * 0.2F);
		this.motionY += (double)((this.rand.nextFloat() - 0.5f) * 0.2F);
		this.motionZ += (double)((this.rand.nextFloat() - 0.5f) * 0.2F);
		this.particleRed = color.getFloatR();
		this.particleGreen = color.getFloatG();
		this.particleBlue = color.getFloatB();
		this.particleScale *= this.rand.nextFloat() * 0.5F + 1F;
		this.lavaParticleScale = this.particleScale;
		this.particleMaxAge = (int)(8d / (Math.random() * 0.8D + 0.2D));
		this.setParticleTextureIndex(rand.nextInt(2));
	}

	public PhaserBoltRecoil(World world, double x, double y, double z, Color color)
	{
		this(world, x, y, z, color, 0, 0, 0);
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
		short short1 = 240;
		int j = i >> 16 & 255;
		return short1 | j << 16;
	}

	/**
	 * Gets how bright this entity is.
	 */
	public float getBrightness(float f)
	{
		return 1.0F;
	}

	@Override
	public void renderParticle(VertexBuffer buffer, Entity entity, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ)
	{
		float f6 = ((float)this.particleAge + partialTicks) / (float)this.particleMaxAge;
		this.particleScale = this.lavaParticleScale * (1.0F - f6 * f6);
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

		float f = (float)this.particleAge / (float)this.particleMaxAge;

		this.motionY -= 0.03D;
		try
		{
			this.moveEntity(this.motionX, this.motionY, this.motionZ);
		}
		catch (Exception e)
		{
			this.setExpired();
		}

		this.motionX *= 0.9990000128746033D;
		this.motionY *= 0.9990000128746033D;
		this.motionZ *= 0.9990000128746033D;

		if (this.isCollided)
		{
			this.motionX *= 0.699999988079071D;
			this.motionY *= 0.699999988079071D;
		}
	}
}
