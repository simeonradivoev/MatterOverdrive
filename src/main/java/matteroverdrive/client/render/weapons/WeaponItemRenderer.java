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

package matteroverdrive.client.render.weapons;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import matteroverdrive.client.resources.data.WeaponMetadataSection;
import matteroverdrive.items.weapon.EnergyWeapon;
import matteroverdrive.util.MOLog;
import matteroverdrive.util.math.MOMathHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.client.model.obj.OBJModel;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.Level;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simeon on 11/8/2015.
 */
public abstract class WeaponItemRenderer implements IPerspectiveAwareModel
{
    protected ResourceLocation weaponModelLocation;
    protected OBJModel weaponModel;
    protected WeaponMetadataSection weaponMetadata;
    protected OBJModel.OBJBakedModel bakedModel;
    private Matrix4f matrix;

    public WeaponItemRenderer(ResourceLocation weaponModelLocation)
    {
        matrix = new Matrix4f();
        this.weaponModelLocation = weaponModelLocation;
        createModel(this.weaponModelLocation);
        loadWeaponMetadata();
    }

    protected void loadWeaponMetadata()
    {
        weaponMetadata = new WeaponMetadataSection();

        try {
            IResource metadataResource = Minecraft.getMinecraft().getResourceManager().getResource(weaponModelLocation);
            if (metadataResource.hasMetadata()) {
                IMetadataSection section = metadataResource.getMetadata("weapon");
                if (section instanceof WeaponMetadataSection) {
                    weaponMetadata = ((WeaponMetadataSection) section);
                }
            }
        } catch (IOException e) {
            MOLog.log(Level.ERROR,e,"There was a problem reading weapon metadata from %s",weaponMetadata);
        }
    }

    protected void createModel(ResourceLocation weaponModelLocation)
    {
        try
        {
            weaponModel = (OBJModel) OBJLoader.INSTANCE.loadModel(weaponModelLocation);
            ImmutableMap<String,String> customOptions = new ImmutableMap.Builder<String,String>().put("flip-v","true").put("ambient","false").build();
            weaponModel = (OBJModel)weaponModel.process(customOptions);
        } catch (Exception e)
        {
            MOLog.error("Missing weapon model.",e);
        }
    }

    public void bakeModel()
    {
        List<String> visibleGroups = new ArrayList<>();
        visibleGroups.add(OBJModel.Group.ALL);
        bakedModel = (OBJModel.OBJBakedModel)weaponModel.bake(new OBJModel.OBJState(visibleGroups,true), DefaultVertexFormats.ITEM, new Function<ResourceLocation, TextureAtlasSprite>()
        {
            @Nullable
            @Override
            public TextureAtlasSprite apply(@Nullable ResourceLocation input)
            {
                return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(input.toString());
            }
        });
    }

    @Override
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType)
    {
        matrix.setIdentity();
        if (cameraTransformType == ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND)
        {
            GlStateManager.scale(1.6f,1.6f,1.6f);
            GlStateManager.rotate(180,0,1,0);
            GlStateManager.translate(0.5f,0.5f,0.35f);
        }
        else if (cameraTransformType == ItemCameraTransforms.TransformType.GUI)
        {
            //matrix.setRotation(new AxisAngle4f(new Vector3f(0,1,0),90));
            GlStateManager.rotate(25,0,0,1);
            GlStateManager.rotate(90,0,1,0);


            GlStateManager.translate(0.7,0.5,0.3);
            //GlStateManager.scale(2.5f,2.5f,2.5f);
        }
        else if (cameraTransformType == ItemCameraTransforms.TransformType.GROUND)
        {
            matrix.setScale(1.2f);
            matrix.setTranslation(new javax.vecmath.Vector3f(0.6f,0.5f,0.3f));
        }
        return new ImmutablePair<>(this,matrix);
    }

    public void transformFirstPersonWeapon(EnergyWeapon energyWeapon,ItemStack weaponStack,float zoomValue,float recoilValue)
    {
        transformRecoil(recoilValue,zoomValue);
        GlStateManager.translate(0,MOMathHelper.Lerp(0,0.04,zoomValue),MOMathHelper.Lerp(0,-0.3,zoomValue));
    }

    protected void transformRecoil(float recoilValue,float zoomValue)
    {
        GlStateManager.translate(0,recoilValue * -0.005f,recoilValue * -0.02f);
        GlStateManager.rotate(recoilValue * 0.7f, -1, 0, 0);
    }

    public void renderHand(RenderPlayer renderPlayer)
    {
        renderPlayer.renderLeftArm(Minecraft.getMinecraft().thePlayer);
    }

    public void transformHand(float recoilValue,float zoomValue)
    {
        transformRecoil(recoilValue,zoomValue);
        GlStateManager.translate(MOMathHelper.Lerp(0.01,-0.15,zoomValue),-0.3,0.4);
        GlStateManager.rotate(MOMathHelper.Lerp(35,10,zoomValue),0,0,1);
        GlStateManager.rotate(MOMathHelper.Lerp(20,0,zoomValue),1,0,0);
        GlStateManager.scale(0.4,0.4,0.4);
    }

    @Override
    public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand)
    {
        return bakedModel.getQuads(state,side,rand);
    }

    @Override
    public boolean isAmbientOcclusion()
    {
        return true;
    }

    @Override
    public boolean isGui3d()
    {
        return true;
    }

    @Override
    public boolean isBuiltInRenderer()
    {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleTexture()
    {
        return bakedModel.getParticleTexture();
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms()
    {
        return bakedModel.getItemCameraTransforms();
    }


    public float getHorizontalSpeed()
    {
        return 0.05f;
    }

    public WeaponMetadataSection getWeaponMetadata()
    {
        return weaponMetadata;
    }

    @Override
    public ItemOverrideList getOverrides()
    {
        return ItemOverrideList.NONE;
    }
}
