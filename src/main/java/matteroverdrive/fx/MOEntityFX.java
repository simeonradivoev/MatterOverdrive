package matteroverdrive.fx;

import matteroverdrive.client.data.Color;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Simeon on 1/18/2016.
 */
@SideOnly(Side.CLIENT)
public abstract class MOEntityFX extends EntityFX
{
    protected ParticleIcon particleIcon;

    public MOEntityFX(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn)
    {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
    }

    protected MOEntityFX(World worldIn, double posXIn, double posYIn, double posZIn)
    {
        super(worldIn, posXIn, posYIn, posZIn);
    }

    public void renderParticle(WorldRenderer worldRendererIn, Entity entityIn, float partialTicks, float p_180434_4_, float p_180434_5_, float p_180434_6_, float p_180434_7_, float p_180434_8_)
    {
        float f = (float)this.particleTextureIndexX / 16.0F;
        float f1 = f + 0.0624375F;
        float f2 = (float)this.particleTextureIndexY / 16.0F;
        float f3 = f2 + 0.0624375F;
        float f4 = 0.1F * this.particleScale;

        if (this.particleIcon != null)
        {
            f = this.particleIcon.getMinU();
            f1 = this.particleIcon.getMaxU();
            f2 = this.particleIcon.getMinV();
            f3 = this.particleIcon.getMaxV();
        }

        float f5 = (float)(this.prevPosX + (this.posX - this.prevPosX) * (double)partialTicks - interpPosX);
        float f6 = (float)(this.prevPosY + (this.posY - this.prevPosY) * (double)partialTicks - interpPosY);
        float f7 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * (double)partialTicks - interpPosZ);
        int i = this.getBrightnessForRender(partialTicks);
        int j = i >> 16 & 65535;
        int k = i & 65535;
        worldRendererIn.pos((double)(f5 - p_180434_4_ * f4 - p_180434_7_ * f4), (double)(f6 - p_180434_5_ * f4), (double)(f7 - p_180434_6_ * f4 - p_180434_8_ * f4)).tex((double)f1, (double)f3).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
        worldRendererIn.pos((double)(f5 - p_180434_4_ * f4 + p_180434_7_ * f4), (double)(f6 + p_180434_5_ * f4), (double)(f7 - p_180434_6_ * f4 + p_180434_8_ * f4)).tex((double)f1, (double)f2).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
        worldRendererIn.pos((double)(f5 + p_180434_4_ * f4 + p_180434_7_ * f4), (double)(f6 + p_180434_5_ * f4), (double)(f7 + p_180434_6_ * f4 + p_180434_8_ * f4)).tex((double)f, (double)f2).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
        worldRendererIn.pos((double)(f5 + p_180434_4_ * f4 - p_180434_7_ * f4), (double)(f6 - p_180434_5_ * f4), (double)(f7 + p_180434_6_ * f4 - p_180434_8_ * f4)).tex((double)f, (double)f3).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
    }

    public void setColorRGBA(Color colorRGBA)
    {
        this.particleRed = colorRGBA.getFloatR();
        this.particleGreen = colorRGBA.getFloatG();
        this.particleBlue = colorRGBA.getFloatB();
        this.particleAlpha = colorRGBA.getFloatA();
    }

    @SideOnly(Side.CLIENT)
    public static class ParticleIcon
    {
        private float uMin,uMax,vMin,vMax;

        public ParticleIcon(){};

        public ParticleIcon(float uMin,float vMin,float uMax,float vMax)
        {
            this.uMin = uMin;
            this.uMax = uMax;
            this.vMin = vMin;
            this.vMax = vMax;
        }

        public static final ParticleIcon fromWithAndHeight(int x,int y,int width,int height,int textureSize)
        {
            ParticleIcon particleIcon = new ParticleIcon();
            particleIcon.uMin = (float)x / (float)textureSize;
            particleIcon.vMin = (float)y / (float)textureSize;
            particleIcon.uMax = particleIcon.uMin + ((float)width / (float)textureSize);
            particleIcon.vMax = particleIcon.vMin + ((float)height / (float)textureSize);
            return particleIcon;
        }

        public float getMinU()
        {
            return uMin;
        }

        public float getMaxU()
        {
            return uMax;
        }

        public float getMinV()
        {
            return vMin;
        }

        public float getMaxV()
        {
            return vMax;
        }
    }
}
