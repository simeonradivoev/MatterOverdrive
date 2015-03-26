package com.MO.MatterOverdrive.client.render;

import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.tile.pipes.TileEntityMatterPipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;

import javax.vecmath.Vector2f;

/**
 * Created by Simeon on 3/7/2015.
 */
public class TileEntityRendererMatterPipe extends TileEntityRendererPipe
{
    public TileEntityRendererMatterPipe()
    {
        texture = new ResourceLocation(Reference.PATH_BLOCKS + "matter_pipe.png");
    }

    @Override
    protected  Vector2f getCoreUV(TileEntity entity)
    {
        if(entity instanceof TileEntityMatterPipe)
        {
            if(((TileEntityMatterPipe)entity).matterVisible())
            {
                return  new Vector2f(0,1);
            }
        }

        return  new Vector2f(0,0);
    }

    @Override
    protected  Vector2f getSidesUV(TileEntity entity,ForgeDirection dir)
    {
        if(entity instanceof TileEntityMatterPipe)
        {
            if(((TileEntityMatterPipe)entity).matterVisible())
            {
                return  new Vector2f(1,1);
            }
        }

        return  new Vector2f(1,0);
    }
}
