package com.MO.MatterOverdrive.compat.modules;

import com.MO.MatterOverdrive.compat.Compat;
import com.MO.MatterOverdrive.init.MatterOverdriveItems;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import crazypants.enderio.api.IMC;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;

/**
 * @author shadowfacts
 */
@Compat("EnderIO")
public class CompatEnderIO {

	private static StringBuilder sb = new StringBuilder();

	private static ArrayList<String> recipes = new ArrayList<String>();

	@Compat.Init
	public static void init(FMLInitializationEvent event) {
		addAllRecipes();

		sb.append("<SAGMillRecipes>");
		sb.append("<recipeGroup name=\"MatterOverdrive\">");
		for (String s : recipes) {
			sb.append(s);
		}
		sb.append("</recipeGroup>");
		sb.append("</SAGMillRecipes>");

		FMLInterModComms.sendMessage("EnderIO", IMC.SAG_RECIPE, sb.toString());
		System.out.println(new ItemStack(MatterOverdriveItems.dilithium_ctystal).getUnlocalizedName());
	}

	private static void addAllRecipes() {
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

}
