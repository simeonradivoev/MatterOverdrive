package matteroverdrive.gui.element;

import cofh.lib.gui.GuiBase;
import cofh.lib.gui.element.ElementTextFieldFiltered;
import matteroverdrive.Reference;
import matteroverdrive.data.ScaleTexture;
import net.minecraft.util.ResourceLocation;

/**
 * Created by Simeon on 5/3/2015.
 */
public class MOElementTextFieldFiltered extends ElementTextFieldFiltered {

    private ScaleTexture background;
    private int textOffsetX,textOffsetY;

    public MOElementTextFieldFiltered(GuiBase gui, int posX, int posY, int width, int height)
    {
        this(gui,posX,posY,width,height,(short)32);
    }

    public MOElementTextFieldFiltered(GuiBase gui, int posX, int posY, int width, int height, short limit)
    {
        super(gui, posX, posY, width, height, limit);
        background = new ScaleTexture(new ResourceLocation(Reference.PATH_ELEMENTS + "search_field.png"),166,14);
    }

    @Override
    public void drawBackground(int mouseX, int mouseY, float gameTicks)
    {
        background.Render(posX - textOffsetX,posY -textOffsetY,sizeX,sizeY);
    }

    public void setBacground(ScaleTexture texture)
    {
        background = texture;
    }

    public void setTextOffset(int x,int y)
    {
        textOffsetX = x;
        textOffsetY = y;
    }
}
