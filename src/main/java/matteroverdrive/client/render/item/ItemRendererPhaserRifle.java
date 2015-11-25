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
import matteroverdrive.init.MatterOverdriveItems;
import matteroverdrive.items.weapon.PhaserRifle;
import matteroverdrive.util.RenderUtils;
import matteroverdrive.util.WeaponHelper;
import matteroverdrive.util.animation.MOEasing;
import matteroverdrive.util.math.MOMathHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.*;
/**
 * Created by Simeon on 7/25/2015.
 */
public class ItemRendererPhaserRifle implements IItemRenderer
{
    public static final String TEXTURE = Reference.PATH_ITEM + "phaser_rifle.png";
    public static final String MODEL = Reference.PATH_MODEL + "item/phaser_rifle.obj";
    public static final String FLASH_TEXTURE = Reference.PATH_FX + "phaser_rifle_flash.png";
    public static final float SCALE = 1f;
    public static final float THIRD_PERSON_SCALE = 0.8f;
    public static final float ITEM_SCALE = 0.4f;
    public static final float SCALE_DROP = 0.5f;
    public static float RECOIL_TIME = 0;
    public static float RECOIL_AMOUNT = 0;
    private static float ZOOM_TIME = 0;

    public static IModelCustom phaserModel;
    public static ResourceLocation phaserTexture;
    public static ResourceLocation flashTexture;

