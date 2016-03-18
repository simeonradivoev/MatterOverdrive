package matteroverdrive.items;

import matteroverdrive.MatterOverdrive;
import net.minecraft.item.ItemRecord;
import net.minecraft.util.ResourceLocation;

/**
 * @author shadowfacts
 */
public class ItemRecordTransformation extends ItemRecord {

	public ItemRecordTransformation() {
		super("mo.transformation");
		setUnlocalizedName("record");
		setRegistryName("recordTransformation");
		setCreativeTab(MatterOverdrive.tabMatterOverdrive);
	}

	@Override
	public ResourceLocation getRecordResource(String name) {
		return new ResourceLocation("mo", "transformation_music");
	}

}
