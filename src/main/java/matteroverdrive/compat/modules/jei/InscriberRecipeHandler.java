package matteroverdrive.compat.modules.jei;

import matteroverdrive.data.recipes.InscriberRecipe;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

import javax.annotation.Nonnull;

/**
 * @author shadowfacts
 */
public class InscriberRecipeHandler implements IRecipeHandler<InscriberRecipe>
{
	@Nonnull
	@Override
	public Class<InscriberRecipe> getRecipeClass()
	{
		return InscriberRecipe.class;
	}

	@Nonnull
	@Override
	@Deprecated
	public String getRecipeCategoryUid()
	{
		return InscriberRecipeCategory.UID;
	}

	@Nonnull
	@Override
	public String getRecipeCategoryUid(@Nonnull InscriberRecipe recipe)
	{
		return getRecipeCategoryUid();
	}

	@Nonnull
	@Override
	public IRecipeWrapper getRecipeWrapper(@Nonnull InscriberRecipe recipe)
	{
		return new InscriberRecipeWrapper(recipe);
	}

	@Override
	public boolean isRecipeValid(@Nonnull InscriberRecipe recipe)
	{
		return true;
	}

}
