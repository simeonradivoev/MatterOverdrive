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

package matteroverdrive.client.render.item;/* Created by Simeon on 10/17/2015. */

import matteroverdrive.Reference;
import matteroverdrive.util.RenderUtils;
import matteroverdrive.util.WeaponHelper;
import matteroverdrive.util.animation.MOEasing;
import matteroverdrive.util.math.MOMathHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.obj.WavefrontObject;
import org.lwjgl.opengl.GL11;

import java.util.Random;

import static org.lwjgl.opengl.GL11.*;

public class ItemRendererOmniTool extends WeaponItemRenderer
{
    public static final String TEXTURE = Reference.PATH_ITEM + "wielder.png";
    public static final String MODEL = Reference.PATH_MODEL + "item/wielder.obj";
    public static final float SCALE = 7f;
    public static final float THIRD_PERSON_SCALE = 3f;
    public static final float ITEM_SCALE = 3;
    public static final float SCALE_DROP = 2.5f;
    public static float RECOIL_TIME = 0;
    public static float RECOIL_AMOUNT = 0;
    private Random random;

    public ItemRendererOmniTool()
    {
        super(new ResourceLocation(MODEL),new ResourceLocation(TEXTURE));
        random = new Random();
    }

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
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
        glTranslated(0, 0, -0.25);
        glRotated(180, 0, 1, 0);
        renderGun(ItemRenderType.INVENTORY,item);
        glPopMatrix();
    }

    void renderThirdPerson(ItemRenderType renderType, ItemStack item)
    {
        glPushMatrix();
        glScaled(THIRD_PERSON_SCALE, THIRD_PERSON_SCALE, THIRD_PERSON_SCALE);
        glTranslated(0.3, 0.3, 0.3);
        glRotated(60, -1, 0, 1);
        glRotated(40, 0, 1, 0);
        renderGun(renderType,item);
        glPopMatrix();
    }

    void renderDrop(ItemStack item)
    {
        glPushMatrix();
        glScaled(SCALE_DROP, SCALE_DROP, SCALE_DROP);
        glTranslated(0, 0, 0);
        glRotated(15, 1, 0, 0);
        renderGun(ItemRenderType.ENTITY,item);
        glPopMatrix();
    }

    void renderFirstPerson(ItemStack item)
    {
        glPushMatrix();
        RECOIL_TIME = MOMathHelper.Lerp(RECOIL_TIME, 0, 0.1f);
        float recoilValue = MOEasing.Quad.easeInOut(RECOIL_TIME, 0, 1, 1f);

        glTranslatef(0, 0, recoilValue * 0.01f * RECOIL_AMOUNT);
        glRotated(recoilValue * 2 * RECOIL_AMOUNT, 1, 0, 0);

        Minecraft.getMinecraft().renderViewEntity.rotationPitch += recoilValue * random.nextGaussian() * 0.03f - recoilValue * 0.02f;
        Minecraft.getMinecraft().renderViewEntity.rotationYaw += recoilValue * 0.02f * random.nextGaussian();

        glColor3f(1, 1, 1);
        renderHand();

        glPushMatrix();
        glScaled(SCALE, SCALE, SCALE);
        if(Minecraft.getMinecraft().thePlayer.isUsingItem())
        {
            glTranslated(0.15, 0.03, -0.0);
            glRotated(35, 0, -1, 0);
            glRotated(25, 1, 0, 0);
            glScaled(1, 1, 0.7);
        }
        else
        {
            glTranslated(0.15, 0.03, -0.0);
            glRotated(40, 0, -1, 0);
            glRotated(5, 1, 0, 0);
            glScaled(1, 1, 0.7);
        }
        renderGun(ItemRenderType.EQUIPPED_FIRST_PERSON,item);
        glPopMatrix();
        glPopMatrix();
    }

    void renderGun(ItemRenderType renderType, ItemStack item)
    {
        glEnable(GL_NORMALIZE);
        bindTexture(weaponTexture);
        weaponModel.renderOnly("welder_arms_base","wielder_arms","grip");
        renderBarrel(item);

        RenderUtils.applyColor(WeaponHelper.getColor(item));
        weaponModel.renderOnly("hull", "sights_rail","side_rail");

        glDisable(GL_LIGHTING);
        RenderUtils.disableLightmap();
        glColor3f(1,1,1);
        weaponModel.renderPart("indicator");
        glEnable(GL_LIGHTING);
        RenderUtils.enableLightmap();
    }

    void renderHand()
    {
        if (!Minecraft.getMinecraft().thePlayer.isInvisible()) {
            GL11.glPushMatrix();
            ResourceLocation skin = Minecraft.getMinecraft().thePlayer.getLocationSkin();
            Minecraft.getMinecraft().getTextureManager().bindTexture(skin);

            glTranslated(-0.2, 0.2f, 0.7);
            glRotated(45, 0, 1, 0);
            glRotated(90, 0, 0, -1);
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
            Tessellator.instance.addVertexWithUV(0, 0, 0, 44f / 64f, 20f / 32f);
            Tessellator.instance.addVertexWithUV(0, 0, width, 40f / 64f, 20f / 32f);
            Tessellator.instance.addVertexWithUV(0, length, width, 40f / 64f, 1f);
            Tessellator.instance.addVertexWithUV(0, length, 0, 44f / 64f, 1f);
            Tessellator.instance.draw();
            GL11.glPopMatrix();
        }
    }

    public WavefrontObject getModel()
    {
        return weaponModel;
    }
}
