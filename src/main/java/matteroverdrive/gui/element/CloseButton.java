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
import matteroverdrive.container.IButtonHandler;
import matteroverdrive.gui.MOGuiBase;
import matteroverdrive.util.MOStringHelper;
import net.minecraft.client.Minecraft;

public class CloseButton extends MOElementButton
{

	public CloseButton(MOGuiBase gui, IButtonHandler handler, int posX, int posY, String name)
	{
		super(gui,handler, posX, posY, name, 0, 0, 9, 0, 9, 9, Reference.PATH_ELEMENTS + "close_button.png");
		this.setTexture(Reference.PATH_ELEMENTS + "close_button.png", 18, 9);
		this.setToolTip(MOStringHelper.translateToLocal("gui.tooltip.close"));
	}

	@Override
	public void onAction(int mouseX, int mouseY,int mouseButton)
	{
		Minecraft.getMinecraft().thePlayer.closeScreen();
        super.onAction(mouseX,mouseY,mouseButton);
	}

}
