package com.MO.MatterOverdrive.gui.element;

import com.MO.MatterOverdrive.container.IButtonHandler;
import net.minecraft.client.Minecraft;
import cofh.lib.gui.GuiBase;
import cofh.lib.gui.element.ElementButton;

import com.MO.MatterOverdrive.Reference;

public class CloseButton extends MOElementButton
{

	public CloseButton(GuiBase gui,IButtonHandler handler, int posX, int posY, String name)
	{
		super(gui,handler, posX, posY, name, 0, 0, 9, 0, 9, 9,
				Reference.PATH_ELEMENTS + "close_button.png");
		this.setTexture(Reference.PATH_ELEMENTS + "close_button.png", 18, 9);
		this.setToolTip("Close");
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
