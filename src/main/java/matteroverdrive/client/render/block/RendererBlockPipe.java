package matteroverdrive.client.render.block;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import matteroverdrive.tile.pipes.TileEntityPipe;
import matteroverdrive.util.math.MOMathHelper;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;

/**
 * Created by Simeon on 6/18/2015.
 */
public class RendererBlockPipe implements ISimpleBlockRenderingHandler
{
    public static int rendererID;

    public RendererBlockPipe()
    {
        rendererID = RenderingRegistry.getNextAvailableRenderId();
    }

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer)
    {
        IIcon icon = block.getIcon(0,metadata);
        GL11.glPushMatrix();
        GL11.glScaled(2,2,2);
        GL11.glTranslated(-0.5,-0.5,-0.5);
        //GL11.glDisable(GL11.GL_LIGHTING);
        Tessellator.instance.startDrawingQuads();
        renderPipe(0, 0, 0, block, 0, icon, 15728704);
        Tessellator.instance.draw();
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glPopMatrix();
    }


    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
    {
        int meta = world.getBlockMetadata(x,y,z);
        IIcon icon = block.getIcon(world,x,y,z,meta);
        int connections = ((TileEntityPipe)world.getTileEntity(x,y,z)).getConnections();
        int brightness = block.getMixedBrightnessForBlock(world, x, y, z);
        renderPipe(x,y,z,block,connections,icon,brightness);
        //renderer.renderStandardBlock(block,x,y,z);
        return true;
    }

    protected void renderPipe(int x,int y,int z,Block block,int connections,IIcon icon,int brigtness)
    {
        float step = 1f/3f;
        Tessellator.instance.setColorRGBA(255,255,255,255);
        Tessellator.instance.setBrightness(brigtness);

        for (int i = 0;i < 6;i++)
        {
            if (MOMathHelper.getBoolean(connections,i))
            {
                ForgeDirection direction = ForgeDirection.getOrientation(i);
                if (direction == ForgeDirection.UP || direction == ForgeDirection.DOWN) {
                    drawCubeUpDown(x + step + step * direction.offsetX, y + step + step * direction.offsetY, z + step + step * direction.offsetZ, 1f / 3f, icon, 6, 0);
                }else if (direction == ForgeDirection.NORTH || direction == ForgeDirection.SOUTH)
                {
                    drawCubeNorthSouth(x + step + step * direction.offsetX, y + step + step * direction.offsetY, z + step + step * direction.offsetZ, 1f / 3f, icon, 6, 0);
                }
                else
                {
                    drawCube(x + step + step * direction.offsetX, y + step + step * direction.offsetY, z + step + step * direction.offsetZ, 1f / 3f, icon, 6, 0);
                }
            }
        }

        if (connections == 3) {
            drawCubeUpDown(x + step, y + step, z + step, 1f / 3f, icon, 6, 0);
        }else if (connections == 12) {
            drawCubeNorthSouth(x + step,y + step,z + step,1f /3f,icon,6,0);
        }else if (connections == 48) {
            drawCube(x + step, y + step, z + step, 1f / 3f, icon, 6, 0);
        }else
        {
            drawCube(x + step, y + step, z + step, 1f / 3f, icon, 0, 0);
        }
    }

    public void drawCube(double x,double y,double z,float size,IIcon icon,int u,int v)
    {
        //bottom side
        Tessellator.instance.setColorOpaque_F(0.6f,0.65f,0.7f);
        Tessellator.instance.setNormal(0,-1,0);
        Tessellator.instance.addVertexWithUV(x,y,z,icon.getInterpolatedU(u),icon.getInterpolatedV(v));
        Tessellator.instance.addVertexWithUV(x+size,y,z,icon.getInterpolatedU(u +6),icon.getInterpolatedV(v));
        Tessellator.instance.addVertexWithUV(x+size,y,z+size,icon.getInterpolatedU(u + 6), icon.getInterpolatedV(v +6));
        Tessellator.instance.addVertexWithUV(x,y,z+size,icon.getInterpolatedU(u), icon.getInterpolatedV(v+6));

        //top side
        Tessellator.instance.setColorOpaque_F(1f,1f,1f);
        Tessellator.instance.setNormal(0,1,0);
        Tessellator.instance.addVertexWithUV(x,y + size,z,icon.getInterpolatedU(u),icon.getInterpolatedV(v));
        Tessellator.instance.addVertexWithUV(x,y + size,z+size,icon.getInterpolatedU(u), icon.getInterpolatedV(v+6));
        Tessellator.instance.addVertexWithUV(x+size,y + size,z+size,icon.getInterpolatedU(u + 6), icon.getInterpolatedV(v+6));
        Tessellator.instance.addVertexWithUV(x+size,y + size,z,icon.getInterpolatedU(u + 6),icon.getInterpolatedV(v));

        //west
        Tessellator.instance.setColorOpaque_F(0.7f,0.75f,0.8f);
        Tessellator.instance.setNormal(-1,0,0);
        Tessellator.instance.addVertexWithUV(x,y,z,icon.getInterpolatedU(u),icon.getInterpolatedV(v));
        Tessellator.instance.addVertexWithUV(x,y,z+size,icon.getInterpolatedU(u), icon.getInterpolatedV(v+6));
        Tessellator.instance.addVertexWithUV(x,y + size,z+size,icon.getInterpolatedU(u+6), icon.getInterpolatedV(v+6));
        Tessellator.instance.addVertexWithUV(x,y + size,z,icon.getInterpolatedU(u+6),icon.getInterpolatedV(v));

        //east
        Tessellator.instance.setColorOpaque_F(0.7f,0.75f,0.8f);
        Tessellator.instance.setNormal(1,0,0);
        Tessellator.instance.addVertexWithUV(x + size,y,z,icon.getInterpolatedU(u),icon.getInterpolatedV(v));
        Tessellator.instance.addVertexWithUV(x + size,y + size,z,icon.getInterpolatedU(u+6),icon.getInterpolatedV(v));
        Tessellator.instance.addVertexWithUV(x + size,y + size,z+size,icon.getInterpolatedU(u+6), icon.getInterpolatedV(v+6));
        Tessellator.instance.addVertexWithUV(x + size,y,z+size,icon.getInterpolatedU(u), icon.getInterpolatedV(v+6));

        //south
        Tessellator.instance.setColorOpaque_F(0.9f,0.95f,1f);
        Tessellator.instance.setNormal(0,0,1);
        Tessellator.instance.addVertexWithUV(x,y,z + size,icon.getInterpolatedU(u),icon.getInterpolatedV(v));
        Tessellator.instance.addVertexWithUV(x + size,y,z + size,icon.getInterpolatedU(u+6),icon.getInterpolatedV(v));
        Tessellator.instance.addVertexWithUV(x + size,y + size,z + size,icon.getInterpolatedU(u+6), icon.getInterpolatedV(v+6));
        Tessellator.instance.addVertexWithUV(x,y + size,z + size,icon.getInterpolatedU(u), icon.getInterpolatedV(v+6));

        //north
        Tessellator.instance.setColorOpaque_F(0.9f,0.95f,1f);
        Tessellator.instance.setNormal(0,0,-1);
        Tessellator.instance.addVertexWithUV(x,y,z,icon.getInterpolatedU(u),icon.getInterpolatedV(v));
        Tessellator.instance.addVertexWithUV(x,y + size,z,icon.getInterpolatedU(u), icon.getInterpolatedV(v+6));
        Tessellator.instance.addVertexWithUV(x + size,y + size,z,icon.getInterpolatedU(u+6), icon.getInterpolatedV(v+6));
        Tessellator.instance.addVertexWithUV(x + size,y,z,icon.getInterpolatedU(u+6),icon.getInterpolatedV(v));
    }

    public void drawCubeUpDown(double x,double y,double z,float size,IIcon icon,int u,int v)
    {
        //bottom side
        Tessellator.instance.setColorOpaque_F(0.6f,0.65f,0.7f);
        Tessellator.instance.setNormal(0,-1,0);
        Tessellator.instance.addVertexWithUV(x,y,z,icon.getInterpolatedU(u),icon.getInterpolatedV(v));
        Tessellator.instance.addVertexWithUV(x+size,y,z,icon.getInterpolatedU(u +6),icon.getInterpolatedV(v));
        Tessellator.instance.addVertexWithUV(x+size,y,z+size,icon.getInterpolatedU(u + 6), icon.getInterpolatedV(v +6));
        Tessellator.instance.addVertexWithUV(x,y,z+size,icon.getInterpolatedU(u), icon.getInterpolatedV(v+6));

        //top side
        Tessellator.instance.setColorOpaque_F(1f,1f,1f);
        Tessellator.instance.setNormal(0,1,0);
        Tessellator.instance.addVertexWithUV(x,y + size,z,icon.getInterpolatedU(u),icon.getInterpolatedV(v));
        Tessellator.instance.addVertexWithUV(x,y + size,z+size,icon.getInterpolatedU(u), icon.getInterpolatedV(v+6));
        Tessellator.instance.addVertexWithUV(x+size,y + size,z+size,icon.getInterpolatedU(u + 6), icon.getInterpolatedV(v+6));
        Tessellator.instance.addVertexWithUV(x+size,y + size,z,icon.getInterpolatedU(u + 6),icon.getInterpolatedV(v));

        //west
        Tessellator.instance.setColorOpaque_F(0.7f,0.75f,0.8f);
        Tessellator.instance.setNormal(-1,0,0);
        Tessellator.instance.addVertexWithUV(x,y,z,icon.getInterpolatedU(u),icon.getInterpolatedV(v));
        Tessellator.instance.addVertexWithUV(x,y,z+size,icon.getInterpolatedU(u), icon.getInterpolatedV(v+6));
        Tessellator.instance.addVertexWithUV(x,y + size,z+size,icon.getInterpolatedU(u+6), icon.getInterpolatedV(v+6));
        Tessellator.instance.addVertexWithUV(x,y + size,z,icon.getInterpolatedU(u+6),icon.getInterpolatedV(v));

        //east
        Tessellator.instance.setColorOpaque_F(0.7f,0.75f,0.8f);
        Tessellator.instance.setNormal(1,0,0);
        Tessellator.instance.addVertexWithUV(x + size,y,z,icon.getInterpolatedU(u),icon.getInterpolatedV(v));
        Tessellator.instance.addVertexWithUV(x + size,y + size,z,icon.getInterpolatedU(u+6),icon.getInterpolatedV(v));
        Tessellator.instance.addVertexWithUV(x + size,y + size,z+size,icon.getInterpolatedU(u+6), icon.getInterpolatedV(v+6));
        Tessellator.instance.addVertexWithUV(x + size,y,z+size,icon.getInterpolatedU(u), icon.getInterpolatedV(v+6));

        //south
        Tessellator.instance.setColorOpaque_F(0.9f,0.95f,1f);
        Tessellator.instance.setNormal(0,0,1);
        Tessellator.instance.addVertexWithUV(x,y,z + size,icon.getInterpolatedU(u),icon.getInterpolatedV(v+6));
        Tessellator.instance.addVertexWithUV(x + size,y,z + size,icon.getInterpolatedU(u),icon.getInterpolatedV(v));
        Tessellator.instance.addVertexWithUV(x + size,y + size,z + size,icon.getInterpolatedU(u+6), icon.getInterpolatedV(v));
        Tessellator.instance.addVertexWithUV(x,y + size,z + size,icon.getInterpolatedU(u+6), icon.getInterpolatedV(v+6));

        //north
        Tessellator.instance.setColorOpaque_F(0.9f,1f,0.95f);
        Tessellator.instance.setNormal(0,0,-1);
        Tessellator.instance.addVertexWithUV(x,y,z,icon.getInterpolatedU(u+6),icon.getInterpolatedV(v));
        Tessellator.instance.addVertexWithUV(x,y + size,z,icon.getInterpolatedU(u), icon.getInterpolatedV(v));
        Tessellator.instance.addVertexWithUV(x + size,y + size,z,icon.getInterpolatedU(u), icon.getInterpolatedV(v+6));
        Tessellator.instance.addVertexWithUV(x + size,y,z,icon.getInterpolatedU(u+6),icon.getInterpolatedV(v+6));
    }

    public void drawCubeNorthSouth(double x,double y,double z,float size,IIcon icon,int u,int v)
    {
        //bottom side
        Tessellator.instance.setColorOpaque_F(0.6f,0.65f,0.7f);
        Tessellator.instance.setNormal(0,-1,0);
        Tessellator.instance.addVertexWithUV(x,y,z,icon.getInterpolatedU(u),icon.getInterpolatedV(v+6));
        Tessellator.instance.addVertexWithUV(x+size,y,z,icon.getInterpolatedU(u),icon.getInterpolatedV(v));
        Tessellator.instance.addVertexWithUV(x+size,y,z+size,icon.getInterpolatedU(u + 6), icon.getInterpolatedV(v));
        Tessellator.instance.addVertexWithUV(x,y,z+size,icon.getInterpolatedU(u+6), icon.getInterpolatedV(v+6));

        //top side
        Tessellator.instance.setColorOpaque_F(1f,1f,1f);
        Tessellator.instance.setNormal(0,1,0);
        Tessellator.instance.addVertexWithUV(x,y + size,z,icon.getInterpolatedU(u+6),icon.getInterpolatedV(v));
        Tessellator.instance.addVertexWithUV(x,y + size,z+size,icon.getInterpolatedU(u), icon.getInterpolatedV(v));
        Tessellator.instance.addVertexWithUV(x+size,y + size,z+size,icon.getInterpolatedU(u), icon.getInterpolatedV(v+6));
        Tessellator.instance.addVertexWithUV(x+size,y + size,z,icon.getInterpolatedU(u + 6),icon.getInterpolatedV(v+6));

        //west
        Tessellator.instance.setColorOpaque_F(0.7f,0.75f,0.8f);
        Tessellator.instance.setNormal(-1,0,0);
        Tessellator.instance.addVertexWithUV(x,y,z,icon.getInterpolatedU(u+6),icon.getInterpolatedV(v));
        Tessellator.instance.addVertexWithUV(x,y,z+size,icon.getInterpolatedU(u), icon.getInterpolatedV(v));
        Tessellator.instance.addVertexWithUV(x,y + size,z+size,icon.getInterpolatedU(u), icon.getInterpolatedV(v+6));
        Tessellator.instance.addVertexWithUV(x,y + size,z,icon.getInterpolatedU(u+6),icon.getInterpolatedV(v+6));

        //east
        Tessellator.instance.setColorOpaque_F(0.7f,0.75f,0.8f);
        Tessellator.instance.setNormal(1,0,0);
        Tessellator.instance.addVertexWithUV(x + size,y,z,icon.getInterpolatedU(u),icon.getInterpolatedV(v+6));
        Tessellator.instance.addVertexWithUV(x + size,y + size,z,icon.getInterpolatedU(u),icon.getInterpolatedV(v));
        Tessellator.instance.addVertexWithUV(x + size,y + size,z+size,icon.getInterpolatedU(u+6), icon.getInterpolatedV(v));
        Tessellator.instance.addVertexWithUV(x + size,y,z+size,icon.getInterpolatedU(u+6), icon.getInterpolatedV(v+6));

        //south
        Tessellator.instance.setColorOpaque_F(0.9f,0.95f,1f);
        Tessellator.instance.setNormal(0,0,1);
        Tessellator.instance.addVertexWithUV(x,y,z + size,icon.getInterpolatedU(u),icon.getInterpolatedV(v+6));
        Tessellator.instance.addVertexWithUV(x + size,y,z + size,icon.getInterpolatedU(u),icon.getInterpolatedV(v));
        Tessellator.instance.addVertexWithUV(x + size,y + size,z + size,icon.getInterpolatedU(u+6), icon.getInterpolatedV(v));
        Tessellator.instance.addVertexWithUV(x,y + size,z + size,icon.getInterpolatedU(u+6), icon.getInterpolatedV(v+6));

        //north
        Tessellator.instance.setColorOpaque_F(0.9f,0.95f,1f);
        Tessellator.instance.setNormal(0,0,-1);
        Tessellator.instance.addVertexWithUV(x,y,z,icon.getInterpolatedU(u+6),icon.getInterpolatedV(v));
        Tessellator.instance.addVertexWithUV(x,y + size,z,icon.getInterpolatedU(u), icon.getInterpolatedV(v));
        Tessellator.instance.addVertexWithUV(x + size,y + size,z,icon.getInterpolatedU(u), icon.getInterpolatedV(v+6));
        Tessellator.instance.addVertexWithUV(x + size,y,z,icon.getInterpolatedU(u+6),icon.getInterpolatedV(v+6));
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return true;
    }

    @Override
    public int getRenderId() {
        return rendererID;
    }
}
