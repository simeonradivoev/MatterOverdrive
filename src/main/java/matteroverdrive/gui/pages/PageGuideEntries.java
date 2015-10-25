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
import matteroverdrive.client.render.HoloIcon;
import matteroverdrive.data.Bounds;
import matteroverdrive.data.ScaleTexture;
import matteroverdrive.gui.GuiMatterScanner;
import matteroverdrive.gui.MOGuiBase;
import matteroverdrive.gui.element.*;
import matteroverdrive.gui.events.ITextHandler;
import matteroverdrive.guide.GuideCategory;
import matteroverdrive.guide.MOGuideEntry;
import matteroverdrive.guide.MatterOverdriveGuide;
import matteroverdrive.init.MatterOverdriveItems;
import matteroverdrive.network.packet.server.PacketDataPadCommands;
import matteroverdrive.proxy.ClientProxy;
import matteroverdrive.util.MOStringHelper;
import matteroverdrive.util.RenderUtils;
import matteroverdrive.util.math.MOMathHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Simeon on 8/28/2015.
 */
public class PageGuideEntries extends ElementBaseGroup implements ITextHandler
{
    private Map<String,Bounds> groups;
    private List<ElementGuideEntry> guideEntries;
    private ElementStatesHoloIcons orderButtonElement;
    private MOElementTextField searchField;
    private PageGuideDescription pageGuideDescription;
    private ItemStack dataPadStack;
    private static int scrollX;
    private static int scrollY;
    private static String searchFilter = "";
    private boolean mouseIsDown;
    private int lastMouseX;
    private int lastMouseY;
    private int innerHeight;
    private int innerWidth;
    ScaleTexture groupBackground = new ScaleTexture(new ResourceLocation(Reference.PATH_ELEMENTS + "guide_group.png"),16,16).setOffsets(5,5,5,5);
    private ResourceLocation background = new ResourceLocation(Reference.PATH_ELEMENTS + "guide_cuircit_background.png");
    private List<ElementGuideCategory> categories;

    public PageGuideEntries(MOGuiBase gui, int posX, int posY, int width, int height, String name,PageGuideDescription pageGuideDescription)
    {
        super(gui, posX, posY, width, height);
        this.setName(name);
        guideEntries = new ArrayList<>(MatterOverdriveGuide.getGuides().size());
        groups = new HashMap<>();

        for (MOGuideEntry guideEntry : MatterOverdriveGuide.getGuides())
        {
            ElementGuideEntry entry = new ElementGuideEntry(gui,this,guideEntry.getGuiPosX(),32 + guideEntry.getGuiPosY(), guideEntry);
            entry.setName(GuiMatterScanner.QUIDE_ELEMENTS_NAME);
            guideEntries.add(entry);
        }

        orderButtonElement = new ElementStatesHoloIcons(gui,this,sizeX - 38,2,16,16,"orderType",new HoloIcon[]{ClientProxy.holoIcons.getIcon("list"),ClientProxy.holoIcons.getIcon("grid"),ClientProxy.holoIcons.getIcon("sort_random")});
        orderButtonElement.setNormalTexture(null);
        orderButtonElement.setOverTexture(null);
        orderButtonElement.setDownTexture(null);
        orderButtonElement.setColor(Reference.COLOR_MATTER);
        searchField = new MOElementTextField(gui,this,28,4,128,10);
        searchField.setBackground(null);
        searchField.setHoloIcon(ClientProxy.holoIcons.getIcon("page_icon_search"));
        searchField.setColor(Reference.COLOR_MATTER);
        innerHeight = sizeY;
        innerWidth = sizeX;
        this.pageGuideDescription = pageGuideDescription;
        categories = new ArrayList<>();
        for (GuideCategory category : MatterOverdriveGuide.getCategories().values())
        {
            ElementGuideCategory guideCategory = new ElementGuideCategory(gui,this,0,0,category.getDisplayName(),22,22,category);
            guideCategory.setDisabledTexture(MOElementButton.HOVER_TEXTURE_DARK);
            categories.add(guideCategory);
        }
    }

    @Override
    public void init()
    {
        super.init();
        elements.add(orderButtonElement);
        elements.add(searchField);
        searchField.setText(searchFilter);
        orderButtonElement.setSelectedState(MatterOverdriveItems.dataPad.getOrdering(dataPadStack));
        for (ElementGuideEntry entry : guideEntries)
        {
            elements.add(entry);
        }
    }

