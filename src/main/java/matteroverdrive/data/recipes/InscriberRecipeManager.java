package matteroverdrive.data.recipes;

import matteroverdrive.tile.TileEntityInscriber;
import net.minecraft.item.ItemStack;
import net.shadowfacts.shadowmc.recipe.RecipeManager;

/**
 * @author shadowfacts
 */
public class InscriberRecipeManager extends RecipeManager<TileEntityInscriber, InscriberRecipe>
{

	public InscriberRecipeManager()
	{
		super(InscriberRecipe.class);
	}

	public boolean isPrimaryInput(ItemStack stack) {
		return recipes.stream()
				.map(InscriberRecipe::getMain)
				.filter(s -> ItemStack.areItemsEqual(s, stack))
				.findFirst()
				.isPresent();
	}

	public boolean isSecondaryInput(ItemStack stack) {
		return recipes.stream()
				.map(InscriberRecipe::getSec)
				.filter(s -> ItemStack.areItemsEqual(s, stack))
				.findFirst()
				.isPresent();
	}

}
