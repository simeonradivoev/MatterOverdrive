package matteroverdrive.fx;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import matteroverdrive.Reference;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

/**
 * Created by Simeon on 4/17/2015.
 */
@SideOnly(Side.CLIENT)
public class VentParticle extends EntityFX
{
    public static final ResourceLocation texture = new ResourceLocation(Reference.PATH_PARTICLE + "steam_particle.png");

    public VentParticle(World world, double x, double y, double z, double dirx, double diry, double dirz)
    {
        this(world, x, y, z, dirx, diry, dirz, 1.0F);
    }

    public VentParticle(World world, double x, double y, double z, double dirX, double dirY, double dirZ, float scale)
    {
        super(world, x, y, z, 0.0D, 0.0D, 0.0D);
        //this.motionX *= 0.10000000149011612D;
        //this.motionY *= 0.10000000149011612D;
        //this.motionZ *= 0.10000000149011612D;
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
        //this.posX += this.motionX;
        //this.posY += this.motionY;
        //this.posZ += this.motionZ;

        if (this.posY == this.prevPosY)
        {
            //this.motionX *= 1.1D;
            //this.motionZ *= 1.1D;
        }

        this.motionX *= 0.9599999785423279D;
        this.motionY *= 0.9599999785423279D;
        this.motionZ *= 0.9599999785423279D;

        if (this.onGround)
        {
            //this.motionX *= 0.699999988079071D;
            //this.motionZ *= 0.699999988079071D;
        }
    }
}
