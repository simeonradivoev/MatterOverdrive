package com.MO.MatterOverdrive.fx;

import com.MO.MatterOverdrive.data.IconHolder;
import com.MO.MatterOverdrive.util.math.MOMathHelper;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.World;

/**
 * Created by Simeon on 6/2/2015.
 */
public class AndroidTeleportParticle extends EntityFX
{

    public AndroidTeleportParticle(World p_i1209_1_, double p_i1209_2_, double p_i1209_4_, double p_i1209_6_)
    {
        super(p_i1209_1_, p_i1209_2_, p_i1209_4_, p_i1209_6_, 0, 0, 0);
        this.particleRed = this.particleGreen = this.particleBlue = 1.0F;
        //this.particleMaxAge = (int)(8.0D / (Math.random() * 0.8D + 0.2D)) + 4;
        this.particleMaxAge = 16;
        this.noClip = true;
        this.particleIcon = new IconHolder(0,0,32f/128f,32f/128f);
    }

    public void renderParticle(Tessellator p_70539_1_, float p_70539_2_, float xOffet, float yOffset, float zOffset, float p_70539_6_, float p_70539_7_)
    {
        super.renderParticle(p_70539_1_,p_70539_2_,xOffet,yOffset,zOffset,p_70539_6_,p_70539_7_);
    }

    public int getBrightnessForRender(float p_70070_1_)
    {
        float f1 = ((float)this.particleAge + p_70070_1_) / (float)this.particleMaxAge;

        if (f1 < 0.0F)
        {
            f1 = 0.0F;
        }

        if (f1 > 1.0F)
        {
            f1 = 1.0F;
        }

        int i = super.getBrightnessForRender(p_70070_1_);
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

        float f2 = super.getBrightness(p_70013_1_);
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
        double time = (double)this.particleAge / (double)this.particleMaxAge;

        if (this.particleAge++ >= this.particleMaxAge)
        {
            this.setDead();
        }

        this.particleScale = (float)MOMathHelper.easeIn(particleAge,10,-10,particleMaxAge);
    }
}
