package matteroverdrive.gui.element;

import cofh.lib.gui.GuiBase;
import cofh.lib.render.RenderHelper;
import cofh.lib.util.helpers.MathHelper;
import matteroverdrive.Reference;
import matteroverdrive.api.matter.IMatterStorage;
import matteroverdrive.util.MatterHelper;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;

import java.util.List;


public class ElementMatterStored extends MOElementBase
{
	public static final ResourceLocation DEFAULT_TEXTURE = new ResourceLocation(Reference.PATH_ELEMENTS + "Matter.png");
	public static final int DEFAULT_SCALE = 42;
    private int drain = 0;

	protected IMatterStorage storage;

	// If this is enabled, 1 pixel of energy will always show in the bar as long as it is non-zero.
	protected boolean alwaysShowMinimum = false;

	public ElementMatterStored(GuiBase gui, int posX, int posY, IMatterStorage storage) {

		super(gui, posX, posY);
		this.storage = storage;

		this.texture = DEFAULT_TEXTURE;
		this.sizeX = 16;
		this.sizeY = DEFAULT_SCALE;

		this.texW = 32;
		this.texH = 64;
	}

	public ElementMatterStored setAlwaysShow(boolean show) {

		alwaysShowMinimum = show;
		return this;
	}

	@Override
	public void drawBackground(int mouseX, int mouseY, float gameTicks) {

		int amount = getScaled();

		RenderHelper.bindTexture(texture);
		drawTexturedModalRect(posX, posY, 0, 0, sizeX, sizeY);
		drawTexturedModalRect(posX, posY + DEFAULT_SCALE - amount, 16, DEFAULT_SCALE - amount, sizeX, amount);
	}

	@Override
	public void drawForeground(int mouseX, int mouseY) {

	}

	@Override
	public void addTooltip(List<String> list) {

		if (storage.getCapacity() < 0)
        {
			list.add("Infinite " + MatterHelper.MATTER_UNIT);
		} else
        {
			list.add(storage.getMatterStored() + " / " + storage.getCapacity() + MatterHelper.MATTER_UNIT);
		}

        if(drain > 0)
        {
            list.add(EnumChatFormatting.GREEN + "+" +  MatterHelper.formatMatter(drain));
        }
        else if(drain < 0)
        {
            list.add(EnumChatFormatting.RED +  MatterHelper.formatMatter(drain));
        }
	}

	protected int getScaled() {

		if (storage.getCapacity() <= 0)
		{
			return sizeY;
		}
		long fraction = (long) storage.getMatterStored() * sizeY / storage.getCapacity();

		return alwaysShowMinimum && storage.getMatterStored() > 0 ? Math.max(1, MathHelper.round(fraction)) : MathHelper.round(fraction);
	}

    public void setDrain(int amount)
    {
        this.drain = amount;
    }
}
