package matteroverdrive.client.render.weapons;

import matteroverdrive.api.weapon.IWeaponModule;
import matteroverdrive.client.RenderHandler;
import matteroverdrive.client.render.weapons.layers.IWeaponLayer;
import matteroverdrive.client.render.weapons.modules.IModuleRender;
import matteroverdrive.client.resources.data.WeaponMetadataSection;
import matteroverdrive.handler.weapon.ClientWeaponHandler;
import matteroverdrive.items.weapon.EnergyWeapon;
import matteroverdrive.util.MOInventoryHelper;
import matteroverdrive.util.RenderUtils;
import matteroverdrive.util.WeaponHelper;
import matteroverdrive.util.animation.MOEasing;
import matteroverdrive.util.math.MOMathHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderSpecificHandEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Simeon on 2/18/2016.
 */
public class WeaponRenderHandler
{
	private final Minecraft mc = Minecraft.getMinecraft();
	private final Map<Class<? extends IWeaponModule>, IModuleRender> moduleRenders;
	private final List<IWeaponLayer> weaponLayers;

	public WeaponRenderHandler()
	{
		this.moduleRenders = new HashMap<>();
		weaponLayers = new ArrayList<>();
	}

	@SubscribeEvent
	public void onHandRender(RenderSpecificHandEvent event)
	{
		ItemStack weapon = event.getItemStack();

		if (weapon != null && weapon.getItem() instanceof EnergyWeapon)
		{
			event.setCanceled(true);

			GlStateManager.clear(GL11.GL_DEPTH_BUFFER_BIT);
			EntityRenderer entityRenderer = mc.entityRenderer;
			GlStateManager.matrixMode(GL11.GL_PROJECTION);
			GlStateManager.loadIdentity();
			float f = 0.07F;

			Project.gluPerspective(35f, (float)mc.displayWidth / (float)mc.displayHeight, 0.05F, (float)(mc.gameSettings.renderDistanceChunks * 32));
			GlStateManager.matrixMode(GL11.GL_MODELVIEW);
			GlStateManager.loadIdentity();

			GlStateManager.pushMatrix();
			hurtCameraEffect(event.getPartialTicks());

			if (mc.gameSettings.viewBobbing)
			{
				setupViewBobbing(event.getPartialTicks());
			}

			boolean flag = this.mc.getRenderViewEntity() instanceof EntityLivingBase && ((EntityLivingBase)this.mc.getRenderViewEntity()).isPlayerSleeping();

			if (this.mc.gameSettings.thirdPersonView == 0 && !flag && !this.mc.gameSettings.hideGUI && !this.mc.playerController.isSpectator())
			{
				float zoomValue = MOEasing.Sine.easeInOut(ClientWeaponHandler.ZOOM_TIME, 0, 1, 1f);
				float recoilValue = MOEasing.Quad.easeInOut(ClientWeaponHandler.RECOIL_TIME, 0, 1, 1f) * MathHelper.clamp_float(ClientWeaponHandler.RECOIL_AMOUNT, 0, 20);
				transformFirstPerson(zoomValue);

				WeaponItemRenderer model = getWeaponModel(weapon);
				if (model != null)
				{
					EntityPlayerSP player = this.mc.thePlayer;
					float f1 = player.getSwingProgress(event.getPartialTicks());
					float f2 = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * event.getPartialTicks();
					float f3 = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * event.getPartialTicks();
					this.rotateAroundXAndY(f2, f3);
					this.setLightmap(player);
					this.rotateArm(model, player, event.getPartialTicks());
					GlStateManager.enableRescaleNormal();

					entityRenderer.enableLightmap();
					Render<AbstractClientPlayer> render = Minecraft.getMinecraft().getRenderManager().getEntityRenderObject(Minecraft.getMinecraft().thePlayer);
					if (render instanceof RenderPlayer)
					{
						GlStateManager.pushMatrix();
						RenderUtils.bindTexture(player.getLocationSkin());
						model.transformHand(recoilValue, zoomValue);
						model.renderHand((RenderPlayer)render);
						GlStateManager.popMatrix();
					}

					List<ItemStack> modules = MOInventoryHelper.getStacks(weapon);
					transformFromModules(modules, model.getWeaponMetadata(), weapon, event.getPartialTicks(), zoomValue);
					model.transformFirstPersonWeapon((EnergyWeapon)weapon.getItem(), weapon, zoomValue, recoilValue);
					renderWeaponAndModules(modules, model, weapon, event.getPartialTicks());
					renderLayers(model.getWeaponMetadata(), weapon, event.getPartialTicks());
					entityRenderer.disableLightmap();
				}

				RenderHelper.disableStandardItemLighting();
			}

			GlStateManager.popMatrix();

			if (this.mc.gameSettings.viewBobbing)
			{
				this.setupViewBobbing(event.getPartialTicks());
			}

		}
	}

