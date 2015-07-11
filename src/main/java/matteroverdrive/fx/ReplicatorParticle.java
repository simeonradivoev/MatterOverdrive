package matteroverdrive.fx;

import matteroverdrive.Reference;
import matteroverdrive.util.Vector3;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class ReplicatorParticle extends EntityFX
{
	public static final ResourceLocation texture = new ResourceLocation(Reference.PATH_PARTICLE + "replication_particle.png");
	public static float replicatorScale = 0.1f;

	/** the scale of the flame FX */
    private float flameScale;
    private static final String __OBFID = "CL_00000907";
    private double centerX,centerY,centerZ;
    private double pointGravityScale = 1.0D;

    public ReplicatorParticle(World p_i1209_1_, double p_i1209_2_, double p_i1209_4_, double p_i1209_6_, double p_i1209_8_, double p_i1209_10_, double p_i1209_12_)
    {
        super(p_i1209_1_, p_i1209_2_, p_i1209_4_, p_i1209_6_, p_i1209_8_, p_i1209_10_, p_i1209_12_);
        this.motionX = this.motionX * 0.009999999776482582D + p_i1209_8_;
        this.motionY = this.motionY * 0.009999999776482582D + p_i1209_10_;
        this.motionZ = this.motionZ * 0.009999999776482582D + p_i1209_12_;
        double d6 = p_i1209_2_ + (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.05F);
        d6 = p_i1209_4_ + (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.05F);
        d6 = p_i1209_6_ + (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.05F);
        this.flameScale = this.particleScale;
        this.particleRed = this.particleGreen = this.particleBlue = 1.0F;
        this.particleMaxAge = (int)(8.0D / (Math.random() * 0.8D + 0.2D)) + 4;
        this.noClip = true;
        this.setParticleTextureIndex(1);
    }

    public void renderParticle(Tessellator p_70539_1_, float p_70539_2_, float p_70539_3_, float p_70539_4_, float p_70539_5_, float p_70539_6_, float p_70539_7_)
    {
        float f6 = ((float)this.particleAge + p_70539_2_) / (float)this.particleMaxAge;
        this.particleScale = this.flameScale * (1.0F - f6 * f6 * 0.5F);
        super.renderParticle(p_70539_1_, p_70539_2_, p_70539_3_, p_70539_4_, p_70539_5_, p_70539_6_, p_70539_7_);
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

        //this.moveEntity(this.motionX, this.motionY, this.motionZ);
        Vector3 motion = new Vector3(this.motionX, this.motionY, this.motionZ);
        Vector3 center = new Vector3(this.centerX, this.centerY, this.centerZ);
        Vector3 position = new Vector3(this.posX, this.posY, this.posZ);
        Vector3 gravityCenter = center.subtract(position).scale(this.pointGravityScale);
        Vector3 dir = gravityCenter.add(motion);
        
        this.motionX = dir.getX();
        this.motionY = dir.getY();
        this.motionZ = dir.getZ();
        
        this.boundingBox.offset(this.motionX, this.motionY, this.motionZ);
        this.posX = (this.boundingBox.minX + this.boundingBox.maxX) / 2.0D;
        this.posY = this.boundingBox.minY + (double)this.yOffset - (double)this.ySize;
        this.posZ = (this.boundingBox.minZ + this.boundingBox.maxZ) / 2.0D;
        
        double speedOverTime = 1D;
        this.motionX *= speedOverTime;
        this.motionY *= speedOverTime;
        this.motionZ *= speedOverTime;

        if (this.onGround)
        {
            //this.motionX *= 0.699999988079071D;
            //this.motionZ *= 0.699999988079071D;
        }
    }
    
    public void setCenter(double x,double y,double z)
    {
    	this.centerX = x;
    	this.centerY = y;
    	this.centerZ = z;
    }
    
    public void setParticleAge(int age)
    {
    	this.particleMaxAge = age;
    }

	public double getPointGravityScale() {
		return pointGravityScale;
	}

	public void setPointGravityScale(double pointGravityScale) {
		this.pointGravityScale = pointGravityScale;
	}
    
    
}
