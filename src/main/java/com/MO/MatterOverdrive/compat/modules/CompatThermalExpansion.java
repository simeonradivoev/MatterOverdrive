package com.MO.MatterOverdrive.compat.modules;

import cofh.api.modhelpers.ThermalExpansionHelper;
import com.MO.MatterOverdrive.compat.Compat;
import static com.MO.MatterOverdrive.init.MatterOverdriveBlocks.*;
import static com.MO.MatterOverdrive.init.MatterOverdriveItems.*;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import net.minecraft.item.ItemStack;

/**
 * Compatibility for Thermal Expansion
 *
 * @author shadowfacts
 */
@Compat("ThermalExpansion")
public class CompatThermalExpansion {

	@Compat.Init
	public static void init(FMLInitializationEvent event) {
//		1 Tritanium Ore		->	2 Tritanium Dust
		ThermalExpansionHelper.addPulverizerRecipe(800,
				new ItemStack(tritaniumOre),
				new ItemStack(tritanium_dust, 2));

//		1 Tritanium Ingot	->	1 Tritanium Dust
		ThermalExpansionHelper.addPulverizerRecipe(4000,
				new ItemStack(tritanium_ingot),
				new ItemStack(tritanium_dust));
	}

}
