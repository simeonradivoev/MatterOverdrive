package matteroverdrive.data.inventory;

import matteroverdrive.container.slot.SlotMatter;
import matteroverdrive.proxy.ClientProxy;
import matteroverdrive.util.MatterHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

/**
 * Created by Simeon on 3/16/2015.
 */
public class MatterSlot extends Slot
{
    public MatterSlot(boolean isMainSlot) {
        super(isMainSlot);
    }

    @Override
    public boolean isValidForSlot(ItemStack itemStack)
    {
        return MatterHelper.containsMatter(itemStack);
    }

    @Override
    public IIcon getTexture()
    {
        return ClientProxy.holoIcons.getIcon("decompose");
    }

    @Override
    boolean isEqual(net.minecraft.inventory.Slot slot)
    {
        return slot instanceof SlotMatter;
    }
}