    @Override
    public void updateInfo()
    {
        super.updateInfo();
        groups.clear();

        int groupPadding = 6;
        int topOffset = 22;
        int bottomOffset = 16;
        int leftOffset = 8;
        int rightOffset = 8;


        int x = leftOffset + scrollX;
        int y = topOffset + scrollY;
        int heightCount = 0;
        int widthCount = 0;

        for (ElementGuideEntry entry : guideEntries)
        {
            if (searchFilterMatch(entry.getEntry(),searchFilter) && getActiveCategory().getEntries().contains(entry.getEntry()))
            {
                entry.setVisible(true);
            }else
            {
                entry.setVisible(false);
            }

            if (orderButtonElement.getSelectedState() == 0)
            {
                if (entry.isVisible())
                {
                    entry.setPosition(x + 16, y);
                    entry.setShowLabel(true);
                    y += entry.getHeight() + 4;
                    heightCount += entry.getHeight() + 4;
                }
            }
            else if (orderButtonElement.getSelectedState() == 1)
            {
                if (entry.isVisible())
                {
                    entry.setPosition(x, y);
                    entry.setShowLabel(false);
                    x += entry.getWidth() + 4;
                    if (x > sizeX - entry.getHeight() - 4) {
                        x = 8;
                        y += entry.getHeight() + 4;
                        heightCount += entry.getHeight() + 4;
                    }
                }
            }else
            {
                if (entry.isVisible())
                {
                    entry.setPosition(x + entry.getEntry().getGuiPosX(), y + entry.getEntry().getGuiPosY());
                    entry.setShowLabel(false);
                    widthCount = Math.max(widthCount, entry.getEntry().getGuiPosX() + entry.getWidth() + groupPadding + 4);
                    heightCount = Math.max(heightCount, entry.getEntry().getGuiPosY() + entry.getHeight() + groupPadding + 4);

                    if (entry.getEntry().getGroup() != null)
                    {
                        if (!groups.containsKey(entry.getEntry().getGroup()))
                        {
                            Bounds bounds = new Bounds(entry.getPosX()-groupPadding,entry.getPosY()-groupPadding,entry.getPosX()+entry.getWidth()+groupPadding,entry.getPosY()+entry.getHeight()+groupPadding);
                            groups.put(entry.getEntry().getGroup(),bounds);
                        }else
                        {
                            groups.get(entry.getEntry().getGroup()).extend(entry.getPosX() - groupPadding,entry.getPosY() - groupPadding,entry.getPosX()+entry.getWidth()+groupPadding,entry.getPosY()+entry.getHeight()+groupPadding);
                        }
                    }
                }
            }


        }

        innerWidth = Math.max(widthCount + leftOffset,sizeX);
        innerHeight = Math.max(heightCount + topOffset,sizeY);
    }

    private boolean searchFilterMatch(MOGuideEntry entry,String searchFilter)
    {
        if (entry.getDisplayName().toLowerCase().contains(searchFilter.toLowerCase()))
        {
            return true;
        }
        return false;
    }

    @Override
    public void drawBackground(int mouseX, int mouseY, float gameTicks)
    {
        RenderUtils.beginStencil();
        //glColor4f(1,1,1,1);
        RenderUtils.drawStencil(posX, posY, posX + sizeX, posY + sizeY, 1);
        RenderUtils.endStencil();
        glEnable(GL_STENCIL_TEST);
        glEnable(GL_BLEND);
        glDisable(GL_ALPHA_TEST);
        glColor4d(1,1,1,0.1);
        gui.bindTexture(background);
        RenderUtils.drawPlane(sizeX / 2 - 512 + scrollX / 2, sizeY / 2 - 512 + scrollY / 2, 0, 1024, 1024);
        glEnable(GL_ALPHA_TEST);
        super.drawBackground(mouseX, mouseY, gameTicks);
        glDisable(GL_STENCIL_TEST);
    }

