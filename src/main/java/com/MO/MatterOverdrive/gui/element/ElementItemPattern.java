package com.MO.MatterOverdrive.gui.element;

import cofh.lib.gui.GuiBase;
import com.MO.MatterOverdrive.data.ScaleTexture;
import com.MO.MatterOverdrive.util.MatterDatabaseHelper;
import com.MO.MatterOverdrive.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.lwjgl.opengl.GL11;

import java.util.List;

/**
 * Created by Simeon on 4/27/2015.
 */
public class ElementItemPattern extends ElementSlot
{
    ScaleTexture texture;
    NBTTagCompound tagCompound;
    int amount = 0;

    public ElementItemPattern(GuiBase gui,NBTTagCompound tagCompound,String bgType,int width,int height)
    {
        super(gui, 0, 0, width, height, bgType);
        this.texture = new ScaleTexture(getTexture(bgType),width,height).setOffsets(2,2,2,2);
        this.tagCompound = tagCompound;
        this.name = MatterDatabaseHelper.GetItemStackFromNBT(tagCompound).getDisplayName();
    }

    @Override
    public void drawForeground(int mouseX, int mouseY)
    {
        if (tagCompound != null)
        {
            ItemStack itemStack = ItemStack.loadItemStackFromNBT(tagCompound);
            RenderUtils.renderStack(posX + 3, posY + 3, itemStack);

            if (amount > 0) {
                GL11.glPushMatrix();
                GL11.glTranslatef(0, 0, 100);
                gui.drawCenteredString(getFontRenderer(), Integer.toString(amount), posX + 17, posY + 12, 0xFFFFFF);
                GL11.glPopMatrix();
            }
            RenderHelper.enableGUIStandardItemLighting();
        }
    }

    @Override
    public void drawBackground(int mouseX, int mouseY, float gameTicks)
    {
        texture.Render(posX, posY, sizeX, sizeY);
    }

    @Override
    public void addTooltip(List<String> list)
    {
        if (tagCompound != null) {
            list.addAll(ItemStack.loadItemStackFromNBT(tagCompound).getTooltip(Minecraft.getMinecraft().thePlayer, false));
            String name = list.get(0);
            int progress = MatterDatabaseHelper.GetProgressFromNBT(tagCompound);
            name = MatterDatabaseHelper.getPatternInfoColor(progress) + name + " [" + progress + "%]";
            list.set(0, name);
        }
    }

    public NBTTagCompound getTagCompound()
    {
        return tagCompound;
    }

    public void setTagCompound(NBTTagCompound tagCompound)
    {
        this.tagCompound = tagCompound;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount)
    {
        this.amount = amount;
    }

    public ScaleTexture getTexture()
    {
        return texture;
    }
}
