package matteroverdrive.compat.modules.top;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import mcjty.theoneprobe.api.IElement;
import mcjty.theoneprobe.api.IItemStyle;
import mcjty.theoneprobe.apiimpl.client.ElementItemStackRender;
import mcjty.theoneprobe.apiimpl.client.ElementTextRender;
import mcjty.theoneprobe.apiimpl.styles.ItemStyle;
import mcjty.theoneprobe.network.NetworkTools;
import net.minecraft.item.ItemStack;

/**
 * @author shadowfacts
 */
@AllArgsConstructor
public class StackTitleElement implements IElement
{
	public static int ID;

	private static final IItemStyle style = new ItemStyle().width(16).height(16);
	private ItemStack stack;

	public StackTitleElement(ByteBuf buf)
	{
		this(NetworkTools.readItemStack(buf));
	}

	@Override
	public void render(int x, int y)
	{
		ElementItemStackRender.render(stack, style, x, y);
		ElementTextRender.render(stack.getDisplayName(), x + style.getWidth() + 5, y + (style.getHeight() / 2) - 5);
	}

	@Override
	public int getWidth()
	{
		return style.getWidth() + 4 + ElementTextRender.getWidth(stack.getDisplayName());
	}

	@Override
	public int getHeight()
	{
		return style.getHeight();
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		NetworkTools.writeItemStack(buf, stack);
	}

	@Override
	public int getID()
	{
		return ID;
	}
}
