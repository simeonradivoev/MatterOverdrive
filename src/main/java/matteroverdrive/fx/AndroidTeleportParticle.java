package matteroverdrive.fx;

import matteroverdrive.data.IconHolder;
import matteroverdrive.util.math.MOMathHelper;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.World;

/**
 * Created by Simeon on 6/2/2015.
 */
public class AndroidTeleportParticle extends EntityFX
{

    public AndroidTeleportParticle(World world, double x, double y, double z)
    {
        super(world, x, y, z, 0, 0, 0);
        this.particleRed = this.particleGreen = this.particleBlue = 1.0F;
        this.particleMaxAge = 16;
        this.noClip = true;
        this.particleIcon = new IconHolder(0, 0, 32f / 128f, 32f / 128f, 32, 32);
    }

    public void renderParticle(Tessellator tess, float f, float xOffset, float yOffset, float zOffset, float p_70539_6_, float p_70539_7_)
    {
        super.renderParticle(tess, f, xOffset, yOffset, zOffset, p_70539_6_, p_70539_7_);
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
    public float getBrightness(float f)
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

        float f2 = super.getBrightness(f);
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

        if (this.particleAge++ >= this.particleMaxAge)
        {
            this.setDead();
        }

        this.particleScale = (float)MOMathHelper.easeIn(particleAge, 10, -10, particleMaxAge);
    }
}
