package matteroverdrive.items.food;

import matteroverdrive.MatterOverdrive;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by Simeon on 4/24/2015.
 */
public class EarlGrayTea extends ItemFood
{
    public EarlGrayTea(String name)
    {
        super(4, 0.8F, false);
        setUnlocalizedName(name);
        setAlwaysEdible();
    }

    public void Register()
    {
        setCreativeTab(MatterOverdrive.tabMatterOverdrive_food);
        GameRegistry.registerItem(this, this.getUnlocalizedName().substring(5));
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack itemStack, World world, EntityPlayer player)
    {
        super.onItemUseFinish(itemStack,world,player);

        if (!player.capabilities.isCreativeMode)
        {
            --itemStack.stackSize;
        }

        if (!world.isRemote)
        {
            player.curePotionEffects(itemStack);
        }

        if (itemStack.stackSize > 0)
        {
            player.inventory.addItemStackToInventory(new ItemStack(Items.glass_bottle));
        }

        return itemStack.stackSize <= 0 ? new ItemStack(Items.glass_bottle) : itemStack;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack itemStack)
    {
        return EnumAction.DRINK;
    }
}
