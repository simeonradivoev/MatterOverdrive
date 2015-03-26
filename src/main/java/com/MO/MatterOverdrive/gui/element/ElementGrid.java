package com.MO.MatterOverdrive.gui.element;

import cofh.lib.gui.GuiBase;
import cofh.lib.gui.element.ElementBase;
import cofh.lib.gui.element.listbox.IListBoxElement;
import cofh.lib.util.helpers.StringHelper;
import com.MO.MatterOverdrive.util.math.MOMathHelper;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Simeon on 3/13/2015.
 */
public class ElementGrid extends ElementBase implements Collection<IGridElement>
{
    private int maxWidth;
    private List<IGridElement> _elements = new LinkedList<IGridElement>();
    float scrollYSmooth = 0;
    int scrollX = 0;
    int scrollY = 0;
    int scrollSpeed = 10;

    public ElementGrid(GuiBase guiBase, int x, int y,int width,int height,int maxWidth)
    {
        super(guiBase, x, y,width,height);
        this.maxWidth = maxWidth;
    }

    @Override
    public void drawBackground(int z, int y, float ticks)
    {

    }

    @Override
    public void drawForeground(int x, int y)
    {
        int widthCount = 0;
        int height = (int)scrollYSmooth;
        int maxTempHeigh = 0;

        GL11.glPushMatrix();
        GL11.glTranslatef(this.posX,this.posY,0);

        for (IGridElement element : _elements)
        {
            if(height < this.sizeY)
            {
                element.draw(this, widthCount, height, 0, 0);
                maxTempHeigh = Math.max(maxTempHeigh, element.getHeight());
                widthCount += element.getWidth();
                GL11.glTranslatef(element.getWidth(),0,0);

                if (widthCount >= this.maxWidth)
                {
                    height += maxTempHeigh;
                    GL11.glTranslatef(-widthCount,height,0);
                    widthCount = 0;
                    maxTempHeigh = 0;
                }
            }
        }

        GL11.glPopMatrix();

        manageDrag(height);
    }

    private void manageDrag(int maxHeight)
    {
        scrollY = (int)Math.copySign(Math.min(Math.abs(scrollY), maxHeight - this.sizeY), scrollY);
        scrollYSmooth = MOMathHelper.Lerp(scrollYSmooth,scrollY,0.1f);
    }

    public int getRows()
    {
        int rows = 1;
        int widthCount = 0;
        for (int i = 0; i < _elements.size(); i++)
        {
            if(widthCount > this.maxWidth)
            {
                widthCount = 0;
                rows++;
            }
            else
            {
                widthCount += _elements.get(i).getWidth();
            }
        }
        return rows;
    }

    public int getInternalHeight()
    {
        int widthCount = 0;
        int height = 0;
        int maxTempHeigh = 0;

        for (int i = 0; i < _elements.size(); i++)
        {
            if(widthCount > this.maxWidth)
            {
                height += maxTempHeigh;
                maxTempHeigh = 0;
                widthCount = 0;
            }
            else
            {
                widthCount += _elements.get(i).getWidth();
                maxTempHeigh = Math.max(maxTempHeigh,_elements.get(i).getHeight());
            }
        }
        return height;
    }

    @Override
    public boolean onMouseWheel(int mouseX, int mouseY, int movement) {

        if (StringHelper.isControlKeyDown())
        {
            if (movement > 0) {
                //scrollLeft();
            } else if (movement < 0) {
                //scrollRight();
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

    public void scrollDown()
    {
        if(scrollY < 0)
        {
            scrollY = Math.min(scrollY + scrollSpeed,0);
            onScrollV(scrollY);
        }
    }

    public void scrollUp() {

        scrollY-=scrollSpeed;
        onScrollV(scrollY);
    }

    protected void onScrollV(int newStartIndex)
    {

    }

    public void drawTexturedModalRect(int x, int y, int u, int v, int width, int height,int texWidth,int texHeight) {

        gui.drawSizedTexturedModalRect(x, y, u, v, width, height, texWidth, texHeight);
    }

    //region Collection methods
    @Override
    public int size() {
        return _elements.size();
    }

    @Override
    public boolean isEmpty() {
        return _elements.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return _elements.contains(o);
    }

    @Override
    public Iterator<IGridElement> iterator() {
        return _elements.iterator();
    }

    @Override
    public Object[] toArray() {
        return _elements.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return _elements.toArray(a);
    }

    @Override
    public boolean add(IGridElement element) {
        return _elements.add(element);
    }

    @Override
    public boolean remove(Object o) {
        return remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return _elements.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends IGridElement> c) {
        return _elements.addAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return _elements.retainAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return this._elements.retainAll(c);
    }

    @Override
    public void clear() {
        _elements.clear();
    }
    //endregion
}
