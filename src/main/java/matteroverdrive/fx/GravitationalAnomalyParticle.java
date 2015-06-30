package matteroverdrive.fx;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

/**
 * Created by Simeon on 5/13/2015.
 */
@SideOnly(Side.CLIENT)
public class GravitationalAnomalyParticle extends EntityFX
{
    float smokeParticleScale;
    Vec3 center;
    private static final String __OBFID = "CL_00000924";

    public GravitationalAnomalyParticle(World p_i1225_1_, double p_i1225_2_, double p_i1225_4_, double p_i1225_6_, Vec3 center)
    {
        this(p_i1225_1_, p_i1225_2_, p_i1225_4_, p_i1225_6_, center, 1.0F);
    }

    public GravitationalAnomalyParticle(World p_i1226_1_, double p_i1226_2_, double p_i1226_4_, double p_i1226_6_, Vec3 center, float p_i1226_14_)
    {
        super(p_i1226_1_, p_i1226_2_, p_i1226_4_, p_i1226_6_, 0.0D, 0.0D, 0.0D);
        this.particleRed = this.particleGreen = this.particleBlue = (float)(Math.random() * 0.30000001192092896D);
        this.particleScale *= 0.75F;
        this.particleScale *= p_i1226_14_;
        this.smokeParticleScale = this.particleScale;
        this.particleMaxAge = (int)(8.0D / (Math.random() * 0.8D + 0.2D));
        this.particleMaxAge = (int)((float)this.particleMaxAge * p_i1226_14_);
        this.noClip = true;
        this.center = center;
    }

    public void renderParticle(Tessellator p_70539_1_, float p_70539_2_, float p_70539_3_, float p_70539_4_, float p_70539_5_, float p_70539_6_, float p_70539_7_)
    {
        float f6 = ((float)this.particleAge + p_70539_2_) / (float)this.particleMaxAge * 32.0F;

        if (f6 < 0.0F)
        {
            f6 = 0.0F;
        }

        if (f6 > 1.0F)
        {
            f6 = 1.0F;
        }

        this.particleScale = this.smokeParticleScale * f6;
        super.renderParticle(p_70539_1_, p_70539_2_, p_70539_3_, p_70539_4_, p_70539_5_, p_70539_6_, p_70539_7_);
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
            this.setDead();
        }

        this.setParticleTextureIndex(7 - this.particleAge * 8 / this.particleMaxAge);
        //this.motionY += 0.004D;
        this.moveEntity(this.motionX, this.motionY, this.motionZ);

        this.motionX = (center.xCoord - posX) * 0.1;
        this.motionY = (center.yCoord - posY) * 0.1;
        this.motionZ = (center.zCoord - posZ) * 0.1;
    }
}
