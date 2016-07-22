package matteroverdrive.compat.modules;

import matteroverdrive.compat.Compat;
import matteroverdrive.init.MatterOverdriveBlocks;
import matteroverdrive.init.MatterOverdriveItems;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

/**
 * @author shadowfacts
 */
@Compat(CompatEnderIO.ID)
public class CompatEnderIO
{

	public static final String ID = "EnderIO";

	/**
	 * EnderIO IMC keys
	 */
	public final class IMC {

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

	private static StringBuilder sb = new StringBuilder();

	@Compat.Init
	public static void init(FMLInitializationEvent event)
	{
//		SAG Mill recipes
		sb.append("<SAGMillRecipes>");
		sb.append("<recipeGroup name=\"MatterOverdrive\">");

		addSagMillRecipe("DilithiumOre", 3000, MatterOverdriveBlocks.dilithium_ore, MatterOverdriveItems.dilithium_ctystal, 1, MatterOverdriveItems.dilithium_ctystal, 0.25f);
		addSagMillRecipe("TritaniumOre", 3600, MatterOverdriveBlocks.tritaniumOre, new ItemStack(MatterOverdriveItems.tritanium_dust, 2), 1, Blocks.COBBLESTONE, 0.15f);
		addSagMillRecipe("TirtaniumIngot", 1000, MatterOverdriveItems.tritanium_ingot, MatterOverdriveItems.tritanium_dust, 1);
		addSagMillRecipe("TritaniumPlate", 3000, MatterOverdriveItems.tritanium_plate, new ItemStack(MatterOverdriveItems.tritanium_dust, 3), 1);

		sb.append("</recipeGroup>");
		sb.append("</SAGMillRecipes>");

		FMLInterModComms.sendMessage(ID, IMC.SAG_RECIPE, sb.toString());

//		Powered Spawner cost multipliers
		NBTTagCompound tag1 = new NBTTagCompound();
		tag1.setString("entityName", "rogue_android");
		tag1.setInteger("costMultiplier", 2);
		FMLInterModComms.sendMessage(ID, IMC.POWERED_SPAWNER_COST_MULTIPLIER, tag1);

		NBTTagCompound tag2 = new NBTTagCompound();
		tag2.setString("entityName", "ranged_rogue_android");
		tag2.setInteger("costMultiplier", 2);
		FMLInterModComms.sendMessage(ID, IMC.POWERED_SPAWNER_COST_MULTIPLIER, tag2);

//		Soul Vial blacklists
		FMLInterModComms.sendMessage(ID, IMC.SOUL_VIAL_BLACKLIST, "drone");

//		Teleporter blacklists
		FMLInterModComms.sendMessage(ID, IMC.TELEPORT_BLACKLIST_ADD, "drone");
	}

	private static void addSagMillRecipe(String name, int energy, Object inputObj, Object... outputData)
	{
		ItemStack input = getStack(inputObj);
		ItemStack[] outputs;
		float[] outputChances;

		if (outputData.length % 2 != 0) throw new RuntimeException("Invalid number of output parameters " + outputData.length);
		outputs = new ItemStack[outputData.length / 2];
		outputChances = new float[outputData.length / 2];

		for (int i = 0; i < outputData.length; i++) {
			if (i % 2 == 0) {
				outputs[i / 2] = getStack(outputData[i]);
			} else {
				outputChances[i / 2] = ((Number)outputData[i]).floatValue();
			}
		}

		sb.append(String.format("<recipe name=\"%s\" energyCost=\"%d\">", name, energy));
		sb.append("<input>");
		sb.append(String.format("<itemStack modID=\"%s\" itemName=\"%s\" number=\"%d\" itemMeta=\"%d\"></itemStack>", getModID(input), getItemName(input), input.stackSize, input.getMetadata()));
		sb.append("</input>");
		sb.append("<output>");
		for (int i = 0; i < outputs.length; i++)
		{
			ItemStack output = outputs[i];
			float chance = outputChances[i];
			sb.append(String.format("<itemStack modID=\"%s\" itemName=\"%s\" number=\"%d\" itemMeta=\"%d\" chance=\"%f\"></itemStack>", getModID(output), getItemName(output), output.stackSize, output.getMetadata(), chance));
		}
		sb.append("</output>");
		sb.append("</recipe>");

	}

	private static ItemStack getStack(Object obj)
	{
		if (obj instanceof ItemStack) return (ItemStack)obj;
		else if (obj instanceof Item) return new ItemStack((Item)obj);
		else if (obj instanceof Block) return new ItemStack((Block)obj);
		else throw new RuntimeException("Invalid input type " + obj.getClass().getName());
	}

	private static String getModID(ItemStack stack) {
		return ForgeRegistries.ITEMS.getKey(stack.getItem()).getResourceDomain();
	}

	private static String getItemName(ItemStack stack) {
		return ForgeRegistries.ITEMS.getKey(stack.getItem()).getResourcePath();
	}

}
