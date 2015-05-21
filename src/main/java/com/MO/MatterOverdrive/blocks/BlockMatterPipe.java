package com.MO.MatterOverdrive.blocks;

import cofh.lib.util.helpers.BlockHelper;
import com.MO.MatterOverdrive.tile.pipes.TileEntityMatterPipe;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Created by Simeon on 3/7/2015.
 */
public class BlockMatterPipe extends BlockPipe
{

    public BlockMatterPipe(Material material, String name)
    {
        super(material, name);
        setHardness(10.0F);
        this.setResistance(5.0f);
        setRotationType(BlockHelper.RotationType.PREVENT);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return new TileEntityMatterPipe();
    }
}
