package com.MO.MatterOverdrive.gui.element;

import cofh.lib.gui.element.ElementBase;
import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.gui.MOGuiBase;
import com.MO.MatterOverdrive.guide.MOGuideEntry;
import net.minecraft.util.ResourceLocation;

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
