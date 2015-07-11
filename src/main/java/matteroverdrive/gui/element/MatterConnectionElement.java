package matteroverdrive.gui.element;

import cofh.lib.gui.GuiBase;
import cofh.lib.render.RenderHelper;
import matteroverdrive.Reference;
import matteroverdrive.util.RenderUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;

import java.util.List;

/**
 * Created by Simeon on 3/14/2015.
 */
public class MatterConnectionElement extends MOElementBase
{
    public static final ResourceLocation texture = new ResourceLocation(Reference.PATH_ELEMENTS + "side_slot_bg.png");

    int id;
    int count;

    @Override
    public void addTooltip(List<String> list)
    {
        list.add(StatCollector.translateToLocal(Item.getItemById(id).getUnlocalizedName() + ".name") + " [" + count + "]");
    }

    public MatterConnectionElement(GuiBase gui,int id,int count)
    {
        this(gui, 22, 22, id, count);
    }

    public MatterConnectionElement(GuiBase gui, int width, int height,int id,int count) {
        super(gui,0,0, width, height);

        this.id = id;
        this.count = count;
    }

    @Override
    public void drawBackground(int mouseX, int mouseY, float gameTicks)
    {
        GL11.glColor3f(1, 1, 1);
        RenderHelper.bindTexture(texture);
        gui.drawSizedTexturedModalRect(posX, posY, 0, 0, 22, 22, 22, 22);
    }

    @Override
    public void drawForeground(int mouseX, int mouseY)
    {
        RenderUtils.renderStack(posX + 2, posY + 2, new ItemStack(Item.getItemById(id)));
        gui.getFontRenderer().drawStringWithShadow(Integer.toString(count),posX + 8,posY + 24, 0xFFFFFF);
    }
}
