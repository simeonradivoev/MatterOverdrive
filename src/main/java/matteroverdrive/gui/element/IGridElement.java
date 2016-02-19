package matteroverdrive.gui.element;

/**
 * Created by Simeon on 3/14/2015.
 */
public interface IGridElement
{
    int getHeight();

    int getWidth();

    Object getValue();

    void draw(ElementGrid listBox, int x, int y, int backColor, int textColor);
}
