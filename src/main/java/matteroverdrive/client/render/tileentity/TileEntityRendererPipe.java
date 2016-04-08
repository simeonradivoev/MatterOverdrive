package matteroverdrive.client.render.tileentity;

import matteroverdrive.Reference;
import matteroverdrive.tile.pipes.TileEntityPipe;
import matteroverdrive.util.math.MOMathHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

import javax.vecmath.*;

public class TileEntityRendererPipe extends TileEntitySpecialRenderer<TileEntityPipe> {

	private static final Vector4d pv1 = new Vector4d(1,1,0,1);
	private static final Vector4d pv2 = new Vector4d(1,0,0,1);
	private static final Vector4d pv3 = new Vector4d(0,0,0,1);
	private static final Vector4d pv4 = new Vector4d(0,1,0,1);

	ResourceLocation texture;
	protected static final double size = 5 * (1d / 15d);
	final boolean drawInside = false;
	final float texPixel = 1f / 16f;

    public TileEntityRendererPipe()
    {
        texture = new ResourceLocation(Reference.PATH_BLOCKS + "pipe.png");
    }

	@Override
	public void renderTileEntityAt(TileEntityPipe pipe, double x, double y, double z, float f,int destroyStage)
	{
		GlStateManager.pushMatrix();

		GlStateManager.translate(x, y, z);
		if(texture != null)
			this.bindTexture(texture);

		drawCore(pipe, x, y, z, f, drawSides(pipe, x, y, z, f));

		GlStateManager.popMatrix();
	}

    protected int drawSides(TileEntityPipe pipe, double x, double y, double z, float f)
    {
		int connections = pipe.getConnectionsMask();
        for (int i = 0; i < 6; i++)
        {
            if (MOMathHelper.getBoolean(connections,i))
            {
                drawSide(pipe, EnumFacing.VALUES[i]);
            }
        }
		return connections;
    }

    protected void drawCore(TileEntityPipe tile, double x, double y, double z, float f, int sides)
	{
		Vector2f uv = getCoreUV(tile);
		AxisAngle4d rotation = new AxisAngle4d();

		if(drawInside)
			GL11.glDisable(GL11.GL_CULL_FACE);

		if(sides == 3)
        {
			uv = getSidesUV(tile, EnumFacing.UP);
			rotation = new AxisAngle4d(0, 0, 1, 90);
        }
        else if(sides == 12)
        {
        	uv = getSidesUV(tile, EnumFacing.NORTH);
            rotation = new AxisAngle4d(0, 1, 0, 90);
        }
        else if(sides == 48)
        {
        	uv = getSidesUV(tile, EnumFacing.WEST);
            rotation = new AxisAngle4d(0, 0, 1, 180);
        }

		drawCube(uv,rotation,new Vec3d(0,0,0));
		GL11.glEnable(GL11.GL_CULL_FACE);
	}

    protected  Vector2f getCoreUV(TileEntity entity)
    {
        return  new Vector2f(0, 0);
    }

    protected  Vector2f getSidesUV(TileEntity entity,EnumFacing dir)
    {
        return  new Vector2f(1, 0);
    }

    protected void drawSide(TileEntityPipe tile,EnumFacing dir)
	{
		if(drawInside)
			GL11.glDisable(GL11.GL_CULL_FACE);

		Vec3d offset = new Vec3d(dir.getDirectionVec().getX() * size, dir.getDirectionVec().getY() * size, dir.getDirectionVec().getZ() * size);
		Vector2f uv = getSidesUV(tile, dir);

		if(dir ==  EnumFacing.UP || dir == EnumFacing.DOWN)
		{
			drawCube(uv,new AxisAngle4d(0, 0, 1, 90),offset);
		}
        else if(dir ==  EnumFacing.WEST || dir == EnumFacing.EAST)
		{
			drawCube(uv,new AxisAngle4d(0, 0, 1, 180),offset);
		}
        else
		{
			drawCube(uv,new AxisAngle4d(0, 1, 0, 90),offset);
		}

		GL11.glEnable(GL11.GL_CULL_FACE);
	}

	void drawCube(Vector2f uv,AxisAngle4d rot,Vec3d pos) {
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

	void drawPlane(Vector3d pos,AxisAngle4d rot,double scale,Vector2f uv,AxisAngle4d globalRot,Vec3d globalPos)
	{
		GlStateManager.pushMatrix();

		Tessellator.getInstance().getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_NORMAL);
		GlStateManager.translate(globalPos.xCoord, globalPos.yCoord, globalPos.zCoord);
		GlStateManager.translate(0.5 - scale / 2, 0.5 - scale / 2, 0.5 - scale / 2);

		GlStateManager.translate(scale / 2, scale / 2, scale / 2);
		GlStateManager.rotate((float)globalRot.angle,(float)globalRot.x, (float)globalRot.y, (float)globalRot.z);
		GlStateManager.translate(-scale / 2, -scale / 2, -scale / 2);

		GlStateManager.scale(scale, scale, scale);
		GlStateManager.translate(pos.x, pos.y, pos.z);
		GlStateManager.rotate((float)rot.angle, (float)rot.x, (float)rot.y, (float)rot.z);


		Vector2f uv1 = new Vector2f(1, 1);
		uv1.add(uv);
		uv1.scale(6 * texPixel);

		Vector2f uv2 = new Vector2f(1, 0);
		uv2.add(uv);
		uv2.scale(6 * texPixel);

		Vector2f uv3 = new Vector2f(0, 0);
		uv3.add(uv);
		uv3.scale(6 * texPixel);

		Vector2f uv4 = new Vector2f(0, 1);
		uv4.add(uv);
		uv4.scale(6 * texPixel);

        Vector3f normal = new Vector3f(0,0,-1);


		addVertexWithUV(pv1, uv1.x, uv1.y,normal);
		addVertexWithUV(pv2 ,uv2.x, uv2.y,normal);
		addVertexWithUV(pv3, uv3.x, uv3.y,normal);
		addVertexWithUV(pv4, uv4.x, uv4.y,normal);
		Tessellator.getInstance().draw();
		GlStateManager.popMatrix();
	}

	void addVertexWithUV(Vector4d vec,float u,float v,Vector3f normal)
	{
        Tessellator.getInstance().getBuffer().pos(vec.x, vec.y, vec.z).tex(u,v).normal(normal.getX(),normal.getY(),normal.getZ()).endVertex();
	}
}
