package matteroverdrive.data.matter;

import net.minecraft.item.ItemStack;

/**
 * Created by Simeon on 1/17/2016.
 */
public class DamageAwareStackHandler extends MatterEntryHandlerAbstract<ItemStack>
{
    private final int damage;
    private final int matter;
    private final boolean finalHandle;

    public DamageAwareStackHandler(int damage,int matter)
    {
        this.damage = damage;
        this.matter = matter;
        this.finalHandle = false;
    }

    public DamageAwareStackHandler(int damage,int matter,boolean finalHandle)
    {
        this.damage = damage;
        this.matter = matter;
        this.finalHandle = finalHandle;
    }

    public DamageAwareStackHandler(int damage,int matter,boolean finalHandle,int priority)
    {
        this.priority = priority;
        this.damage = damage;
        this.matter = matter;
        this.finalHandle = finalHandle;
    }

    @Override
    public int modifyMatter(ItemStack itemStack,int originalMatter)
    {
        if (itemStack.getItemDamage() == damage)
        {
            return matter;
        }
        return originalMatter;
    }

    @Override
    public boolean finalModification(ItemStack itemStack)
    {
        if (itemStack.getItemDamage() == damage)
        {
            return finalHandle;
        }
        return false;
    }
}
