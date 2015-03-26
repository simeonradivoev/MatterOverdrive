package com.MO.MatterOverdrive.blocks;

import com.MO.MatterOverdrive.tile.pipes.TileEntityNetworkPipe;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Created by Simeon on 3/15/2015.
 */
public class BlockNetworkPipe extends BlockPipe
{

    public BlockNetworkPipe(Material material, String name)
    {
        super(material, name);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return new TileEntityNetworkPipe();
    }
}
