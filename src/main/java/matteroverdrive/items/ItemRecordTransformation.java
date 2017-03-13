package matteroverdrive.items;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.init.MatterOverdriveSounds;
import net.minecraft.item.ItemRecord;
import net.minecraft.util.ResourceLocation;
import net.shadowfacts.shadowmc.ShadowMC;
import net.shadowfacts.shadowmc.item.ItemModelProvider;

/**
 * @author shadowfacts
 */
public class ItemRecordTransformation extends ItemRecord implements ItemModelProvider
{

	private static final ResourceLocation SOUND = new ResourceLocation(Reference.MOD_ID, "transformation_music");

	public ItemRecordTransformation()
	{
		super("mo.transformation", MatterOverdriveSounds.musicTransformation);
		setUnlocalizedName("record");
		setRegistryName("record_transformation");
		setCreativeTab(MatterOverdrive.tabMatterOverdrive);
	}

	@Override
	public ResourceLocation getRecordResource(String name)
	{
		return SOUND;
	}

	@Override
	public void initItemModel()
	{
		ShadowMC.proxy.registerItemModel(this, 0, getRegistryName());
	}

}
