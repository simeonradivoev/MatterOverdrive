package matteroverdrive.fx;

import matteroverdrive.client.data.Color;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Simeon on 1/18/2016.
 */
@SideOnly(Side.CLIENT)
public abstract class MOEntityFX extends EntityFX
{
    public MOEntityFX(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn)
    {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
    }

    protected MOEntityFX(World worldIn, double posXIn, double posYIn, double posZIn)
    {
        super(worldIn, posXIn, posYIn, posZIn);
    }

    public void setColorRGBA(Color colorRGBA)
    {
        this.particleRed = colorRGBA.getFloatR();
        this.particleGreen = colorRGBA.getFloatG();
        this.particleBlue = colorRGBA.getFloatB();
        this.particleAlpha = colorRGBA.getFloatA();
    }

    public void setParticleMaxAge(int maxAge)
    {
        this.particleMaxAge = maxAge;
    }

    public void setRenderDistanceWeight(float renderDistanceWeight)
    {
        this.renderDistanceWeight = renderDistanceWeight;
    }
}
