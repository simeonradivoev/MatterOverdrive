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

package matteroverdrive.client.render.tileentity;

import matteroverdrive.blocks.BlockNetworkSwitch;
import matteroverdrive.tile.TileEntityMachinePacketQueue;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

/**
 * Created by Simeon on 8/22/2015.
 */
public class TileEntityRendererPacketQueue extends TileEntitySpecialRenderer
{
	final Block fakeBlock = new BlockNetworkSwitch(Material.IRON, "fake_block");

	@Override
	public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float ticks, int destoryStage)
	{
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		if (tileEntity instanceof TileEntityMachinePacketQueue)
		{
			if (((TileEntityMachinePacketQueue)tileEntity).flashTime > 0)
			{
				renderBlock(fakeBlock);
			}
		}

		GlStateManager.popMatrix();
	}

	private void renderBlock(Block block)
	{
		/*float distance = 0.1f;

        GlStateManager.disableLighting();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL_ONE,GL_ONE);
        RenderUtils.disableLightmap();
        RenderUtils.setBlockTextureSheet();
        RenderUtils.drawCube(-0.01, -0.01, -0.01, 1.02, 1.02, 1.02, icon.getMinU(), icon.getMinV(), icon.getMaxU(), icon.getMaxV(), Reference.COLOR_HOLO);
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        RenderUtils.enableLightmap();*/
	}
}
