package matteroverdrive.compat.modules.jei;

import matteroverdrive.container.ContainerInscriber;
import matteroverdrive.gui.GuiInscriber;
import matteroverdrive.handler.recipes.InscriberRecipes;
import matteroverdrive.init.MatterOverdriveBlocks;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

/**
 * @author shadowfacts
 */
@JEIPlugin
public class MOPlugin implements IModPlugin
{

	@Override
	public void register(@Nonnull IModRegistry registry)
	{
		registry.addRecipeCategories(new InscriberRecipeCategory(registry.getJeiHelpers().getGuiHelper()));

		registry.addRecipeHandlers(new InscriberRecipeHandler());

		registry.addRecipes(InscriberRecipes.getRecipes());

		registry.addRecipeCategoryCraftingItem(new ItemStack(MatterOverdriveBlocks.inscriber), InscriberRecipeCategory.UID);

		registry.getRecipeTransferRegistry().addRecipeTransferHandler(ContainerInscriber.class, InscriberRecipeCategory.UID, 0, 2, 8, 36);

		registry.addRecipeClickArea(GuiInscriber.class, 32, 55, 24, 16, InscriberRecipeCategory.UID);

		registry.addAdvancedGuiHandlers(new MOAdvancedGuiHandler());
	}

	@Override
	public void onRuntimeAvailable(@Nonnull IJeiRuntime jeiRuntime)
	{

	}

}
