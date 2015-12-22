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

import cofh.lib.gui.element.listbox.IListBoxElement;
import matteroverdrive.client.data.Color;
import matteroverdrive.gui.MOGuiBase;
import matteroverdrive.gui.events.IListHandler;
import matteroverdrive.util.MOStringHelper;
import matteroverdrive.util.RenderUtils;
import net.minecraft.util.MathHelper;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class MOElementListBox extends MOElementBase {

    IListHandler listHandler;

	public int borderColor = new Color(120, 120, 120, 255).getColor();
	public int backgroundColor = new Color(0, 0, 0, 255).getColor();
	public int selectedLineColor = new Color(0, 0, 0, 255).getColor();
	public int textColor = new Color(150, 150, 150, 255).getColor();
	public int selectedTextColor = new Color(255, 255, 255, 255).getColor();

	private final int _marginTop = 2;
	private final int _marginLeft = 2;
	private final int _marginRight = 2;
	private final int _marginBottom = 2;

	private final List<IMOListBoxElement> _elements = new LinkedList<IMOListBoxElement>();

	private int _firstIndexDisplayed;
	protected int _selectedIndex;
	private int scrollHoriz;

	public MOElementListBox(MOGuiBase containerScreen,IListHandler listHandler, int x, int y, int width, int height) {

		super(containerScreen, x, y, width, height);
        this.listHandler = listHandler;
	}

    public MOElementListBox(MOGuiBase containerScreen, int x, int y, int width, int height)
    {
        this(containerScreen,containerScreen,x,y,width,height);
    }

	public void add(IMOListBoxElement element) {

		_elements.add(element);
	}

	public void add(Collection<? extends IMOListBoxElement> elements) {

		_elements.addAll(elements);
	}

	public void remove(IListBoxElement element) {

		_elements.remove(element);
	}

	public void removeAt(int index) {

		_elements.remove(index);
	}

	public void clear()
	{
		_elements.clear();
	}

	public int getInternalWidth() {

		int width = 0;
		for (int i = 0; i < getElementCount(); i++) {
			width = Math.max(getElementWidth(i), width);
		}
		return width;
	}

	public int getInternalHeight() {

		int height = 0;
		for (int i = 0; i < getElementCount(); i++) {
			height += getElementHeight(i);
		}
		return height;
	}

	public int getContentWidth() {

		return sizeX - _marginLeft - _marginRight;
	}

	public int getContentHeight() {

		return sizeY - _marginTop - _marginBottom;
	}

	public int getContentTop() {

		return posY + _marginTop;
	}

	public int getContentLeft() {

		return posX + _marginLeft;
	}

	public final int getContentBottom() {

		return getContentTop() + getContentHeight();
	}

	public final int getContentRight() {

		return getContentLeft() + getContentWidth();
	}

	@Override
	public void drawBackground(int mouseX, int mouseY, float gameTicks)
	{
		int heightDrawn = 0;
		int nextElement = _firstIndexDisplayed;

		glDisable(GL_LIGHTING);
		glPushMatrix();

		RenderUtils.beginStencil();
		drawStencil(getContentLeft(), getContentTop(), getContentRight(), getContentBottom(), 1);

		glTranslated(-scrollHoriz, 0, 0);

		int e = getElementCount();
		while (nextElement < e && heightDrawn <= getContentHeight())
		{
			if (shouldBeDisplayed(getElement(nextElement))) {

				if (this._selectedIndex == nextElement) {
					DrawElement(nextElement,getContentLeft(),getContentTop() + heightDrawn,selectedLineColor,selectedTextColor,true,true);
				} else {
					DrawElement(nextElement,getContentLeft(),getContentTop() + heightDrawn,selectedLineColor,this.textColor,false,true);
				}
				heightDrawn += getElementHeight(nextElement);
			}
			nextElement++;
		}

		RenderUtils.endStencil();

		glPopMatrix();
	}

	@Override
	public void drawForeground(int mouseX, int mouseY)
	{
		int heightDrawn = 0;
		int nextElement = _firstIndexDisplayed;

        glDisable(GL_LIGHTING);
        glPushMatrix();

		RenderUtils.beginStencil();
		drawStencil(getContentLeft(), getContentTop(), getContentRight(), getContentBottom(), 1);

		glTranslated(-scrollHoriz, 0, 0);

		int e = getElementCount();
		while (nextElement < e && heightDrawn <= getContentHeight())
		{
			if (shouldBeDisplayed(getElement(nextElement)))
			{
				if (this._selectedIndex == nextElement) {
					DrawElement(nextElement, getContentLeft(), getContentTop() + heightDrawn, selectedLineColor, selectedTextColor, true, false);
				} else {
					DrawElement(nextElement,getContentLeft(), getContentTop() + heightDrawn, selectedLineColor, this.textColor, false, false);
				}

				glDisable(GL_STENCIL_TEST);
				if (getContentTop() + heightDrawn <= mouseY && getContentTop() + heightDrawn + getElementHeight(nextElement) >= mouseY &&
						mouseX >= getContentLeft() && mouseX <= getContentLeft() + getElementWidth(nextElement)) {
					drawElementTooltip(nextElement,mouseX,mouseY);
				}
				glEnable(GL_STENCIL_TEST);
				heightDrawn += getElementHeight(nextElement);
			}
			nextElement++;
		}

		RenderUtils.endStencil();

		glPopMatrix();
	}

	@Override
	public boolean onMousePressed(int mouseX, int mouseY, int mouseButton)
	{
		int heightChecked = 0;
		for (int i = _firstIndexDisplayed; i < getElementCount(); i++)
		{
			if (heightChecked > getContentHeight()) {
				break;
			}
			if (shouldBeDisplayed(getElement(i)))
			{
				int elementHeight = getElementHeight(i);
				if (getContentTop() + heightChecked <= mouseY && getContentTop() + heightChecked + elementHeight >= mouseY) {
					setSelectedIndex(i);
					onElementClicked(i,mouseX - getContentLeft(),mouseY - (getContentTop() + heightChecked));
					break;
				}
				heightChecked += elementHeight;
			}
		}
		return true;
	}

	@Override
	public boolean onMouseWheel(int mouseX, int mouseY, int movement) {
		if (MOStringHelper.isControlKeyDown()) {
			if (movement > 0) {
				scrollLeft();
			} else if (movement < 0) {
				scrollRight();
			}
		} else {
			if (movement > 0) {
				scrollUp();
			} else if (movement < 0) {
				scrollDown();
			}
		}
		return true;
	}

	public void scrollDown() {

		int heightDisplayed = 0;
		int elementsDisplayed = 0;
		for (int i = _firstIndexDisplayed; i < getElementCount(); i++) {
			if (heightDisplayed + getElementHeight(i) > sizeY) {
				break;
			}
			heightDisplayed += getElementHeight(i);
			elementsDisplayed++;
		}

		if (_firstIndexDisplayed + elementsDisplayed < getElementCount()) {
			_firstIndexDisplayed++;
		}

		onScrollV(_firstIndexDisplayed);
	}

	public void scrollUp() {

		if (_firstIndexDisplayed > 0) {
			_firstIndexDisplayed--;
		}
		onScrollV(_firstIndexDisplayed);
	}

	public void scrollLeft() {

		scrollHoriz = Math.max(scrollHoriz - 15, 0);
		onScrollH(scrollHoriz);
	}

	public void scrollRight() {

		scrollHoriz = Math.min(scrollHoriz + 15, getLastScrollPositionH());
		onScrollH(scrollHoriz);
	}

	public int getLastScrollPosition() {

		int position = getElementCount() - 1;
		int heightUsed = getElementHeight(position);

		while (position > 0 && heightUsed < sizeY) {
			position--;
			heightUsed += getElementHeight(position);
		}

		return position + 1;
	}

	public int getLastScrollPositionH() {

		return Math.max(getInternalWidth() - getContentWidth(), 0);
	}

	public int getSelectedIndex() {

		return _selectedIndex;
	}

	public int getIndexOf(Object value) {

		for (int i = 0; i < _elements.size(); i++) {
			if (getElement(i).getValue().equals(value)) {
				return i;
			}
		}
		return -1;
	}

	public IMOListBoxElement getSelectedElement()
	{
		if(_selectedIndex < getElementCount())
			return getElement(MathHelper.clamp_int(_selectedIndex, 0, _elements.size()));
		else
			return null;
	}

	public void setSelectedIndex(int index) {

		if (index >= 0 && index < getElementCount() && index != _selectedIndex) {
			_selectedIndex = index;
			onSelectionChanged(_selectedIndex, getSelectedElement());
		}
	}

	public void scrollToV(int index) {

		if (index >= 0 && index < getElementCount()) {
			_firstIndexDisplayed = index;
		}
	}

	public void scrollToH(int index) {

		if (index >= 0 && index <= getLastScrollPositionH()) {
			scrollHoriz = index;
		}
	}

	//region indirect Element Access
	public void DrawElement(int i,int x,int y,int selectedLineColor,int selectedTextColor, boolean selected,boolean BG)
	{
		getElement(i).draw(this, x, y, selectedLineColor, selectedTextColor, selected, BG);
	}

	public void drawElementTooltip(int index,int mouseX,int mouseY)
	{
		getElement(index).drawToolTop(this, mouseX, mouseY);
	}

	public int getElementHeight(int id)
	{
		return _elements.get(id).getHeight();
	}

	public int getElementWidth(int id)
	{
		return  _elements.get(id).getWidth();
	}

	protected boolean shouldBeDisplayed(IMOListBoxElement element)
	{
		return true;
	}

	public IMOListBoxElement getElement(int index) {

		return _elements.get(index);
	}

	public int getElementCount() {

		return _elements.size();
	}
	//endregion

	//region events
	protected void onElementClicked(int index,int mousX,int mouseY) {

	}

	protected void onScrollV(int newStartIndex) {

	}

	protected void onScrollH(int newStartIndex) {

	}

	protected void onSelectionChanged(int newIndex, IMOListBoxElement imoListBoxElement)
	{
        listHandler.ListSelectionChange(getName(),newIndex);
	}
	//endregion
}
