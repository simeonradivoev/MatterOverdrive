package com.MO.MatterOverdrive.gui.pages;

import cofh.lib.gui.GuiColor;
import cofh.lib.gui.element.ElementBase;
import cofh.lib.gui.element.ElementButton;
import cofh.lib.util.helpers.MathHelper;
import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.data.ScaleTexture;
import com.MO.MatterOverdrive.gui.GuiMatterScanner;
import com.MO.MatterOverdrive.gui.MOGuiBase;
import com.MO.MatterOverdrive.gui.element.ElementBaseGroup;
import com.MO.MatterOverdrive.gui.element.ElementGuideEntry;
import com.MO.MatterOverdrive.gui.element.MOElementButton;
import com.MO.MatterOverdrive.guide.MOGuideEntry;
import com.MO.MatterOverdrive.guide.MatterOverdriveQuide;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simeon on 4/3/2015.
 */
public class PageInfo extends ElementBaseGroup
{
    public static final ScaleTexture GUIDES_BG = new ScaleTexture(new ResourceLocation(Reference.PATH_ELEMENTS + "guides_bg.png"),166,142).setOffsets(80,80,72,69);

    public static final int MAX_GUIDES_PER_ROW = 3;
    public static final String SCROLL_RIGHT_ELEMENT_NAME = "scroll_right";
    public static final String SCROLL_LEFT_ELEMENT_NAME = "scroll_left";
    public static final String RETURN_ELEMENT_NAME = "return";

    public static int guideID = -1;
    public static int tabID;
    public static int scroll;
    List<ElementBase> guideEntries;
    List<String> description;
    ElementButton bt_scroll_right;
    ElementButton bt_scroll_left;
    ElementButton bt_return;


    public PageInfo(MOGuiBase gui, int posX, int posY,int width,int height,String name) {
        super(gui, posX, posY,width,height);
        this.setName(name);

        guideEntries = new ArrayList<ElementBase>(MatterOverdriveQuide.getQuides().size());
        description = new ArrayList<String>();

        for (int i = 0 ; i < MatterOverdriveQuide.getQuides().size();i++)
        {
            ElementGuideEntry entry = new ElementGuideEntry(gui,0,0,MatterOverdriveQuide.getQuides().get(i),i);
            entry.setName(GuiMatterScanner.QUIDE_ELEMENTS_NAME);
            guideEntries.add(entry);
        }

        bt_scroll_right = new MOElementButton(gui,this,sizeX - 30,sizeY - 30,SCROLL_RIGHT_ELEMENT_NAME,0,0,10,0,10,10,"");
        bt_scroll_right.setTexture(Reference.PATH_ELEMENTS + "scroll_right.png",20,10);
        bt_scroll_left = new MOElementButton(gui,this,50,sizeY - 30,SCROLL_LEFT_ELEMENT_NAME,0,0,10,0,10,10,"");
        bt_scroll_left.setTexture(Reference.PATH_ELEMENTS + "scroll_left.png",20,10);
        bt_return = new MOElementButton(gui,this,150,sizeY - 30,RETURN_ELEMENT_NAME,0,0,11,0,11,11,"");
        bt_return.setTexture(Reference.PATH_ELEMENTS + "return_arrow.png", 22, 11);

        elements.add(bt_scroll_left);
        elements.add(bt_scroll_right);
        elements.add(bt_return);

        loadQuideInfo(guideID);
    }

    @Override
    public void drawBackground(int mouseX, int mouseY, float gameTicks)
    {
        GL11.glColor4f(1, 1, 1, 1);
        GUIDES_BG.Render(41, 26, 244,198);
        super.drawBackground(mouseX, mouseY, gameTicks);
    }

    @Override
    public void drawForeground(int mouseX, int mouseY)
    {

        if(guideID >= 0)
        {
            boolean lastUnicodeFlag = Minecraft.getMinecraft().fontRenderer.getUnicodeFlag();
            //set yunicode for smaller text and all characters
            Minecraft.getMinecraft().fontRenderer.setUnicodeFlag(true);

            if(tabID == 0)
            {
                DrawDescription();
            }

            Minecraft.getMinecraft().fontRenderer.setUnicodeFlag(lastUnicodeFlag);
        }
        else
        {
            for (int i = 0 ; i < guideEntries.size();i++)
            {
                int x = 48 + (i % MAX_GUIDES_PER_ROW) * 24;
                int y = 34 + 24 * (i / MAX_GUIDES_PER_ROW);

                guideEntries.get(i).setPosition(x,y);
            }
        }

        super.drawForeground(mouseX, mouseY);
    }

    private void DrawDescription()
    {
        if (scroll < description.size() && scroll >= 0)
        {
            Minecraft.getMinecraft().fontRenderer.setUnicodeFlag(true);
            Minecraft.getMinecraft().fontRenderer.drawSplitString(description.get(scroll), 56, 44, 210, Reference.COLOR_MATTER.getColor());
        }

        handleScrollButtons();
    }

    private void handleScrollButtons()
    {
        bt_scroll_left.setVisible(true);
        bt_scroll_right.setVisible(true);

        if(scroll >= description.size()-1)
        {
            bt_scroll_right.setVisible(false);
        }
        if (scroll <= 0)
        {
            bt_scroll_left.setVisible(false);
        }
    }

    @Override
    public void handleElementButtonClick(String buttonName, int mouseButton)
    {
        if(buttonName == SCROLL_LEFT_ELEMENT_NAME)
        {
            scroll--;
        }
        else if(buttonName == SCROLL_RIGHT_ELEMENT_NAME)
        {
            scroll++;
        }
        else if(buttonName == RETURN_ELEMENT_NAME)
        {
            OpenQuide(-1);
        }

        scroll = MathHelper.clampI(scroll,0,description.size()-1);
    }

    public void OpenQuide(int id)
    {
        this.guideID = id;
        loadQuideInfo(id);
        this.scroll = 0;
        this.tabID = 0;
    }

    private void loadQuideInfo(int guideID)
    {
        if(guideID >= 0 && guideID < MatterOverdriveQuide.getQuides().size())
        {

            MOGuideEntry entry = MatterOverdriveQuide.getQuides().get(guideID);
            ResourceLocation descriptionLocation = entry.description;

            try {
                InputStream descriptionStream = Minecraft.getMinecraft().getResourceManager().getResource(descriptionLocation).getInputStream();
                LineNumberReader descriptionReader = new LineNumberReader(new InputStreamReader(descriptionStream));
                String line;
                description.clear();

                while ((line = descriptionReader.readLine()) != null) {
                    description.add(line);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<ElementBase> getElements()
    {
        if(guideID < 0)
            return guideEntries;
        else
            return this.elements;
    }
}
