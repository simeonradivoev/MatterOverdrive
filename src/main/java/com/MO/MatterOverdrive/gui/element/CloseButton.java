package com.MO.MatterOverdrive.gui.element;

import net.minecraft.client.Minecraft;
import cofh.lib.gui.GuiBase;
import cofh.lib.gui.element.ElementButton;

import com.MO.MatterOverdrive.Reference;

public class CloseButton extends ElementButton
{

	public CloseButton(GuiBase gui, int posX, int posY, String name) 
	{
		super(gui, posX, posY, name, 0, 0, 9, 0, 9, 9,
				Reference.PATH_ELEMENTS + "close_button.png");
		this.setTexture(Reference.PATH_ELEMENTS + "close_button.png", 18, 9);
	}

	@Override
	public boolean onMousePressed(int x, int y, int mouseButton) 
	{
		if (isEnabled()) 
		{
			Minecraft.getMinecraft().thePlayer.closeScreen();
		}
		return super.onMousePressed(x, y, mouseButton);
	}
}
