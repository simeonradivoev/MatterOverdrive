/*
 * This file is part of Matter Overdrive
 * Copyright (c) 2015., Simeon Radivoev, All rights reserved.
 *
 * Matter Overdrive is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Matter Overdrive is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Matter Overdrive.  If not, see <http://www.gnu.org/licenses>.
 */

package matteroverdrive.items;

import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.api.inventory.IUpgrade;
import matteroverdrive.api.inventory.UpgradeTypes;
import matteroverdrive.init.MatterOverdriveItems;
import matteroverdrive.items.includes.MOBaseItem;
import matteroverdrive.util.MOStringHelper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Simeon on 4/10/2015.
 */
public class ItemUpgrade extends MOBaseItem implements IUpgrade
{
    public static final String[] subItemNames = {"base","speed","power","failsafe","range","power_storage","hyper_speed","matter_storage"};

    public ItemUpgrade(String name)
    {
        super(name);
        this.setMaxStackSize(16);
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        setCreativeTab(MatterOverdrive.tabMatterOverdrive_upgrades);
    }

    @Override
    public boolean hasDetails(ItemStack itemStack)
    {
        int damage = itemStack.getItemDamage();
        return damage != 0;
    }

    public int getMetadata(int damage)
    {
        return damage;
    }

    @Override
    public void addDetails(ItemStack itemstack, EntityPlayer player, List infos)
    {
        super.addDetails(itemstack,player,infos);
        Map<UpgradeTypes,Double> stats = getUpgrades(itemstack);
        for (final Map.Entry<UpgradeTypes, Double> entry : stats.entrySet())
        {
            infos.add(MOStringHelper.toInfo(entry.getKey(),entry.getValue()));
        }
    }

    @Override
    public void register(String name)
    {
        super.register(name);
        GameRegistry.addRecipe(new ItemStack(this, 1, 0), " R ", " C ", " T ", 'G', Blocks.glass, 'R', Items.redstone, 'T', MatterOverdriveItems.tritanium_plate, 'C', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 0));
        //speed
        GameRegistry.addRecipe(new ItemStack(this,1,1)," R ","GUG"," E ",'U',this,'G',Items.glowstone_dust,'R',Items.redstone,'E',Items.emerald);
        //power
        GameRegistry.addRecipe(new ItemStack(this,1,2)," B ","RUR"," C ",'U',this,'B',MatterOverdriveItems.battery,'R',Items.redstone,'C',Items.quartz);
        //failsafe
        GameRegistry.addRecipe(new ItemStack(this, 1, 3), " D ", "RUR", " G ", 'U', this, 'D', Items.diamond, 'R', Items.redstone, 'G', Items.gold_ingot);
        //range
        GameRegistry.addRecipe(new ItemStack(this, 1, 4), " E ", "RUR", " G ", 'U', this, 'E', Items.ender_pearl, 'R', Items.redstone, 'G', Items.gold_ingot);
        //power storage
        GameRegistry.addRecipe(new ItemStack(this, 1, 5), "   ", "RUR", " B ", 'U', this, 'B', MatterOverdriveItems.hc_battery, 'R', Items.redstone, 'G', Items.gold_ingot);
        //hyper speed
        GameRegistry.addShapelessRecipe(new ItemStack(this, 1, 6), MatterOverdriveItems.dilithium_ctystal,Items.nether_star,new ItemStack(this,1,1));
        //matter storage
        GameRegistry.addRecipe(new ItemStack(this, 1, 7), " R ", "MUM", " R ", 'U', this, 'M', MatterOverdriveItems.s_magnet, 'R',Items.redstone);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs creativeTabs, List list)
    {
        for (int i = 0; i < subItemNames.length;i++)
        {
            list.add(new ItemStack(item, 1, i));
        }
    }

    /*@Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int damage)
    {
        int j = MathHelper.clamp_int(damage, 0, (icons.length-1));
        return this.icons[j];
    }*/

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        int i = MathHelper.clamp_int(stack.getItemDamage(), 0, (subItemNames.length-1));
        return super.getUnlocalizedName() + "." + subItemNames[i];
    }

    /*@SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister)
    {
        this.icons = new IIcon[subItemNames.length];

        for (int i = 0; i < subItemNames.length; ++i)
        {
            this.icons[i] = iconRegister.registerIcon(Reference.MOD_ID + ":" + getUnlocalizedName().substring(5) + "_" + subItemNames[i]);
        }

        this.itemIcon = this.icons[0];
    }*/


    @Override
    public Map<UpgradeTypes, Double> getUpgrades(ItemStack itemStack)
    {
        HashMap<UpgradeTypes,Double> upgrades = new HashMap<>();
        int damage = itemStack.getItemDamage();
        switch (damage)
        {
            case 1:
                //the speed upgrade
                upgrades.put(UpgradeTypes.Speed,0.75);
                upgrades.put(UpgradeTypes.PowerUsage,1.25);
                upgrades.put(UpgradeTypes.Fail,1.25);
                upgrades.put(UpgradeTypes.MatterUsage,1.25);
                break;
            case 2:
                //less power upgrade
                upgrades.put(UpgradeTypes.Speed,1.5);
                upgrades.put(UpgradeTypes.PowerUsage,0.75);
                upgrades.put(UpgradeTypes.Fail,1.25);
                break;
            case 3:
                //less chance to fail upgrade
                upgrades.put(UpgradeTypes.Fail,0.5);
                upgrades.put(UpgradeTypes.Speed,1.25);
                upgrades.put(UpgradeTypes.PowerUsage,1.25);
                upgrades.put(UpgradeTypes.MatterUsage,1.25);
                break;
            case 4:
                //range upgrade
                upgrades.put(UpgradeTypes.Range,4d);
                upgrades.put(UpgradeTypes.PowerUsage,1.5);
                upgrades.put(UpgradeTypes.MatterUsage,1.5);
                break;
            case 5:
                upgrades.put(UpgradeTypes.PowerStorage,2d);
                break;
                //hyper speed
            case 6:
                upgrades.put(UpgradeTypes.Speed,0.15);
                upgrades.put(UpgradeTypes.PowerUsage,2d);
                upgrades.put(UpgradeTypes.MatterUsage,2d);
                upgrades.put(UpgradeTypes.Fail,1.25);
                break;
            case 7:
                upgrades.put(UpgradeTypes.MatterStorage,2d);
        }
        return upgrades;
    }

    @Override
    public UpgradeTypes getMainUpgrade(ItemStack itemStack) {
        int damage = itemStack.getItemDamage();
        switch (damage)
        {
            case 1:
                //the speed upgrade
                return UpgradeTypes.Speed;
            case 2:
                //less power upgrade
                return UpgradeTypes.PowerUsage;
            case 3:
                //less chance to fail upgrade
                return UpgradeTypes.Fail;
            case 4:
                //range upgrade
                return UpgradeTypes.Range;
            case 5:
                return UpgradeTypes.PowerStorage;
            //hyper speed
            case 6:
                return UpgradeTypes.Speed;
            case 7:
                return UpgradeTypes.MatterStorage;
        }
        return null;
    }
}
