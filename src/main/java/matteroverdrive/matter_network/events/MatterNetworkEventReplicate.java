package matteroverdrive.matter_network.events;

import matteroverdrive.data.matter_network.IMatterNetworkEvent;
import matteroverdrive.data.matter_network.ItemPattern;
import net.minecraft.item.ItemStack;

/**
 * Created by Simeon on 2/5/2016.
 */
public class MatterNetworkEventReplicate implements IMatterNetworkEvent
{
    public final ItemPattern pattern;
    public int amount;

    public MatterNetworkEventReplicate(ItemPattern itemPattern,int amount)
    {
        this.pattern = itemPattern;
        this.amount = amount;
    }

    public static class Request extends MatterNetworkEventReplicate
    {
        private boolean accepted;

        public Request(ItemPattern itemPattern, int amount)
        {
            super(itemPattern, amount);
        }

        public void markAccepted(){this.accepted = true;}
        public boolean isAccepted(){return accepted;}
    }

    public static class Ccomplete extends MatterNetworkEventReplicate
    {
        public final ItemStack itemStack;

        public Ccomplete(ItemStack itemStack,ItemPattern itemPattern, int amount)
        {
            super(itemPattern, amount);
            this.itemStack = itemStack;
        }
    }
}
