package matteroverdrive.fx;

import matteroverdrive.data.IconHolder;
import matteroverdrive.util.animation.MOEasing;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.World;

/**
 * Created by Simeon on 1/2/2016.
 */
public class ShockwaveParticle extends EntityFX
{
    private float maxScale;

    public ShockwaveParticle(World p_i1218_1_, double p_i1218_2_, double p_i1218_4_, double p_i1218_6_,float maxScale)
    {
        super(p_i1218_1_, p_i1218_2_, p_i1218_4_, p_i1218_6_);
        this.maxScale = maxScale;
        this.particleIcon = new IconHolder(96f / 128f, 0, 1f, 32f / 128f, 32, 32);
        this.particleMaxAge = (int) (maxScale * 5);
    }

    @Override
    public void renderParticle(Tessellator tess, float f, float xOffset, float yOffset, float zOffset, float p_70539_6_, float p_70539_7_)
    {
        float f6 = (float)this.particleTextureIndexX / 16.0F;
        float f7 = f6 + 0.0624375F;
        float f8 = (float)this.particleTextureIndexY / 16.0F;
        float f9 = f8 + 0.0624375F;
        float particleScale = this.particleScale;

        if (this.particleIcon != null)
        {
            f6 = this.particleIcon.getMinU();
            f7 = this.particleIcon.getMaxU();
            f8 = this.particleIcon.getMinV();
            f9 = this.particleIcon.getMaxV();
        }

        float f11 = (float)(this.prevPosX + (this.posX - this.prevPosX) * (double)f - interpPosX);
        float f12 = (float)(this.prevPosY + (this.posY - this.prevPosY) * (double)f - interpPosY);
        float f13 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * (double)f - interpPosZ);
        float particleAge = 1f - (float)this.particleAge / (float)this.particleMaxAge;
        tess.setColorRGBA_F(this.particleRed * particleAge, this.particleGreen * particleAge, this.particleBlue * particleAge, this.particleAlpha * particleAge);
        tess.addVertexWithUV((double)(f11 - particleScale), (double)(f12), (double)(f13 - particleScale), (double)f7, (double)f9);
        tess.addVertexWithUV((double)(f11 - particleScale), (double)(f12), (double)(f13 + particleScale), (double)f7, (double)f8);
        tess.addVertexWithUV((double)(f11 + particleScale), (double)(f12), (double)(f13 + particleScale), (double)f6, (double)f8);
        tess.addVertexWithUV((double)(f11 + particleScale), (double)(f12), (double)(f13 - particleScale), (double)f6, (double)f9);
    }

    @Override
    public void onUpdate()
    {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        if (this.particleAge++ >= this.particleMaxAge)
        {
            this.setDead();
        }

        this.particleScale = MOEasing.Quart.easeOut((float)this.particleAge / (float)this.particleMaxAge,0,1,1) * maxScale;
    }
}
