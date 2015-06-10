package com.MO.MatterOverdrive.items;

import com.MO.MatterOverdrive.MatterOverdrive;
import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.items.includes.MOBaseItem;
import cpw.mods.fml.common.registry.GameRegistry;
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
        setCreativeTab(MatterOverdrive.tabMatterOverdrive);
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
