package matteroverdrive.items.weapon.module;

import matteroverdrive.Reference;
import matteroverdrive.api.weapon.IWeaponModule;
import matteroverdrive.items.includes.MOBaseItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * Created by Simeon on 2/16/2016.
 */
public class WeaponModuleRicochet extends MOBaseItem implements IWeaponModule
{
	public WeaponModuleRicochet(String name)
	{
		super(name);
	}

	@Override
	public int getSlot(ItemStack module)
	{
		return Reference.MODULE_OTHER;
	}

	@Override
	public String getModelPath()
	{
		return null;
	}

	@Override
	public ResourceLocation getModelTexture(ItemStack module)
	{
		return null;
	}

	@Override
	public String getModelName(ItemStack module)
	{
		return null;
	}

	@Override
	public float modifyWeaponStat(int statID, ItemStack module, ItemStack weapon, float originalStat)
	{
		if (statID == Reference.WS_RICOCHET)
		{
			return 1;
		}
		return originalStat;
	}
}