    @Override
    public void drawForeground(int mouseX, int mouseY)
    {
        RenderUtils.beginStencil();
        RenderUtils.drawStencil(posX, posY, posX + sizeX, posY + sizeY, 1);
        if(orderButtonElement.getSelectedState() > 1) {
            for (Map.Entry<String, Bounds> group : groups.entrySet()) {
                getFontRenderer().setUnicodeFlag(true);
                Bounds b = group.getValue();
                RenderUtils.applyColor(Reference.COLOR_MATTER);
                groupBackground.Render(14 + b.getMinX(), 14 + b.getMinY(), b.getWidth(), b.getHeight());
                String groupName = MOStringHelper.translateToLocal(String.format("guide.group.%s.name", group.getKey()));
                int groupNameWidth = getFontRenderer().getStringWidth(groupName);
                getFontRenderer().drawString(groupName, 14 + scrollX + b.getMinX() + b.getWidth() / 2 - groupNameWidth / 2, 10 + b.getMinY(), Reference.COLOR_MATTER.getColor());
                getFontRenderer().setUnicodeFlag(false);
            }
        }
        super.drawForeground(mouseX, mouseY);
        RenderUtils.endStencil();
    }

    @Override
    public void update(int mouseX, int mouseY)
    {
        super.update(mouseX, mouseY);

        int mouseXDelta = mouseX - lastMouseX;
        int mouseYDelta = mouseY - lastMouseY;

        if (mouseIsDown)
        {
            if (mouseX > 0 && mouseX < sizeX && mouseY > 0 && mouseY < sizeY)
            {
                scrollX += mouseXDelta;
                scrollY += mouseYDelta;
            }
        }

        scrollX = Math.min(scrollX, 0);
        scrollX = Math.max(scrollX, sizeX - innerWidth);
        scrollY = Math.min(scrollY, 0);
        scrollY = Math.max(scrollY, sizeY - innerHeight);

        lastMouseX = mouseX;
        lastMouseY = mouseY;
    }

    public boolean onMouseWheel(int mouseX, int mouseY, int movement)
    {
        scrollY += MOMathHelper.Lerp(scrollX,scrollX+movement,0.1f);

        scrollX = Math.min(scrollX, 0);
        scrollX = Math.max(scrollX, sizeX - innerWidth);
        scrollY = Math.min(scrollY, 0);
        scrollY = Math.max(scrollY, sizeY - innerHeight);

        return true;
    }

    @Override
    public boolean onMousePressed(int mouseX, int mouseY, int mouseButton)
    {
        if (mouseButton == 0)
        {
            mouseIsDown = true;
        }
        return super.onMousePressed(mouseX,mouseY,mouseButton);
    }

    @Override
    public void onMouseReleased(int mouseX, int mouseY)
    {
        if (mouseIsDown)
        {
            mouseIsDown = false;
        }
        super.onMouseReleased(mouseX, mouseY);
    }

    @Override
    public void handleElementButtonClick(ElementBase element,String buttonName, int mouseButton)
    {
        if (element instanceof ElementGuideEntry)
        {
            pageGuideDescription.OpenGuide(((ElementGuideEntry) element).getEntry().getId());
            MatterOverdrive.packetPipeline.sendToServer(new PacketDataPadCommands(dataPadStack));
            ((MOGuiBase)gui).setPage(1);
        }else if (element.equals(orderButtonElement))
        {
            MatterOverdriveItems.dataPad.setOrdering(dataPadStack,orderButtonElement.getSelectedState());
            MatterOverdrive.packetPipeline.sendToServer(new PacketDataPadCommands(dataPadStack));
        }else if (element instanceof ElementGuideCategory)
        {
            setActiveCategory(((ElementGuideCategory) element).getCategory().getName());
        }
    }

    public void setDataPadStack(ItemStack dataPadStack)
    {
        this.dataPadStack = dataPadStack;
    }

    @Override
    public void textChanged(String elementName, String text, boolean typed)
    {
        searchFilter = text;
    }

    public void setActiveCategory(String category)
    {
        MatterOverdriveItems.dataPad.setCategory(dataPadStack, category);
        MatterOverdrive.packetPipeline.sendToServer(new PacketDataPadCommands(dataPadStack));
        ((MOGuiBase)gui).setPage(0);
        groups.clear();
    }

    public GuideCategory getActiveCategory()
    {
        String category = MatterOverdriveItems.dataPad.getCategory(dataPadStack);
        GuideCategory cat = MatterOverdriveGuide.getCategory(category);
        if (cat == null)
        {
            return MatterOverdriveGuide.getCategory("general");
        }
        return cat;
    }

    public List<ElementGuideCategory> getCategories(){return categories;}
}
