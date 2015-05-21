package com.MO.MatterOverdrive.client.render;

import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by Simeon on 5/18/2015.
 */
public class RendererBlockGravitationalStabilizer extends MOBlockRenderer
{
    public static int renderID;

    public RendererBlockGravitationalStabilizer()
    {
        renderID = RenderingRegistry.getNextAvailableRenderId();
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
    {
        int meta = world.getBlockMetadata(x,y,z);
        if (meta == ForgeDirection.WEST.ordinal() || meta == ForgeDirection.EAST.ordinal())
        {
            renderer.uvRotateTop = 1;
            renderer.uvRotateBottom = 1;
        }
        super.renderWorldBlock(world,x,y,z,block,modelId,renderer);

        renderer.uvRotateTop = 0;
        renderer.uvRotateBottom = 0;
        return true;
    }

    @Override
    public int getRenderId()
    {
        return renderID;
    }
}
