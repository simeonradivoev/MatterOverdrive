package matteroverdrive.gui.element;

import matteroverdrive.Reference;
import matteroverdrive.gui.MOGuiBase;
import matteroverdrive.guide.MOGuideEntry;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.List;

/**
 * Created by Simeon on 4/4/2015.
 */
public class ElementGuideEntry extends MOElementBase
{
    public static final ResourceLocation BG = new ResourceLocation(Reference.PATH_ELEMENTS + "quide_element_bg.png");
    private MOGuideEntry entry;
    int id;

    public ElementGuideEntry(MOGuiBase gui, int posX, int posY,MOGuideEntry entry,int id)
    {
        super(gui, posX, posY);
        this.entry = entry;
        this.id = id;
        this.setSize(22,22);
    }

    @Override
    public void addTooltip(List<String> list)
    {
        list.addAll(entry.tooltip);
    }

    @Override
    public void drawBackground(int mouseX, int mouseY, float gameTicks)
    {
        GL11.glColor3f(1, 1, 1);
        gui.bindTexture(BG);
        gui.drawSizedTexturedModalRect(this.posX,this.posY,0,0,22,22,22,22);
    }

    @Override
    public void drawForeground(int mouseX, int mouseY)
    {
        entry.RenderIcon(this.posX + 3, this.posY + 3);
    }

    public boolean onMousePressed(int mouseX, int mouseY, int mouseButton)
    {
        ((MOGuiBase)gui).handleListChange(name,mouseButton,id);
        return true;
    }
}
