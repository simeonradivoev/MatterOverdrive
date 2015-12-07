/*
 * This file is part of Matter Overdrive
 * Copyright (c) 2015., Simeon Radivoev, All rights reserved.
 *
 * Matter Overdrive is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Matter Overdrive is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Matter Overdrive.  If not, see <http://www.gnu.org/licenses>.
 */

package matteroverdrive.client.render.block;

import cpw.mods.fml.client.registry.RenderingRegistry;
import matteroverdrive.blocks.BlockDecorative;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;

/**
 * Created by Simeon on 11/27/2015.
 */
public class RendererBlockDecorativeVertical extends MOBlockRenderer
{
    public static int renderID;

    public RendererBlockDecorativeVertical()
    {
        renderID = RenderingRegistry.getNextAvailableRenderId();
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
    {
        int meta = world.getBlockMetadata(x,y,z);
        if (block instanceof BlockDecorative)
        {
            if (((BlockDecorative) block).canBeRotated()) {
                renderer.uvRotateBottom = meta;
                renderer.uvRotateTop = meta;
                renderer.uvRotateEast = meta;
                renderer.uvRotateWest = meta;
                renderer.uvRotateNorth = meta;
                renderer.uvRotateSouth = meta;
            }
            else
            {
                //GuiColor color = new GuiColor(block.getRenderColor(meta));
                //renderer.colorRedTopRight = renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = color.getFloatR();
            }
        }
        renderer.renderStandardBlock(block, x, y, z);
        renderer.uvRotateBottom = 0;
        renderer.uvRotateTop = 0;
        renderer.uvRotateEast = 0;
        renderer.uvRotateWest = 0;
        renderer.uvRotateNorth = 0;
        renderer.uvRotateSouth = 0;
        return true;
    }

    @Override
    public int getRenderId()
    {
        return renderID;
    }
}
