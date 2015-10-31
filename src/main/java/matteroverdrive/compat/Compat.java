package matteroverdrive.compat;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to mark a class that is an inter-mod compatibility module.
 *
 * @author shadowfacts
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Compat
{

	/**
	 * The mod id that this compat module is for.
	 */
	String value();

	/**
	 * Used to mark a method of a compatibility module to be run in the pre-initialization phase.
	 * Any method marked with this annotation must have 1 argument, a {@link cpw.mods.fml.common.event.FMLPreInitializationEvent}.
	 * Any method marked with this annotation must be static.
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	@interface PreInit {}

	/**
	 * Used to mark a method of a compatibility module to be run in the initialization phase.
	 * Any method marked with this annotation must have 1 argument, a {@link cpw.mods.fml.common.event.FMLInitializationEvent}.
	 * Any method marked with this annotation must be static.
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	@interface Init {}

	/**
	 * Used to mark a method of a compatibility module to be run in the post-initialization phase.
	 * Any method marked with this annotation must have 1 argument, a {@link cpw.mods.fml.common.event.FMLPostInitializationEvent}.
	 * Any method marked with this annotation must be static.
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	@interface PostInit {}

}
