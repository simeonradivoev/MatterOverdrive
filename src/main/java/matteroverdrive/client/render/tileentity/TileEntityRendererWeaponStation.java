package matteroverdrive.client.render.tileentity;

import matteroverdrive.tile.TileEntityWeaponStation;
import matteroverdrive.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;

/**
 * Created by Simeon on 4/17/2015.
 */
public class TileEntityRendererWeaponStation extends TileEntityRendererStation<TileEntityWeaponStation>
{

	EntityItem itemEntity;

	public TileEntityRendererWeaponStation()
	{
		super();
	}

	@Override
	protected void renderHologram(TileEntityWeaponStation weaponStation, double x, double y, double z, float partialTicks, double noise)
	{
		if (isUsable(weaponStation))
		{
			ItemStack stack = weaponStation.getStackInSlot(weaponStation.INPUT_SLOT);
			if (stack != null)
			{
				if (itemEntity == null)
				{
					itemEntity = new EntityItem(weaponStation.getWorld(), weaponStation.getPos().getX(), weaponStation.getPos().getY(), weaponStation.getPos().getZ(), stack);
				}
				else if (!ItemStack.areItemStacksEqual(itemEntity.getEntityItem(), stack))
				{
					itemEntity.setEntityItemStack(stack);
				}

				itemEntity.hoverStart = weaponStation.getWorld().getWorldTime();
				GlStateManager.translate(x + 0.5f, y + 0.8f, z + 0.5f);
				GlStateManager.scale(0.5, 0.5, 0.5);
				RenderHelper.enableStandardItemLighting();
				GlStateManager.rotate(getWorld().getWorldTime(), 0, 1, 0);
				RenderUtils.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
				IBakedModel model = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(stack);
				model = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(model, ItemCameraTransforms.TransformType.GROUND, false);
				Minecraft.getMinecraft().getRenderItem().renderItem(stack, model);
				RenderHelper.disableStandardItemLighting();
			}
		}
		else
		{
			super.renderHologram(weaponStation, x, y, z, partialTicks, noise);
		}
	}
}

