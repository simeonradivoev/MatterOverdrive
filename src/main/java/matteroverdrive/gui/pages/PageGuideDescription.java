/*
 * This file is part of Matter Overdrive
 * Copyright (c) 2015., Simeon Radivoev, All rights reserved.
 *
 * Matter Overdrive is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Matter Overdrive is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Matter Overdrive.  If not, see <http://www.gnu.org/licenses>.
 */

package matteroverdrive.gui.pages;

import cofh.lib.gui.element.ElementBase;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.data.ScaleTexture;
import matteroverdrive.gui.MOGuiBase;
import matteroverdrive.gui.element.ElementBaseGroup;
import matteroverdrive.gui.element.MOElementButton;
import matteroverdrive.guide.GuideElementPage;
import matteroverdrive.guide.IGuideElement;
import matteroverdrive.guide.MOGuideEntry;
import matteroverdrive.guide.MatterOverdriveGuide;
import matteroverdrive.init.MatterOverdriveItems;
import matteroverdrive.network.packet.server.PacketDataPadCommands;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.Level;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Simeon on 4/3/2015.
 */
public class PageGuideDescription extends ElementBaseGroup
{
    public static final ScaleTexture GUIDES_BG = new ScaleTexture(new ResourceLocation(Reference.PATH_ELEMENTS + "guides_bg.png"),166,142).setOffsets(80,80,72,69);

    public static final String SCROLL_RIGHT_ELEMENT_NAME = "scroll_right";
    public static final String SCROLL_LEFT_ELEMENT_NAME = "scroll_left";
    public static final String RETURN_ELEMENT_NAME = "return";

    DocumentBuilderFactory builderFactory;
    DocumentBuilder builder;

    public static int tabID;
    public static int scroll;
    private static Stack<HistoryEntry> historyStack = new Stack<>();

    List<IGuideElement> pages;
    MOElementButton bt_scroll_right;
    MOElementButton bt_scroll_left;
    public final MOElementButton bt_return;
    private ItemStack dataPadStack;

