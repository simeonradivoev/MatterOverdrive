package com.MO.MatterOverdrive.gui.element;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import cofh.lib.gui.GuiBase;
import cofh.lib.gui.element.ElementBase;
import cofh.lib.render.RenderHelper;

public class ElementProgress extends ElementBase
{
	float value;
	float maxValue;
	boolean showText = true;
	int bgU;
	int bgV;
	int fillU;
	int fillV;
	int fillSizeX;
	int fillSizeY;
	int fillX;
	int fillY;
	int textX;
	int textY;
	String text;
	int textColor;
	
	public ElementProgress(GuiBase gui, int fillX,int fillY,int posX, int posY,int bgU,int bgV,int fillU,int fillV,int fillSizeX,int fillSizeY,int sizeX,int sizeY) 
	{
		super(gui, posX, posY,sizeX,sizeY);
		this.fillU = fillU;
		this.fillV = fillV;
		this.bgU = bgU;
		this.bgV = bgV;
		this.fillSizeX = fillSizeX;
		this.fillSizeY = fillSizeY;
		this.fillX = fillX;
		this.fillY = fillY;
	}
	
	@Override
	public void drawBackground(int mouseX, int mouseY, float gameTicks) 
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		RenderHelper.bindTexture(texture);
		drawTexturedModalRect(this.posX,this.posY,this.bgU,this.bgV,this.sizeX,this.sizeY);
	}

	@Override
	public void drawForeground(int mouseX, int mouseY) 
	{
		// TODO Auto-generated method stub
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		RenderHelper.bindTexture(texture);
		drawTexturedModalRect(this.fillX,this.fillY,this.fillU,this.fillV,this.Scale(this.fillSizeX),this.fillSizeY);
		if(this.isShowText())
		{
			this.drawCenteredString(Minecraft.getMinecraft().fontRenderer, this.text, this.posX + this.textX, this.posY + this.textY, this.textColor);
		}
	}
	
	public void setValue(float value)
	{
		this.value = value;
	}

	public float getValue() 
	{
		return value;
	}

	public boolean isShowText() {
		return showText;
	}
	
	public void setShowText(boolean value)
	{
		this.showText = value;
	}
	
	private int Scale(int value)
	{
		return (int)(value * (this.value/maxValue));
	}

	public float getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(float maxValue) {
		this.maxValue = maxValue;
	}
	
	public void SetTextPostition(int x,int y)
	{
		this.textX = x;
		this.textY = y;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getTextColor() {
		return textColor;
	}

	public void setTextColor(int textColor) {
		this.textColor = textColor;
	}
}
