package matteroverdrive.gui.element;

import cofh.lib.gui.GuiBase;
import matteroverdrive.Reference;
import matteroverdrive.data.ScaleTexture;
import matteroverdrive.util.RenderUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * Created by Simeon on 3/26/2015.
 */
public class ElementItemPreview extends MOElementBase
{
    ScaleTexture background = new ScaleTexture(new ResourceLocation(Reference.PATH_ELEMENTS + "item_preview_bg.png"),40,48).setOffsets(22,22,18,18);
    ItemStack itemStack;
    float itemSize = 2;

    public ElementItemPreview(GuiBase gui, int posX, int posY,ItemStack itemStack)
    {
        super(gui, posX, posY);
        this.sizeX = 47;
        this.sizeY = 47;
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
        background.Render(posX,posY,sizeX,sizeY);
    }

    @Override
    public void drawForeground(int mouseX, int mouseY)
    {
        if(itemStack != null)
        {
            GL11.glPushMatrix();
            GL11.glTranslatef(this.posX + 7,this.posY + 7,0);
            GL11.glScaled(itemSize,itemSize,itemSize);
            RenderUtils.renderStack(0, 0, itemStack);
            GL11.glPopMatrix();
        }
    }
}
