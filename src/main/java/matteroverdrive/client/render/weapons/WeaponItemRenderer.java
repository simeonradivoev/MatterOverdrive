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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.vecmath.AxisAngle4f;
import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Simeon on 11/8/2015.
 */
public abstract class WeaponItemRenderer implements IPerspectiveAwareModel
{
	protected ResourceLocation weaponModelLocation;
	protected OBJModel weaponModel;
	protected WeaponMetadataSection weaponMetadata;
	protected OBJModel.OBJBakedModel bakedModel;
	private Matrix4f identity;
	private Map<ItemCameraTransforms.TransformType, Matrix4f> transforms = new HashMap<>();

	public WeaponItemRenderer(ResourceLocation weaponModelLocation)
	{
		this.weaponModelLocation = weaponModelLocation;
		createModel(this.weaponModelLocation);
		loadWeaponMetadata();

		identity = new Matrix4f();
		identity.setIdentity();

		Matrix4f mat = new Matrix4f();
		mat.setIdentity();
		mat.rotY((float)Math.toRadians(180));
		mat.setTranslation(new Vector3f(-0.8f, 0.8f, -0.6f));
		mat.setScale(1.6f);
		transforms.put(ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, mat);

		mat = new Matrix4f();
		mat.setIdentity();
		mat.mul(getCombinedRotation(45f, 135f, 0f));
		mat.setTranslation(new Vector3f(-0.3f, 1.3f, 0f));
		mat.setScale(1.75f);
		transforms.put(ItemCameraTransforms.TransformType.GUI, mat);

		mat = new Matrix4f();
		mat.setIdentity();
		mat.setTranslation(new Vector3f(0.6f, 0.5f, 0.3f));
		mat.setScale(1.2f);
		transforms.put(ItemCameraTransforms.TransformType.GROUND, mat);
	}

	protected void loadWeaponMetadata()
	{
		weaponMetadata = new WeaponMetadataSection();

		try
		{
			IResource metadataResource = Minecraft.getMinecraft().getResourceManager().getResource(weaponModelLocation);
			if (metadataResource.hasMetadata())
			{
				IMetadataSection section = metadataResource.getMetadata("weapon");
				if (section instanceof WeaponMetadataSection)
				{
					weaponMetadata = ((WeaponMetadataSection)section);
				}
			}
		}
		catch (IOException e)
		{
			MOLog.log(Level.ERROR, e, "There was a problem reading weapon metadata from %s", weaponMetadata);
		}
	}

	protected void createModel(ResourceLocation weaponModelLocation)
	{
		try
		{
			weaponModel = (OBJModel)OBJLoader.INSTANCE.loadModel(weaponModelLocation);
			ImmutableMap<String, String> customOptions = new ImmutableMap.Builder<String, String>().put("flip-v", "true").put("ambient", "false").build();
			weaponModel = (OBJModel)weaponModel.process(customOptions);
		}
		catch (Exception e)
		{
			MOLog.error("Missing weapon model.", e);
		}
	}

	@SuppressWarnings("deprecation")
	public void bakeModel()
	{
		List<String> visibleGroups = new ArrayList<>();
		visibleGroups.add(OBJModel.Group.ALL);
		bakedModel = (OBJModel.OBJBakedModel)weaponModel.bake(new OBJModel.OBJState(visibleGroups, true), DefaultVertexFormats.ITEM, input -> {
			return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(input.toString());
		});
	}

	@Override
	public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType type)
	{
		if (type == ItemCameraTransforms.TransformType.GUI) {
			Matrix4f mat = new Matrix4f();
			mat.setIdentity();

			return ImmutablePair.of(this, mat);
		}
		Matrix4f mat = transforms.get(type);
		if (mat == null) mat = identity;
		return ImmutablePair.of(this, mat);
	}

	public void transformFirstPersonWeapon(EnergyWeapon energyWeapon, ItemStack weaponStack, float zoomValue, float recoilValue)
	{
		transformRecoil(recoilValue, zoomValue);
		GlStateManager.translate(0, MOMathHelper.Lerp(0, 0.04, zoomValue), MOMathHelper.Lerp(0, -0.3, zoomValue));
	}

	protected void transformRecoil(float recoilValue, float zoomValue)
	{
		GlStateManager.translate(0, recoilValue * -0.005f, recoilValue * -0.02f);
		GlStateManager.rotate(recoilValue * 0.7f, -1, 0, 0);
	}

	public void renderHand(RenderPlayer renderPlayer)
	{
		renderPlayer.renderLeftArm(Minecraft.getMinecraft().thePlayer);
	}

	public void transformHand(float recoilValue, float zoomValue)
	{
		transformRecoil(recoilValue, zoomValue);
		GlStateManager.translate(MOMathHelper.Lerp(0.01, -0.15, zoomValue), -0.3, 0.4);
		GlStateManager.rotate(MOMathHelper.Lerp(35, 10, zoomValue), 0, 0, 1);
		GlStateManager.rotate(MOMathHelper.Lerp(20, 0, zoomValue), 1, 0, 0);
		GlStateManager.scale(0.4, 0.4, 0.4);
	}

	@Nonnull
	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand)
	{
		return bakedModel.getQuads(state, side, rand);
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

	@Nonnull
	@Override
	public TextureAtlasSprite getParticleTexture()
	{
		return bakedModel.getParticleTexture();
	}

	@Nonnull
	@Override
	@Deprecated
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

	@Nonnull
	@Override
	public ItemOverrideList getOverrides()
	{
		return ItemOverrideList.NONE;
	}

	private static Matrix4f getCombinedRotation(float x, float y, float z) {
		Matrix4f xMat = new Matrix4f();
		xMat.rotX((float)Math.toRadians(x));
		Matrix4f yMat = new Matrix4f();
		yMat.rotY((float)Math.toRadians(y));
		Matrix4f zMat = new Matrix4f();
		zMat.rotZ((float)Math.toRadians(z));
		xMat.mul(yMat);
		xMat.mul(zMat);
		return xMat;
	}

}