	public WeaponItemRenderer getWeaponModel(ItemStack weaponStack)
	{
		IBakedModel bakedModel = mc.getRenderItem().getItemModelMesher().getItemModel(weaponStack);
		if (bakedModel instanceof WeaponItemRenderer)
		{
			return (WeaponItemRenderer)bakedModel;
		}
		return null;
	}

	public void renderWeaponAndModules(List<ItemStack> modules, WeaponItemRenderer model, ItemStack weapon, float partialTicks)
	{
		renderWeapon(model, weapon);
		renderModules(modules, model.getWeaponMetadata(), weapon, partialTicks);
	}

	public void onModelBake(TextureMap textureMap, RenderHandler renderHandler)
	{
		for (IModuleRender render : moduleRenders.values())
		{
			render.onModelBake(textureMap, renderHandler);
		}
	}

	public void onTextureStich(TextureMap textureMap, RenderHandler renderHandler)
	{
		for (IModuleRender render : moduleRenders.values())
		{
			render.onTextureStich(textureMap, renderHandler);
		}
	}

	private void rotateAroundXAndY(float angleX, float angleY)
	{
		GlStateManager.pushMatrix();
		GlStateManager.rotate(angleX, 1.0F, 0.0F, 0.0F);
		GlStateManager.rotate(angleY, 0.0F, 1.0F, 0.0F);
		RenderHelper.enableStandardItemLighting();
		GlStateManager.popMatrix();
	}

