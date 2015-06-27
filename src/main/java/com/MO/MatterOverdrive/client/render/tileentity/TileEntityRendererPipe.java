package com.MO.MatterOverdrive.client.render.tileentity;

import javax.vecmath.*;

import cofh.lib.render.RenderHelper;
import com.MO.MatterOverdrive.init.MatterOverdriveBlocks;
import com.MO.MatterOverdrive.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.IIcon;
import org.lwjgl.Sys;
import org.lwjgl.opengl.GL11;

import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.tile.pipes.TileEntityPipe;
import com.MO.MatterOverdrive.util.math.MOMathHelper;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL12;

public class TileEntityRendererPipe extends TileEntitySpecialRenderer {
	
	private static Vector4d pv1 = new Vector4d(1,1,0,1);
	private static Vector4d pv2 = new Vector4d(1,0,0,1);
	private static Vector4d pv3 = new Vector4d(0,0,0,1);
	private static Vector4d pv4 = new Vector4d(0,1,0,1);

	ResourceLocation texture;
	protected static double size = 5 * (1d / 15d);
	boolean drawInside = false;
	float texPixel = 1f / 16f;

    public TileEntityRendererPipe()
    {
        texture = new ResourceLocation(Reference.PATH_BLOCKS + "pipe.png");
    }

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x,
			double y, double z, float f)
	{

		if(tileentity instanceof TileEntityPipe)
        {
            TileEntityPipe pipe = (TileEntityPipe) tileentity;
            GL11.glPushMatrix();

			GL11.glTranslated(x, y, z);
            if(texture != null)
                this.bindTexture(texture);

            drawCore(pipe,x,y,z,f,DrawSides(pipe, x, y, z, f));

            GL11.glPopMatrix();
        }
	}

    protected int DrawSides(TileEntityPipe pipe, double x,
                             double y, double z, float f)
    {
		int connections = pipe.getConnections();
        for (int i = 0; i < 6; i++)
        {
            if (MOMathHelper.getBoolean(connections,i))
            {
                drawSide(pipe,ForgeDirection.values()[i]);
            }
        }
		return connections;
    }

    protected void drawCore(TileEntityPipe tile,double x,
                            double y, double z, float f,int sides)
	{
		Vector2f uv = getCoreUV(tile);
		AxisAngle4d rotation = new AxisAngle4d();
		
		if(drawInside)
			GL11.glDisable(GL11.GL_CULL_FACE);
		
		if(sides == 3)
        {
			uv = getSidesUV(tile,ForgeDirection.UP);
			rotation = new AxisAngle4d(0,0,1,90);
        }
        else if(sides == 12)
        {
        	uv = getSidesUV(tile,ForgeDirection.NORTH);
            rotation = new AxisAngle4d(0,1,0,90);
        }
        else if(sides == 48)
        {
        	uv = getSidesUV(tile,ForgeDirection.WEST);
            rotation = new AxisAngle4d(0,0,1,180);
        }
		
		drawCube(uv,rotation,new Vector3d());
		GL11.glEnable(GL11.GL_CULL_FACE);
	}

    protected  Vector2f getCoreUV(TileEntity entity)
    {
        return  new Vector2f(0,0);
    }

    protected  Vector2f getSidesUV(TileEntity entity,ForgeDirection dir)
    {
        return  new Vector2f(1,0);
    }

    protected void drawSide(TileEntityPipe tile,ForgeDirection dir)
	{
		if(drawInside)
			GL11.glDisable(GL11.GL_CULL_FACE);
		
		Vector3d offset = new Vector3d(dir.offsetX * size, dir.offsetY * size, dir.offsetZ * size);
		Vector2f uv = getSidesUV(tile,dir);
		
		if(dir ==  ForgeDirection.UP || dir == ForgeDirection.DOWN)
		{
			drawCube(uv,new AxisAngle4d(0,0,1,90),offset);
		}
        else if(dir ==  ForgeDirection.WEST || dir == ForgeDirection.EAST)
		{
			drawCube(uv,new AxisAngle4d(0,0,1,180),offset);
		}
        else
		{
			drawCube(uv,new AxisAngle4d(0,1,0,90),offset);
		}
		
		GL11.glEnable(GL11.GL_CULL_FACE);
	}
	
	void drawCube(Vector2f uv,AxisAngle4d rot,Vector3d pos) {
        //down
        drawPlane(new Vector3d(0, 0, 1), new AxisAngle4d(1, 0, 0, -90), size, uv, rot, pos);
        //up
        drawPlane(new Vector3d(0, 1, 0), new AxisAngle4d(1, 0, 0, 90), size, uv, rot, pos);
        //north
        drawPlane(new Vector3d(), new AxisAngle4d(0, 1, 0, 0), size, uv, rot, pos);
        //south
        drawPlane(new Vector3d(1, 0, 1), new AxisAngle4d(0, 1, 0, 180), size, uv, rot, pos);
        //west
        drawPlane(new Vector3d(0, 0, 1), new AxisAngle4d(0, 1, 0, 90), size, uv, rot, pos);
        //east
        drawPlane(new Vector3d(1, 0, 0), new AxisAngle4d(0, 1, 0, 270), size, uv, rot, pos);
    }
	
	void drawPlane(Vector3d pos,AxisAngle4d rot,double scale,Vector2f uv,AxisAngle4d globalRot,Vector3d globalPos)
	{
		Tessellator.instance.startDrawingQuads();
		GL11.glPushMatrix();
		
		
		GL11.glTranslated(globalPos.x, globalPos.y, globalPos.z);
		GL11.glTranslated(0.5 - scale / 2, 0.5 - scale / 2, 0.5 - scale / 2);
		
		GL11.glTranslated(scale / 2, scale / 2, scale / 2);
		GL11.glRotated(globalRot.angle,globalRot.x, globalRot.y, globalRot.z);
		GL11.glTranslated(-scale / 2, -scale / 2, -scale / 2);
		
		GL11.glScaled(scale, scale, scale);
		GL11.glTranslated(pos.x, pos.y, pos.z);
		GL11.glRotated(rot.angle, rot.x, rot.y, rot.z);
		
		
		Vector2f uv1 = new Vector2f(1,1);
		uv1.add(uv);
		uv1.scale(6*texPixel);
		
		Vector2f uv2 = new Vector2f(1,0);
		uv2.add(uv);
		uv2.scale(6*texPixel);
		
		Vector2f uv3 = new Vector2f(0,0);
		uv3.add(uv);
		uv3.scale(6*texPixel);
		
		Vector2f uv4 = new Vector2f(0,1);
		uv4.add(uv);
		uv4.scale(6*texPixel);

        Vector3f normal = new Vector3f(0,0,-1);
		
		
		addVertexWithUV(pv1,uv1.x,uv1.y);
        addNormal(normal);
		addVertexWithUV(pv2,uv2.x,uv2.y);
        addNormal(normal);
		addVertexWithUV(pv3,uv3.x,uv3.y);
        addNormal(normal);
		addVertexWithUV(pv4,uv4.x,uv4.y);
        addNormal(normal);
		Tessellator.instance.draw();
		GL11.glPopMatrix();
	}
	
	void addVertexWithUV(Vector4d vec,float u,float v)
	{
        Tessellator.instance.addVertexWithUV(vec.x, vec.y, vec.z, u,v);
	}

    void addNormal(Vector3f vec)
    {
        Tessellator.instance.setNormal(vec.x,vec.y,vec.z);
    }
}
