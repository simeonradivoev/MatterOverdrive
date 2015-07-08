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

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import matteroverdrive.Reference;
import matteroverdrive.init.MatterOverdriveIcons;
import matteroverdrive.util.RenderUtils;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import net.minecraftforge.client.model.obj.GroupObject;
import net.minecraftforge.client.model.obj.WavefrontObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

/**
 * Created by Simeon on 7/8/2015.
 */
public class RendererBlockPatternStorage implements ISimpleBlockRenderingHandler
{
    public static int renderID;
    private IModelCustom model;

    public RendererBlockPatternStorage()
    {
        renderID = RenderingRegistry.getNextAvailableRenderId();
        model = AdvancedModelLoader.loadModel(new ResourceLocation(Reference.MODEL_PATTERN_STORAGE));
    }

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer)
    {
        Tessellator.instance.startDrawing(GL11.GL_TRIANGLES);
        Matrix4f rot = new Matrix4f();
        rot.translate(new Vector3f(-0.5f,-0.5f,-0.5f));
        renderBlock(rot,0,0,0,240);
        Tessellator.instance.draw();
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
    {
        Matrix4f rot = new Matrix4f();
        RenderUtils.rotateFromBlock(rot, world, x, y, z);
        renderBlock(rot, x, y, z, block.getMixedBrightnessForBlock(world, x, y, z));
        return true;
    }

    protected void renderBlock(Matrix4f mat,int x,int y,int z,int brightness)
    {
        Tessellator.instance.draw();
        Tessellator.instance.startDrawing(GL11.GL_TRIANGLES);
        GroupObject base = ((WavefrontObject) model).groupObjects.get(1);
        GroupObject vent = ((WavefrontObject) model).groupObjects.get(0);


        RenderUtils.tesseleteModelAsBlock(mat, base, MatterOverdriveIcons.pattern_storage, x, y, z, brightness, true, null);
        RenderUtils.tesseleteModelAsBlock(mat, vent, MatterOverdriveIcons.Vent, x, y, z, brightness, true, null);
        Tessellator.instance.draw();
        Tessellator.instance.startDrawingQuads();
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return true;
    }

    @Override
    public int getRenderId() {
        return renderID;
    }
}