	private void setLightmap(EntityPlayerSP player)
	{
		int i = this.mc.theWorld.getCombinedLight(new BlockPos(player.posX, player.posY + (double)player.getEyeHeight(), player.posZ), 0);
		float f = (float)(i & 65535);
		float f1 = (float)(i >> 16);
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, f, f1);
	}

	private void rotateArm(WeaponItemRenderer weaponItemRenderer, EntityPlayerSP player, float partialTicks)
	{
		float f = player.prevRenderArmPitch + (player.renderArmPitch - player.prevRenderArmPitch) * partialTicks;
		float f1 = player.prevRenderArmYaw + (player.renderArmYaw - player.prevRenderArmYaw) * partialTicks;
		GlStateManager.rotate((player.rotationPitch - f) * 0.1F, 1.0F, 0.0F, 0.0F);
		GlStateManager.rotate((player.rotationYaw - f1) * weaponItemRenderer.getHorizontalSpeed(), 0.0F, 1.0F, 0.0F);
	}

	private void renderWeapon(WeaponItemRenderer model, ItemStack weapon)
	{
		renderModel(model, weapon);
	}

	private void transformFromModules(List<ItemStack> modules, WeaponMetadataSection weaponMeta, ItemStack weapon, float ticks, float zoomValue)
	{
		if (modules != null)
		{
			for (ItemStack module : modules)
			{
				IModuleRender render = moduleRenders.get(module.getItem().getClass());
				if (render != null)
				{
					render.transformWeapon(weaponMeta, weapon, module, ticks, zoomValue);
				}
			}
		}
	}

	private void renderModules(List<ItemStack> modules, WeaponMetadataSection weaponMeta, ItemStack weapon, float ticks)
	{
		if (modules != null)
		{
			for (ItemStack module : modules)
			{
				IModuleRender render = moduleRenders.get(module.getItem().getClass());
				if (render != null)
				{
					GlStateManager.pushMatrix();
					render.renderModule(weaponMeta, weapon, module, ticks);
					GlStateManager.popMatrix();
				}
			}
		}
	}

	private void renderLayers(WeaponMetadataSection weaponMeta, ItemStack weapon, float ticks)
	{
		for (IWeaponLayer layer : weaponLayers)
		{
			layer.renderLayer(weaponMeta, weapon, ticks);
		}
	}

	public void renderModel(IBakedModel model, ItemStack weapon)
	{
		RenderUtils.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		Tessellator tessellator = Tessellator.getInstance();
		VertexBuffer worldrenderer = tessellator.getBuffer();
		worldrenderer.begin(7, DefaultVertexFormats.ITEM);

		for (EnumFacing enumfacing : EnumFacing.values())
		{
			this.renderQuads(worldrenderer, model.getQuads(null, enumfacing, 0), -1, weapon);
		}

		this.renderQuads(worldrenderer, model.getQuads(null, null, 0), WeaponHelper.getColor(weapon), weapon);
		tessellator.draw();
	}

	public IBakedModel getItemModel(ItemStack weapon)
	{
		return mc.getRenderItem().getItemModelMesher().getItemModel(weapon);
	}

	private void renderQuads(VertexBuffer renderer, List<BakedQuad> quads, int color, ItemStack stack)
	{
		boolean flag = color == -1 && stack != null;
		int i = 0;

		for (int j = quads.size(); i < j; ++i)
		{
			BakedQuad bakedquad = quads.get(i);
			int k = color;

			// TODO: 3/25/2016 Find how get get stack color
			/*if (flag && bakedquad.hasTintIndex())
			{
                k = stack.getItem().getColorFromItemStack(stack, bakedquad.getTintIndex());

                if (EntityRenderer.anaglyphEnable)
                {
                    k = TextureUtil.anaglyphColor(k);
                }

                k = k | -16777216;
            }*/

			net.minecraftforge.client.model.pipeline.LightUtil.renderQuadColorSlow(renderer, bakedquad, k);
		}
	}

	@SubscribeEvent
	public void handleCameraRecoil(EntityViewRenderEvent.CameraSetup event)
	{
		event.setRoll(event.getRoll() + ClientWeaponHandler.CAMERA_RECOIL_AMOUNT * ClientWeaponHandler.CAMERA_RECOIL_TIME);
		event.setPitch(event.getPitch() + Math.abs(ClientWeaponHandler.CAMERA_RECOIL_AMOUNT) * ClientWeaponHandler.CAMERA_RECOIL_TIME * 0.5f);
	}

	private void transformFirstPerson(float zoomValue)
	{
		GlStateManager.translate(MOMathHelper.Lerp(0.13f, 0, zoomValue), -0.18, -0.55);
		GlStateManager.rotate(MOMathHelper.Lerp(185, 180, zoomValue), 0, 1, 0);
		GlStateManager.rotate(MOMathHelper.Lerp(3, 0, zoomValue), -1, 0, 0);
		GlStateManager.scale(1, 1, 0.8);
		//GlStateManager.rotate(-80,0,1,0);
	}

	private void setupViewBobbing(float partialTicks)
	{
		if (this.mc.getRenderViewEntity() instanceof EntityPlayer)
		{
			EntityPlayer entityplayer = (EntityPlayer)this.mc.getRenderViewEntity();
			float f = entityplayer.distanceWalkedModified - entityplayer.prevDistanceWalkedModified;
			float f1 = -(entityplayer.distanceWalkedModified + f * partialTicks);
			float f2 = entityplayer.prevCameraYaw + (entityplayer.cameraYaw - entityplayer.prevCameraYaw) * partialTicks;
			float f3 = entityplayer.prevCameraPitch + (entityplayer.cameraPitch - entityplayer.prevCameraPitch) * partialTicks;
			GlStateManager.translate(MathHelper.sin(f1 * (float)Math.PI) * f2 * 0.05F, -Math.abs(MathHelper.cos(f1 * (float)Math.PI) * f2) * 0.1f, 0.0F);
			GlStateManager.rotate(MathHelper.sin(f1 * (float)Math.PI) * f2 * 0f, 0.0F, 0.0F, 1.0F);
			GlStateManager.rotate(Math.abs(MathHelper.cos(f1 * (float)Math.PI - 0.2F) * f2) * 1f, 1.0F, 0.0F, 0.0F);
			GlStateManager.rotate(f3, 1.0F, 0.0F, 0.0F);
		}
	}

	private void hurtCameraEffect(float partialTicks)
	{
		if (this.mc.getRenderViewEntity() instanceof EntityLivingBase)
		{
			EntityLivingBase entitylivingbase = (EntityLivingBase)this.mc.getRenderViewEntity();
			float f = (float)entitylivingbase.hurtTime - partialTicks;

			if (entitylivingbase.getHealth() <= 0.0F)
			{
				float f1 = (float)entitylivingbase.deathTime + partialTicks;
				GlStateManager.rotate(40.0F - 8000.0F / (f1 + 200.0F), 0.0F, 0.0F, 1.0F);
			}

			if (f < 0.0F)
			{
				return;
			}

			f = f / (float)entitylivingbase.maxHurtTime;
			f = MathHelper.sin(f * f * f * f * (float)Math.PI);
			float f2 = entitylivingbase.attackedAtYaw;
			GlStateManager.rotate(-f2, 0.0F, 1.0F, 0.0F);
			GlStateManager.rotate(-f * 14.0F, 0.0F, 0.0F, 1.0F);
			GlStateManager.rotate(f2, 0.0F, 1.0F, 0.0F);
		}
	}

	public void addModuleRender(Class<? extends IWeaponModule> moduleClass, IModuleRender render)
	{
		this.moduleRenders.put(moduleClass, render);
	}

	public void addWeaponLayer(IWeaponLayer layer)
	{
		this.weaponLayers.add(layer);
	}
}
