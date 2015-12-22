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
import matteroverdrive.api.weapon.IWeaponModule;
import matteroverdrive.api.weapon.IWeaponScope;
import matteroverdrive.client.resources.data.WeaponMetadataSection;
import matteroverdrive.proxy.ClientProxy;
import matteroverdrive.util.RenderUtils;
import matteroverdrive.util.WeaponHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.obj.GroupObject;
import net.minecraftforge.client.model.obj.WavefrontObject;

import java.io.IOException;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Simeon on 11/8/2015.
 */
public abstract class WeaponItemRenderer implements IItemRenderer
{
    protected ResourceLocation weaponTexture;
    protected ResourceLocation weaponModelLocation;
    protected WavefrontObject weaponModel;
    protected Vec3 scopePosition;

    public WeaponItemRenderer(ResourceLocation weaponModelLocation,ResourceLocation weaponTexture)
    {
        this.weaponModelLocation = weaponModelLocation;
        this.weaponTexture = weaponTexture;
        weaponModel = (WavefrontObject) AdvancedModelLoader.loadModel(weaponModelLocation);
        loadWeaponMetadata();
    }

    protected void loadWeaponMetadata()
    {
        try {
            IResource metadataResource = Minecraft.getMinecraft().getResourceManager().getResource(weaponModelLocation);
            if (metadataResource.hasMetadata()) {
                IMetadataSection section = metadataResource.getMetadata("weapon");
                if (section instanceof WeaponMetadataSection) {
                    scopePosition = ((WeaponMetadataSection) section).getScopePosition();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void renderBarrel(ItemStack weaponStack)
    {
        bindTexture(weaponTexture);
        ItemStack barrelStack = WeaponHelper.getModuleAtSlot(Reference.MODULE_BARREL,weaponStack);
        if (barrelStack != null)
        {
            GroupObject object = getModelPart(barrelStack.getUnlocalizedName().substring(5).replace('.','_'));
            if (object != null) {
                object.render();
                return;
            }
        }

        renderDefaultBarrel(weaponStack);
    }

    protected void renderScope(ItemStack weapon)
    {
        ItemStack scopeStack = WeaponHelper.getModuleAtSlot(Reference.MODULE_SIGHTS,weapon);
        if (scopeStack != null && scopeStack.getItem() instanceof IWeaponModule)
        {
            glPushMatrix();
            glTranslated(scopePosition.xCoord,scopePosition.yCoord,scopePosition.zCoord);
            String moduleObjectName = ((IWeaponModule)scopeStack.getItem()).getModelName(scopeStack);
            WavefrontObject model = ClientProxy.renderHandler.getWeaponModuleModelRegistry().getModel(((IWeaponModule)scopeStack.getItem()).getModelPath());
            ResourceLocation moduleTexture = ((IWeaponModule) scopeStack.getItem()).getModelTexture(scopeStack);
            if (moduleTexture != null) {
                bindTexture(moduleTexture);
            } else {
                bindTexture(weaponTexture);
            }

            if (model != null) {
                for (GroupObject object : model.groupObjects) {
                    if (object.name.equalsIgnoreCase(moduleObjectName + "_window")) {
                        RenderUtils.disableLightmap();
                        glEnable(GL_BLEND);
                        glBlendFunc(GL_ONE, GL_ONE);
                        glDepthFunc(GL_LEQUAL);
                        glEnable(GL_DEPTH_TEST);
                        glDepthMask(true);
                        object.render();

                        RenderUtils.enableLightmap();
                        glDisable(GL_BLEND);
                        break;
                    }
                }

                model.renderOnly(moduleObjectName);
            }
            glPopMatrix();
        }
    }

    protected void renderModule(IWeaponModule weaponModule,ItemStack weaponModuleStack,ItemStack weaponStack)
    {
        WavefrontObject model = ClientProxy.renderHandler.getWeaponModuleModelRegistry().getModel(weaponModule.getModelPath());
        if (model != null)
        {
            ResourceLocation moduleTexture = weaponModule.getModelTexture(weaponModuleStack);
            if (moduleTexture != null)
            {
                bindTexture(moduleTexture);
            }else
            {
                bindTexture(weaponTexture);
            }
            String moduleObjectName = weaponModule.getModelName(weaponModuleStack);
            model.renderOnly(moduleObjectName);

        }
    }

    public float getScopeOffset(ItemStack weapon)
    {
        ItemStack scopeStack = WeaponHelper.getModuleAtSlot(Reference.MODULE_SIGHTS,weapon);
        if (scopeStack != null && scopeStack.getItem() instanceof IWeaponScope)
        {
            return ((IWeaponScope) scopeStack.getItem()).getYOffset(scopeStack,weapon);
        }
        return 0;
    }

    protected void renderDefaultBarrel(ItemStack weaponStack)
    {
        weaponModel.renderPart("weapon_module_barrel_none");
    }

    protected void bindTexture(ResourceLocation texture)
    {
        Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
    }

    protected GroupObject getModelPart(String part)
    {
        for (GroupObject object : weaponModel.groupObjects)
        {
            if (object.name.equalsIgnoreCase(part))
            {
                return object;
            }
        }
        return null;
    }

    public ResourceLocation getWeaponTexture(){return weaponTexture;}
}
