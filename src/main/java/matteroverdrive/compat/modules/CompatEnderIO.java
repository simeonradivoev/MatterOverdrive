package matteroverdrive.compat.modules;

import matteroverdrive.compat.Compat;
import matteroverdrive.init.MatterOverdriveItems;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;

import java.util.ArrayList;

/**
 * Compatibility for Ender IO
 *
 * @author shadowfacts
 */
@Compat("EnderIO")
public class CompatEnderIO
{

	private static final StringBuilder sb = new StringBuilder();
	private static final ArrayList<String> recipes = new ArrayList<>();

	@Compat.Init
	public static void init(FMLInitializationEvent event)
	{
		addAllRecipes();

		sb.append("<SAGMillRecipes>");
		sb.append("<recipeGroup name=\"MatterOverdrive\">");
		for (String s : recipes)
		{
			sb.append(s);
		}
		sb.append("</recipeGroup>");
		sb.append("</SAGMillRecipes>");

		FMLInterModComms.sendMessage("EnderIO", IMC.SAG_RECIPE, sb.toString());
		System.out.println(new ItemStack(MatterOverdriveItems.dilithium_ctystal).getUnlocalizedName());
	}

	private static void addAllRecipes()
	{
//		1 Dilithium Ore		->	1 Dilithium Crystal
		recipes.add("<recipe name=\"DilithiumOre\" energyCost=\"2000\">" +
				"	<input>" +
				"		<itemStack modID=\"mo\" itemName=\"dilithium_ore\" />" +
				"	</input>" +
				"	<output>" +
				"		<itemStack modID=\"mo\" itemName=\"dilithium_crystal\" />" +
				"	</output>" +
				"</recipe>");

//		1 Tritanium Ore		->	2 Tritanium Dust
		recipes.add("<recipe name=\"TritaniumOre\" energyCost=\"2000\">" +
				"	<input>" +
				"		<itemStack modID=\"mo\" itemName=\"tritanium_ore\" />" +
				"	</input>" +
				"	<output>" +
				"		<itemStack modID=\"mo\" itemName=\"tritanium_dust\" number=\"2\" />" +
				"	</output>" +
				"</recipe>");

//		1 Tritanium Ingot	->	1 Tritanium Dust
		recipes.add("<recipe name=\"TritaniumIngot\" energyCost=\"1000\">" +
				"	<input>" +
				"		<itemStack modID=\"mo\" itemName=\"tritanium_ingot\" />" +
				"	</input>" +
				"	<output>" +
				"		<itemStack modID=\"mo\" itemName=\"tritanium_dust\" />" +
				"	</output>" +
				"</recipe>");

//		1 Tritanium Plate	->	3 Tritanium Dust
		recipes.add("<recipe name=\"TritaniumPlate\" energyCost=\"3000\">" +
				"	<input>" +
				"		<itemStack modID=\"mo\" itemName=\"tritanium_plate\" />" +
				"	</input>" +
				"	<output>" +
				"		<itemStack modID=\"mo\" itemName=\"tritanium_dust\" number=\"3\" />" +
				"	</output>" +
				"</recipe>");
	}

	/**
	 * EnderIO IMC message names
	 */
	private class IMC
	{
		public static final String VAT_RECIPE = "recipe:vat";
		public static final String SAG_RECIPE = "recipe:sagmill";
		public static final String ALLOY_RECIPE = "recipe:alloysmelter";
		public static final String ENCHANTER_RECIPE = "recipe:enchanter";
		public static final String SLINE_N_SPLICE_RECIPE = "recipe:slicensplice";
		public static final String SOUL_BINDER_RECIPE = "recipe:soulbinder";
		public static final String PAINTER_WHITELIST_ADD = "painter:whitelist:add";
		public static final String PAINTER_BLACKLIST_ADD = "painter:blacklist:add";
		public static final String POWERED_SPAWNER_BLACKLIST_ADD = "poweredSpawner:blacklist:add";
		public static final String POWERED_SPAWNER_COST_MULTIPLIER = "poweredSpawner:costMultiplier";
		public static final String SOUL_VIAL_BLACKLIST = "soulVial:blacklist:add";
		public static final String FLUID_FUEL_ADD = "fluidFuel:add";
		public static final String FLUID_COOLANT_ADD = "fluidCoolant:add";
		public static final String TELEPORT_BLACKLIST_ADD = "teleport:blacklist:add";
		public static final String REDSTONE_CONNECTABLE_ADD = "redstone:connectable:add";
	}

}
