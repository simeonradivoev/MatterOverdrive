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

package matteroverdrive.compat.modules.nei;

import matteroverdrive.Reference;
import matteroverdrive.data.recipes.InscriberRecipe;
import matteroverdrive.handler.recipes.InscriberRecipes;
import matteroverdrive.util.MOStringHelper;
import matteroverdrive.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Simeon on 12/2/2015.
 */
public class CraftingHandlerInscriber //extends TemplateRecipeHandler
{
    ResourceLocation background = new ResourceLocation(Reference.PATH_GUI + "inscriber_nei.png");
    ResourceLocation arrowTexture = new ResourceLocation(Reference.TEXTURE_ARROW_PROGRESS);

    /*@Override
    public void loadTransferRects()
    {
        this.transferRects.add(new RecipeTransferRect(new Rectangle(55,20, 26, 18), "mo_inscriber", new Object[0]));
    }

    @Override
    public String getGuiTexture() {
        return Reference.PATH_GUI + "inscriber_nei.png";
    }

    @Override
    public void drawProgressBar(int x, int y, int tx, int ty, int w, int h, float completion, int direction)
    {
        GuiDraw.changeTexture(arrowTexture);
        GuiDraw.drawTexturedModalRect(x, y, 24, 0, 48, 16);
    }

    @Override
    public void drawExtras(int recipe)
    {
        GuiDraw.changeTexture(arrowTexture);
        int ticks = ((CashedInscriberRecipe)arecipes.get(recipe)).getTime();
        int width = (int)(((float)(this.cycleticks % ticks) / (float)ticks) * 24);
        RenderUtils.drawPlaneWithUV(57,20,0,width,16,0.5,0,width/48f,1);

        Minecraft.getMinecraft().fontRendererObj.drawString(String.format("-%,d RF",((CashedInscriberRecipe)arecipes.get(recipe)).getEnergy()),72,52,Reference.COLOR_HOLO_RED.getColor());
    }

    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if(outputId.equals("mo_inscriber") && this.getClass() == CraftingHandlerInscriber.class) {
            Iterator<InscriberRecipe> iterator = InscriberRecipes.getRecipes().iterator();

            while(iterator.hasNext()) {
                InscriberRecipe recipe = iterator.next();
                CashedInscriberRecipe cachedRecipe = new CashedInscriberRecipe(recipe);
                cachedRecipe.computeVisuals();
                this.arecipes.add(cachedRecipe);
            }
        } else {
            super.loadCraftingRecipes(outputId, results);
        }

    }

    @Override
    public void drawBackground(int recipe) {
        GL11.GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GuiDraw.changeTexture(this.getGuiTexture());
        //GL11.glDisable(GL11.GL_TEXTURE_2D);
        RenderUtils.drawPlane(23,5,0,120,72);
    }

    @Override
    public int recipiesPerPage() {
        return 1;
    }

    @Override
    public void loadCraftingRecipes(ItemStack result) {
        Iterator<InscriberRecipe> iterator = InscriberRecipes.getRecipes().iterator();

        while (iterator.hasNext()) {
            InscriberRecipe recipe = iterator.next();
            if (NEIServerUtils.areStacksIdentical(result,recipe.getRecipeOutput())) {
                CashedInscriberRecipe cachedRecipe = new CashedInscriberRecipe(recipe);
                cachedRecipe.computeVisuals();
                this.arecipes.add(cachedRecipe);
            }
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        Iterator<InscriberRecipe> iterator = InscriberRecipes.getRecipes().iterator();

        while (iterator.hasNext()) {
            InscriberRecipe recipe = iterator.next();
            if (NEIServerUtils.areStacksIdentical(ingredient,recipe.getMain()) || NEIServerUtils.areStacksIdentical(ingredient,recipe.getSec()))
            {
                CashedInscriberRecipe cachedRecipe = new CashedInscriberRecipe(recipe);
                cachedRecipe.computeVisuals();
                this.arecipes.add(cachedRecipe);
            }
        }
    }

    @Override
    public boolean hasOverlay(GuiContainer gui, Container container, int recipe) {
        return false;
    }

    @Override
    public IRecipeOverlayRenderer getOverlayRenderer(GuiContainer gui, int recipe) {
        return null;
    }

    @Override
    public IOverlayHandler getOverlayHandler(GuiContainer gui, int recipe) {
        return null;
    }

    @Override
    public String getRecipeName() {
        return MOStringHelper.translateToLocal("gui.inscriber.name");
    }

    @Override
    public String getOverlayIdentifier() {
        return "mo_inscriber";
    }

    public class CashedInscriberRecipe extends CachedRecipe
    {
        PositionedStack main;
        PositionedStack sec;
        PositionedStack result;
        int time;
        int energy;

        public CashedInscriberRecipe(InscriberRecipe recipe)
        {
            main = new PositionedStack(recipe.getMain(),33,20);
            sec = new PositionedStack(recipe.getSec(),33,47);
            result = new PositionedStack(recipe.getRecipeOutput(),91,20);
            time = recipe.getTime();
            energy = recipe.getEnergy();
        }

        @Override
        public List<PositionedStack> getIngredients() {
            ArrayList stacks = new ArrayList();
            stacks.add(main);
            stacks.add(sec);
            return stacks;
        }

        @Override
        public PositionedStack getResult() {
            return result;
        }

        public void computeVisuals() {
            this.main.generatePermutations();
            this.sec.generatePermutations();
            this.result.generatePermutations();
        }

        public int getTime()
        {
            return time;
        }

        public int getEnergy()
        {
            return energy;
        }
    }*/
}