    public PageGuideDescription(MOGuiBase gui, int posX, int posY, int width, int height, String name) {
        super(gui, posX, posY, width, height);
        this.setName(name);

        pages = new ArrayList<>();

        bt_scroll_right = new MOElementButton(gui,this,sizeX - 20,sizeY - 16,SCROLL_RIGHT_ELEMENT_NAME,0,0,10,0,10,10,"");
        bt_scroll_right.setTexture(Reference.PATH_ELEMENTS + "scroll_right.png",20,10);
        bt_scroll_left = new MOElementButton(gui,this,10,sizeY - 16,SCROLL_LEFT_ELEMENT_NAME,0,0,10,0,10,10,"");
        bt_scroll_left.setTexture(Reference.PATH_ELEMENTS + "scroll_left.png", 20, 10);
        bt_return = new MOElementButton(gui,this,sizeX/2 - 5,sizeY - 16,RETURN_ELEMENT_NAME,0,0,11,0,11,11,"");
        bt_return.setTexture(Reference.PATH_ELEMENTS + "return_arrow.png", 22, 11);

        builderFactory = DocumentBuilderFactory.newInstance();
        builderFactory.setIgnoringElementContentWhitespace(true);
        try {
            builder = builderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void init()
    {
        super.init();
        elements.add(bt_scroll_left);
        elements.add(bt_scroll_right);
        elements.add(bt_return);

        loadGuideInfo(MatterOverdriveItems.dataPad.getGuideID(dataPadStack));
    }

    @Override
    public void drawForeground(int mouseX, int mouseY)
    {
        if(MatterOverdriveItems.dataPad.getGuideID(dataPadStack) >= 0)
        {
            boolean lastUnicodeFlag = Minecraft.getMinecraft().fontRenderer.getUnicodeFlag();
            //set yunicode for smaller text and all characters
            Minecraft.getMinecraft().fontRenderer.setUnicodeFlag(true);

            if(tabID == 0)
            {
                DrawDescription(mouseX,mouseY);
            }

            Minecraft.getMinecraft().fontRenderer.setUnicodeFlag(lastUnicodeFlag);
        }

        super.drawForeground(mouseX, mouseY);
    }

    private void DrawDescription(int mouseX,int mouseY)
    {
        if (scroll < pages.size() && scroll >= 0)
        {
            MOGuideEntry guideEntry = MatterOverdriveGuide.getQuide(MatterOverdriveItems.dataPad.getGuideID(dataPadStack));
            int x = posX;
            int y = posY;

            if (pages.get(scroll) != null && scroll < pages.size()) {
                glPushMatrix();
                glTranslated(x, y, 0);
                IGuideElement element = pages.get(scroll);
                element.drawElement(sizeX,mouseX-x ,mouseY-y);
                glPopMatrix();
            }else
            {
                drawNoInfo();
            }
        }else {
            drawNoInfo();
        }

        handleScrollButtons();
    }

    private void drawNoInfo()
    {
        int noInfoWidth = getFontRenderer().getStringWidth("No Info...");
        getFontRenderer().drawString("No Info...", sizeX / 2 - noInfoWidth / 2, sizeY / 2, Reference.COLOR_HOLO_RED.getColor());
    }

    private void handleScrollButtons()
    {
        bt_scroll_left.setVisible(true);
        bt_scroll_right.setVisible(true);

        if(scroll >= pages.size()-1)
        {
            bt_scroll_right.setVisible(false);
        }
        if (scroll <= 0)
        {
            bt_scroll_left.setVisible(false);
        }
    }

    @Override
    public void handleElementButtonClick(ElementBase element,String buttonName, int mouseButton)
    {
        if(element.equals(bt_scroll_left))
        {
            scroll--;
        }
        else if(element.equals(bt_scroll_right))
        {
            scroll++;
        }
        else if (element.equals(bt_return))
        {
            undo();
        }

        scroll = MathHelper.clamp_int(scroll,0, pages.size()-1);
    }

    public void OpenGuide(int id,boolean writeToHistory)
    {
        OpenGuide(id,0,writeToHistory);
    }

    public void OpenGuide(int id,int page,boolean writeToHistory)
    {
        if (MatterOverdriveItems.dataPad.getGuideID(dataPadStack) != id)
        {
            if (writeToHistory)
                historyStack.push(new HistoryEntry(MatterOverdriveItems.dataPad.getGuideID(dataPadStack),scroll));

            loadGuideInfo(id);
            MatterOverdriveItems.dataPad.setOpenGuide(dataPadStack, id);
            MatterOverdrive.packetPipeline.sendToServer(new PacketDataPadCommands(dataPadStack));
            this.scroll = page;
            this.tabID = 0;
        }
    }

    private void undo()
    {
        if (historyStack.size() > 0)
        {
            HistoryEntry historyEntry = historyStack.pop();
            OpenGuide(historyEntry.entry,false);
            scroll = historyEntry.scroll;
        }
        else
        {
            ((MOGuiBase)gui).setPage(0);
        }
    }

    private void loadGuideInfo(int guideID)
    {
        pages.clear();

        if(guideID >= 0)
        {
            MOGuideEntry entry = MatterOverdriveGuide.getQuide(guideID);
            InputStream descriptionStream = entry.getDescriptionStream();
            if (descriptionStream != null) {
                try {
                    Document document = builder.parse(descriptionStream);
                    document.normalize();
                    Element rootNode = (Element) document.getElementsByTagName("entry").item(0);
                    NodeList pagesNodes = rootNode.getElementsByTagName("page");
                    Map<String,String> stylesheetMap = loadStyleSheetMap(rootNode);

                    for (int i = 0; i < pagesNodes.getLength(); i++)
                    {
                        GuideElementPage page = new GuideElementPage();
                        page.setGUI((MOGuiBase)gui);
                        page.loadElement(entry,(Element)pagesNodes.item(i),stylesheetMap,sizeX,sizeY);
                        pages.add(page);
                    }

                } catch (SAXException e) {
                    MatterOverdrive.log.log(Level.ERROR, e, "XML for guide entry %s is not valid", entry.getDisplayName());
                } catch (IOException e) {
                    MatterOverdrive.log.log(Level.ERROR, e, "there was a problem reading language file for entry %s", entry.getDisplayName());
                }
            }else
            {
                MatterOverdrive.log.warn("Guide Entry file for %s missing at: %s",entry.getDisplayName(),entry.getDescriptionPath("language"));
            }
        }
    }

    private Map<String,String> loadStyleSheetMap(Element element)
    {
        if (element.hasAttribute("stylesheet"))
        {
            try {
                Map<String,String> styleMap = new HashMap<>();
                InputStream stylesheetStream = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation(element.getAttribute("stylesheet"))).getInputStream();
                String rawStyle = IOUtils.toString(stylesheetStream, "UTF-8");
                rawStyle = rawStyle.replaceAll("\\r|\\n|\\s+","");
                rawStyle = rawStyle.replaceAll("(?s)/\\*.*?\\*/", "");  //remove comments
                Matcher matcher = Pattern.compile("([^\\}\\{]+)(\\{[^\\}]+\\})", Pattern.DOTALL | Pattern.MULTILINE).matcher(rawStyle);
                while (matcher.find())
                {
                    styleMap.put(matcher.group(1), matcher.group(2).substring(1,matcher.group(2).length()-1));
                }
                return styleMap;
            } catch (IOException e) {
                MatterOverdrive.log.log(Level.ERROR,e,"There was a problem loading the stylesheet");
            }
        }
        return null;
    }

    public void setDataPadStack(ItemStack stack)
    {
        dataPadStack = stack;
    }

    private class HistoryEntry
    {
        public final int entry;
        public final int scroll;
        public HistoryEntry(int entry,int scroll)
        {
            this.entry = entry;
            this.scroll = scroll;
        }
    }
}
