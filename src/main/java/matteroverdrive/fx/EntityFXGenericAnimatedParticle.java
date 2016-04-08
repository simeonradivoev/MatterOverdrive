package matteroverdrive.fx;

import matteroverdrive.client.data.TextureAtlasSpriteParticle;
import matteroverdrive.proxy.ClientProxy;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

/**
 * Created by Simeon on 2/20/2016.
 */
public class EntityFXGenericAnimatedParticle extends MOEntityFX
{
    private boolean bottomPivot;

    public EntityFXGenericAnimatedParticle(World worldIn, double posXIn, double posYIn, double posZIn, float particleSize, ResourceLocation sprite)
    {
        super(worldIn, posXIn, posYIn, posZIn);
        this.particleScale = particleSize;
        TextureAtlasSprite originalSprite = ClientProxy.renderHandler.getRenderParticlesHandler().getSprite(sprite);
        if (originalSprite instanceof TextureAtlasSpriteParticle)
        {
            this.particleTexture = ((TextureAtlasSpriteParticle) originalSprite).copy();
        }else
        {
            this.particleTexture = originalSprite;
        }
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();
        if (this.particleTexture instanceof TextureAtlasSpriteParticle)
        {
            ((TextureAtlasSpriteParticle) this.particleTexture).updateParticleAnimation();
        }
    }

    @Override
    public void renderParticle(VertexBuffer worldRendererIn, Entity entityIn, float partialTicks, float p_180434_4_, float p_180434_5_, float p_180434_6_, float p_180434_7_, float p_180434_8_)
    {
        if (bottomPivot)
        {
            float f = (float) this.particleTextureIndexX / 16.0F;
            float f1 = f + 0.0624375F;
            float f2 = (float) this.particleTextureIndexY / 16.0F;
            float f3 = f2 + 0.0624375F;
            float f4 = 0.1F * this.particleScale;

            if (this.particleTexture != null)
            {
                f = this.particleTexture.getMinU();
                f1 = this.particleTexture.getMaxU();
                f2 = this.particleTexture.getMinV();
                f3 = this.particleTexture.getMaxV();
            }

            float f5 = (float) (this.prevPosX + (this.posX - this.prevPosX) * (double) partialTicks - interpPosX);
            float f6 = (float) (this.prevPosY + (this.posY - this.prevPosY) * (double) partialTicks - interpPosY);
            float f7 = (float) (this.prevPosZ + (this.posZ - this.prevPosZ) * (double) partialTicks - interpPosZ);
            int i = this.getBrightnessForRender(partialTicks);
            int j = i >> 16 & 65535;
            int k = i & 65535;
            worldRendererIn.pos((double) (f5 - p_180434_4_ * f4 - p_180434_7_ * f4), (double) (f6), (double) (f7 - p_180434_6_ * f4 - p_180434_8_ * f4)).tex((double) f1, (double) f3).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
            worldRendererIn.pos((double) (f5 - p_180434_4_ * f4 + p_180434_7_ * f4), (double) (f6 + p_180434_5_ * f4 * 2), (double) (f7 - p_180434_6_ * f4 + p_180434_8_ * f4)).tex((double) f1, (double) f2).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
            worldRendererIn.pos((double) (f5 + p_180434_4_ * f4 + p_180434_7_ * f4), (double) (f6 + p_180434_5_ * f4 * 2), (double) (f7 + p_180434_6_ * f4 + p_180434_8_ * f4)).tex((double) f, (double) f2).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
            worldRendererIn.pos((double) (f5 + p_180434_4_ * f4 - p_180434_7_ * f4), (double) (f6), (double) (f7 + p_180434_6_ * f4 - p_180434_8_ * f4)).tex((double) f, (double) f3).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
        }else
        {
            super.renderParticle(worldRendererIn,entityIn,partialTicks,p_180434_4_,p_180434_5_,p_180434_6_,p_180434_7_,p_180434_8_);
        }
    }

    public void setBottomPivot(boolean bottomPivot)
    {
        this.bottomPivot = bottomPivot;
    }
}
