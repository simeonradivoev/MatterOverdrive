package matteroverdrive.api.inventory;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;

/**
 * Created by Simeon on 1/5/2016.
 */
public interface IBlockScanner
{
	RayTraceResult getScanningPos(ItemStack itemStack, EntityLivingBase player);

	boolean destroysBlocks(ItemStack itemStack);

	boolean showsGravitationalWaves(ItemStack itemStack);
}
