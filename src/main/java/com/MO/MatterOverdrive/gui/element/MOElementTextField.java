package com.MO.MatterOverdrive.gui.element;

import com.MO.MatterOverdrive.util.RenderUtils;
import org.lwjgl.opengl.GL11;

import com.MO.MatterOverdrive.Reference;

import cofh.lib.gui.GuiBase;
import cofh.lib.gui.element.ElementTextField;

public class MOElementTextField extends ElementTextField
{

	public MOElementTextField(GuiBase gui, int posX, int posY,int width,int height) {
		super(gui, posX, posY, width, height);
		
		this.setTexture(Reference.PATH_ELEMENTS + "search_field.png", 166, 14);
	}
	
	@Override
	public void drawBackground(int mouseX, int mouseY, float gameTicks) 
	{
		RenderUtils.drawSizeableHorizontal(posX,posY,0,0,sizeX,sizeY,texW,texH,texture,0,20);
	}
}
