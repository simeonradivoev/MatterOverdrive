package matteroverdrive.container;

import net.minecraft.entity.player.EntityPlayer;

/**
 * Created by Simeon on 5/1/2015.
 */
public class ContainerMatterScanner extends MOBaseContainer
{

    public ContainerMatterScanner()
    {
        super(null);
    }

    @Override
    public boolean canInteractWith(EntityPlayer p_75145_1_) {
        return false;
    }
}
