package com.MO.MatterOverdrive.blocks;

import com.MO.MatterOverdrive.api.IScannable;
import com.MO.MatterOverdrive.blocks.includes.MOBlockContainer;
import com.MO.MatterOverdrive.handler.MOConfigurationHandler;
import com.MO.MatterOverdrive.tile.TileEntityGravitationalAnomaly;
import com.MO.MatterOverdrive.util.IConfigSubscriber;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by Simeon on 5/11/2015.
 */
public class BlockGravitationalAnomaly extends MOBlockContainer implements IScannable, IConfigSubscriber
{
    public BlockGravitationalAnomaly(Material material, String name)
    {
        super(material, name);
        setBlockBounds(0.3f,0.3f,0.3f,0.6f,0.6f,0.6f);
        setBlockUnbreakable();
        setResistance(6000000.0F);
        disableStats();
    }
    @Override
    public boolean isNormalCube()
    {
        return false;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World p_149668_1_, int p_149668_2_, int p_149668_3_, int p_149668_4_)
    {
        return null;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World p_149633_1_, int p_149633_2_, int p_149633_3_, int p_149633_4_)
    {
        this.setBlockBoundsBasedOnState(p_149633_1_, p_149633_2_, p_149633_3_, p_149633_4_);
        return super.getSelectedBoundingBoxFromPool(p_149633_1_, p_149633_2_, p_149633_3_, p_149633_4_);
    }

    @Override
    public MovingObjectPosition collisionRayTrace(World p_149731_1_, int p_149731_2_, int p_149731_3_, int p_149731_4_, Vec3 p_149731_5_, Vec3 p_149731_6_)
    {
        this.setBlockBoundsBasedOnState(p_149731_1_, p_149731_2_, p_149731_3_, p_149731_4_);
        return super.collisionRayTrace(p_149731_1_, p_149731_2_, p_149731_3_, p_149731_4_, p_149731_5_, p_149731_6_);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
    {
        TileEntity tileEntity = world.getTileEntity(x,y,z);
        if (tileEntity != null && tileEntity instanceof TileEntityGravitationalAnomaly)
        {
            double range = ((TileEntityGravitationalAnomaly) tileEntity).getEventHorizon();
            range = Math.max(range,0.4);
            float rangeMin = (float)(0.5 - (range/2));
            float rangeMax = (float)(0.5 + (range/2));
            setBlockBounds(rangeMin,rangeMin,rangeMin,rangeMax,rangeMax,rangeMax);
        }
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }
    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TileEntityGravitationalAnomaly();
    }

    @Override
    public void addInfo(World world, double x, double y, double z, List<String> infos)
    {
        TileEntity tileEntity = world.getTileEntity((int)x,(int)y,(int)z);

        if (tileEntity != null && tileEntity instanceof TileEntityGravitationalAnomaly)
        {
            ((TileEntityGravitationalAnomaly) tileEntity).addInfo(world,x,y,z,infos);
        }
    }

    @Override
    public void onScan(World world, double x, double y, double z, EntityPlayer player, ItemStack scanner)
    {

    }

    @Override
    public boolean canEntityDestroy(IBlockAccess world, int x, int y, int z, Entity entity)
    {
        return false;
    }

    @Override
    public int getRenderType()
    {
        return -1;
    }

    @Override
    public void onConfigChanged(MOConfigurationHandler config)
    {
        TileEntityGravitationalAnomaly.BLOCK_ENTETIES = config.getBool(MOConfigurationHandler.KEY_GRAVITATIONAL_ANOMALY_BLOCK_ENTITIES, MOConfigurationHandler.CATEGORY_SERVER, true, "Should the blocks drop entities or be directly consumed when destroyed by the gravitational anomaly");
        TileEntityGravitationalAnomaly.FALLING_BLOCKS = config.getBool(MOConfigurationHandler.KEY_GRAVITATIONAL_ANOMALY_FALLING_BLOCKS,MOConfigurationHandler.CATEGORY_SERVER,true,"Should blocks be turned into falling blocks when broken");
    }
}
