package com.MO.MatterOverdrive.items;

import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.items.includes.MOBaseItem;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;

import java.util.List;

/**
 * Created by Simeon on 3/15/2015.
 */
public class Isolinear_circuit extends MOBaseItem
{
    public static final String[] subItemNames = {"mk1","mk2","mk3","mk4"};
    @SideOnly(Side.CLIENT)
    private IIcon[] icons;

    public Isolinear_circuit(String name)
    {
        super(name);
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
    }

    @Override
    public void Register(String name)
    {
        super.Register(name);
        GameRegistry.addRecipe(new ItemStack(this, 1, 0), new Object[]{"II ", "RR ", "GG ", 'G', Item.getItemFromBlock(Blocks.glass), 'R', Items.redstone, 'I', Items.iron_ingot});
        GameRegistry.addShapelessRecipe(new ItemStack(this, 1, 1), new Object[]{new ItemStack(this, 1, 0), Items.gold_ingot, Items.gold_ingot});
        GameRegistry.addShapelessRecipe(new ItemStack(this, 1, 2), new Object[]{new ItemStack(this, 1, 1), Items.diamond, Items.diamond});
        GameRegistry.addShapelessRecipe(new ItemStack(this, 1, 3), new Object[]{new ItemStack(this, 1, 2), Items.emerald, Items.emerald});
    }

    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs creativeTabs, List list)
    {
        list.add(new ItemStack(item, 1, 0));
        list.add(new ItemStack(item, 1, 1));
        list.add(new ItemStack(item, 1, 2));
        list.add(new ItemStack(item, 1, 3));
    }

    /**
     * Gets an icon index based on an item's damage value
     */
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int damage)
    {
        int j = MathHelper.clamp_int(damage, 0, 3);
        return this.icons[j];
    }

    /**
     * Returns the unlocalized name of this item. This version accepts an ItemStack so different stacks can have
     * different names based on their damage or NBT.
     */
    public String getUnlocalizedName(ItemStack stack)
    {
        int i = MathHelper.clamp_int(stack.getItemDamage(), 0, 3);
        return super.getUnlocalizedName() + "." + subItemNames[i];
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister)
    {
        this.icons = new IIcon[subItemNames.length];

        for (int i = 0; i < subItemNames.length; ++i)
        {
            this.icons[i] = iconRegister.registerIcon(Reference.MOD_ID + ":" + getUnlocalizedName().substring(5) + "_" + subItemNames[i]);
        }

        this.itemIcon = this.icons[0];
    }
}
