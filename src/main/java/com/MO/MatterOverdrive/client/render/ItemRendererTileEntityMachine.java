package com.MO.MatterOverdrive.client.render;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.IItemRenderer;

/**
 * Created by Simeon on 4/5/2015.
 */
public class ItemRendererTileEntityMachine implements IItemRenderer
{
    private TileEntitySpecialRenderer renderer;
    private TileEntity tileEntity;

    public ItemRendererTileEntityMachine(TileEntitySpecialRenderer renderer, TileEntity tileEntity)
    {
        this.renderer = renderer;
        this.tileEntity = tileEntity;
    }

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type)
    {
        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper)
    {
        return true;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data)
    {
        renderer.renderTileEntityAt(tileEntity,0D,-0.1D,0D,0F);
    }
}
