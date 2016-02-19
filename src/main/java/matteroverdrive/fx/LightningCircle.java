package matteroverdrive.fx;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

/**
 * Created by Simeon on 2/11/2016.
 */
public class LightningCircle extends MOEntityFX
{
    private float randomness;
    private float speed;
    private float scale;
    private float growth;

    public LightningCircle(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn,float randomness,float speed,float scale,float growth)
    {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn);
        this.particleIcon = ParticleIcon.fromWithAndHeight(96,0,32,32,128);
        this.particleMaxAge = 10 + (int) (this.rand.nextFloat() * 10);
        this.randomness = randomness;
        this.speed = speed;
        this.scale = scale;
        this.growth = growth;
        this.renderDistanceWeight = Minecraft.getMinecraft().gameSettings.renderDistanceChunks / 6d;
        setEntityBoundingBox(new AxisAlignedBB(xCoordIn-scale-growth,yCoordIn-2*randomness,zCoordIn-scale-growth,xCoordIn+scale+growth,yCoordIn+2*randomness,zCoordIn+scale+growth));
    }

    public void renderParticle(WorldRenderer worldRendererIn, Entity entityIn, float partialTicks, float rotX, float rotXZ, float rotZ, float rotYZ, float rotXY)
    {
        particleScale = scale + ((float) particleAge / (float)particleMaxAge) * growth;
        rand.setSeed((long) (particleAge * speed) + hashCode());
        Vec3 randomDir = new Vec3(rand.nextGaussian() * randomness,rand.nextGaussian() * randomness,rand.nextGaussian() * randomness);

        for (double i = 0;i < Math.PI*2;i+=0.1)
        {
            double tickX = prevPosX + ((this.posX - this.prevPosX) * (double) partialTicks - interpPosX);
            double tickY = prevPosY + ((this.posY - this.prevPosY) * (double) partialTicks - interpPosY);
            double tickZ = prevPosZ + ((this.posZ - this.prevPosZ) * (double) partialTicks - interpPosZ);

            int b = this.getBrightnessForRender(partialTicks);
            int j = b >> 16 & 65535;
            int k = b & 65535;

            Vec3 pos = new Vec3(Math.sin(i) * particleScale,0,Math.cos(i) * particleScale).add(randomDir);
            worldRendererIn.pos(pos.xCoord + tickX, pos.yCoord + tickY, pos.zCoord + tickZ).tex(0,0).color(this.particleRed, this.particleGreen, this.particleBlue ,this.particleAlpha).lightmap(j, k).endVertex();

            randomDir = randomDir.addVector(rand.nextGaussian() * randomness,rand.nextGaussian() * randomness,rand.nextGaussian() * randomness);
            pos = new Vec3(Math.sin(i+0.1) * particleScale,0,Math.cos(i+0.1) * particleScale).add(randomDir);
            worldRendererIn.pos(pos.xCoord + tickX, pos.yCoord + tickY, pos.zCoord + tickZ).tex(0,0).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
        }
    }
}
