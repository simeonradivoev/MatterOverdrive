package matteroverdrive.gui.element;

import cofh.lib.gui.GuiBase;
import cofh.lib.gui.element.ElementBase;
import matteroverdrive.gui.events.IListHandler;
import matteroverdrive.util.RenderUtils;
import matteroverdrive.util.math.MOMathHelper;
import net.minecraft.util.MathHelper;

/**
 * Created by Simeon on 6/20/2015.
 */
public class ElementGroupList extends ElementBaseGroup {

    int selectedIndex;
    int scroll;
    int smoothScroll;
    float smoothScrollMultiply = 0.1f;
    int padding = 6;
    IListHandler listHandler;

    public ElementGroupList(GuiBase gui,IListHandler listHandler, int posX, int posY, int width, int height)
    {
        super(gui, posX, posY, width, height);
        this.listHandler = listHandler;
    }

    @Override
    public void init()
    {
        super.init();
    }

    @Override
    public void update(int mouseX, int mouseY)
    {
        super.update(mouseX,mouseY);
        int heightCount = 0;
        int elementsHeight = -(getElementsHeight() - sizeY);

        for (int i = 0;i < elements.size();i++)
        {
            elements.get(i).setPosition(0,heightCount + smoothScroll);
            heightCount += elements.get(i).getHeight() + padding;
            if (smoothScroll + heightCount >= 0 && heightCount + smoothScroll - elements.get(i).getHeight() < sizeY)
            {
                elements.get(i).setVisible(true);
            }else
            {
                elements.get(i).setVisible(false);
            }
        }

        smoothScroll = (int)MOMathHelper.Lerp(smoothScroll,scroll,smoothScrollMultiply);
        selectedIndex = MathHelper.clamp_int(selectedIndex,0,elements.size()-1);
    }

    @Override
    public void drawBackground(int mouseX, int mouseY, float gameTicks)
    {
        RenderUtils.beginStencil();
        drawStencil(posX, posY, posX + sizeX, posY + sizeY, 1);
        super.drawBackground(mouseX,mouseY,gameTicks);
        RenderUtils.endStencil();
    }

    @Override
    public void drawForeground(int mouseX, int mouseY)
    {
        RenderUtils.beginStencil();
        drawStencil(posX, posY, posX + sizeX, posY + sizeY, 1);
        super.drawForeground(mouseX, mouseY);
        RenderUtils.endStencil();
    }

    @Override
    public boolean onMousePressed(int mouseX, int mouseY, int mouseButton)
    {
        mouseX -= this.getGlobalX();
        mouseY -= this.getGlobalY();

        for (int i = getElements().size(); i-- > 0;)
        {
            ElementBase c = getElements().get(i);
            if (!c.isVisible() || !c.isEnabled() || !c.intersectsWith(mouseX, mouseY)) {
                continue;
            }
            if (c.onMousePressed(mouseX, mouseY, mouseButton))
            {
                setSelectedIndex(i);
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean onMouseWheel(int mouseX, int mouseY, int movement) {

        boolean didScroll = super.onMouseWheel(mouseX,mouseY,movement);
        scroll += movement * 0.2;
        limitScroll();
        return didScroll;
    }

    public void limitScroll()
    {
        int elementsHeight = -(getElementsHeight() - sizeY + 32);
        scroll = Math.min(0,scroll);
        scroll = Math.max(elementsHeight,scroll);
    }

    public int getElementsHeight()
    {
        int height = 0;
        for (int i = 0;i < elements.size();i++)
        {
            height += elements.get(i).getHeight() + padding;
        }
        return height;
    }

    public int getSelectedIndex()
    {
        return selectedIndex;
    }

    public void setSelectedIndex(int selectedIndex)
    {
        int newSelectedIndex = MathHelper.clamp_int(selectedIndex,0,elements.size()-1);
        if (newSelectedIndex != this.selectedIndex)
        {
            this.selectedIndex = newSelectedIndex;
            listHandler.ListSelectionChange(getName(),selectedIndex);
        }else
        {
            this.selectedIndex = newSelectedIndex;
        }
    }

    public boolean isSelected(ElementBase elementBase)
    {
        if (selectedIndex < elements.size()) {
            return elements.get(selectedIndex).equals(elementBase);
        }
        return false;
    }
    public void setScroll(int scroll){this.scroll = scroll;}
    public int getScroll(){return this.scroll;}
}
