package matteroverdrive.items.tools;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.init.MatterOverdriveItems;
import net.minecraft.item.ItemSpade;
import net.minecraft.util.ResourceLocation;
import net.shadowfacts.shadowmc.item.ItemModelProvider;

/**
 * @author shadowfacts
 */
public class TritaniumShovel extends ItemSpade implements ItemModelProvider
{

	public TritaniumShovel(String name)
	{
		super(MatterOverdriveItems.TOOL_MATERIAL_TRITANIUM);
		setUnlocalizedName(name);
		setRegistryName(new ResourceLocation(Reference.MOD_ID, name));
	}

	@Override
	public void initItemModel()
	{
		MatterOverdrive.proxy.registerItemModel(this, 0, getRegistryName().getResourcePath());
	}

}
