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

package matteroverdrive.gui.element;

import matteroverdrive.Reference;
import matteroverdrive.data.ScaleTexture;
import matteroverdrive.gui.MOGuiBase;
import matteroverdrive.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.List;

public class ElementProgress extends MOElementBase
{
	public static final ScaleTexture FILL_TEXTURE = new ScaleTexture(new ResourceLocation(Reference.PATH_ELEMENTS + "progress_slider_fill.png"),9,9).setOffsets(3,6,4,4);

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

	public ElementProgress(MOGuiBase gui, int fillX, int fillY, int posX, int posY, int bgU, int bgV, int fillU, int fillV, int fillSizeX, int fillSizeY, int sizeX, int sizeY)
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
	public void updateInfo()
	{

	}

	@Override
	public void init()
	{

	}

	@Override
	public void addTooltip(List<String> var1, int mouseX, int mouseY)
	{

	}

	@Override
	public void drawBackground(int mouseX, int mouseY, float gameTicks) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		RenderUtils.bindTexture(texture);
		drawTexturedModalRect(this.posX, this.posY, this.bgU, this.bgV, this.sizeX, this.sizeY);

		FILL_TEXTURE.render(this.fillX,this.fillY,this.Scale(this.fillSizeX),fillSizeY);
		//drawTexturedModalRect(this.fillX,this.fillY,this.fillU,this.fillV,,this.fillSizeY);

		if(this.isShowText())
		{
			this.drawCenteredString(Minecraft.getMinecraft().fontRendererObj, this.text, this.posX + this.textX, this.posY + this.textY, this.textColor);
		}
	}

	@Override
	public void drawForeground(int mouseX, int mouseY)
	{
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
