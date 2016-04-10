package matteroverdrive.data;

import net.minecraft.entity.player.EntityPlayer;

/**
 * Created by Simeon on 3/16/2015.
 */
public interface IUsableCondition
{
	boolean usableByPlayer(EntityPlayer player);
}
