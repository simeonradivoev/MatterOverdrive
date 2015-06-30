package matteroverdrive.gui.element;

import cofh.lib.gui.GuiBase;
import cofh.lib.gui.element.ElementBase;
import cofh.lib.util.helpers.MathHelper;
import cofh.lib.util.helpers.StringHelper;
import matteroverdrive.util.RenderUtils;
import matteroverdrive.util.math.MOMathHelper;


/**
 * Created by Simeon on 3/13/2015.
 */
public class ElementGrid extends ElementBaseGroup
{
    private int maxWidth;
    float scrollYSmooth = 0;
    int scrollX = 0;
    int scrollY = 0;
    int scrollSpeed = 10;
    int marginTop = 0;
    int marginLeft = 0;

    public ElementGrid(GuiBase guiBase, int x, int y,int width,int height,int maxWidth)
    {
        super(guiBase,x, y,width,height);
        this.maxWidth = maxWidth;
    }

    @Override
    public void update(int mouseX,int mouseY)
    {

    }

    private void manageDrag(int maxHeight)
    {
        scrollY = Math.max(scrollY, -maxHeight);
        scrollYSmooth = MOMathHelper.Lerp(scrollYSmooth,scrollY,0.1f);
    }

    @Override
    public void drawBackground(int mouseX, int mouseY, float gameTicks)
    {
        int widthCount = marginLeft;
        int height = marginTop;
        int maxTempHeigh = 0;

        for (ElementBase element : elements)
        {
            if (shouldBeDisplayed(element))
            {

                if (widthCount > this.maxWidth - element.getWidth()) {
                    height += maxTempHeigh;
                    widthCount = marginLeft;
                    maxTempHeigh = 0;
                }


                if (MathHelper.round(height + scrollYSmooth) < this.sizeY || maxTempHeigh == 0 && MathHelper.round(height + scrollYSmooth) > -maxTempHeigh) {
                    element.setPosition(widthCount, MathHelper.round(height + scrollYSmooth));
                    element.setVisible(true);
                } else {
                    element.setVisible(false);
                }

                maxTempHeigh = Math.max(maxTempHeigh, element.getHeight() + 2);
                widthCount += element.getWidth() + 3;
            }else
            {
                element.setVisible(false);
            }
        }

        manageDrag(height);


        RenderUtils.beginStencil();
        drawStencil(posX, posY, posX + sizeX, posY + sizeY, 1);
        super.drawBackground(mouseX, mouseY, gameTicks);
        RenderUtils.endStencil();
    }

    @Override
    public void drawForeground(int mouseX, int mouseY)
    {
        RenderUtils.beginStencil();
        drawStencil(posX, posY, sizeX + posX, sizeY + posX, 1);
        super.drawForeground(mouseX, mouseY);
        RenderUtils.endStencil();
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
        scrollY-=scrollSpeed;
        onScrollV(scrollY);
    }

    public void scrollUp() {

        if(scrollY < 0)
        {
            scrollY = Math.min(scrollY + scrollSpeed,0);
            onScrollV(scrollY);
        }
    }

    protected void onScrollV(int newStartIndex)
    {
        scrollY = newStartIndex;
    }

    public void drawTexturedModalRect(int x, int y, int u, int v, int width, int height,int texWidth,int texHeight) {

        gui.drawSizedTexturedModalRect(x, y, u, v, width, height, texWidth, texHeight);
    }

    public void setMargins(int left,int right,int top,int bottom)
    {
        marginTop = top;
        marginLeft = left;
    }

    public boolean shouldBeDisplayed(ElementBase element)
    {
        return true;
    }
}
