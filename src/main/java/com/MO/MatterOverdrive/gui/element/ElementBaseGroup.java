package com.MO.MatterOverdrive.gui.element;

import cofh.lib.gui.GuiBase;
import cofh.lib.gui.element.ElementBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simeon on 4/3/2015.
 */
public class ElementBaseGroup extends ElementBase
{
    protected ArrayList<ElementBase> elements = new ArrayList<ElementBase>();

    public ElementBaseGroup(GuiBase gui, int posX, int posY)
    {
        super(gui, posX, posY);
    }

    @Override
    public void addTooltip(List<String> list) {
        ElementBase element = getElementAtPosition(gui.getMouseX() + this.posX, gui.getMouseY() + this.posY);

        if (element != null) {
            if (element.isVisible())
                element.addTooltip(list);
        }
    }

    protected ElementBase getElementAtPosition(int mX, int mY) {

        for (int i = getElements().size(); i-- > 0;) {
            ElementBase element = getElements().get(i);
            if (element.intersectsWith(mX, mY)) {
                return element;
            }
        }
        return null;
    }

    @Override
    public void drawBackground(int mouseX, int mouseY, float gameTicks) {
        for (int i = getElements().size(); i-- > 0;)
        {
            ElementBase c = getElements().get(i);
            c.drawBackground(mouseX,mouseY,gameTicks);
        }
    }

    @Override
    public void drawForeground(int mouseX, int mouseY)
    {
        for (int i = getElements().size(); i-- > 0;)
        {
            ElementBase c = getElements().get(i);
            c.drawForeground(mouseX, mouseY);
        }
    }

    @Override
    public void update(int mouseX, int mouseY)
    {
        for (int i = 0;i < getElements().size();i++)
        {
            getElements().get(i).update(mouseX, mouseY);
        }
        update();
    }

    @Override
    public boolean onMousePressed(int mouseX, int mouseY, int mouseButton)
    {
        for (int i = getElements().size(); i-- > 0;) {
            ElementBase c = getElements().get(i);
            if (!c.isVisible() || !c.isEnabled() || !c.intersectsWith(mouseX, mouseY)) {
                continue;
            }
            if (c.onMousePressed(mouseX, mouseY, mouseButton))
            {
                return true;
            }
        }

        return false;
    }

    @Override
    public void onMouseReleased(int mouseX, int mouseY) {

        for (int i = getElements().size(); i-- > 0;) {
            ElementBase c = getElements().get(i);
            if (!c.isVisible() || !c.isEnabled()) { // no bounds checking on mouseUp events
                continue;
            }
            c.onMouseReleased(mouseX, mouseY);
        }
    }

    @Override
    public boolean onMouseWheel(int mouseX, int mouseY, int movement) {

        for (int i = getElements().size(); i-- > 0;)
        {
            ElementBase c = getElements().get(i);
            if (!c.isVisible() || !c.isEnabled() || !c.intersectsWith(mouseX, mouseY)) {
                continue;
            }
            if (c.onMouseWheel(mouseX, mouseY, movement)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onKeyTyped(char characterTyped, int keyPressed) {

        for (int i = getElements().size(); i-- > 0;)
        {
            ElementBase c = getElements().get(i);
            if (!c.isVisible() || !c.isEnabled()) {
                continue;
            }
            if (c.onKeyTyped(characterTyped, keyPressed)) {
                return true;
            }
        }
        return false;
    }

    public List<? extends ElementBase> getElements()
    {
        return elements;
    }
}
