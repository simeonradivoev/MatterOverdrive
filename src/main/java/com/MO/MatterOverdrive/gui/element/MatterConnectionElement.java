package com.MO.MatterOverdrive.gui.element;

import cofh.lib.gui.GuiColor;
import cofh.lib.render.RenderHelper;
import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.api.matter.IMatterNetworkConnection;
import com.MO.MatterOverdrive.util.RenderUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * Created by Simeon on 3/14/2015.
 */
public class MatterConnectionElement implements IGridElement
{
    public static final ResourceLocation texture = new ResourceLocation(Reference.PATH_ELEMENTS + "side_slot_bg.png");

    int id;
    int count;

    public  MatterConnectionElement(int id,int count)
    {
        this.id = id;
        this.count = count;
    }

    @Override
    public int getHeight() {
        return 32;
    }

    @Override
    public int getWidth() {
        return 32;
    }

    @Override
    public Object getValue() {
        return null;
    }

    @Override
    public void draw(ElementGrid listBox, int x, int y, int backColor, int textColor)
    {
        GL11.glColor3f(1,1,1);
        RenderHelper.bindTexture(texture);
        listBox.drawTexturedModalRect(0, 0, 0, 0, 22, 22, 22, 22);
        RenderUtils.renderStack(2,2,new ItemStack(Item.getItemById(id)));
        listBox.getFontRenderer().drawStringWithShadow(Integer.toString(count),10, 22, 0xFFFFFF);
    }
}