    public ItemRendererPhaserRifle()
    {
        phaserTexture = new ResourceLocation(TEXTURE);
        phaserModel = AdvancedModelLoader.loadModel(new ResourceLocation(MODEL));
        flashTexture = new ResourceLocation(FLASH_TEXTURE);
    }

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return type != ItemRenderType.EQUIPPED_FIRST_PERSON;
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
        glTranslated(0, 0.03, -0.7);
        glRotated(0, 0, 1, 0);
        renderGun(ItemRenderType.INVENTORY,item);
        glPopMatrix();
    }

    void renderThirdPerson(ItemRenderType renderType, ItemStack item)
    {
        glPushMatrix();
        glScaled(THIRD_PERSON_SCALE, THIRD_PERSON_SCALE, THIRD_PERSON_SCALE);
        glTranslated(1, 0.83, 1);
        glRotated(-135, 0, 1, 0);
        glRotated(60, 1, 0, 0);
        renderGun(renderType,item);
        glPopMatrix();
    }

    void renderDrop(ItemStack item)
    {
        glPushMatrix();
        glScaled(SCALE_DROP, SCALE_DROP, SCALE_DROP);
        glTranslated(0, 0, -0.7);
        renderGun(ItemRenderType.ENTITY,item);
        glPopMatrix();
    }

    void renderFirstPerson(ItemStack item)
    {
        ZOOM_TIME = MOMathHelper.Lerp(ZOOM_TIME,Minecraft.getMinecraft().thePlayer.isUsingItem() ? 1f : 0,0.2f);
        RECOIL_TIME = MOMathHelper.Lerp(RECOIL_TIME,0,0.1f);
        float zoomValue = MOEasing.Quart.easeOut(ZOOM_TIME, 0, 1, 1f);
        float recoilValue = MOEasing.Quad.easeInOut(RECOIL_TIME, 0, 1, 1f);

        GL11.glPushMatrix();
            ResourceLocation skin = Minecraft.getMinecraft().thePlayer.getLocationSkin();
            Minecraft.getMinecraft().getTextureManager().bindTexture(skin);

            glTranslated(2.0, MOMathHelper.Lerp(-0.3f, -0.4f, zoomValue), MOMathHelper.Lerp(-1, -1.1f, zoomValue));
            glTranslatef(0, recoilValue * 0.05f * RECOIL_AMOUNT, 0);
            glRotated(MOMathHelper.Lerp(45, 0, zoomValue), 1, 1, 0);
            glRotated(MOMathHelper.Lerp(0, MOMathHelper.Lerp(3, 0, zoomValue), recoilValue), 0, 0, 1);
            double length = 1.8;
            double width = 0.6;
            double depth = 0.5;

        if (!Minecraft.getMinecraft().thePlayer.isInvisible()) {
            Tessellator.instance.startDrawingQuads();
            Tessellator.instance.setNormal(0, 0, -1);
            Tessellator.instance.addVertexWithUV(0, 0, 0, 44f / 64f, 20f / 32f);
            Tessellator.instance.addVertexWithUV(0, length, 0, 44f / 64f, 1);
            Tessellator.instance.addVertexWithUV(depth, length, 0, 48f / 64f, 1);
            Tessellator.instance.addVertexWithUV(depth, 0, 0, 48f / 64f, 20f / 32f);


            Tessellator.instance.setNormal(-1, 0, 0);
            Tessellator.instance.addVertexWithUV(0, 0, 0, 44f / 64f, 20f / 32f);
            Tessellator.instance.addVertexWithUV(0, 0, width, 40f / 64f, 20f / 32f);
            Tessellator.instance.addVertexWithUV(0, length, width, 40f / 64f, 1f);
            Tessellator.instance.addVertexWithUV(0, length, 0, 44f / 64f, 1f);
            Tessellator.instance.draw();
        }

        GL11.glPopMatrix();

        glPushMatrix();
        glScaled(SCALE, SCALE, SCALE);

        glRotated(MOMathHelper.Lerp(30, 25, zoomValue), 0, 0, 1);
        glRotated(MOMathHelper.Lerp(90, 85, zoomValue), 0, 1, 0);

        glTranslated(MOMathHelper.Lerp(0, 0.86f, zoomValue), MOMathHelper.Lerp(-0.4f, -0.05f, zoomValue), MOMathHelper.Lerp(1.2f, 0.6f, zoomValue));
        glScaled(1, 1, 0.6);

        glTranslatef(0, -recoilValue * 0.05f * RECOIL_AMOUNT, -recoilValue * 0.05f * RECOIL_AMOUNT);
        glRotated(recoilValue * 2 * RECOIL_AMOUNT, -1, 0, 0);

        renderGun(ItemRenderType.EQUIPPED_FIRST_PERSON,item);
        glPopMatrix();

        renderMuzzle(item, recoilValue, zoomValue);
    }

    private void renderMuzzle(ItemStack item,float recoilValue,float zoomValue) {
        glPushMatrix();
        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE);
        glDisable(GL_LIGHTING);
        RenderUtils.disableLightmap();
        double size = 1 + 1 * MatterOverdriveItems.phaserRifle.getHeat(item) / MatterOverdriveItems.phaserRifle.getMaxHeat(item);

        glTranslated(3, 1.9, MOMathHelper.Lerp(-0.1f, -0.7f, zoomValue));
        glRotated(90, 0, 1, 0);
        glRotated(15, -1, 0, 0);
        float scale = MOEasing.Quart.easeIn(recoilValue, 0, 1, 1);
        GuiColor color = WeaponHelper.getColor(item);
        RenderUtils.applyColorWithMultipy(color,scale);

        Minecraft.getMinecraft().renderEngine.bindTexture(flashTexture);
        Tessellator.instance.startDrawingQuads();
        Tessellator.instance.addVertexWithUV(-size, -size, 0, 0, 0);
        Tessellator.instance.addVertexWithUV(-size, size, 0, 0, 1);
        Tessellator.instance.addVertexWithUV(size,size,0,1,1);
        Tessellator.instance.addVertexWithUV(size,-size,0,1,0);
        Tessellator.instance.draw();

        glDisable(GL_BLEND);
        glEnable(GL_LIGHTING);
        glPopMatrix();
    }

    void renderGun(ItemRenderType renderType, ItemStack item)
    {
        GuiColor color = WeaponHelper.getColor(item);
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

        glColor4f(color.getFloatR(), color.getFloatG(), color.getFloatB(), color.getFloatA());
        Minecraft.getMinecraft().renderEngine.bindTexture(phaserTexture);
        phaserModel.renderAll();
    }

    public boolean isRifleZoomed(ItemStack itemStack)
    {
        return Minecraft.getMinecraft().currentScreen == null
                && Minecraft.getMinecraft().thePlayer.getHeldItem() != null
                && Minecraft.getMinecraft().thePlayer.getHeldItem().getItem() instanceof PhaserRifle
                && Mouse.isButtonDown(1);
    }
}
