package matteroverdrive.fx;

import net.minecraft.client.particle.EntityFX;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Simeon on 4/17/2015.
 */
@SideOnly(Side.CLIENT)
public class VentParticle extends EntityFX
{
    public VentParticle(World world, double x, double y, double z, double dirX, double dirY, double dirZ)
    {
        this(world, x, y, z, dirX, dirY, dirZ, 1.0F);
    }

    public VentParticle(World world, double x, double y, double z, double dirX, double dirY, double dirZ, float scale)
    {
        super(world, x, y, z, 0.0D, 0.0D, 0.0D);
        this.motionX = dirX;
        this.motionY = dirY;
        this.motionZ = dirZ;
        this.particleRed = this.particleGreen = this.particleBlue = 1 - rand.nextFloat() * 0.3f;
        this.particleScale = scale;
        this.particleMaxAge = (int)(8.0D / (Math.random() * 0.8D + 0.2D));
        this.noClip = false;
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
        this.moveEntity(this.motionX, this.motionY, this.motionZ);

        this.motionX *= 0.9599999785423279D;
        this.motionY *= 0.9599999785423279D;
        this.motionZ *= 0.9599999785423279D;
    }
}
