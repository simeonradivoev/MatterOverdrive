package matteroverdrive.data.inventory;

import matteroverdrive.api.matter.IMatterPatternStorage;
import matteroverdrive.proxy.ClientProxy;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

/**
 * Created by Simeon on 3/27/2015.
 */
public class PatternStorageSlot extends Slot
{
    public PatternStorageSlot(boolean isMainSlot) {
        super(isMainSlot);
    }

    @Override
    public boolean isValidForSlot(ItemStack item)
    {
        return item.getItem() instanceof IMatterPatternStorage;
    }

    @Override
    public IIcon getTexture()
    {
        return ClientProxy.holoIcons.getIcon("pattern_storage");
    }
}
