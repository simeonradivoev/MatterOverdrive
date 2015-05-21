package com.MO.MatterOverdrive.tile;

import cofh.lib.util.helpers.BlockHelper;
import com.MO.MatterOverdrive.MatterOverdrive;
import com.MO.MatterOverdrive.blocks.BlockGravitationalAnomaly;
import com.MO.MatterOverdrive.client.RenderParticlesHandler;
import com.MO.MatterOverdrive.fx.GravitationalStabilizerBeamParticle;
import com.MO.MatterOverdrive.proxy.ClientProxy;
import com.MO.MatterOverdrive.util.math.MOMathHelper;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.util.vector.Vector3f;

/**
 * Created by Simeon on 5/12/2015.
 */
public class TileEntityMachineGravitationalStabilizer extends MOTileEntityMachineEnergy implements IMOTickable
{
    MovingObjectPosition hit;

    public TileEntityMachineGravitationalStabilizer()
    {
        super(4);
    }

    @Override
    public void updateEntity()
    {
        super.updateEntity();

        if (worldObj.isRemote)
        {
            spawnParticles(worldObj);
            hit = seacrhForAnomalies(worldObj);
        }
    }

    MovingObjectPosition seacrhForAnomalies(World world)
    {
        ForgeDirection front = ForgeDirection.getOrientation(world.getBlockMetadata(xCoord, yCoord, zCoord));
        for (int i = 1;i < 64;i++)
        {
            Block block = world.getBlock(xCoord + front.offsetX * i, yCoord + front.offsetY * i, zCoord + front.offsetZ * i);
            if (block instanceof BlockGravitationalAnomaly || block.getMaterial() == null || block.getMaterial().isOpaque())
            {
                return new MovingObjectPosition(xCoord + front.offsetX * i, yCoord + front.offsetY * i, zCoord + front.offsetZ * i, front.getOpposite().ordinal(),Vec3.createVectorHelper(xCoord + (front.offsetX * i) - Math.abs(front.offsetX) * 0.5, yCoord + (front.offsetY * i) - Math.abs(front.offsetY) * 0.5, zCoord + (front.offsetZ * i) - Math.abs(front.offsetZ) * 0.5));
            }
        }
        return null;
    }

    void manageAnomalies(World world)
    {
        hit = seacrhForAnomalies(world);
        if (hit != null && world.getTileEntity(hit.blockX,hit.blockY,hit.blockZ) instanceof TileEntityGravitationalAnomaly)
        {
            ((TileEntityGravitationalAnomaly) world.getTileEntity(hit.blockX,hit.blockY,hit.blockZ)).supress(1d / 7d);
        }
    }

    @SideOnly(Side.CLIENT)
    void spawnParticles(World world)
    {
        if (hit != null && world.getTileEntity(hit.blockX,hit.blockY,hit.blockZ) instanceof TileEntityGravitationalAnomaly)
        {
            if (random.nextFloat() < 0.2f) {
                float r = (float) getBeamColorR();
                float g = (float) getBeamColorG();
                float b = (float) getBeamColorB();

                if (r != 0 || g != 0 || b != 0) {
                    ForgeDirection up = ForgeDirection.getOrientation(BlockHelper.getAboveSide(worldObj.getBlockMetadata(xCoord, yCoord, zCoord)));
                    GravitationalStabilizerBeamParticle particle = new GravitationalStabilizerBeamParticle(worldObj, new Vector3f(xCoord + 0.5f, yCoord + 0.5f, zCoord + 0.5f), new Vector3f(hit.blockX + 0.5f, hit.blockY + 0.5f, hit.blockZ + 0.5f), new Vector3f(up.offsetX, up.offsetY, up.offsetZ), 1f, 0.3f, 80);
                    particle.setColor(r, g, b, 1);
                    ClientProxy.renderHandler.getRenderParticlesHandler().addEffect(particle, RenderParticlesHandler.Blending.Additive);
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public double getMaxRenderDistanceSquared()
    {
        return 4086 * 2;
    }

    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox()
    {
        Block type = getBlockType();
        AxisAlignedBB bb = type.getCollisionBoundingBoxFromPool(worldObj, xCoord, yCoord, zCoord);
        if (hit != null)
        {
            return bb.expand(hit.blockX - xCoord,hit.blockY - yCoord,hit.blockZ - zCoord);
        }
        return bb;
    }

    @Override
    public String getSound() {
        return "force_field";
    }

    @Override
    public boolean hasSound() {
        return true;
    }

    @Override
    public boolean isActive() {
        return hit != null;
    }

    @Override
    public float soundVolume() {
        return (float) Math.max(Math.max(getBeamColorR(), getBeamColorG()), getBeamColorB()) * 0.5f;
    }

    public double getBeamColorR()
    {
        return MOMathHelper.noise(0,yCoord,worldObj.getWorldTime() * 0.01);
    }

    public double getBeamColorG()
    {
        return MOMathHelper.noise(xCoord,0,worldObj.getWorldTime() * 0.01);
    }

    public double getBeamColorB()
    {
        return MOMathHelper.noise(0,0,zCoord + worldObj.getWorldTime() * 0.01);
    }

    public MovingObjectPosition getHit()
    {
        return hit;
    }

    @Override
    public boolean shouldRenderInPass(int pass)
    {
        return pass == 1;
    }

    @Override
    public void onServerTick(TickEvent.WorldTickEvent event)
    {
        if (worldObj == null)
            return;

        if (event.phase.equals(TickEvent.Phase.START))
        {
            manageAnomalies(worldObj);
        }
    }

    @Override
    public int getPhase()
    {
        return 0;
    }
}
