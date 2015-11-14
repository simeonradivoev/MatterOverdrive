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

package matteroverdrive.client.render.item;

import cofh.lib.gui.GuiColor;
import matteroverdrive.Reference;
import matteroverdrive.api.weapon.IWeaponModule;
import matteroverdrive.items.weapon.Phaser;
import matteroverdrive.items.weapon.module.WeaponModuleColor;
import matteroverdrive.util.RenderUtils;
import matteroverdrive.util.WeaponHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.obj.WavefrontObject;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Simeon on 3/11/2015.
 */
public class ItemRendererPhaser extends WeaponItemRenderer
{
    public static final String TEXTURE = Reference.PATH_ITEM + "phaser2.png";
    private static final String TEXTURE_COLOR_MASK = Reference.PATH_ITEM + "phaser_color_mask.png";
    public static final String MODEL = Reference.PATH_MODEL + "item/phaser2.obj";
    public static final String MODEL_3DS = Reference.PATH_MODEL + "item/phaser2.3ds";
    public static final float SCALE = 6.0f;
    public static final float THIRD_PERSON_SCALE = 3f;
    public static final float ITEM_SCALE = 3;
    public static final float SCALE_DROP = 2.5f;

    public static ResourceLocation phaserTextureColorMask;

    public ItemRendererPhaser()
    {
        super((WavefrontObject)AdvancedModelLoader.loadModel(new ResourceLocation(MODEL)),new ResourceLocation(TEXTURE));
        phaserTextureColorMask = new ResourceLocation(TEXTURE_COLOR_MASK);
    }

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type)
    {
        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper)
    {
        return true;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data)
    {
        if(type == ItemRenderType.EQUIPPED_FIRST_PERSON)
        {
        	renderFirstPerson(item);
        }
        else if(type == ItemRenderType.INVENTORY)
        {
        	renderItem(item);
        }
        else if(type == ItemRenderType.ENTITY)
        {
        	renderDrop(item);
        }
        else
        {
        	renderThirdPerson(type,item);
        }
    }

    void renderItem(ItemStack item)
    {
    	glPushMatrix();
        glScaled(ITEM_SCALE, ITEM_SCALE, ITEM_SCALE);
        glTranslated(0, -0, -0.15);
        glRotated(0, 0, 1, 0);
        renderGun(ItemRenderType.INVENTORY,item);
        glPopMatrix();
    }

    void renderThirdPerson(ItemRenderType renderType, ItemStack item)
    {
        glPushMatrix();
        glScaled(THIRD_PERSON_SCALE, THIRD_PERSON_SCALE, THIRD_PERSON_SCALE);
        glTranslated(0.32, 0.23, 0.32);
        glRotated(-135, 0, 1, 0);
        glRotated(60, 1, 0, 0);
        renderGun(renderType,item);
        glPopMatrix();
    }

    void renderDrop(ItemStack item)
    {
        glPushMatrix();
        glScaled(SCALE_DROP, SCALE_DROP, SCALE_DROP);
        glTranslated(0, 0, 0);
        glRotated(15, 1, 0, 0);
        renderGun(ItemRenderType.ENTITY, item);
        glPopMatrix();
    }

    void renderFirstPerson(ItemStack item)
    {
        glPushMatrix();
        renderHand();
        glPopMatrix();

        glPushMatrix();
        glScaled(SCALE, SCALE, SCALE);
        if(Minecraft.getMinecraft().thePlayer.isUsingItem())
        {
            glTranslated(0.3, -0.07, 0);
            glRotated(150, 0, 1, 0);
            glRotated(-26, 1, 0, 0);
            glRotated(-5, 1, 0, 1);
        }
        else
        {
            glTranslated(0.15, -0.06, 0.05);
            glRotated(137.5, 0, 1, 0);
            glRotated(-6, 1, 0, 0);
        }

        renderGun(ItemRenderType.EQUIPPED_FIRST_PERSON,item);
        glPopMatrix();
    }

    void renderHand()
    {
        GL11.glPushMatrix();
        ResourceLocation skin = Minecraft.getMinecraft().thePlayer.getLocationSkin();
        Minecraft.getMinecraft().getTextureManager().bindTexture(skin);

        glTranslated(-0.2, 0.2f, 0.7);
        glRotated(45, 0, 1, 0);
        glRotated(100, 0, 0, -1);
        double length = 1.8;
        double width = 0.9;
        double depth = 0.7;

        Tessellator.instance.startDrawingQuads();
        Tessellator.instance.setNormal(0, 0, -1);
        Tessellator.instance.addVertexWithUV(0, 0, 0, 44f / 64f, 20f / 32f);
        Tessellator.instance.addVertexWithUV(0, length, 0, 44f / 64f, 1);
        Tessellator.instance.addVertexWithUV(depth, length, 0, 48f / 64f, 1);
        Tessellator.instance.addVertexWithUV(depth, 0, 0, 48f / 64f, 20f / 32f);


        Tessellator.instance.setNormal(-1, 0, 0);
        Tessellator.instance.addVertexWithUV(0, 0, 0, 44f/64f,20f/32f);
        Tessellator.instance.addVertexWithUV(0, 0, width, 40f/64f,20f/32f);
        Tessellator.instance.addVertexWithUV(0, length, width, 40f / 64f, 1f);
        Tessellator.instance.addVertexWithUV(0, length, 0, 44f / 64f, 1f);
        Tessellator.instance.draw();
        GL11.glPopMatrix();
    }

    void renderGun(ItemRenderType renderType, ItemStack item)
    {
        bindTexture(weaponTexture);
        weaponModel.renderOnly("grip");

        renderBarrel(item);

        GuiColor color = WeaponModuleColor.defaultColor;
        ItemStack color_module = WeaponHelper.getModuleAtSlot(Reference.MODULE_COLOR, item);
        if (color_module != null)
        {
            IWeaponModule module = (IWeaponModule)color_module.getItem();
            Object colorObject = module.getValue(color_module);
            if(colorObject instanceof GuiColor)
            {
                color = (GuiColor)colorObject;
            }
        }

        RenderUtils.applyColor(color);
        weaponModel.renderOnly("Base","display");
        glColor3f(1,1,1);

        glDisable(GL_LIGHTING);
        RenderUtils.disableLightmap();
        renderLevelSlider(item);
        glEnable(GL_LIGHTING);
        glColor3f(1,1,1);
    }

    private void renderLevelSlider(ItemStack item)
    {
        glDisable(GL_TEXTURE_2D);

        glDisable(GL_LIGHTING);
        glDisable(GL_BLEND);
        glColor4f(0, 0, 0, 1);
        weaponModel.renderPart("level_bg");
        setIndicatorColor(item);
        glPushMatrix();
        double power = ((double)item.getTagCompound().getByte("power") + 1) / (double) Phaser.MAX_LEVEL;
        glTranslated(0.042, 0, 0);
        glScaled(power, 1, 1);
        weaponModel.renderPart("level_slider");
        glPopMatrix();
        glEnable(GL_LIGHTING);
        glEnable(GL_TEXTURE_2D);
    }

    private void setIndicatorColor(ItemStack item)
    {
        if (Phaser.isKillMode(item))
        {
            glColor3d(1, 0, 0);
        }
        else
        {
            glColor3d(0, 1, 0);
        }
    }
}
