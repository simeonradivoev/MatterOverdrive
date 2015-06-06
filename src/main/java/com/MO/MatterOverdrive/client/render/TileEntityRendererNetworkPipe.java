package com.MO.MatterOverdrive.client.render;

import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.tile.pipes.TileEntityPipe;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import net.minecraftforge.common.util.ForgeDirection;
import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Simeon on 3/7/2015.
 */
public class TileEntityRendererNetworkPipe extends TileEntityRendererPipe
{

    public TileEntityRendererNetworkPipe()
    {
        texture = new ResourceLocation(Reference.PATH_BLOCKS + "network_pipe.png");
    }

    @Override
    protected void drawCore(TileEntityPipe tile,double x,
                            double y, double z, float f,int sides)
    {
        super.drawCore(tile,x,y,z,f,sides);
    }

    @Override
    protected void drawSide(TileEntityPipe tile,ForgeDirection dir)
    {
        super.drawSide(tile, dir);
    }
}
