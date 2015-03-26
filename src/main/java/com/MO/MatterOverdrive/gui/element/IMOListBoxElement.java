package com.MO.MatterOverdrive.gui.element;

public interface IMOListBoxElement 
{
	public int getHeight();

	public int getWidth();

	public Object getValue();

	public void draw(MOElementListBox listBox, int x, int y, int backColor, int textColor, boolean selected);
	
	public void drawToolTop(MOElementListBox listBox,int x,int y);

}
