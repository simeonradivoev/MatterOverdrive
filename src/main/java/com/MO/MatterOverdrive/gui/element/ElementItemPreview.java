package com.MO.MatterOverdrive.gui.element;

import cofh.lib.gui.GuiBase;
import cofh.lib.gui.element.ElementBase;
import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.util.RenderUtils;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

/**
 * Created by Simeon on 3/26/2015.
 */
public class ElementItemPreview extends ElementBase
{
    ItemStack itemStack;
    float itemSize = 2;

    public ElementItemPreview(GuiBase gui, int posX, int posY,ItemStack itemStack)
    {
        super(gui, posX, posY);
        this.sizeX = 40;
        this.sizeY = 48;
        this.setTexture(Reference.PATH_ELEMENTS + "item_preview_bg.png",40,48);
        this.itemStack = itemStack;
    }

    public void setItemSize(float itemSize)
    {
        this.itemSize = itemSize;
    }

    public void setItemStack(ItemStack itemStack)
    {
        this.itemStack = itemStack;
    }

    @Override
    public void drawBackground(int mouseX, int mouseY, float gameTicks)
    {
        GL11.glPushMatrix();
        GL11.glTranslatef(this.posX, this.posY, 0);
        gui.bindTexture(this.texture);
        GL11.glColor3f(1, 1, 1);
        this.drawTexturedModalRect(0,0,0,0,this.sizeX,this.sizeY);
        GL11.glPopMatrix();
    }

    @Override
    public void drawForeground(int mouseX, int mouseY)
    {
        if(itemStack != null)
        {
            GL11.glPushMatrix();
            GL11.glTranslatef(this.posX + 3,this.posY + 7,0);
            GL11.glScaled(itemSize,itemSize,itemSize);
            RenderUtils.renderStack(0, 0, itemStack);
            GL11.glPopMatrix();
        }
    }
}
