package com.MO.MatterOverdrive.compat;

import com.MO.MatterOverdrive.compat.modules.*;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

/**
 *
 *
 * @author shadowfacts
 */
public class MatterOverdriveCompat {

	private static ArrayList<Class> modules = new ArrayList<Class>();

//	Add additional modules here
	static {
		register(CompatThermalExpansion.class);
	}

	private static boolean register(Class clazz) {
		if (clazz.isAnnotationPresent(Compat.class)) {
			Compat annotation = (Compat)clazz.getAnnotation(Compat.class);
			if (Loader.isModLoaded(annotation.value())) {
				modules.add(clazz);
				return true;
			} else {
				System.out.println(String.format("The mod %s was not loaded, skipping compatibility module.", annotation.value()));
				return false;
			}
		}
		System.out.println("There was a problem register a compatibility module!");
		return false;
	}

	public static void preInit(FMLPreInitializationEvent event) {
		System.out.println("Attempting to run pre-initialization methods for all registered compatibility modules.");
		for (Class clazz : modules) {
			for (Method m : clazz.getMethods()) {
				if (m.isAnnotationPresent(Compat.PreInit.class) && Modifier.isStatic(m.getModifiers())) {
					try {
						m.invoke(null, event);
					} catch (ReflectiveOperationException e) {
						Compat annotation = (Compat)clazz.getAnnotation(Compat.class);
						System.err.println("There was an error trying to invoke the pre-initialization method of the compatibility module for " + annotation.value());
						e.printStackTrace();
					}
				}
			}
		}
	}

	public static void init(FMLInitializationEvent event) {
		System.out.println("Attempting to run initialization methods for all registered compatibility modules.");
		for (Class clazz : modules) {
			for (Method m : clazz.getMethods()) {
				if (m.isAnnotationPresent(Compat.Init.class) && Modifier.isStatic(m.getModifiers())) {
					try {
						m.invoke(null, event);
					} catch (ReflectiveOperationException e) {
						Compat annotation = (Compat)clazz.getAnnotation(Compat.class);
						System.err.println("There was an error trying to invoke the initialization method of the compatibility module for " + annotation.value());
						e.printStackTrace();
					}
				}
			}
		}
	}

	public static void postInit(FMLPostInitializationEvent event) {
		System.out.println("Attempting to run post-initialization methods for all registered compatibility modules.");
		for (Class clazz : modules) {
			for (Method m : clazz.getMethods()) {
				if (m.isAnnotationPresent(Compat.PostInit.class) && Modifier.isStatic(m.getModifiers())) {
					try {
						m.invoke(null, event);
					} catch (ReflectiveOperationException e) {
						Compat annotation = (Compat)clazz.getAnnotation(Compat.class);
						System.out.println("There was an error trying to invoke the post-initialization method of the compatibility module for " + annotation.value());
						e.printStackTrace();
					}
				}
			}
		}
	}

}
