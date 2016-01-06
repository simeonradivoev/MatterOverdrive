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

import matteroverdrive.Reference;
import matteroverdrive.proxy.ClientProxy;
import matteroverdrive.util.RenderUtils;
import matteroverdrive.util.WeaponHelper;
import matteroverdrive.util.animation.MOEasing;
import matteroverdrive.util.math.MOMathHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;

import java.util.Random;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Simeon on 12/8/2015.
 */
public class ItemRendererIonSniper extends WeaponItemRenderer
{
    public static final String TEXTURE = Reference.PATH_ITEM + "ion_sniper.png";
    public static final String MODEL = Reference.PATH_MODEL + "item/ion_sniper.obj";
    public static final float SCALE = 0.06f;
    public static final float THIRD_PERSON_SCALE = 0.048f;
    public static final float ITEM_SCALE = 0.015f;
    public static final float SCALE_DROP = 0.03f;
    private Random random;
    private Vec3 scopePosition;

    public ItemRendererIonSniper()
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
        glTranslated(0, 0, -0.6);
        glRotated(0, 0, 1, 0);
        glScaled(ITEM_SCALE, ITEM_SCALE, ITEM_SCALE);
        renderGun(ItemRenderType.INVENTORY,item);
        glPopMatrix();
    }

    void renderThirdPerson(ItemRenderType renderType, ItemStack item)
    {
        glPushMatrix();
        glTranslated(1, 0.83, 1);
        glRotated(-135, 0, 1, 0);
        glRotated(60, 1, 0, 0);
        glScaled(THIRD_PERSON_SCALE, THIRD_PERSON_SCALE, THIRD_PERSON_SCALE);
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
        float zoomValue = MOEasing.Sine.easeInOut(ClientProxy.instance().getClientWeaponHandler().ZOOM_TIME, 0, 1, 1f);
        float recoilValue = MOEasing.Quart.easeInOut(getRecoilTime(), 0, 1, 1f);

        GL11.glPushMatrix();
        ResourceLocation skin = Minecraft.getMinecraft().thePlayer.getLocationSkin();
        Minecraft.getMinecraft().getTextureManager().bindTexture(skin);
        Minecraft.getMinecraft().renderViewEntity.rotationPitch += recoilValue * random.nextGaussian() * 0.1f - ((1-zoomValue*0.5f) * recoilValue * getRecoilAmount()) * 0.2f;
        Minecraft.getMinecraft().renderViewEntity.rotationYaw += recoilValue * 0.05f * random.nextGaussian();

        glTranslated(2.0, MOMathHelper.Lerp(-0.3f, -0.5f, zoomValue), MOMathHelper.Lerp(-1, -1.1f, zoomValue));
        glTranslatef(0, recoilValue * 0.05f * getRecoilAmount(), 0);
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

        glRotated(MOMathHelper.Lerp(30, 25, zoomValue), 0, 0, 1);
        glRotated(MOMathHelper.Lerp(90, 85, zoomValue), 0, 1, 0);

        glTranslated(MOMathHelper.Lerp(0, 1f, zoomValue), MOMathHelper.Lerp(-0.4f, -0.1f + getScopeOffset(item), zoomValue), MOMathHelper.Lerp(1.2f, 0.6f, zoomValue));
        glScaled(1, 1, 0.6);

        glTranslatef(0, -recoilValue * 0.05f * getRecoilAmount(), -recoilValue * 0.05f * getRecoilAmount());
        glRotated(recoilValue * 2 * getRecoilAmount(), -1, 0, 0);

        glScaled(SCALE, SCALE, SCALE);
        renderGun(ItemRenderType.EQUIPPED_FIRST_PERSON,item);
        glPopMatrix();
    }

    void renderGun(ItemRenderType renderType, ItemStack item)
    {
        RenderUtils.applyColor(WeaponHelper.getColor(item));
        renderScope(item);
        bindTexture(weaponTexture);
        weaponModel.renderAll();
    }
}
