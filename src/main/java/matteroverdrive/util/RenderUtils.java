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

package matteroverdrive.util;

import com.mojang.realmsclient.gui.ChatFormatting;
import matteroverdrive.blocks.includes.MOBlock;
import matteroverdrive.client.data.Color;
import matteroverdrive.client.render.tileentity.TileEntityRendererPatternMonitor;
import matteroverdrive.util.math.MOMathHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class RenderUtils
{
	private static final FontRenderer fontRenderer = Minecraft.getMinecraft().fontRendererObj;
	private static final RenderItem renderItem   = Minecraft.getMinecraft().getRenderItem();
	private static float lastLightMapX,lastLightMapY;

	public static void renderStack(int x, int y, ItemStack stack)
	{
		renderStack(x,y,100,stack,false);
	}

	public static void renderStack(int x, int y,int z, ItemStack stack,boolean renderOverlay) {
		if (stack != null && stack.getItem() != null) {
			GlStateManager.pushMatrix();
            GlStateManager.color(1,1,1);
			RenderHelper.enableGUIStandardItemLighting();
            GlStateManager.translate(0.0F, 0.0F, z);
			renderItem.zLevel = -150;
			FontRenderer font = null;
			if (stack != null) font = stack.getItem().getFontRenderer(stack);
			if (font == null) font = Minecraft.getMinecraft().fontRendererObj;
			renderItem.renderItemAndEffectIntoGUI(stack, x, y);
			if (renderOverlay)
				renderItemOverlayIntoGUI(font, stack, x, y, stack.stackSize > 1 ? Integer.toString(stack.stackSize) : null);
			renderItem.zLevel = 0.0F;

			GlStateManager.enableAlpha();
			GlStateManager.disableBlend();
			RenderHelper.disableStandardItemLighting();
			GlStateManager.popMatrix();
		}
	}

    /**
     * Should be the same as {@link RenderItem#renderItemOverlayIntoGUI(FontRenderer, ItemStack, int, int, String)}}
     */
	public static void renderItemOverlayIntoGUI(FontRenderer fr, ItemStack stack, int xPosition, int yPosition, String text)
	{
		if (stack != null)
		{
			if (stack.stackSize != 1 || text != null)
			{
				String s = text == null ? String.valueOf(stack.stackSize) : text;

				if (text == null && stack.stackSize < 1)
				{
					s = ChatFormatting.RED + String.valueOf(stack.stackSize);
				}

				GlStateManager.disableLighting();
				//GlStateManager.disableDepth();
				GlStateManager.disableBlend();
				GlStateManager.translate(0,0,3);
				fr.drawStringWithShadow(s, (float)(xPosition + 19 - 2 - fr.getStringWidth(s)), (float)(yPosition + 6 + 3), 16777215);
				GlStateManager.enableLighting();
				//GlStateManager.enableDepth();
			}

			if (stack.getItem().showDurabilityBar(stack))
			{
				double health = stack.getItem().getDurabilityForDisplay(stack);
				int j = (int)Math.round(13.0D - health * 13.0D);
				int i = (int)Math.round(255.0D - health * 255.0D);
				GlStateManager.disableLighting();
				//GlStateManager.disableDepth();
				GlStateManager.disableTexture2D();
				GlStateManager.disableAlpha();
				GlStateManager.disableBlend();
				Tessellator tessellator = Tessellator.getInstance();
				VertexBuffer worldrenderer = tessellator.getBuffer();
				func_181565_a(worldrenderer, xPosition + 2, yPosition + 13, 13, 2, 0, 0, 0, 255);
				func_181565_a(worldrenderer, xPosition + 2, yPosition + 13, 12, 1, (255 - i) / 4, 64, 0, 255);
				func_181565_a(worldrenderer, xPosition + 2, yPosition + 13, j, 1, 255 - i, i, 0, 255);
				//GlStateManager.enableBlend(); // Forge: Disable Blend because it screws with a lot of things down the line.
				GlStateManager.enableAlpha();
				GlStateManager.enableTexture2D();
				GlStateManager.enableLighting();
				//GlStateManager.enableDepth();
			}
		}
	}

	private static void func_181565_a(VertexBuffer p_181565_1_, int p_181565_2_, int p_181565_3_, int p_181565_4_, int p_181565_5_, int p_181565_6_, int p_181565_7_, int p_181565_8_, int p_181565_9_)
	{
		p_181565_1_.begin(7, DefaultVertexFormats.POSITION_COLOR);
		p_181565_1_.pos((double)(p_181565_2_ + 0), (double)(p_181565_3_ + 0), 0.0D).color(p_181565_6_, p_181565_7_, p_181565_8_, p_181565_9_).endVertex();
		p_181565_1_.pos((double)(p_181565_2_ + 0), (double)(p_181565_3_ + p_181565_5_), 0.0D).color(p_181565_6_, p_181565_7_, p_181565_8_, p_181565_9_).endVertex();
		p_181565_1_.pos((double)(p_181565_2_ + p_181565_4_), (double)(p_181565_3_ + p_181565_5_), 0.0D).color(p_181565_6_, p_181565_7_, p_181565_8_, p_181565_9_).endVertex();
		p_181565_1_.pos((double)(p_181565_2_ + p_181565_4_), (double)(p_181565_3_ + 0), 0.0D).color(p_181565_6_, p_181565_7_, p_181565_8_, p_181565_9_).endVertex();
		Tessellator.getInstance().draw();
	}

	public static void renderStack(int x, int y, ItemStack stack,float opacity) {
		if (stack != null && stack.getItem() != null) {
            GlStateManager.color(1,1,1);
            GlStateManager.disableCull();
            GlStateManager.depthMask(true);
			RenderHelper.enableGUIStandardItemLighting();
			renderItem.renderItemIntoGUI(stack, x, y);
			RenderHelper.disableStandardItemLighting();
            GlStateManager.enableCull();
		}
	}

	public static void rotateFromBlock(World world, BlockPos pos)
	{
		if(world != null)
		{
            IBlockState blockState = world.getBlockState(pos);
			EnumFacing rotation = blockState.getValue(MOBlock.PROPERTY_DIRECTION);

            switch (rotation)
            {
                case WEST:
                    GlStateManager.rotate(-90, 0, 1, 0);
                    break;
                case EAST:
                    GlStateManager.rotate(90, 0, 1, 0);
                    break;
                case NORTH:
                    GlStateManager.rotate(-180, 0, 1, 0);
                    break;
            }
		}

		//System.out.println("Metadata " + metadata + "at [" + x +","+ y +","+ z + "]");
	}

	public static void rotateFromBlock(Matrix4f mat,IBlockAccess world,BlockPos pos)
	{
		if(world != null)
		{
            IBlockState blockState = world.getBlockState(pos);
			EnumFacing rotation = blockState.getValue(MOBlock.PROPERTY_DIRECTION);

			Vector3f axis = new Vector3f(0,1,0);

			if (rotation == EnumFacing.WEST)
			{
				mat.rotate(-(float) (Math.PI / 2), axis);
			}
			else if (rotation == EnumFacing.EAST) {
				mat.rotate((float) (Math.PI / 2), axis);
			}
			else if (rotation == EnumFacing.NORTH) {
				mat.rotate(-(float) (Math.PI), axis);
			}
		}
	}

	public static void drawCircle(double radius,int segments)
	{
		glBegin(GL_POLYGON);
		for (int i = 0;i < segments;i++)
		{
			glVertex3d(Math.sin((i/(double)segments)*Math.PI*2) * radius,Math.cos((i/(double)segments)*Math.PI*2) * radius,0);
		}
		glEnd();
	}

	public static void drawPlane(double size)
	{
		drawPlane(size, size);
	}
	public static void drawPlane(double x,double y,double z,double sizeX,double sizeY)
	{
		drawPlaneWithUV(x, y, z, sizeX, sizeY, 0, 0, 1, 1);
	}
	public static void drawPlane(double sizeX,double sizeY)
	{
		drawPlaneWithUV(0, 0, 0, sizeX, sizeY, 0, 0, 1, 1);
	}
	public static void drawPlaneWithUV(double sizeX,double sizeY,double uStart,double vStart,double uSize,double vSize)
	{
		drawPlaneWithUV(0,0,0,sizeX,sizeY,uStart,vStart,uSize,vSize);
	}
	public static void drawPlaneWithUV(double x,double y,double z,double sizeX,double sizeY,double uStart,double vStart,double uSize,double vSize)
	{
        VertexBuffer wr = Tessellator.getInstance().getBuffer();

        wr.begin(GL_QUADS,DefaultVertexFormats.POSITION_TEX);
        wr.pos(x + sizeX, y + sizeY, z).tex(uStart + uSize, vStart + vSize).endVertex();
        wr.pos(x + sizeX, y, z).tex( uStart + uSize, vStart).endVertex();
        wr.pos(x, y, z).tex(uStart, vStart).endVertex();
        wr.pos(x, y + sizeY, z).tex(uStart, vStart + vSize).endVertex();
        Tessellator.getInstance().draw();
	}
	public static void drawPlaneWithUV(double x,double y,double z,double sizeX,double sizeY,double uStart,double vStart,double uSize,double vSize,Color color)
	{
		VertexBuffer wr = Tessellator.getInstance().getBuffer();

        wr.begin(GL_QUADS,DefaultVertexFormats.POSITION_TEX_COLOR);
        wr.pos(x + sizeX, y + sizeY, z).tex( uStart + uSize, vStart + vSize).color(color.getFloatR(),color.getFloatG(),color.getFloatB(),color.getFloatA()).endVertex();
        wr.pos(x + sizeX, y, z).tex(uStart + uSize, vStart).color(color.getFloatR(),color.getFloatG(),color.getFloatB(),color.getFloatA()).endVertex();
        wr.pos(x, y, z).tex(uStart, vStart).color(color.getFloatR(),color.getFloatG(),color.getFloatB(),color.getFloatA()).endVertex();
        wr.pos(x, y + sizeY, z).tex(uStart, vStart + vSize).color(color.getFloatR(),color.getFloatG(),color.getFloatB(),color.getFloatA()).endVertex();
		Tessellator.getInstance().draw();
	}

	public static void drawStencil(int xMin, int yMin, int xMax, int yMax, int mask) {
		GlStateManager.disableTexture2D();
		GL11.glStencilFunc(GL_ALWAYS, mask, mask);
		GL11.glStencilOp(0, 0, GL_REPLACE);
		GL11.glStencilMask(1);
		GL11.glColorMask(false, false, false, false);
		GL11.glDepthMask(false);
        VertexBuffer wr = Tessellator.getInstance().getBuffer();
		wr.begin(GL_QUADS,DefaultVertexFormats.POSITION);
        wr.pos((double)xMin, (double)yMax, 0.0D).endVertex();
        wr.pos((double)xMax, (double)yMax, 0.0D).endVertex();
        wr.pos((double)xMax, (double)yMin, 0.0D).endVertex();
        wr.pos((double)xMin, (double)yMin, 0.0D).endVertex();
		Tessellator.getInstance().draw();
		GL11.glEnable(GL_TEXTURE_2D);
		GL11.glStencilFunc(GL_EQUAL, mask, mask);
		GL11.glStencilMask(0);
		GL11.glColorMask(true, true, true, false);
		GL11.glDepthMask(true);
	}

	public static void drawCube(double sizeX,double sizeY,double sizeZ,Color color)
	{
		drawCube(0, 0, 0, sizeX, sizeY, sizeZ, color);
	}

	public static void drawCube(double sizeX,double sizeY,double sizeZ,Color color,float multiply)
	{
		drawCube(0, 0, 0, sizeX, sizeY, sizeZ, new Color((int)(color.getIntR() * multiply),(int)(color.getIntG() * multiply),(int)(color.getIntB() * multiply)));
	}

	public static void drawCube(double x,double y,double z,double sizeX,double sizeY,double sizeZ,Color color)
	{
		drawCube(x,y,z,sizeX,sizeY,sizeZ,0,0,1,1,color);
	}

	public static void drawCube(double x,double y,double z,double sizeX,double sizeY,double sizeZ,double minU,double minV,double maxU,double maxV,Color color)
	{
        VertexBuffer wr = Tessellator.getInstance().getBuffer();

		wr.begin(GL_QUADS,DefaultVertexFormats.POSITION_TEX_COLOR);

		float r = 1;
		float g = 1;
		float b = 1;
		float a = 1;

		if (color != null)
		{
			r = color.getFloatR();
			g = color.getFloatG();
			b = color.getFloatB();
			a = color.getFloatA();
		}
		//tessellator.setBrightness(255);

		//base
		wr.pos(x, y, z).tex(minU, minV).color(r,g,b,a).endVertex();
        wr.pos(x + sizeX, y, z).tex(maxU, minV).color(r,g,b,a).endVertex();
        wr.pos(x + sizeX, y, z + sizeZ).tex(maxU, maxV).color(r,g,b,a).endVertex();
        wr.pos(x, y, z + sizeZ).tex(minU, maxV).color(r,g,b,a).endVertex();

		//top
        wr.pos(x + sizeX, y + sizeY, z).tex(maxU, minV).color(r,g,b,a).endVertex();
        wr.pos(x, y + sizeY, z).tex(minU, minV).color(r,g,b,a).endVertex();
        wr.pos(x, y + sizeY, z + sizeZ).tex(minU, maxV).color(r,g,b,a).endVertex();
        wr.pos(x + sizeX, y + sizeY, z + sizeZ).tex(maxU, maxV).color(r,g,b,a).endVertex();

		//west
        wr.pos(x, y, z).tex(minU, minV).color(r,g,b,a).endVertex();
        wr.pos(x, y, z + sizeZ).tex( maxU, minV).color(r,g,b,a).endVertex();
        wr.pos(x, y + sizeY, z + sizeZ).tex(maxU, maxV).color(r,g,b,a).endVertex();
        wr.pos(x, y + sizeY, z).tex(minU, maxV).color(r,g,b,a).endVertex();

		//east
        wr.pos(x + sizeX, y, z + sizeZ).tex(maxU, minV).color(r,g,b,a).endVertex();
        wr.pos(x + sizeX, y, z).tex(minU, minV).color(r,g,b,a).endVertex();
        wr.pos(x + sizeX, y + sizeY, z).tex(minU, maxV).color(r,g,b,a).endVertex();
        wr.pos(x + sizeX, y + sizeY, z + sizeZ).tex(maxU, maxV).color(r,g,b,a).endVertex();

		//north
        wr.pos(x, y, z).tex(minU, minV).color(r,g,b,a).endVertex();
        wr.pos(x, y + sizeY, z).tex(minU, maxV).color(r,g,b,a).endVertex();
        wr.pos(x + sizeX, y + sizeY, z).tex(maxU, maxV).color(r,g,b,a).endVertex();
        wr.pos(x + sizeX, y, z).tex(maxU, minV).color(r,g,b,a).endVertex();

		//south
        wr.pos(x, y + sizeY, z + sizeZ).tex(minU, maxV).color(r,g,b,a).endVertex();
        wr.pos(x, y,z + sizeZ).tex(minU, minV).color(r,g,b,a).endVertex();
        wr.pos(x + sizeX, y, z + sizeZ).tex(maxU, minV).color(r,g,b,a).endVertex();
        wr.pos(x + sizeX, y + sizeY, z + sizeZ).tex(maxU, maxV).color(r,g,b,a).endVertex();

		Tessellator.getInstance().draw();
	}

	/*public static void tesseleteModelAsBlock(Matrix4f mat,GroupObject object,IIcon icon,int x,int y,int z,int brightness,boolean lighting,Color color)
	{
		float uSize = icon.getMaxU() - icon.getMinU();
		float vSize = icon.getMaxV() - icon.getMinV();
		float textureOffset = 0.00005F;
		Vector4f pos = new Vector4f(0,0,0,1);

		for (Face face : object.faces)
		{
			if (face.faceNormal == null)
			{
				face.faceNormal = face.calculateFaceNormal();
			}
			float averageU = 0F;
			float averageV = 0F;

			if ((face.textureCoordinates != null) && (face.textureCoordinates.length > 0))
			{
				for (int i = 0; i < face.textureCoordinates.length; ++i)
				{
					averageU += face.textureCoordinates[i].u;
					averageV += face.textureCoordinates[i].v;
				}

				averageU = averageU / face.textureCoordinates.length;
				averageV = averageV / face.textureCoordinates.length;
			}

			float offsetU, offsetV;

			for (int i = 0; i < face.vertices.length; ++i)
			{

				if ((face.textureCoordinates != null) && (face.textureCoordinates.length > 0))
				{
					offsetU = textureOffset;
					offsetV = textureOffset;

					if (face.textureCoordinates[i].u > averageU)
					{
						offsetU = -offsetU;
					}
					if (face.textureCoordinates[i].v > averageV)
					{
						offsetV = -offsetV;
					}

					pos.x = face.vertices[i].x;
					pos.y = face.vertices[i].y;
					pos.z = face.vertices[i].z;
					pos.w = 1;
					Matrix4f.transform(mat, pos, pos);

					Tessellator.instance.setNormal(face.faceNormal.x, face.faceNormal.y, face.faceNormal.z);
					float colorMul = 1f;
					if (lighting)
					{
						colorMul = 0.7f + 0.3f * Vector3f.dot(new Vector3f(face.faceNormal.x,face.faceNormal.y,face.faceNormal.z),new Vector3f(-0.3f,1,0));
					}
					if (color != null) {
						Tessellator.instance.setColorOpaque_F(color.getFloatR() * colorMul, color.getFloatG() * colorMul, color.getFloatB() * colorMul);
					}else
					{
						Tessellator.instance.setColorOpaque_F(colorMul, colorMul, colorMul);
					}
					if (brightness >= 0) {
						Tessellator.instance.setBrightness(brightness);
					}
					Tessellator.instance.addVertexWithUV(x + 0.5f + pos.x, y + pos.y, z + 0.5f + pos.z, icon.getMinU() + face.textureCoordinates[i].u * uSize + offsetU, icon.getMinV() + face.textureCoordinates[i].v * vSize + offsetV);
				}
				else
				{
					Tessellator.instance.addVertex(x + face.vertices[i].x, y + face.vertices[i].y, z + face.vertices[i].z);
				}
			}
		}
	}*/

	public static int lerp(int a,int b,float lerp)
	{
		int MASK1 = 0xff00ff;
		int MASK2 = 0x00ff00;

		int f2 = Math.round(256 * lerp);
		int f1 = Math.round(256 - f2);

		return ((((( a & MASK1 ) * f1 ) + ( ( b & MASK1 ) * f2 )) >> 8 ) & MASK1 ) | ((((( a & MASK2 ) * f1 ) + ( ( b & MASK2 ) * f2 )) >> 8 ) & MASK2 );
	}

	public static Color lerp(Color a,Color b,float lerp)
	{
		return new Color(lerp(a.getIntR(),b.getIntR(),lerp),lerp(a.getIntG(),b.getIntG(),lerp),lerp(a.getIntB(),b.getIntB(),lerp),lerp(a.getIntA(),b.getIntA(),lerp));
	}

	public static void applyColor(int color)
	{
		GlStateManager.color((float) (color >> 16 & 255) / 255f, (float) (color >> 8 & 255) / 255f, (float) (color & 255) / 256f, (float) (color >> 24 & 255) / 255f);
	}

	public static void applyColor(Color color) {
		GlStateManager.color(color.getFloatR(), color.getFloatG(), color.getFloatB());
	}

	public static void applyColorWithMultipy(Color color,float mul) {
		GlStateManager.color(color.getFloatR() * mul, color.getFloatG() * mul, color.getFloatB() * mul);
	}

	public static void applyColorWithMultipy(int color,float mul) {
		GlStateManager.color((color >> 16 & 255) / 255f * mul, (color >> 8 & 255) / 255f * mul, (color & 255) / 256f * mul, (float) (color >> 24 & 255) / 255f);
	}

    public static void applyColorWithAdd(Color color,float add)
    {
		GlStateManager.color(color.getFloatR() + add, color.getFloatG() + add, color.getFloatB() + add);
    }

	public static void applyColorWithAlpha(Color color,float alphaMultiply)
	{
		GlStateManager.color(color.getFloatR(), color.getFloatG(), color.getFloatB(), color.getFloatA()*alphaMultiply);
	}
	public static void applyColorWithAlpha(Color color)
	{
		GlStateManager.color(color.getFloatR(), color.getFloatG(), color.getFloatB(), color.getFloatA());
	}

    public static void beginDrawinngBlockScreen(double x, double y, double z,EnumFacing side,Color color,TileEntity entity)
    {
        beginDrawinngBlockScreen(x, y, z, side, color, entity, 0.05, 1);
    }

    public static void beginDrawinngBlockScreen(double x, double y, double z,EnumFacing side,Color color,TileEntity entity,double offset,float glowAlpha)
    {
        GlStateManager.enableBlend();
        GlStateManager.disableLighting();
        disableLightmap();

        Vector3f dir = new Vector3f(side.getDirectionVec().getX(),side.getDirectionVec().getY(),side.getDirectionVec().getZ());
        Vector3f front = new Vector3f(0, 0, -1);
        Vector3f c = Vector3f.cross(front,dir, null);
        double omega = Math.acos(Vector3f.dot(front,dir));
		if (omega == Math.PI)
		{
			c.y = 1;
		}

        GlStateManager.pushMatrix();
        GlStateManager.translate(dir.x * (0.5 + offset), dir.y * (0.5 + offset), dir.z * (0.5 + offset));
        GlStateManager.translate(x + 0.5, y + 0.5, z + 0.5);
        GlStateManager.rotate((float)(omega * (180 / Math.PI)), c.x, c.y, c.z);
		GlStateManager.rotate(180, 0, 0, 1);
        GlStateManager.translate(-0.5, -0.5, 0);

        GlStateManager.blendFunc(GL_ONE, GL_ONE_MINUS_SRC_COLOR);

        float multiply = (float) (MOMathHelper.noise(entity.getPos().getX(),entity.getWorld().getWorldTime() * 0.01,entity.getPos().getZ()) * 0.5 + 0.5) * glowAlpha;
        GlStateManager.color(color.getFloatR() * multiply, color.getFloatG() * multiply, color.getFloatB() * multiply);
        Minecraft.getMinecraft().getTextureManager().bindTexture(TileEntityRendererPatternMonitor.screenTextureGlow);
        RenderUtils.drawPlane(1);

        GlStateManager.translate(0,0,-0.05);
        GlStateManager.color(color.getFloatR() * 0.05f, color.getFloatG() * 0.05f, color.getFloatB() * 0.05f);
        Minecraft.getMinecraft().getTextureManager().bindTexture(TileEntityRendererPatternMonitor.screenTextureBack);
        RenderUtils.drawPlane(1);
    }

    public static void drawScreenInfoWithGlobalAutoSize(String[] info, Color color, EnumFacing side, int leftMargin, int rightMargin, float maxScaleFactor)
    {
        GlStateManager.blendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.pushMatrix();
        GlStateManager.translate(0,0,-0.03f);
        GlStateManager.scale(0.01f, 0.01f, 0.01f);
        int height = 0;
        int maxWidth = 0;

        int sizeX = 100 - leftMargin - rightMargin;
        int sizeY = 80;

        float scaleFactor = 1;

		for (String anInfo : info)
		{
			if (maxWidth < fontRenderer.getStringWidth(anInfo))
			{
				maxWidth = fontRenderer.getStringWidth(anInfo);
			}
		}

        if (maxWidth > 0) {
            scaleFactor = MathHelper.clamp_float((float) sizeX / (float) maxWidth, 0.02f, maxScaleFactor);
        }

		for (String anInfo : info)
		{
			int scaledHeight = (int) (fontRenderer.FONT_HEIGHT * scaleFactor);

			if (height + scaledHeight < sizeY)
			{
				height += scaledHeight;
			}
		}

        height = MathHelper.clamp_int(height,0,sizeY);

        int yCount = 0;
		for (String anInfo : info)
		{
            GlStateManager.pushMatrix();
			int scaledHeight = (int) (fontRenderer.FONT_HEIGHT * scaleFactor);

			if (yCount + scaledHeight < sizeY)
			{
				int width = fontRenderer.getStringWidth(anInfo);
                GlStateManager.translate(leftMargin + sizeX / 2, 50 + yCount + scaledHeight / 2 - height / 2, 0);
                GlStateManager.scale(scaleFactor, scaleFactor, 0);
				fontRenderer.drawString(anInfo, -width / 2, -fontRenderer.FONT_HEIGHT / 2, color.getColor());

			} else
			{
				GlStateManager.popMatrix();
				break;
			}

			GlStateManager.popMatrix();
			yCount += scaledHeight;
		}
        GlStateManager.popMatrix();
    }

    public static void drawScreenInfoWithLocalAutoSize(String[] info, Color color, EnumFacing side, int leftMargin, int rightMargin, float maxScaleFactor)
    {
        GlStateManager.blendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.pushMatrix();
        GlStateManager.translate(0,0,-0.03f);
        GlStateManager.scale(0.01f, 0.01f, 0.01f);
        int height = 0;
		int maxWidth = 0;

        int sizeX = 100 - leftMargin - rightMargin;
        int sizeY = 80;

		for (String anInfo : info)
		{
			float scaleFactor = 1;
			int width = fontRenderer.getStringWidth(anInfo);
			if (width > 0)
			{
				scaleFactor = MathHelper.clamp_float((float) sizeX / (float) width, 0.02f, maxScaleFactor);
			}
			int scaledHeight = (int) (fontRenderer.FONT_HEIGHT * scaleFactor);

			if (maxWidth < fontRenderer.getStringWidth(anInfo))
			{
				maxWidth = fontRenderer.getStringWidth(anInfo);
			}

			if (height + scaledHeight < sizeY)
			{
				height += scaledHeight;
			}
		}

        height = MathHelper.clamp_int(height,0,sizeY);

		int yCount = 0;
		for (String anInfo : info)
		{
            GlStateManager.pushMatrix();
			float scaleFactor = 1;
			int width = fontRenderer.getStringWidth(anInfo);
			if (width > 0)
			{
				scaleFactor = MathHelper.clamp_float((float) sizeX / (float) width, 0.02f, maxScaleFactor);
			}
			int scaledHeight = (int) (fontRenderer.FONT_HEIGHT * scaleFactor);

			if (yCount + scaledHeight < sizeY)
			{
                GlStateManager.translate(leftMargin + sizeX / 2, 50 + yCount + scaledHeight / 2 - height / 2, 0);
				GlStateManager.scale(scaleFactor, scaleFactor, 0);
				fontRenderer.drawString(anInfo, -width / 2, -fontRenderer.FONT_HEIGHT / 2, color.getColor());

			} else
			{
				GlStateManager.popMatrix();
				break;
			}

            GlStateManager.popMatrix();
			yCount += scaledHeight;
		}
        GlStateManager.popMatrix();
    }

    public static void endDrawinngBlockScreen()
    {
        GlStateManager.popMatrix();
        GlStateManager.blendFunc(GL_SRC_ALPHA,GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
    }

	public static void disableLightmap()
	{
		lastLightMapX = OpenGlHelper.lastBrightnessX;
		lastLightMapY = OpenGlHelper.lastBrightnessY;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f);
	}

	public static void enableLightmap()
	{
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lastLightMapX, lastLightMapY);
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
		catch(Exception ignored)
		{

		}
	}

	public static void bindTexture(ResourceLocation texture)
	{
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
	}

	//From CodeChickenLib
	public static void translateToWorldCoords(Entity entity, float frame)
	{
		double interpPosX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * frame;
		double interpPosY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * frame;
		double interpPosZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * frame;

        GlStateManager.translate(-interpPosX, -interpPosY, -interpPosZ);
	}

	public static void rotateTo(Entity viewer)
	{
        GlStateManager.rotate(viewer.rotationYaw,0,-1,0);
        GlStateManager.rotate(viewer.rotationPitch, 1, 0, 0);
	}

	public static void tessalateParticle(Entity viewer, TextureAtlasSprite particleIcon, double scale, Vec3d position, Color color)
	{
		tessalateParticle(viewer, particleIcon, scale, position, color.getFloatR(), color.getFloatG(), color.getFloatB(), color.getFloatA());
	}

	public static void tessalateParticle(Entity viewer, TextureAtlasSprite particleIcon,double scale,Vec3d position,float r,float g,float b,float a)
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
		VertexBuffer wr = Tessellator.getInstance().getBuffer();
		wr.pos(x - f1 * scale - f3 * scale, y - f5 * scale,z - f2 * scale - f4 * scale).tex((double)uMax, (double)vMax).color(r, g, b, a).endVertex();
		wr.pos(x - f1 * scale + f3 * scale, y + f5 * scale, z - f2 * scale + f4 * scale).tex((double) uMax, (double) vMin).color(r, g, b, a).endVertex();
		wr.pos(x + f1 * scale + f3 * scale, y + f5 * scale, z + f2 * scale + f4 * scale).tex((double)uMin, (double)vMin).color(r, g, b, a).endVertex();
		wr.pos(x + f1 * scale - f3 * scale, y - f5 * scale, z + f2 * scale - f4 * scale).tex((double)uMin, (double)vMax).color(r, g, b, a).endVertex();
	}

	public static void enable3DRender() {
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        GlStateManager.disableCull();
	 }

	 public static void enable2DRender() {
         GlStateManager.disableLighting();
         GlStateManager.disableDepth();
         GlStateManager.enableCull();
	 }

	public static void drawSizedTexturedModalRect(int x, int y, int u, int v, int width, int height,int widthU, int heightV, float texW, float texH,float zLevel)
	{
        Tessellator.getInstance().getBuffer().begin(GL_QUADS,DefaultVertexFormats.POSITION_TEX);
		tessalateSizedModelRect(x, y, u, v, width, height, widthU, heightV, texW, texH, zLevel);
        Tessellator.getInstance().draw();
	}

	public static void tessalateSizedModelRect(int x,int y,int u, int v,int width,int height,int widthU,int heightV,float texW,float texH,float zLevel)
	{
		float texU = 1 / texW;
		float texV = 1 / texH;
		VertexBuffer wr = Tessellator.getInstance().getBuffer();
		wr.pos(x, y + height, zLevel).tex(u * texU, (v + heightV) * texV).endVertex();
		wr.pos(x + width, y + height, zLevel).tex( (u + widthU) * texU, (v + heightV) * texV).endVertex();
		wr.pos(x + width, y, zLevel).tex( (u + widthU) * texU, v * texV).endVertex();
		wr.pos(x, y, zLevel).tex( u * texU, v * texV).endVertex();
	}

	public static void drawString(String string,int x,int y,Color color,float multiply)
	{
		drawString(Minecraft.getMinecraft().fontRendererObj, string, x, y, color, multiply);
	}

	public static void drawString(FontRenderer fontRenderer,String string,int x,int y,Color color,float multiply)
	{
		fontRenderer.drawString(string, x, y, color.multiplyWithoutAlpha(multiply).getColor());
	}

	public static void beginStencil()
	{
		glEnable(GL_STENCIL_TEST);
		GlStateManager.colorMask(false, false, false, false);
		GlStateManager.depthMask(false);
		glStencilFunc(GL_ALWAYS, 1, 0xFF); // Set any stencil to 1
		glStencilOp(GL_KEEP, GL_KEEP, GL_REPLACE);
		glStencilMask(0xFF); // Write to stencil buffer
		GlStateManager.clear(GL_STENCIL_BUFFER_BIT);
	}

	public static void beginDrawingDepthMask()
	{
		GlStateManager.clear(GL_DEPTH_BUFFER_BIT);
		GlStateManager.clearDepth(1f);
		GlStateManager.depthFunc(GL_LESS);
		GlStateManager.enableDepth();
		GlStateManager.depthMask(true);
		GlStateManager.colorMask(false,false,false,false);
		GlStateManager.disableTexture2D();
	}

	public static void beginDepthMasking()
	{
		GlStateManager.enableTexture2D();
		GlStateManager.depthMask(false);
		GlStateManager.colorMask(true,true,true,true);
		GlStateManager.enableDepth();
		GlStateManager.depthFunc(GL11.GL_GREATER);
	}

	public static void endStencil()
	{
		glStencilFunc(GL_EQUAL, 1, 0xFF); // Pass test if stencil value is 1
		glStencilMask(0x00); // Don't write anything to stencil buffer
		GlStateManager.depthMask(true); // Write to depth buffer
		GlStateManager.colorMask(true, true, true, false);
		glDisable(GL_STENCIL_TEST);
	}

	public static void endDepthMask()
	{
		GlStateManager.depthFunc(GL_LEQUAL);
		GlStateManager.depthMask(true);
		GlStateManager.disableDepth();
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
		drawSizedTexturedModalRect(left, top, u, v, width, v+chunkSize, width, chunkSize, texW, texH, zLevel);
		//middle
		drawSizedTexturedModalRect(left, top + chunkSize, u, chunkSize, width, height - chunkSize * 2, width, texH - chunkSize * 2, texW, texH, zLevel);
		//bottom
		drawSizedTexturedModalRect(left, top + height - chunkSize, u, v+texH - chunkSize, width, chunkSize, width, chunkSize, texW, texH, zLevel);
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
		VertexBuffer wr = Tessellator.getInstance().getBuffer();
		wr.begin(GL_TRIANGLES,DefaultVertexFormats.POSITION);
		wr.pos(x-size, y, z).endVertex();
		wr.pos(x + size, y, z-size).endVertex();
		wr.pos(x + size, y, z+size);

		wr.pos(x-size, y, z).endVertex();
		wr.pos(x+size, y, z+size).endVertex();
		wr.pos(x+size, y + size, z).endVertex();

		wr.pos(x-size, y, z).endVertex();
		wr.pos(x+size, y+size, z).endVertex();
		wr.pos(x+size, y, z-size).endVertex();

		wr.pos(x + size, y, z - size).endVertex();
		wr.pos(x + size, y + size, z).endVertex();
		wr.pos(x + size, y, z + size).endVertex();
		Tessellator.getInstance().draw();
	}

    public static void rotateTowards(Vec3d from,Vec3d to,Vec3d up)
    {
        double dot = from.dotProduct(to);
        if (Math.abs(dot - (-1.0)) < Double.MIN_VALUE)
        {
            GlStateManager.rotate(180,(float) up.xCoord,(float)up.yCoord,(float)up.zCoord);
        }
        if (Math.abs(dot - (1.0)) < Double.MIN_VALUE)
        {
            return;
        }

        double rotAngle = Math.acos(dot);
        Vec3d rotAxis = from.crossProduct(to).normalize();
        GlStateManager.rotate((float)(rotAngle * (180d / Math.PI)), (float)rotAxis.xCoord, (float)rotAxis.yCoord, (float)rotAxis.zCoord);
    }

	public static void renderIcon(double x, double y, double z, TextureAtlasSprite icon, int width, int height) {
		VertexBuffer wr = Tessellator.getInstance().getBuffer();
		wr.begin(GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		wr.pos(x, y + (double)height, z).tex((double)icon.getMinU(), (double)icon.getMaxV()).endVertex();
		wr.pos(x + (double)width, y + (double)height, z).tex((double)icon.getMaxU(), (double)icon.getMaxV()).endVertex();
		wr.pos(x + (double)width, y, z).tex((double)icon.getMaxU(), (double)icon.getMinV()).endVertex();
		wr.pos(x, y, z).tex((double)icon.getMinU(), (double)icon.getMinV()).endVertex();
		Tessellator.getInstance().draw();
	}

	public static void setBlockTextureSheet() {
		new ResourceLocation("textures/atlas/blocks.png");
	}
}
