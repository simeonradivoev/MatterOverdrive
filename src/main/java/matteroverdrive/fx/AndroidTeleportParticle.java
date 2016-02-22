package matteroverdrive.fx;

import matteroverdrive.client.render.RenderParticlesHandler;
import matteroverdrive.proxy.ClientProxy;
import matteroverdrive.util.math.MOMathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Simeon on 6/2/2015.
 */
@SideOnly(Side.CLIENT)
public class AndroidTeleportParticle extends MOEntityFX
{
    public AndroidTeleportParticle(World world, double x, double y, double z)
    {
        super(world, x, y, z);
        setSize(1,1);
        this.particleRed = this.particleGreen = this.particleBlue = 1.0F;
        this.particleMaxAge = 16;
        this.noClip = true;
        this.particleIcon = ClientProxy.renderHandler.getRenderParticlesHandler().getSprite(RenderParticlesHandler.star);
    }

    @Override
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
    @Override
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

        this.particleScale = (float)MOMathHelper.easeIn(particleAge, 10, -10, particleMaxAge);
    }
}
