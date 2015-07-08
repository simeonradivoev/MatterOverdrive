package matteroverdrive.util;

import cofh.lib.gui.GuiColor;
import matteroverdrive.client.render.tileentity.TileEntityRendererPatternMonitor;
import matteroverdrive.util.math.MOMathHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class RenderUtils 
{
	private static FontRenderer   fontRenderer = Minecraft.getMinecraft().fontRenderer;
	private static TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();
	private static RenderItem     renderItem   = new RenderItem();
	
	public static void renderStack(int x, int y, ItemStack stack) {
		if (stack != null && stack.getItem() != null) {
			glColor3f(1, 1, 1);
			//glDisable(GL_CULL_FACE);
			glDepthMask(true);
			//RenderHelper.enableGUIStandardItemLighting();
			glEnable(GL_LIGHTING);
			renderItem.renderItemIntoGUI(fontRenderer, textureManager, stack, x, y);
			glDisable(GL_LIGHTING);
			//RenderHelper.disableStandardItemLighting();
			//glEnable(GL_CULL_FACE);
		}
	}

	public static void renderStack(int x, int y, ItemStack stack,float opacity) {
		if (stack != null && stack.getItem() != null) {
			glColor3f(1, 1, 1);
			glDisable(GL_CULL_FACE);
			glDepthMask(true);
			RenderHelper.enableGUIStandardItemLighting();
			renderItem.renderItemIntoGUI(fontRenderer, textureManager, stack, x, y);
			RenderHelper.disableStandardItemLighting();
			glEnable(GL_CULL_FACE);
		}
	}

	public static void rotateFromBlock(World world,int x,int y,int z)
	{
		if(world != null)
		{
			int metadata = world.getBlockMetadata(x, y, z);

			ForgeDirection direction = ForgeDirection.values()[metadata];

			if (direction == ForgeDirection.WEST)
			{
				GL11.glRotated(-90, 0, 1, 0);
			}
			else if (direction == ForgeDirection.EAST) {
				GL11.glRotated(90, 0, 1, 0);
			}
			else if (direction == ForgeDirection.NORTH) {
				GL11.glRotated(-180, 0, 1, 0);
			}
		}

		//System.out.println("Metadata " + metadata + "at [" + x +","+ y +","+ z + "]");
	}

	public static void drawPlane(double size)
	{
		drawPlane(size,size);
	}

	public static void drawPlane(double sizeX,double sizeY)
	{
		drawPlaneWithUV(sizeX, sizeY, 0, 0, 1, 1);
	}

	public static void drawPlaneWithUV(double sizeX,double sizeY,double uStart,double vStart,double uSize,double vSize)
	{
		Tessellator tessellator = Tessellator.instance;

		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV(sizeX, sizeY, 0, uStart + uSize, vStart + vSize);
		tessellator.addVertexWithUV(sizeX, 0, 0, uStart + uSize, vStart);
		tessellator.addVertexWithUV(0, 0, 0, uStart, vStart);
		tessellator.addVertexWithUV(0, sizeY, 0, uStart, vStart + vSize);
		tessellator.draw();
	}

	public static void drawCube(double sizeX,double sizeY,double sizeZ,GuiColor color)
	{
		drawCube(0, 0, 0, sizeX, sizeY, sizeZ, color);
	}

	public static void drawCube(double sizeX,double sizeY,double sizeZ,GuiColor color,float multiply)
	{
		drawCube(0, 0, 0, sizeX, sizeY, sizeZ, new GuiColor((int)(color.getIntR() * multiply),(int)(color.getIntG() * multiply),(int)(color.getIntB() * multiply)));
	}

	public static void drawCube(double x,double y,double z,double sizeX,double sizeY,double sizeZ,GuiColor color)
	{
		Tessellator tessellator = Tessellator.instance;

		tessellator.startDrawingQuads();
		if (color != null)
			tessellator.setColorRGBA(color.getIntR(), color.getIntG(), color.getIntB(), color.getIntA());
		tessellator.setBrightness(255);

		//base
		tessellator.addVertexWithUV(x, y, 0, 0, 0);
		tessellator.addVertexWithUV(x + sizeX, y, 0, 1, 0);
		tessellator.addVertexWithUV(x + sizeX, y, sizeZ, 1, 1);
		tessellator.addVertexWithUV(x, y, sizeZ, 0, 1);

		//top
		tessellator.addVertexWithUV(x + sizeX, y + sizeY, 0, 1, 0);
		tessellator.addVertexWithUV(x, y + sizeY, 0, 0, 0);
		tessellator.addVertexWithUV(x, y + sizeY, sizeZ, 0, 1);
		tessellator.addVertexWithUV(x + sizeX, y + sizeY, sizeZ, 1, 1);

		//west
		tessellator.addVertexWithUV(x, y, 0, 0, 0);
		tessellator.addVertexWithUV(x, y, sizeZ, 1, 0);
		tessellator.addVertexWithUV(x, y + sizeY, sizeZ, 1, 1);
		tessellator.addVertexWithUV(x, y + sizeY, 0, 0, 1);

		//east
		tessellator.addVertexWithUV(x + sizeX, y, sizeZ, 1, 0);
		tessellator.addVertexWithUV(x + sizeX, y, 0, 0, 0);
		tessellator.addVertexWithUV(x + sizeX, y + sizeY, 0, 0, 1);
		tessellator.addVertexWithUV(x + sizeX, y + sizeY, sizeZ, 1, 1);

		//north
		tessellator.addVertexWithUV(x, y, 0, 0, 0);
		tessellator.addVertexWithUV(x, y + sizeY, 0, 0, 1);
		tessellator.addVertexWithUV(x + sizeX, y + sizeY, 0, 1, 1);
		tessellator.addVertexWithUV(x + sizeX, y, 0, 1, 0);

		//south
		tessellator.addVertexWithUV(x, y + sizeY, sizeZ, 0, 1);
		tessellator.addVertexWithUV(x, y,sizeZ, 0, 0);
		tessellator.addVertexWithUV(x + sizeX, y, sizeZ, 1, 0);
		tessellator.addVertexWithUV(x + sizeX, y + sizeY, sizeZ, 1, 1);

		tessellator.draw();
	}

	public static int lerp(int a,int b,float lerp)
	{
		int MASK1 = 0xff00ff;
		int MASK2 = 0x00ff00;

		int f2 = Math.round(256 * lerp);
		int f1 = Math.round(256 - f2);

		return ((((( a & MASK1 ) * f1 ) + ( ( b & MASK1 ) * f2 )) >> 8 ) & MASK1 ) | ((((( a & MASK2 ) * f1 ) + ( ( b & MASK2 ) * f2 )) >> 8 ) & MASK2 );
	}

	public static GuiColor lerp(GuiColor a,GuiColor b,float lerp)
	{
		return new GuiColor(lerp(a.getColor(),b.getColor(),lerp));
	}

	public static void applyColor(int color)
	{
		glColor4f((color & 0x000000ff) / 255f,(color & 0x0000ff00) / 255f,(color & 0x00ff0000) / 256f,(color >> 24)/255f);
	}

	public static void applyColor(GuiColor color) {
		glColor3f(color.getFloatR(), color.getFloatG(), color.getFloatB());
	}

	public static void applyColorWithMultipy(GuiColor color,float mul) {
		glColor3f(color.getFloatR() * mul, color.getFloatG() * mul, color.getFloatB() * mul);
	}

    public static void applyColorWithAdd(GuiColor color,float add)
    {
        glColor3f(color.getFloatR() + add, color.getFloatG() + add, color.getFloatB() + add);
    }

	public static void applyColorWithAlpha(GuiColor color)
	{
		glColor4f(color.getFloatR(), color.getFloatG(), color.getFloatB(), color.getFloatA());
	}

    public static void beginDrawinngBlockScreen(double x, double y, double z,ForgeDirection side,GuiColor color,TileEntity entity)
    {
        beginDrawinngBlockScreen(x, y, z, side, color, entity, 0.05);
    }

    public static void beginDrawinngBlockScreen(double x, double y, double z,ForgeDirection side,GuiColor color,TileEntity entity,double offset)
    {
        glEnable(GL_BLEND);
        glDisable(GL_LIGHTING);
        disableLightmap();

        Vector3f dir = new Vector3f(side.offsetX,side.offsetY,side.offsetZ);
        Vector3f front = new Vector3f(0, 0, -1);
        Vector3f c = Vector3f.cross(front,dir, null);
        double omega = Math.acos(Vector3f.dot(front,dir));
		if (omega == Math.PI)
		{
			c.y = 1;
		}

        glPushMatrix();
        glTranslated(dir.x * (0.5 + offset), dir.y * (0.5 + offset), dir.z * (0.5 + offset));
        glTranslated(x + 0.5, y + 0.5, z + 0.5);
		glRotated(omega * (180 / Math.PI), c.x, c.y, c.z);
		glRotated(180, 0, 0, 1);
        glTranslated(-0.5, -0.5, 0);

        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_COLOR);

        double multiply = MOMathHelper.noise(entity.xCoord,entity.getWorldObj().getWorldTime() * 0.01,entity.zCoord) * 0.5 + 0.5;
        glColor3d(color.getFloatR() * multiply, color.getFloatG() * multiply, color.getFloatB() * multiply);
        Minecraft.getMinecraft().getTextureManager().bindTexture(TileEntityRendererPatternMonitor.screenTextureGlow);
        RenderUtils.drawPlane(1);

        glTranslated(0,0,-0.05);
        glColor3d(color.getFloatR() * 0.05f, color.getFloatG() * 0.05f, color.getFloatB() * 0.05f);
        Minecraft.getMinecraft().getTextureManager().bindTexture(TileEntityRendererPatternMonitor.screenTextureBack);
        RenderUtils.drawPlane(1);
    }

    public static void drawScreenInfo(String[] info,GuiColor color,ForgeDirection side)
    {
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glPushMatrix();
        glTranslated(0,0,-0.03);
        glScaled(0.01, 0.01, 0.01);
        int height = info.length * 12;
        for (int i = 0;i < info.length;i++) {

            int width = fontRenderer.getStringWidth(info[i]);
            fontRenderer.drawString(info[i], 50 - (width / 2), (52 - height / 2) + 12 * i, color.getColor());
        }
        glPopMatrix();
    }

    public static void endDrawinngBlockScreen()
    {
        glPopMatrix();
        glDisable(GL_BLEND);
        glEnable(GL_LIGHTING);
    }

	public static void disableLightmap()
	{
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f);
	}
	
	public static void DrawMultilineInfo(List infos,int x,int y, int maxLines,int maxLineWidth,int color)
	{
		try
		{
			int linesCounter = 0;
			String infoText = StringUtils.join(infos,"\n");
			fontRenderer.drawSplitString(infoText, x, y + linesCounter * 10, maxLineWidth, color);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void DrawMultilineInfoCentered(List infos,int x,int y, int maxLines,int maxLineWidth,int color)
	{
		try
		{
			for(int i = 0;i < Math.min(maxLines, infos.size());i++)
			{
				String info = infos.get(i).toString();
				info = info.substring(0, Math.min(maxLineWidth, info.length()));
				int width = fontRenderer.getStringWidth(info);
				fontRenderer.drawStringWithShadow(info, x - (width/2), y + i * 10, color);
			}
		}
		catch(Exception e)
		{

		}
	}

	//From CodeChickenLib
	public static void translateToWorldCoords(Entity entity, float frame)
	{
		double interpPosX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * frame;
		double interpPosY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * frame;
		double interpPosZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * frame;

		GL11.glTranslated(-interpPosX, -interpPosY, -interpPosZ);
	}

	public static void rotateTo(Entity viewer)
	{
		glRotated(viewer.rotationYaw,0,-1,0);
		glRotated(viewer.rotationPitch, 1, 0, 0);
	}

	public static void tessalateParticle(EntityLivingBase viewer, IIcon particleIcon,double scale,Vec3 position,GuiColor color)
	{
		tessalateParticle(viewer, particleIcon, scale, position, color.getFloatR(), color.getFloatG(), color.getFloatB(), color.getFloatA());
	}

	public static void tessalateParticle(EntityLivingBase viewer, IIcon particleIcon,double scale,Vec3 position,float r,float g,float b,float a)
	{
		float f1 = MathHelper.cos(viewer.rotationYaw * 0.017453292F);
		float f2 = MathHelper.sin(viewer.rotationYaw * 0.017453292F);
		float f3 = -f2 * MathHelper.sin(viewer.rotationPitch * 0.017453292F);
		float f4 = f1 * MathHelper.sin(viewer.rotationPitch * 0.017453292F);
		float f5 = MathHelper.cos(viewer.rotationPitch * 0.017453292F);

		float uMin = particleIcon.getMinU();
		float uMax = particleIcon.getMaxU();
		float vMin = particleIcon.getMinV();
		float vMax = particleIcon.getMaxV();

		float x = (float)position.xCoord;
		float y = (float)position.yCoord;
		float z = (float)position.zCoord;
		Tessellator.instance.setColorRGBA_F(r, g, b, a);
		Tessellator.instance.addVertexWithUV((double)(x - f1 * scale - f3 * scale), (double)(y - f5 * scale), (double)(z - f2 * scale - f4 * scale), (double)uMax, (double)vMax);
		Tessellator.instance.addVertexWithUV((double) (x - f1 * scale + f3 * scale), (double) (y + f5 * scale), (double) (z - f2 * scale + f4 * scale), (double) uMax, (double) vMin);
		Tessellator.instance.addVertexWithUV((double)(x + f1 * scale + f3 * scale), (double)(y + f5 * scale), (double)(z + f2 * scale + f4 * scale), (double)uMin, (double)vMin);
		Tessellator.instance.addVertexWithUV((double)(x + f1 * scale - f3 * scale), (double)(y - f5 * scale), (double)(z + f2 * scale - f4 * scale), (double)uMin, (double)vMax);
	}
	
	public static void enable3DRender() {
		 GL11.glEnable(GL11.GL_LIGHTING);
		 GL11.glEnable(GL11.GL_DEPTH_TEST);
		 GL11.glDisable(GL11.GL_CULL_FACE);
	 }
		 
	 public static void enable2DRender() {
		 GL11.glDisable(GL11.GL_LIGHTING);
		 GL11.glDisable(GL11.GL_DEPTH_TEST);
		 GL11.glEnable(GL11.GL_CULL_FACE);
	 }

	public static void drawSizedTexturedModalRect(int x, int y, int u, int v, int width, int height,int widthU, int heightV, float texW, float texH,float zLevel)
	{
        Tessellator.instance.startDrawingQuads();
		tessalateSizedModelRect(x, y, u, v, width, height, widthU, heightV, texW, texH, zLevel);
        Tessellator.instance.draw();
	}

	public static void tessalateSizedModelRect(int x,int y,int u, int v,int width,int height,int widthU,int heightV,float texW,float texH,float zLevel)
	{
		float texU = 1 / texW;
		float texV = 1 / texH;
		Tessellator.instance.addVertexWithUV(x, y + height, zLevel, u * texU, (v + heightV) * texV);
		Tessellator.instance.addVertexWithUV(x + width, y + height, zLevel, (u + widthU) * texU, (v + heightV) * texV);
		Tessellator.instance.addVertexWithUV(x + width, y, zLevel, (u + widthU) * texU, v * texV);
		Tessellator.instance.addVertexWithUV(x, y, zLevel, u * texU, v * texV);
	}

	public static void drawString(String string,int x,int y,GuiColor color,float multiply)
	{
		drawString(Minecraft.getMinecraft().fontRenderer, string, x, y, color, multiply);
	}

	public static void drawString(FontRenderer fontRenderer,String string,int x,int y,GuiColor color,float multiply)
	{
		fontRenderer.drawString(string, x, y, new GuiColor((int) (color.getIntR() * multiply), (int) (color.getIntG() * multiply), (int) (color.getIntB() * multiply)).getColor());
	}

	public static void beginStencil()
	{
		glEnable(GL_STENCIL_TEST);
		glColorMask(false, false, false, false);
		glDepthMask(false);
		glStencilFunc(GL_ALWAYS, 1, 0xFF); // Set any stencil to 1
		glStencilOp(GL_KEEP, GL_KEEP, GL_REPLACE);
		glStencilMask(0xFF); // Write to stencil buffer
		glClear(GL_STENCIL_BUFFER_BIT);
	}

	public static void endStencil()
	{
		glStencilFunc(GL_EQUAL, 1, 0xFF); // Pass test if stencil value is 1
		glStencilMask(0x00); // Don't write anything to stencil buffer
		glDepthMask(true); // Write to depth buffer
		glColorMask(true, true, true, true);
		glDisable(GL_STENCIL_TEST);
	}

	public static void drawSizeableBackground(int left,int top,int width,int height,int texW,int texH,ResourceLocation texture,float zLevel,int chunkSize)
	{
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);

		//topLeft
		drawSizedTexturedModalRect(left, top, 0, 0, chunkSize, chunkSize, chunkSize, chunkSize, 255, 255, zLevel);
		//top middle
		drawSizedTexturedModalRect(left + chunkSize, top, chunkSize, 0, width - chunkSize * 2, chunkSize, texW - chunkSize * 2, chunkSize, 255, 255, zLevel);
		//top right
		drawSizedTexturedModalRect(left + width - chunkSize,top,texW - chunkSize,0,chunkSize,chunkSize, chunkSize, chunkSize,255,255,zLevel);
		//left middle
		drawSizedTexturedModalRect(left, top + chunkSize, 0, chunkSize, chunkSize, height - chunkSize * 2, chunkSize, texH - chunkSize * 2, 255, 255, zLevel);
		//right middle
		drawSizedTexturedModalRect(left + width - chunkSize, top + chunkSize, texW - chunkSize, chunkSize, chunkSize, height - chunkSize * 2, chunkSize, texH - chunkSize * 2, 255, 255, zLevel);
		//bottom left
		drawSizedTexturedModalRect(left, top + height - chunkSize, 0, texH - chunkSize, chunkSize, chunkSize, chunkSize, chunkSize, 255, 255, zLevel);
		//bottom right
		drawSizedTexturedModalRect(left + width - chunkSize, top + height - chunkSize, texW - chunkSize, texH - chunkSize, chunkSize, chunkSize, chunkSize, chunkSize, 255, 255, zLevel);
		//bottom middle
		drawSizedTexturedModalRect(left + chunkSize, top + height - chunkSize, chunkSize, texH - chunkSize, width - chunkSize * 2, chunkSize, texW - chunkSize * 2, chunkSize, 255, 255, zLevel);
		//midddle
		drawSizedTexturedModalRect(left + chunkSize, top + chunkSize, chunkSize, chunkSize, width - chunkSize * 2, height - chunkSize * 2, texW - chunkSize * 2, texH - chunkSize * 2, 255, 255, zLevel);
	}

	public static void drawSizeableVertical(int left,int top,int u,int v,int width,int height,int texW,int texH,ResourceLocation texture,float zLevel,int chunkSize)
	{
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);

		//top
		drawSizedTexturedModalRect(left, top, u + 0, v + 0, width, v+chunkSize, width, chunkSize, texW, texH, zLevel);
		//middle
		drawSizedTexturedModalRect(left, top + chunkSize, u + 0, chunkSize, width, height - chunkSize * 2, width, texH - chunkSize * 2, texW, texH, zLevel);
		//bottom
		drawSizedTexturedModalRect(left, top + height - chunkSize, u+0, v+texH - chunkSize, width, chunkSize, width, chunkSize, texW, texH, zLevel);
	}

	public static void drawSizeableHorizontal(int left,int top,int u,int v,int width,int height,int texW,int texH,ResourceLocation texture,float zLevel,int chunkSize)
	{
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);

		//left
		drawSizedTexturedModalRect(left, top, u, v, chunkSize, height, chunkSize, height, texW, texH, zLevel);
		//middle
		drawSizedTexturedModalRect(left + chunkSize, top, u+chunkSize, v, width - chunkSize * 2, height, texW - chunkSize * 2, height, texW, texH, zLevel);
		//right
		drawSizedTexturedModalRect(left + width - chunkSize, top, u+texW - chunkSize, v, chunkSize, height, chunkSize, height, texW, texH, zLevel);
	}

	public static void drawShip(double x,double y,double z,double size)
	{
		Tessellator.instance.startDrawing(GL_TRIANGLES);
		Tessellator.instance.addVertex(x-size, y, z);
		Tessellator.instance.addVertex(x + size, y, z-size);
		Tessellator.instance.addVertex(x + size, y, z+size);


		Tessellator.instance.addVertex(x-size, y, z);
		Tessellator.instance.addVertex(x+size, y, z+size);
		Tessellator.instance.addVertex(x+size, y + size, z);

		Tessellator.instance.addVertex(x-size, y, z);
		Tessellator.instance.addVertex(x+size, y+size, z);
		Tessellator.instance.addVertex(x+size, y, z-size);

		Tessellator.instance.addVertex(x + size, y, z - size);
		Tessellator.instance.addVertex(x + size, y + size, z);
		Tessellator.instance.addVertex(x + size, y, z + size);
		Tessellator.instance.draw();
	}

    public static void rotateTowards(Vec3 from,Vec3 to,Vec3 up)
    {
        double dot = from.dotProduct(to);
        if (Math.abs(dot - (-1.0)) < Double.MIN_VALUE)
        {
            glRotated(180,up.xCoord,up.yCoord,up.zCoord);
        }
        if (Math.abs(dot - (1.0)) < Double.MIN_VALUE)
        {
            return;
        }

        double rotAngle = Math.acos(dot);
        Vec3 rotAxis = from.crossProduct(to).normalize();
        glRotated(rotAngle * (180d / Math.PI), rotAxis.xCoord, rotAxis.yCoord, rotAxis.zCoord);
    }
}
