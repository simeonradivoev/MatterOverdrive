package com.MO.MatterOverdrive.gui.element;

import com.MO.MatterOverdrive.data.ScaleTexture;
import com.MO.MatterOverdrive.gui.MOGuiBase;
import com.MO.MatterOverdrive.gui.events.ITextHandler;
import com.MO.MatterOverdrive.util.RenderUtils;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import com.MO.MatterOverdrive.Reference;

import cofh.lib.gui.GuiBase;
import cofh.lib.gui.element.ElementTextField;

public class MOElementTextField extends ElementTextField
{
	protected int textOffsetX,textOffsetY;
	private ScaleTexture background;
	ITextHandler textHandler;

	public MOElementTextField(MOGuiBase gui,ITextHandler textHandler, int posX, int posY,int width,int height) {
		super(gui, posX, posY, width, height);
		background = new ScaleTexture(new ResourceLocation(Reference.PATH_ELEMENTS + "search_field.png"),166,14);
		this.textHandler = textHandler;
	}

	public MOElementTextField(MOGuiBase gui, int posX, int posY,int width,int height)
	{
		this(gui,gui,posX,posY,width,height);
	}
	
	@Override
	public void drawBackground(int mouseX, int mouseY, float gameTicks) 
	{
		background.Render(posX - textOffsetX,posY - textOffsetY,sizeX + textOffsetX,sizeY + textOffsetY);
	}

	public void setBackground(ScaleTexture texture)
	{
		background = texture;
	}

	public void setTextOffset(int x,int y)
	{
		textOffsetX = x;
		textOffsetY = y;
	}

	@Override
	protected void onCharacterEntered(boolean success)
	{
		if (isFocused())
			textHandler.textChanged(getName(),getText(),success);
	}
}
