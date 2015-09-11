/*
 * This file is part of Matter Overdrive
 * Copyright (c) 2015., Simeon Radivoev, All rights reserved.
 *
 * Matter Overdrive is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Matter Overdrive is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Matter Overdrive.  If not, see <http://www.gnu.org/licenses>.
 */

package matteroverdrive.guide;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.init.MatterOverdriveRecipes;
import matteroverdrive.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.ShapedOreRecipe;
import org.lwjgl.opengl.GL11;
import org.w3c.dom.Element;

import java.util.List;

/**
 * Created by Simeon on 8/29/2015.
 */
public class GuideElementRecipe extends GuideElementAbstract
{
    private static final ResourceLocation background = new ResourceLocation(Reference.PATH_ELEMENTS + "guide_recipe.png");
    IRecipe recipe;
    Object[] recipeItems;
    ItemStack output;

    @Override
    public void drawElement(int width,int mouseX,int mouseY)
    {
        GL11.glPushMatrix();
        if (textAlign == 1) {
            GL11.glTranslated(marginLeft + this.width/2 - 110/2, marginTop, 0);
        }
        else
        {
            GL11.glTranslated(marginLeft, marginTop, 0);
        }
        bindTexture(background);
        RenderUtils.applyColor(Reference.COLOR_MATTER);
        RenderUtils.drawPlane(8, 8, 0, 96, 96);
        if (recipeItems != null && recipe != null)
        {

            for (int x = 0;x < 3;x++)
            {
                for (int y = 0;y < 3;y++)
                {
                    int index = x + y * 3;
                    if (index < recipeItems.length) {
                        if (recipeItems[index] instanceof ItemStack) {
                            ItemStack stack = (ItemStack)recipeItems[index];
                            renderStack(stack,x,y);
                        }else if (recipeItems[index] instanceof List)
                        {
                            List stacks = (List)recipeItems[index];
                            int stackIndex = (int)((Minecraft.getMinecraft().theWorld.getWorldTime()/100)%(stacks.size()));
                            if (stackIndex < stacks.size() && stacks.get(stackIndex) instanceof ItemStack) {
                                renderStack((ItemStack)stacks.get(stackIndex), x, y);
                            }
                        }
                    }
                }
            }
        }
        GL11.glPopMatrix();
    }

    private void renderStack(ItemStack stack,int x,int y)
    {
        if (stack != null) {

            GL11.glPushMatrix();
            GL11.glTranslated(10 + x * 33, 9 + y * 33, 0);
            GL11.glScaled(1.5, 1.5, 1.5);
            RenderUtils.renderStack(0, 0, stack);
            GL11.glPopMatrix();
        }
    }

    @Override
    protected void loadContent(MOGuideEntry entry, Element element, int width, int height)
    {
        if (element.hasAttribute("item"))
        {
            output = shortCodeToStack(decodeShortcode(element.getAttribute("item")));
        }else
        {
            output = entry.getStackIcons()[0];
        }

        if (output != null)
        {
            for (IRecipe recipe : MatterOverdriveRecipes.recipes)
            {
                if (ItemStack.areItemStacksEqual(recipe.getRecipeOutput(),output))
                {
                    this.recipe = recipe;
                    break;
                }
            }

            if (recipe == null)
            {
                for (IRecipe recipe : (List<IRecipe>)CraftingManager.getInstance().getRecipeList())
                {
                    if (ItemStack.areItemStacksEqual(recipe.getRecipeOutput(),output))
                    {
                        this.recipe = recipe;
                        break;
                    }
                }
            }
        }
        else
        {
            MatterOverdrive.log.warn("There is no output Itemstack to recipe Guide Element");
        }


        if (recipe != null)
        {
            if (recipe instanceof ShapedRecipes)
            {
                recipeItems = ((ShapedRecipes) recipe).recipeItems;
            }
            else if (recipe instanceof ShapelessRecipes)
            {
                recipeItems = new ItemStack[((ShapelessRecipes) recipe).recipeItems.size()];
                recipeItems = ((ShapelessRecipes) recipe).recipeItems.toArray(recipeItems);
            }
            else if (recipe instanceof ShapedOreRecipe)
            {
                recipeItems = ((ShapedOreRecipe) recipe).getInput();
            }
        }else
        {
            MatterOverdrive.log.warn("Could not find recipe for %s in Guide Recipe Element",output);
        }

        this.height = 100;
        this.width = 100;
    }
}
