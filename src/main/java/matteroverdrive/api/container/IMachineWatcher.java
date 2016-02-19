package matteroverdrive.api.container;

import matteroverdrive.machines.MOTileEntityMachine;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Created by Simeon on 2/6/2016.
 */
public interface IMachineWatcher
{
    EntityPlayer getPlayer();
    void onWatcherAdded(MOTileEntityMachine machine);
    boolean isWatcherValid();
}
