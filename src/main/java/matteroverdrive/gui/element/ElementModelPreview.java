package matteroverdrive.gui.element;

import cofh.lib.gui.GuiBase;
import matteroverdrive.util.RenderUtils;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

/**
 * Created by Simeon on 4/13/2015.
 */
public class ElementModelPreview extends MOElementBase
{
    IItemRenderer renderer;
    ItemStack itemStack;

    public ElementModelPreview(GuiBase gui, int posX, int posY) {
        super(gui, posX, posY);
    }

    public ElementModelPreview(GuiBase gui, int posX, int posY, int width, int height) {
        super(gui, posX, posY, width, height);
    }

    @Override
    public void drawBackground(int mouseX, int mouseY, float gameTicks) {

    }

    @Override
    public void drawForeground(int mouseX, int mouseY)
    {
        if (renderer != null && itemStack != null)
        {
            GL11.glPushMatrix();
            Transform();
            RenderUtils.enable3DRender();
            renderer.renderItem(IItemRenderer.ItemRenderType.INVENTORY, itemStack);
            RenderUtils.enable2DRender();
            GL11.glPopMatrix();
        }
    }

    public void Transform()
    {
        GL11.glTranslatef(posX, posY, 80);
        GL11.glScaled(60, 60, 60);
        GL11.glRotatef(-90, 0, 1, 0);
        GL11.glRotatef(210, 0, 0, 1);
        GL11.glRotatef(-25, 0, 1, 0);
    }

    public IItemRenderer getRenderer() {
        return renderer;
    }

    public void setRenderer(IItemRenderer renderer) {
        this.renderer = renderer;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }
}
