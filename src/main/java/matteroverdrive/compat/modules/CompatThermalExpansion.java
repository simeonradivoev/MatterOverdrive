package matteroverdrive.compat.modules;

import cofh.api.modhelpers.ThermalExpansionHelper;
import matteroverdrive.compat.Compat;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

import static matteroverdrive.init.MatterOverdriveBlocks.dilithium_ore;
import static matteroverdrive.init.MatterOverdriveBlocks.tritaniumOre;
import static matteroverdrive.init.MatterOverdriveItems.dilithium_ctystal;
import static matteroverdrive.init.MatterOverdriveItems.tritanium_dust;
import static matteroverdrive.init.MatterOverdriveItems.tritanium_ingot;
import static matteroverdrive.init.MatterOverdriveItems.tritanium_plate;

/**
 * Compatibility for Thermal Expansion
 *
 * @author shadowfacts
 */
@Compat("ThermalExpansion")
public class CompatThermalExpansion
{

	@Compat.Init
	public static void init(FMLInitializationEvent event)
	{
//		1 Tritanium Ore		->	2 Tritanium Dust
		ThermalExpansionHelper.addPulverizerRecipe(8000,
				new ItemStack(tritaniumOre),
				new ItemStack(tritanium_dust, 2));

//		1 Tritanium Ingot	->	1 Tritanium Dust
		ThermalExpansionHelper.addPulverizerRecipe(4000,
				new ItemStack(tritanium_ingot),
				new ItemStack(tritanium_dust));

//		1 Tritanium Plate	-> 3 Tritanium Dust
		ThermalExpansionHelper.addPulverizerRecipe(12000,
				new ItemStack(tritanium_plate),
				new ItemStack(tritanium_dust, 3));

//		1 Dilithum Ore		-> 1 Dilithium Crystal
		ThermalExpansionHelper.addPulverizerRecipe(4000,
				new ItemStack(dilithium_ore),
				new ItemStack(dilithium_ctystal));
	}

}
