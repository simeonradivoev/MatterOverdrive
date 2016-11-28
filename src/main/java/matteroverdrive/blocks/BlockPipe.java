package matteroverdrive.blocks;

import matteroverdrive.blocks.includes.MOBlockContainer;
import matteroverdrive.client.render.block.RendererBlockPipe;
import matteroverdrive.tile.pipes.TileEntityPipe;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;

public abstract class BlockPipe extends MOBlockContainer
{

	public BlockPipe(Material material, String name)
	{
		super(material, name);
		this.useNeighborBrightness = true;
        this.setRotationType(-1);
	}

    @Override
	public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB bb, List list, Entity e)
    {
		float size = 0.34375f;

        float xMin = size;
        float yMin = size;
        float zMin = size;

        float xMax = 1 - size;
        float yMax = 1 - size;
        float zMax = 1 - size;

        this.setBlockBounds(xMin, yMin, zMin, xMax, yMax, zMax);
        super.addCollisionBoxesToList(world, x, y, z, bb, list, e);

        for(int i = 0;i < 6;i++)
        {
            ForgeDirection dir = ForgeDirection.values()[i];
            if(isConnectableSide(dir,world,x,y,z))
            {
	            if(dir != null)
	            {
	                float xMinNew = xMin + size * dir.offsetX;
                    float xMaxNew = xMax + size * dir.offsetX;
                    float yMinNew = yMin + size * dir.offsetY;
                    float yMaxNew = yMax + size * dir.offsetY;
                    float zMinNew = zMin + size * dir.offsetZ;
                    float zMaxNew = zMax + size * dir.offsetZ;

                    this.setBlockBounds(xMinNew, yMinNew, zMinNew, xMaxNew, yMaxNew, zMaxNew);
                    super.addCollisionBoxesToList(world, x, y, z, bb, list, e);
	            }
            }
        }
    }

	@Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
    {
        float size = 0.34375f;

        float xMin = size;
        float yMin = size;
        float zMin = size;

        float xMax = 1 - size;
        float yMax = 1 - size;
        float zMax = 1 - size;

        for(int i = 0;i < 6;i++)
        {
            ForgeDirection dir = ForgeDirection.values()[i];
            if(isConnectableSide(dir, world, x, y, z))
            {
	            if(dir != null)
	            {
	                if (dir.offsetX < 0) {
	                    xMin = 0;
	                } else if (dir.offsetX > 0) {
	                    xMax = 1;
	                }

	                if (dir.offsetY < 0) {
	                    yMin = 0;
	                } else if (dir.offsetY > 0) {
	                    yMax = 1;
	                }

	                if (dir.offsetZ < 0) {
	                    zMin = 0;
	                } else if (dir.offsetZ > 0) {
	                    zMax = 1;
	                }
	            }
            }
        }

        this.setBlockBounds(xMin, yMin, zMin, xMax, yMax, zMax);
    }

    @Override
    public void onBlockPlacedBy(World World, int x, int y, int z, EntityLivingBase player, ItemStack item)
    {

    }

    public boolean isConnectableSide(ForgeDirection dir, IBlockAccess world, int x, int y, int z)
    {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity instanceof TileEntityPipe)
        {
            return tileEntity != null && ((TileEntityPipe)tileEntity).isConnectableSide(dir);
        }
        return false;
    }

    @Override
	public int getRenderType()
	{
		return RendererBlockPipe.rendererID;
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

}
