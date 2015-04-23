package com.MO.MatterOverdrive.util;

import java.util.List;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import org.apache.logging.log4j.Level;
import org.lwjgl.opengl.GL11;

import scala.reflect.internal.Trees.This;
import cpw.mods.fml.common.FMLLog;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.item.ItemStack;

public class RenderUtils 
{
	private static FontRenderer   fontRenderer = Minecraft.getMinecraft().fontRenderer;
	private static TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();
	private static RenderItem     renderItem   = new RenderItem();
	
	public static void renderStack(int x, int y, ItemStack stack)
	{
		try
		{
			enable3DRender();
			renderItem.renderItemAndEffectIntoGUI(fontRenderer, textureManager, stack, x, y);
			enable2DRender();
		}
		catch(Exception e)
		{
			
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

	public static void disableLightmap()
	{
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f);
	}
	
	public static void DrawMultilineInfo(List infos,int x,int y, int maxLines,int maxLineWidth,int color)
	{
		try
		{
			for(int i = 0;i < Math.min(maxLines, infos.size());i++)
			{
				String info = infos.get(i).toString();
				info = info.substring(0, Math.min(maxLineWidth, info.length()));
				fontRenderer.drawStringWithShadow(info, x, y + i * 10, color);
			}
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
}
