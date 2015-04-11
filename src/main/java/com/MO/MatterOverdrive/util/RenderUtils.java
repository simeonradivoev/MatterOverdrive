package com.MO.MatterOverdrive.util;

import java.util.List;

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
	
	public static void enable3DRender() {
		 GL11.glEnable(GL11.GL_LIGHTING);
		 GL11.glEnable(GL11.GL_DEPTH_TEST);
	 }
		 
	 public static void enable2DRender() {
		 GL11.glDisable(GL11.GL_LIGHTING);
		 GL11.glDisable(GL11.GL_DEPTH_TEST);
	 } 
}
