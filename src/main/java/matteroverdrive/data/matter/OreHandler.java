package matteroverdrive.data.matter;

import net.minecraft.item.ItemStack;

/**
 * Created by Simeon on 1/17/2016.
 */
public class OreHandler extends MatterEntryHandlerAbstract<ItemStack>
{
	private final int matter;
	private final boolean isFinalHandler;

	public OreHandler(int matter)
	{
		this.matter = matter;
		this.isFinalHandler = false;
	}

	public OreHandler(int matter, boolean isFinalHandler)
	{
		this.matter = matter;
		this.isFinalHandler = isFinalHandler;
	}

	public OreHandler(int matter, boolean isFinalHandler, int priority)
	{
		this.matter = matter;
		this.isFinalHandler = isFinalHandler;
		this.priority = priority;
	}

	@Override
	public int modifyMatter(ItemStack itemStack, int originalMatter)
	{
		return matter;
	}

	@Override
	public boolean finalModification(ItemStack itemStack)
	{
		return isFinalHandler;
	}
}
