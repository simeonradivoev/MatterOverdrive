package matteroverdrive.api.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;

/**
 * Created by Simeon on 1/5/2016.
 */
public interface IBlockScanner
{
    MovingObjectPosition getScanningPos(ItemStack itemStack,EntityPlayer player);
    boolean destroysBlocks(ItemStack itemStack);
    boolean showsGravitationalWaves(ItemStack itemStack);
}
