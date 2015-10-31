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

import cofh.lib.gui.GuiColor;
import cofh.lib.gui.element.ElementTextFieldFiltered;
import matteroverdrive.Reference;
import matteroverdrive.client.render.HoloIcon;
import matteroverdrive.data.ScaleTexture;
import matteroverdrive.gui.MOGuiBase;
import matteroverdrive.gui.events.ITextHandler;
import matteroverdrive.proxy.ClientProxy;
import matteroverdrive.util.RenderUtils;
import net.minecraft.util.ResourceLocation;

public class MOElementTextField extends ElementTextFieldFiltered
{
	protected int textOffsetX,textOffsetY;
	private ScaleTexture background;
	ITextHandler textHandler;
	private HoloIcon holoIcon;
    private GuiColor color;

	public MOElementTextField(MOGuiBase gui,ITextHandler textHandler, int posX, int posY,int width,int height) {
		super(gui, posX, posY, width, height);
		background = new ScaleTexture(new ResourceLocation(Reference.PATH_ELEMENTS + "search_field.png"),166,14).setOffsets(18,12,9,3);
		this.textHandler = textHandler;
	}

	public MOElementTextField(MOGuiBase gui, int posX, int posY,int width,int height)
	{
		this(gui, gui, posX, posY, width, height);
	}

	@Override
	public void drawBackground(int mouseX, int mouseY, float gameTicks)
	{
        if (background != null) {
            background.render(posX - textOffsetX, posY - textOffsetY, sizeX + textOffsetX, sizeY + textOffsetY);
        }
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

	public void setHoloIcon(HoloIcon holoIcon)
	{
		this.holoIcon = holoIcon;
	}

    public void setColor(GuiColor color)
    {
        this.color = color;
    }

	@Override
	protected void onCharacterEntered(boolean success)
	{
		if (isFocused())
			textHandler.textChanged(getName(),getText(),success);
	}

	public void drawForeground(int mouseX, int mouseY) {
		super.drawForeground(mouseX,mouseY);
        if (holoIcon != null)
        {
            if (color != null)
                RenderUtils.applyColor(color);

            float heightScale = (float)Math.min(holoIcon.getOriginalHeight(), sizeY) / (float)holoIcon.getOriginalHeight();
            ClientProxy.holoIcons.renderIcon(holoIcon,posX - holoIcon.getOriginalWidth() - 2,posY,(int)(holoIcon.getOriginalWidth() * heightScale),(int)(holoIcon.getOriginalHeight() * heightScale));
        }
	}
}
