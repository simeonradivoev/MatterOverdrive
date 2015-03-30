package com.MO.MatterOverdrive.blocks;

import java.util.List;

import com.MO.MatterOverdrive.api.matter.IMatterConnection;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.MO.MatterOverdrive.blocks.includes.MOBlockContainer;
import com.MO.MatterOverdrive.tile.pipes.TileEntityPipe;

import net.minecraftforge.common.util.ForgeDirection;

public class BlockPipe extends MOBlockContainer
{

	public BlockPipe(Material material, String name) 
	{
		super(material, name);
		this.useNeighborBrightness = true;
		float pixel = 1f/16f;
		
		float size = 0.34375f;

        float xMin = size;
        float yMin = size;
        float zMin = size;

        float xMax = 1 - size;
        float yMax = 1 - size;
        float zMax = 1 - size;
        
        //this.setBlockBounds(xMin, yMin, zMin, xMax, yMax, zMax);
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
    public void setBlockBoundsBasedOnState(IBlockAccess worldAccess, int x, int y, int z)
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
            if(isConnectableSide(dir,worldAccess,x,y,z))
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

    public boolean isConnectableSide(ForgeDirection dir,IBlockAccess world,int x,int y,int z)
    {
        TileEntityPipe pipe = (TileEntityPipe)world.getTileEntity(x,y,z);
        if(pipe != null)
        {
            return pipe.isConnectableSide(dir);
        }
        return  false;
    }

    @Override
	public int getRenderType()
	{
		return -1;
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
	public TileEntity createNewTileEntity(World world, int meta)
	{
		return new TileEntityPipe(IMatterConnection.class);
	}

}
