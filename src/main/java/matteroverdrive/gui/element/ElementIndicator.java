package matteroverdrive.gui.element;

import cofh.lib.gui.GuiBase;
import matteroverdrive.Reference;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * Created by Simeon on 4/12/2015.
 */
public class ElementIndicator extends MOElementBase
{
    private int indication;
    public static final ResourceLocation BG = new ResourceLocation(Reference.PATH_ELEMENTS + "indicator.png");

    public ElementIndicator(GuiBase gui, int posX, int posY)
    {
        super(gui, posX, posY,21,5);
        this.texH = 15;
        this.texW = 21;
    }

    @Override
    public void drawBackground(int mouseX, int mouseY, float gameTicks)
    {
        GL11.glColor3f(1,1,1);
        gui.bindTexture(BG);
        gui.drawSizedTexturedModalRect(posX, posY, 0, 5 * indication, sizeX, sizeY,texW,texH);
    }

    @Override
    public void drawForeground(int mouseX, int mouseY) {

    }

    public void setIndication(int indication)
    {
        this.indication = indication;
    }
}
