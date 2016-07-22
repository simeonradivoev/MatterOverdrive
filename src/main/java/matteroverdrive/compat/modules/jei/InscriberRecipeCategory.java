package matteroverdrive.compat.modules.jei;

import matteroverdrive.Reference;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * @author shadowfacts
 */
public class InscriberRecipeCategory implements IRecipeCategory<InscriberRecipeWrapper>
{

	public static final String UID = "mo.inscriber";
	private final IDrawable background;
	private final IDrawable overlay;
	private final String localizedName;

	public InscriberRecipeCategory(IGuiHelper guiHelper)
	{
		background = guiHelper.createBlankDrawable(120, 72);
		overlay = guiHelper.createDrawable(new ResourceLocation(Reference.PATH_GUI + "inscriber_jei.png"), 0, 0, 120, 72);
		localizedName = I18n.format("mo.jei.inscriber");
	}

	@Nonnull
	@Override
	public String getUid()
	{
		return UID;
	}

	@Nonnull
	@Override
	public String getTitle()
	{
		return localizedName;
	}

	@Nonnull
	@Override
	public IDrawable getBackground()
	{
		return background;
	}

	@Override
	public void drawExtras(@Nonnull Minecraft minecraft)
	{
		GlStateManager.enableAlpha();
		GlStateManager.enableBlend();
		overlay.draw(minecraft);
		GlStateManager.disableBlend();
		GlStateManager.disableAlpha();
	}

	@Override
	public void drawAnimations(@Nonnull Minecraft minecraft)
	{

	}

	@Override
	public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull InscriberRecipeWrapper recipe)
	{
		List inputs = recipe.getInputs();
		recipeLayout.getItemStacks().init(0, true, 9, 14);
		recipeLayout.getItemStacks().set(0, (ItemStack)inputs.get(0));
		recipeLayout.getItemStacks().init(1, true, 9, 41);
		recipeLayout.getItemStacks().set(1, (ItemStack)inputs.get(1));

		recipeLayout.getItemStacks().init(2, false, 67, 14);
		recipeLayout.getItemStacks().set(2, (ItemStack)recipe.getOutputs().get(0));
	}
}
