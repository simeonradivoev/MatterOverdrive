package matteroverdrive.fx;

import matteroverdrive.client.render.RenderParticlesHandler;
import matteroverdrive.proxy.ClientProxy;
import matteroverdrive.util.animation.MOEasing;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

/**
 * Created by Simeon on 1/2/2016.
 */
public class ShockwaveParticle extends MOEntityFX
{
	private float maxScale;

	public ShockwaveParticle(World world, double posX, double posY, double posZ, float maxScale)
	{
		super(world, posX, posY, posZ);
		this.maxScale = maxScale;
		this.particleTexture = ClientProxy.renderHandler.getRenderParticlesHandler().getSprite(RenderParticlesHandler.shockwave);
		this.particleMaxAge = (int)(maxScale * 5);
		this.setEntityBoundingBox(new AxisAlignedBB(posX - maxScale, posY - 0.5, posZ - maxScale, posX + maxScale, posY + 0.5, posZ + maxScale));
		this.renderDistanceWeight = Minecraft.getMinecraft().gameSettings.renderDistanceChunks / 6f;
	}

	@Override
	public void renderParticle(VertexBuffer worldRenderer, Entity entity, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ)
	{
		float f6 = (float)this.particleTextureIndexX / 16.0F;
		float f7 = f6 + 0.0624375F;
		float f8 = (float)this.particleTextureIndexY / 16.0F;
		float f9 = f8 + 0.0624375F;
		float particleScale = this.particleScale;

		if (this.particleTexture != null)
		{
			f6 = this.particleTexture.getMinU();
			f7 = this.particleTexture.getMaxU();
			f8 = this.particleTexture.getMinV();
			f9 = this.particleTexture.getMaxV();
		}

		float f11 = (float)(this.prevPosX + (this.posX - this.prevPosX) * (double)partialTicks - interpPosX);
		float f12 = (float)(this.prevPosY + (this.posY - this.prevPosY) * (double)partialTicks - interpPosY);
		float f13 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * (double)partialTicks - interpPosZ);
		float particleAge = 1f - (float)this.particleAge / (float)this.particleMaxAge;
		float red = this.particleRed * particleAge;
		float green = this.particleGreen * particleAge;
		float blue = this.particleBlue * particleAge;
		float alpha = this.particleAlpha * particleAge;
		int i = this.getBrightnessForRender(partialTicks);
		int j = i >> 16 & 65535;
		int k = i & 65535;
		worldRenderer.pos((double)(f11 - particleScale), (double)(f12), (double)(f13 - particleScale)).tex((double)f7, (double)f9).color(red, green, blue, alpha).lightmap(j, k).endVertex();
		worldRenderer.pos((double)(f11 - particleScale), (double)(f12), (double)(f13 + particleScale)).tex((double)f7, (double)f8).color(red, green, blue, alpha).lightmap(j, k).endVertex();
		worldRenderer.pos((double)(f11 + particleScale), (double)(f12), (double)(f13 + particleScale)).tex((double)f6, (double)f8).color(red, green, blue, alpha).lightmap(j, k).endVertex();
		worldRenderer.pos((double)(f11 + particleScale), (double)(f12), (double)(f13 - particleScale)).tex((double)f6, (double)f9).color(red, green, blue, alpha).lightmap(j, k).endVertex();
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

		this.particleScale = MOEasing.Quart.easeOut((float)this.particleAge / (float)this.particleMaxAge, 0, 1, 1) * maxScale;
	}
}
