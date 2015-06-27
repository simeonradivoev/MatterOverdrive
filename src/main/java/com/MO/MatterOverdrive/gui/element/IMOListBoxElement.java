package com.MO.MatterOverdrive.gui.element;

public interface IMOListBoxElement
{
	String getName();

	int getHeight();

	int getWidth();

	Object getValue();

	void draw(MOElementListBox listBox, int x, int y, int backColor, int textColor, boolean selected,boolean BG);
	
	void drawToolTop(MOElementListBox listBox,int x,int y);

}
