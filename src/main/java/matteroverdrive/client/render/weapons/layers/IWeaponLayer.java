package matteroverdrive.client.render.weapons.layers;

import matteroverdrive.client.resources.data.WeaponMetadataSection;
import net.minecraft.item.ItemStack;

/**
 * Created by Simeon on 2/17/2016.
 */
public interface IWeaponLayer
{
	void renderLayer(WeaponMetadataSection weaponMeta, ItemStack weapon, float ticks, int pass);
}
