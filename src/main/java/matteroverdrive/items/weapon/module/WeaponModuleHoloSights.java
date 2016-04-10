package matteroverdrive.items.weapon.module;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.api.weapon.IWeaponScope;
import matteroverdrive.items.includes.MOBaseItem;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * Created by Simeon on 2/18/2016.
 */
public class WeaponModuleHoloSights extends MOBaseItem implements IWeaponScope
{
	public WeaponModuleHoloSights(String name)
	{
		super(name);
		setCreativeTab(MatterOverdrive.tabMatterOverdrive_modules);
		setHasSubtypes(true);
		this.setMaxStackSize(1);
	}

	@SideOnly(Side.CLIENT)
	public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems)
	{
		for (int i = 0; i < 3; i++)
		{
			subItems.add(new ItemStack(itemIn, 1, i));
		}
	}

	@Override
	public float getZoomAmount(ItemStack scopeStack, ItemStack weaponStack)
	{
		return 0.3f;
	}

	@Override
	public float getAccuracyModify(ItemStack scopeStack, ItemStack weaponStack, boolean zoomed, float originalAccuracy)
	{
		if (zoomed)
		{
			return originalAccuracy * 0.6f;
		}
		return originalAccuracy * 0.8f;
	}

	@Override
	public int getSlot(ItemStack module)
	{
		return Reference.MODULE_SIGHTS;
	}

	@Override
	public String getModelPath()
	{
		return null;
	}

	@Override
	public ResourceLocation getModelTexture(ItemStack module)
	{
		return new ResourceLocation(Reference.PATH_ELEMENTS + String.format("holo_sight_%s.png", module.getItemDamage()));
	}

	@Override
	public String getModelName(ItemStack module)
	{
		return null;
	}

	@Override
	public float modifyWeaponStat(int statID, ItemStack module, ItemStack weapon, float originalStat)
	{
		return originalStat;
	}
}
