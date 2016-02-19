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

package matteroverdrive.gui;

import matteroverdrive.Reference;
import matteroverdrive.client.data.Color;
import matteroverdrive.client.render.HoloIcon;
import matteroverdrive.container.IButtonHandler;
import matteroverdrive.container.MOBaseContainer;
import matteroverdrive.container.slot.SlotPlayerInventory;
import matteroverdrive.data.ScaleTexture;
import matteroverdrive.gui.element.*;
import matteroverdrive.gui.events.IListHandler;
import matteroverdrive.gui.events.ITextHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.FMLClientHandler;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public abstract class MOGuiBase extends GuiContainer implements IButtonHandler,ITextHandler,IListHandler, GuiElementList
{
    public static final SoundHandler guiSoundManager = FMLClientHandler.instance().getClient().getSoundHandler();
    protected int currentPage = -1;
    protected int mouseX = 0;
    protected int mouseY = 0;
    protected SidePannel sidePannel;
    protected CloseButton closeButton;
    ScaleTexture background;
    protected List<ElementBaseGroup> pages;
    protected List<MOElementButton> pageButtons;
    protected ArrayList<MOElementBase> elements;
    protected List<String> tooltip;
    protected boolean tooltips = true;
    protected String name;
    protected int lastIndex = -1;

    int texW;
    int texH;

    //region Constructors
	public MOGuiBase(MOBaseContainer container)
	{
        this(container,225,186);

	}
    public MOGuiBase(MOBaseContainer container,int width,int height)
    {
        super(container);
        background = new ScaleTexture(new ResourceLocation(Reference.PATH_ELEMENTS + "base_gui_hotbar.png"),92,77);
        background.setOffsets(57, 34, 42, 34);
        this.xSize = width;
        this.ySize = height;
        sidePannel = new SidePannel(this,xSize - 12,33,ySize - 34);
        closeButton = new CloseButton(this,this,xSize - 17,6,"close");
        this.texW = 224;
        this.texH = 176;
        super.width = width;
        super.height = height;

        pages = new ArrayList<>(3);
        pageButtons = new ArrayList<>(3);
        tooltip = new LinkedList();
        elements = new ArrayList();

        registerPages(container);
    }
    //endregion

    @Override
    public void initGui()
    {
        super.initGui();
        this.elements.clear();
        for (ElementBaseGroup page : pages)
        {
            page.init();
            this.addElement(page);
        }

        this.addElement(sidePannel);
        sidePannel.init();
        this.addElement(closeButton);

        for (MOElementButton button : pageButtons)
        {
            sidePannel.addElement(button);
        }

        setPage(currentPage);
    }

    //region Main Draw Methods
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTick) {
        this.updateElementInformation();
        super.drawScreen(mouseX, mouseY, partialTick);
        if(this.tooltips && this.mc.thePlayer.inventory.getItemStack() == null) {
            this.addTooltips(this.tooltip);
            this.drawTooltip(this.tooltip);
        }

        this.mouseX = mouseX - this.guiLeft;
        this.mouseY = mouseY - this.guiTop;
        this.updateElements(partialTick);
    }
    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTick, int x, int y) {

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        //RenderUtils.drawSizeableBackground(guiLeft, guiTop, xSize, ySize, texW, texH, texture, this.zLevel, 57);
        if (background != null) {
            background.render(guiLeft, guiTop, xSize, ySize);
        }

        mouseX = x - guiLeft;
        mouseY = y - guiTop;

        GlStateManager.pushMatrix();
        GL11.glTranslatef(guiLeft, guiTop, 0.0F);
        drawElements(partialTick, false);
        GlStateManager.popMatrix();
    }

    protected void drawElements(float partialTicks, boolean foreground) {
        int i;
        MOElementBase element;
        if(foreground) {
            for(i = 0; i < this.elements.size(); ++i) {
                element = this.elements.get(i);
                if(element.isVisible()) {
                    element.drawForeground(this.mouseX, this.mouseY);
                }
            }
        } else {
            for(i = 0; i < this.elements.size(); ++i) {
                element = this.elements.get(i);
                if(element.isVisible()) {
                    element.drawBackground(this.mouseX, this.mouseY, partialTicks);
                }
            }
        }

    }

    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        this.drawElements(0.0F, true);
    }
    //endregion

    //region Tooltips
    public void addTooltips(List<String> list)
    {
        for (int i = elements.size(); i-- > 0;)
        {
            MOElementBase element = elements.get(i);
            if (element instanceof  MOElementBase && element.isVisible() && element.intersectsWith(mouseX, mouseY))
            {
                element.addTooltip(list,mouseX,mouseY);
            }
        }
    }
    public void drawTooltip(List<String> tooltips) {
        this.drawTooltipHoveringText(tooltips, this.mouseX + this.guiLeft, this.mouseY + this.guiTop, this.fontRendererObj);
        this.tooltip.clear();
    }

    @SuppressWarnings("rawtypes")
    protected void drawTooltipHoveringText(List list, int x, int y, FontRenderer font) {

        if (list == null || list.isEmpty()) {
            return;
        }
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        int k = 0;

        for (Object aList : list)
        {
            String s = (String) aList;
            int l = font.getStringWidth(s);

            if (l > k)
            {
                k = l;
            }
        }
        int i1 = x + 12;
        int j1 = y - 12;
        int k1 = 8;

        if (list.size() > 1) {
            k1 += 2 + (list.size() - 1) * 10;
        }
        if (i1 + k > this.width) {
            i1 -= 28 + k;
        }
        if (j1 + k1 + 6 > this.height) {
            j1 = this.height - k1 - 6;
        }
        this.zLevel = 300.0F;
        itemRender.zLevel = 300.0F;
        int l1 = -267386864;
        this.drawGradientRect(i1 - 3, j1 - 4, i1 + k + 3, j1 - 3, l1, l1);
        this.drawGradientRect(i1 - 3, j1 + k1 + 3, i1 + k + 3, j1 + k1 + 4, l1, l1);
        this.drawGradientRect(i1 - 3, j1 - 3, i1 + k + 3, j1 + k1 + 3, l1, l1);
        this.drawGradientRect(i1 - 4, j1 - 3, i1 - 3, j1 + k1 + 3, l1, l1);
        this.drawGradientRect(i1 + k + 3, j1 - 3, i1 + k + 4, j1 + k1 + 3, l1, l1);
        int i2 = 1347420415;
        int j2 = (i2 & 16711422) >> 1 | i2 & -16777216;
        this.drawGradientRect(i1 - 3, j1 - 3 + 1, i1 - 3 + 1, j1 + k1 + 3 - 1, i2, j2);
        this.drawGradientRect(i1 + k + 2, j1 - 3 + 1, i1 + k + 3, j1 + k1 + 3 - 1, i2, j2);
        this.drawGradientRect(i1 - 3, j1 - 3, i1 + k + 3, j1 - 3 + 1, i2, i2);
        this.drawGradientRect(i1 - 3, j1 + k1 + 2, i1 + k + 3, j1 + k1 + 3, j2, j2);

        for (int k2 = 0; k2 < list.size(); ++k2) {
            String s1 = (String) list.get(k2);
            font.drawStringWithShadow(s1, i1, j1, -1);

            if (k2 == 0) {
                j1 += 2;
            }
            j1 += 10;
        }
        this.zLevel = 0.0F;
        itemRender.zLevel = 0.0F;
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
    }
    //endregion

    //region Mouse and Keyboard
    @Override
    public void handleElementButtonClick(MOElementBase element,String buttonName, int mouseButton)
    {
        for (int i = 0;i < pageButtons.size();i++)
        {
            if (i < pages.size())
            {
                if (pageButtons.get(i).equals(element))
                {
                    setPage(i);
                }
            }
        }
    }

    @Override
    protected void keyTyped(char character, int state) throws IOException
    {
        int elementsCount = this.elements.size();

        MOElementBase element;
        do {
            if(elementsCount-- <= 0) {
                super.keyTyped(character, state);
                return;
            }

            element = this.elements.get(elementsCount);
        } while(!element.isVisible() || !element.isEnabled() || !element.onKeyTyped(character, state));

    }

    @Override
    public void handleMouseInput() throws IOException
    {
        int var1 = Mouse.getEventX() * this.width / this.mc.displayWidth;
        int var2 = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
        this.mouseX = var1 - this.guiLeft;
        this.mouseY = var2 - this.guiTop;
        int var3 = Mouse.getEventDWheel();
        if(var3 != 0) {
            int var4 = this.elements.size();

            while(var4-- > 0) {
                MOElementBase var5 = this.elements.get(var4);
                if(var5.isVisible() && var5.isEnabled() && var5.intersectsWith(this.mouseX, this.mouseY) && var5.onMouseWheel(this.mouseX, this.mouseY, var3)) {
                    return;
                }
            }
        }

        super.handleMouseInput();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        mouseX -= this.guiLeft;
        mouseY -= this.guiTop;
        int var4 = this.elements.size();

        MOElementBase var5;
        do {
            if(var4-- <= 0) {

                mouseX += this.guiLeft;
                mouseY += this.guiTop;
                super.mouseClicked(mouseX, mouseY, mouseButton);
                return;
            }

            var5 = this.elements.get(var4);
        } while(!var5.isVisible() || !var5.isEnabled() || !var5.intersectsWith(mouseX, mouseY) || !var5.onMousePressed(mouseX, mouseY, mouseButton));

    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state)
    {
        mouseX -= this.guiLeft;
        mouseY -= this.guiTop;
        if(state >= 0 && state <= 2) {
            int var4 = this.elements.size();

            while(var4-- > 0) {
                MOElementBase var5 = this.elements.get(var4);
                if(var5.isVisible() && var5.isEnabled()) {
                    var5.onMouseReleased(mouseX, mouseY);
                }
            }
        }

        mouseX += this.guiLeft;
        mouseY += this.guiTop;
        super.mouseReleased(mouseX, mouseY, state);
    }

    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        /*Slot var6 = this.getSlotAtPosition(var1, var2);
        ItemStack var7 = this.mc.thePlayer.inventory.getItemStack();
        if(this.field_147007_t && var6 != null && var7 != null && var6 instanceof SlotFalseCopy) {
            if(this.lastIndex != var6.slotNumber) {
                this.lastIndex = var6.slotNumber;
                this.handleMouseClick(var6, var6.slotNumber, 0, 0);
            }
        } else {


        }
        */
        this.lastIndex = -1;
        super.mouseClickMove(mouseX,mouseY,clickedMouseButton,timeSinceLastClick);
    }
    //endregion

    //region Update Methods
    protected void updateElementInformation()
    {
        for (int i = 0; i < pageButtons.size();i++)
        {
            pageButtons.get(i).setPosition(6,8 + (pageButtons.get(i).getHeight() + 2) * i);
        }

        for (int i = elements.size(); i-- > 0;)
        {
            if (elements.get(i) instanceof MOElementBase)
            {
                elements.get(i).updateInfo();
            }
        }


    }
    protected final void updateElements(float partialTicks) {
        int elementCount = this.elements.size();

        while(elementCount-- > 0) {
            MOElementBase element = this.elements.get(elementCount);
            if(element.isVisible() && element.isEnabled()) {
                element.update(this.mouseX, this.mouseY,partialTicks);
            }
        }

    }
    //endregion

    //region Slot Automation
    protected void AddMainPlayerSlots(Container container,GuiElementList elements)
    {
        AddPlayerSlots(45,ySize - 106,container,elements,true,false,"small",null);
    }

    protected void AddHotbarPlayerSlots(Container container,GuiElementList elements)
    {
        AddPlayerSlots(45,ySize - 27,container,elements,false,true,"small",null);
    }

    protected void AddMainPlayerSlots(Container container,GuiElementList elements,String type,Color color,int x,int y)
    {
        AddPlayerSlots(x,y,container,elements,true,false,type,color);
    }

    protected void AddHotbarPlayerSlots(Container container,GuiElementList elements,String type,Color color,int x,int y)
    {
        AddPlayerSlots(x,y,container,elements,false,true,type,color);
    }

    protected void AddPlayerSlots(int x,int y,Container container,GuiElementList elements,boolean main,boolean hotbar,String type,Color color)
    {
        for (int i = 0;i < container.inventorySlots.size();i++)
        {
            if(container.inventorySlots.get(i) instanceof SlotPlayerInventory)
            {
                SlotPlayerInventory slot = (SlotPlayerInventory) container.inventorySlots.get(i);
                if (main && !slot.isHotbar())
                {
                    ElementInventorySlot inventorySlot = new ElementInventorySlot(this, slot,18,18, type);
                    inventorySlot.setColor(color);
                    inventorySlot.setPosition(x + (18 * (slot.getSlotIndex() % 9)),y + (18 * (slot.getSlotIndex() / 9)));
                    elements.addElement(inventorySlot);
                }
                else if (hotbar && slot.isHotbar())
                {
                    ElementInventorySlot inventorySlot = new ElementInventorySlot(this, slot,18,18, type);
                    inventorySlot.setColor(color);
                    inventorySlot.setPosition(x + (18 * (slot.getSlotIndex() % 9)), y);
                    elements.addElement(inventorySlot);
                }
            }
        }
    }
    //endregion

    //region Draw Helper Methods
    public void drawSizedModalRect(int x1, int y1, int x2, int y2, int color) {
        int var6;
        if(x1 < x2) {
            var6 = x1;
            x1 = x2;
            x2 = var6;
        }

        if(y1 < y2) {
            var6 = y1;
            y1 = y2;
            y2 = var6;
        }

        float var7 = (float)(color >> 24 & 255) / 255.0F;
        float var8 = (float)(color >> 16 & 255) / 255.0F;
        float var9 = (float)(color >> 8 & 255) / 255.0F;
        float var10 = (float)(color & 255) / 255.0F;
        Tessellator var11 = Tessellator.getInstance();
        WorldRenderer worldRenderer = var11.getWorldRenderer();
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GlStateManager.blendFunc(770, 771);
        GlStateManager.color(var8, var9, var10, var7);
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos((double)x1, (double)y2, (double)this.zLevel).endVertex();
        worldRenderer.pos((double)x2, (double)y2, (double)this.zLevel).endVertex();
        worldRenderer.pos((double)x2, (double)y1, (double)this.zLevel).endVertex();
        worldRenderer.pos((double)x1, (double)y1, (double)this.zLevel).endVertex();
        var11.draw();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
    }
    public void drawSizedRect(int x1, int y1, int x2, int y2, int color) {
        int var6;
        if(x1 < x2) {
            var6 = x1;
            x1 = x2;
            x2 = var6;
        }

        if(y1 < y2) {
            var6 = y1;
            y1 = y2;
            y2 = var6;
        }

        float var7 = (float)(color >> 24 & 255) / 255.0F;
        float var8 = (float)(color >> 16 & 255) / 255.0F;
        float var9 = (float)(color >> 8 & 255) / 255.0F;
        float var10 = (float)(color & 255) / 255.0F;
        Tessellator var11 = Tessellator.getInstance();
        WorldRenderer worldRenderer = var11.getWorldRenderer();
        GL11.glDisable(3553);
        GlStateManager.color(var8, var9, var10, var7);
        worldRenderer.begin(7,DefaultVertexFormats.POSITION);
        worldRenderer.pos((double)x1, (double)y2, (double)this.zLevel).endVertex();
        worldRenderer.pos((double)x2, (double)y2, (double)this.zLevel).endVertex();
        worldRenderer.pos((double)x2, (double)y1, (double)this.zLevel).endVertex();
        worldRenderer.pos((double)x1, (double)y1, (double)this.zLevel).endVertex();
        var11.draw();
        GL11.glEnable(3553);
    }
    public void drawSizedTexturedModalRect(int x, int y, int u, int v, int width, int height, float texW, float texH) {
        float var9 = 1.0F / texW;
        float var10 = 1.0F / texH;
        Tessellator var11 = Tessellator.getInstance();
        WorldRenderer wr = var11.getWorldRenderer();
        wr.begin(7,DefaultVertexFormats.POSITION_TEX);
        wr.pos((double)(x), (double)(y + height), (double)this.zLevel).tex( (double)((float)(u) * var9), (double)((float)(v + height) * var10)).endVertex();
        wr.pos((double)(x + width), (double)(y + height), (double)this.zLevel).tex((double)((float)(u + width) * var9), (double)((float)(v + height) * var10)).endVertex();
        wr.pos((double)(x + width), (double)(y), (double)this.zLevel).tex((double)((float)(u + width) * var9), (double)((float)(v) * var10)).endVertex();
        wr.pos((double)(x), (double)(y), (double)this.zLevel).tex((double)((float)(u) * var9), (double)((float)(v) * var10)).endVertex();
        var11.draw();
    }
    //endregion

    //region Handlers and Events
    public void onPageChange(int newPage) {}
    public void handleListChange(String listName, int mouseButton,int element) {}
    public void registerPages(MOBaseContainer container) {}
    //endregion

    //region Getters and Setters
    public void setPage(int page)
    {
        page = MathHelper.clamp_int(page, 0, pages.size() - 1);
        if (currentPage != page) {
            onPageChange(page);
        }
        currentPage = page;


        for (int i = 0; i < pages.size(); i++) {
            pages.get(i).setVisible(i == currentPage);

            if (i < pageButtons.size()) {
                pageButtons.get(i).setEnabled(i != currentPage);
            }
        }
    }
    protected MOElementBase getElementAtPosition(int mX, int mY) {

        for (int i = elements.size(); i-- > 0;)
        {
            MOElementBase element = elements.get(i);
            if (element.isVisible() && element.intersectsWith(mX, mY))
            {
                return element;
            }
        }
        return null;
    }
    public void setTooltip(List<String> tooltip)
    {
        this.tooltip = tooltip;
    }
    public void addToTooltip(String string){this.tooltip.add(string);}
    public List<String> getTooltips(){return tooltip;}
    public MOElementIconButton AddPage(ElementBaseGroup page,HoloIcon icon,String tooltip)
    {
        pages.add(page);
        MOElementIconButton button = new MOElementIconButton(this,this,0,0,page.getName(),0,0,24,0,24,0,24,24,"",icon);
        button.setTexture(Reference.PATH_ELEMENTS + "page_button.png", 48, 24);
        button.setToolTip(tooltip);
        pageButtons.add(button);
        return button;
    }
    public MOBaseContainer getContainer()
    {
        return (MOBaseContainer)inventorySlots;
    }
    public MOElementBase addElement(MOElementBase element) {
        this.elements.add(element);
        return element;
    }
    public Slot getSlotAtPosition(int mouseX, int mouseY) {
        for(int var3 = 0; var3 < this.inventorySlots.inventorySlots.size(); ++var3) {
            Slot var4 = this.inventorySlots.inventorySlots.get(var3);
            if(this.isMouseOverSlot(var4, mouseX, mouseY)) {
                return var4;
            }
        }

        return null;
    }
    private boolean isMouseOverSlot(Slot slotIn, int mouseX, int mouseY)
    {
        return this.isPointInRegion(slotIn.xDisplayPosition, slotIn.yDisplayPosition, 16, 16, mouseX, mouseY);
    }
    public FontRenderer getFontRenderer() {
        return this.fontRendererObj;
    }
    public int getGuiLeft() {
        return this.guiLeft;
    }
    public int getGuiTop() {
        return this.guiTop;
    }
    public int getMouseX() {
        return this.mouseX;
    }
    public int getMouseY() {
        return this.mouseY;
    }
    //endregion

    public static void playSound(String soundName, float volume, float pitch) {
        Minecraft.getMinecraft().thePlayer.playSound(soundName,volume, pitch);
    }

    public void bindTexture(ResourceLocation location) {
        this.mc.renderEngine.bindTexture(location);
    }

    public void overlayRecipe() {
    }
}
