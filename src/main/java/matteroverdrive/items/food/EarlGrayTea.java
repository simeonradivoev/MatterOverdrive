package matteroverdrive.items.food;

import cpw.mods.fml.common.registry.GameRegistry;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Created by Simeon on 4/24/2015.
 */
public class EarlGrayTea extends ItemFood
{
    public EarlGrayTea(String name)
    {
        super(4, 0.8F, false);
        setUnlocalizedName(name);
        setTextureName(Reference.MOD_ID + ":" + name);
        setAlwaysEdible();
    }

    public void Register()
    {
        setCreativeTab(MatterOverdrive.tabMatterOverdrive_food);
        GameRegistry.registerItem(this, this.getUnlocalizedName().substring(5));
    }

    @Override
    public ItemStack onEaten(ItemStack itemStack, World world, EntityPlayer player)
    {
        super.onEaten(itemStack,world,player);

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
        return EnumAction.drink;
    }
}
