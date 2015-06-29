package com.MO.MatterOverdrive.compat.modules;

import com.MO.MatterOverdrive.compat.Compat;
import static com.MO.MatterOverdrive.init.MatterOverdriveItems.*;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Compatibility for Ex Nihilo
 *
 * @author shadowfacts
 */
@Compat("exnihilo")
public class CompatExNihilo {

	private static Method registerSieve;

	@Compat.Init
	@SuppressWarnings("unchecked")
	public static void init(FMLInitializationEvent event) {
//		Create Method
		try {
			Class sieveRegistry = Class.forName("exnihilo.registries.SieveRegistry");
//			registerSieve(Block source, Item output, int outputMeta, int rarity)
			registerSieve = sieveRegistry.getMethod("register", Block.class, Item.class, int.class, int.class);
		} catch (ClassNotFoundException e) {
			System.err.println("The Ex Nihilo sieve registry class could not be found.");
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			System.err.println("The register method of the Ex Nihilo sieve registry did not exist.");
			e.printStackTrace();
		}

//		Register Sieve Rewards
//		Percent = 100 / rarity
		register(Blocks.gravel, dilithium_ctystal, 0, 50); // 2%
		register(Blocks.gravel, tritanium_dust, 0, 25); // 4%
	}

	private static void register(Block source, Item output, int outputMeta, int rarity) {
		try {
			registerSieve.invoke(null, source, output, outputMeta, rarity);
		} catch (InvocationTargetException e) {
			System.err.println("There was a problem invoking the register method of the Ex Nihilo sieve registry.");
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			System.err.println("There was a problem accessing the register method of the Ex Nihilo sieve registry.");
			e.printStackTrace();
		}
	}

}
