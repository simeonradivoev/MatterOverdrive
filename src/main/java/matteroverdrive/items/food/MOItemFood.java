package matteroverdrive.items.food;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import net.minecraft.item.ItemFood;
import net.minecraft.util.ResourceLocation;
import net.shadowfacts.shadowmc.item.ItemModelProvider;

/**
 * @author shadowfacts
 */
public class MOItemFood extends ItemFood implements ItemModelProvider
{

	private String name;

	public MOItemFood(String name, int amount, float saturation, boolean isWolfFood)
	{
		super(amount, saturation, isWolfFood);

		setUnlocalizedName(name);
		setRegistryName(new ResourceLocation(Reference.MOD_ID, name));

		setCreativeTab(MatterOverdrive.tabMatterOverdrive_food);
	}

	@Override
	public void initItemModel()
	{
		MatterOverdrive.proxy.registerItemModel(this, 0, name);
	}

}
