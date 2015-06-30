package matteroverdrive.gui.element;

/**
 * Created by Simeon on 3/14/2015.
 */
public interface IGridElement
{
    public int getHeight();

    public int getWidth();

    public Object getValue();

    public void draw(ElementGrid listBox, int x, int y, int backColor, int textColor);
}
