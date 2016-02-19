package matteroverdrive.fx;

import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import org.lwjgl.util.vector.Vector3f;

/**
 * Created by Simeon on 5/13/2015.
 */
public class GravitationalStabilizerBeamParticle extends MOEntityFX
{
    float smokeParticleScale;
    Vector3f from;
    Vector3f to;
    Vector3f up;
    float orbitRadius;
    int startTime;
    private static final String __OBFID = "CL_00000924";

    public GravitationalStabilizerBeamParticle(World world, Vector3f from, Vector3f to, Vector3f up)
    {
        this(world, from, to, up, 1.0F, 1.0F, 40);
    }

    public GravitationalStabilizerBeamParticle(World world, Vector3f from, Vector3f to, Vector3f up, float size, float orbitRadius, int time)
    {
        super(world, from.x, from.y, from.z, 0.0D, 0.0D, 0.0D);
        this.particleRed = this.particleGreen = this.particleBlue = (float)(Math.random() * 0.30000001192092896D);
        this.particleScale *= 0.75F;
        this.particleScale *= size;
        setSize(this.particleScale * 0.1f,this.particleScale * 0.1f);
        this.smokeParticleScale = this.particleScale;
        this.particleMaxAge = time;
        this.noClip = true;
        this.from = from;
        this.to = to;
        this.up = up;
        this.orbitRadius = orbitRadius + (rand.nextFloat() * orbitRadius * 0.5f);
        startTime = rand.nextInt(time);
        this.particleIcon = ParticleIcon.fromWithAndHeight(0,0,32,32,128);
    }

    public void setColor(float r, float g, float b, float a)
    {
        this.particleAlpha = a;
        this.particleBlue = b;
        this.particleRed = r;
        this.particleGreen = g;
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

        float percent = (float)this.particleAge / (float)this.particleMaxAge;
        Vector3f dir = Vector3f.sub(to, from, null);
        Vector3f spiralDir = Vector3f.cross(dir.normalise(null), up, null);
        spiralDir.scale((float) Math.sin((particleAge + startTime) * 0.5) * orbitRadius);
        Vector3f up = new Vector3f(this.up);
        up.scale((float) Math.cos((particleAge + startTime) * 0.5) * orbitRadius);
        Vector3f.add(spiralDir,up,spiralDir);

        dir.scale(percent);
        Vector3f posOnPath = Vector3f.add(from, dir, null);

        setPosition(posOnPath.x + spiralDir.x,posOnPath.y + spiralDir.y,posOnPath.z + spiralDir.z);
    }
}
