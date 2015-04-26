package com.MO.MatterOverdrive.items;

import cofh.lib.gui.GuiColor;
import com.MO.MatterOverdrive.MatterOverdrive;
import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.api.weapon.IWeaponModule;
import com.MO.MatterOverdrive.items.includes.MOBaseItem;
import com.MO.MatterOverdrive.util.MOStringHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.common.DungeonHooks;
import net.minecraftforge.common.MinecraftForge;

import java.util.List;
import java.util.Objects;

/**
 * Created by Simeon on 4/14/2015.
 */
public class WeaponColorModule extends MOBaseItem implements IWeaponModule
{
    private IIcon overlayIcon;
    public static final GuiColor defaultColor = new GuiColor(255,255,255);
    public static final GuiColor colors[] = {
            new GuiColor(204,0,0),      //red
            new GuiColor(0,153,51),     //green
            new GuiColor(0,102,255),    //blue
            new GuiColor(102,51,51),    //brown
            new GuiColor(255,153,255),  //pink
            new GuiColor(153,204,255),  //sky blue
            new GuiColor(212,175,55),   //gold
            new GuiColor(102,255,102),  //lime green
            new GuiColor(30,30,30),     //black
            new GuiColor(128,128,128)   //grey
            };
    public static final String names[] = {"red","green","blue","brown","pink","sky_blue","gold","lime_green","black","grey"};

    public WeaponColorModule(String name)
    {
        super(name);
        setCreativeTab(MatterOverdrive.tabMatterOverdrive_modules);
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.setMaxStackSize(1);
    }

    public void addToDunguns()
    {
        for (int i = 0;i < colors.length;i++)
        {
            int devider = 1;
            if (i == 6)
                devider = 4;

            ChestGenHooks.getInfo(ChestGenHooks.DUNGEON_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(this,1,i),1,1,10 / devider));
            ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_DESERT_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(this,1,i),1,1,20 / devider));
            ChestGenHooks.getInfo(ChestGenHooks.MINESHAFT_CORRIDOR).addItem(new WeightedRandomChestContent(new ItemStack(this,1,i),1,1,10 / devider));
            ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CORRIDOR).addItem(new WeightedRandomChestContent(new ItemStack(this,1,i),1,1,20 / devider));
            ChestGenHooks.getInfo(ChestGenHooks.VILLAGE_BLACKSMITH).addItem(new WeightedRandomChestContent(new ItemStack(this,1,i),1,1,20 / devider));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack itemStack, int pass)
    {
        if (pass == 1 && itemStack != null && itemStack.getItem() != null)
        {
            return colors[itemStack.getItemDamage()].getColor();
        }else
        {
            return 16777215;
        }
    }

    @Override
    public String getItemStackDisplayName(ItemStack itemStack)
    {
        return ("" + StatCollector.translateToLocal(this.getUnlocalizedNameInefficiently(itemStack) + ".name")).trim() + " ("+ MOStringHelper.translateToLocal("module.color." + names[itemStack.getItemDamage()])+")";
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister)
    {
        super.registerIcons(iconRegister);
        overlayIcon = iconRegister.registerIcon(Reference.MOD_ID + ":" + "weapon_module_color_overlay");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamageForRenderPass(int damage, int pass)
    {
        return pass == 1 ? this.overlayIcon : super.getIconFromDamageForRenderPass(damage, pass);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses()
    {
        return true;
    }

    @Override
    public int getSlot(ItemStack itemStack)
    {
        return Reference.MODULE_COLOR;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs creativeTabs, List list)
    {
        for (int i = 0; i < colors.length;i++)
        {
            list.add(new ItemStack(item, 1, i));
        }
    }

    @Override
    public Object getValue(ItemStack itemStack)
    {
        int damage = itemStack.getItemDamage();
        if (damage >= 0 && damage < colors.length)
        {
            return colors[damage];
        }
        return defaultColor;
    }
}
