package matteroverdrive.client.render.tileentity;

import matteroverdrive.Reference;
import matteroverdrive.tile.pipes.TileEntityPipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;

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
    protected void drawCore(TileEntityPipe tile, double x,
                            double y, double z, float f, int sides)
    {
        super.drawCore(tile, x, y, z, f, sides);
    }

    @Override
    protected void drawSide(TileEntityPipe tile, ForgeDirection dir)
    {
        super.drawSide(tile, dir);
    }
}
