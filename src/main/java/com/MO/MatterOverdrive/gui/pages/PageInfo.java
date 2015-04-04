package com.MO.MatterOverdrive.gui.pages;

import cofh.lib.gui.GuiColor;
import cofh.lib.gui.element.ElementBase;
import com.MO.MatterOverdrive.gui.GuiMatterScanner;
import com.MO.MatterOverdrive.gui.MOGuiBase;
import com.MO.MatterOverdrive.gui.element.ElementBaseGroup;
import com.MO.MatterOverdrive.gui.element.ElementGuideEntry;
import com.MO.MatterOverdrive.guide.MOGuideEntry;
import com.MO.MatterOverdrive.guide.MatterOverdriveQuide;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simeon on 4/3/2015.
 */
public class PageInfo extends ElementBaseGroup
{
    public static int guideID = -1;
    public static int tabID;
    public static int scroll;
    List<ElementGuideEntry> guideEntries;

    public PageInfo(MOGuiBase gui, int posX, int posY,String name) {
        super(gui, posX, posY);
        this.setName(name);

        guideEntries = new ArrayList<ElementGuideEntry>(MatterOverdriveQuide.getQuides().size());
        for (int i = 0 ; i < MatterOverdriveQuide.getQuides().size();i++)
        {
            ElementGuideEntry entry = new ElementGuideEntry(gui,0,0,MatterOverdriveQuide.getQuides().get(i),i);
            entry.setName(GuiMatterScanner.QUIDE_ELEMENTS_NAME);
            guideEntries.add(entry);
        }
    }

    @Override
    public void drawBackground(int mouseX, int mouseY, float gameTicks) {
        super.drawBackground(mouseX, mouseY, gameTicks);
    }

    @Override
    public void drawForeground(int mouseX, int mouseY)
    {
        if(guideID >= 0)
        {
            if(tabID == 0)
            {
                MOGuideEntry entry = MatterOverdriveQuide.getQuides().get(guideID);
                if (scroll < entry.description.size()) {
                    Minecraft.getMinecraft().fontRenderer.drawSplitString(entry.description.get(scroll), 48, 48, 158, new GuiColor(255, 255, 255).getColor());
                }
            }
        }
        else
        {
            for (int i = 0 ; i < guideEntries.size();i++)
            {
                int x = 48 + i * 22;
                int y = 34;

                guideEntries.get(i).setPosition(x,y);
            }
        }

        super.drawForeground(mouseX, mouseY);
    }

    public void OpenQuide(int id)
    {
        this.guideID = id;
        this.scroll = 0;
        this.tabID = 0;
    }

    @Override
    public List<? extends ElementBase> getElements()
    {
        if(guideID < 0)
            return guideEntries;
        else
            return this.elements;
    }
}
