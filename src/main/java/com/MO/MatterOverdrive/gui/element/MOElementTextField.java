package com.MO.MatterOverdrive.gui.element;

import org.lwjgl.opengl.GL11;

import com.MO.MatterOverdrive.Reference;

import cofh.lib.gui.GuiBase;
import cofh.lib.gui.element.ElementTextField;

public class MOElementTextField extends ElementTextField
{

	public MOElementTextField(GuiBase gui, int posX, int posY) {
		super(gui, posX, posY, 166, 14);
		
		this.setTexture(Reference.PATH_ELEMENTS + "search_field.png", 166, 14);
	}
	
	@Override
	public void drawBackground(int mouseX, int mouseY, float gameTicks) 
	{
		GL11.glPushMatrix();
		GL11.glTranslatef(this.posX, this.posY, 0);
		gui.bindTexture(texture);
		this.drawTexturedModalRect(0, 0, 0, 0, this.sizeX, this.sizeY);
		GL11.glPopMatrix();
	}
}
