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

import matteroverdrive.container.IButtonHandler;
import matteroverdrive.gui.MOGuiBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

/**
 * Created by Simeon on 5/10/2015.
 */
public class ElementStates extends MOElementButtonScaled
{
	String[] states;
	int selectedState;
	String label;

	public ElementStates(MOGuiBase gui, IButtonHandler buttonHandler, int posX, int posY, String name, String[] states)
	{
		super(gui, buttonHandler, posX, posY, name, 0, 0);
		this.name = name;
		this.states = states;
		this.sizeY = Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT + 10;
		for (String state : states)
		{
			if (this.sizeX < Minecraft.getMinecraft().fontRendererObj.getStringWidth(state))
			{
				this.sizeX = Minecraft.getMinecraft().fontRendererObj.getStringWidth(state);
			}
		}
		this.sizeX += 16;
	}

	public String[] getStates()
	{
		return states;
	}

	public void setStates(String[] states)
	{
		this.states = states;
	}

	public void setSelectedState(int selectedState)
	{
		this.selectedState = selectedState;
		this.text = states[selectedState];
	}

	@Override
	public void drawForeground(int mouseX, int mouseY)
	{
		super.drawForeground(mouseX, mouseY);
		GlStateManager.color(1, 1, 1, 1);
		GlStateManager.enableAlpha();
		GlStateManager.alphaFunc(GL11.GL_GREATER, 0.5f);
		GlStateManager.cullFace(GlStateManager.CullFace.BACK);
		getFontRenderer().drawString(label, posX + sizeX + 4, posY - getFontRenderer().FONT_HEIGHT / 2 + sizeY / 2, getTextColor());
	}

	@Override
	public void onAction(int mouseX, int mouseY, int mouseButton)
	{
		selectedState++;
		if (selectedState >= states.length)
		{
			selectedState = 0;
		}

		if (selectedState < states.length)
		{
			text = states[selectedState];
		}


		buttonHandler.handleElementButtonClick(this, name, selectedState);
	}

	public void setLabel(String label)
	{
		this.label = label;
	}
}
