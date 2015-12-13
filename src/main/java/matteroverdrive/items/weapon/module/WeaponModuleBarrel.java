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

package matteroverdrive.items.weapon.module;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.api.weapon.IWeaponModule;
import matteroverdrive.init.MatterOverdriveItems;
import matteroverdrive.items.includes.MOBaseItem;
import matteroverdrive.util.MOStringHelper;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

import java.util.List;

/**
 * Created by Simeon on 4/15/2015.
 */
public class WeaponModuleBarrel extends MOBaseItem implements IWeaponModule
{
    public static final int DAMAGE_BARREL_ID = 0;
    public static final int FIRE_BARREL_ID = 1;
    public static final int EXPLOSION_BARREL_ID = 2;
    public static final int HEAL_BARREL_ID = 3;
    public static final String[] names = {"damage","fire","explosion","heal"};
    private IIcon[] icons;

    public WeaponModuleBarrel(String name)
    {
        super(name);
        setCreativeTab(MatterOverdrive.tabMatterOverdrive_modules);
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.setMaxStackSize(1);
    }

    public void register()
    {
        super.register();

        //damage barrel
        GameRegistry.addRecipe(new ItemStack(this,1,0)," G ","RDR"," T ",'T', MatterOverdriveItems.tritanium_plate,'D',MatterOverdriveItems.dilithium_ctystal,'R', Items.redstone,'G', Blocks.glass);
        GameRegistry.addRecipe(new ItemStack(this,1,1)," G ","BFB"," T ",'T', MatterOverdriveItems.tritanium_plate,'F',Items.fire_charge,'B', Items.blaze_rod,'G', Blocks.glass);
        GameRegistry.addRecipe(new ItemStack(this,1,2)," B ","BRB","DTD",'T', MatterOverdriveItems.tritanium_plate,'R',Items.blaze_rod,'B', Blocks.tnt,'G', Blocks.glass,'D',Items.diamond);
        GameRegistry.addRecipe(new ItemStack(this,1,3)," S ","SAS","ETE",'T', MatterOverdriveItems.tritanium_plate,'A',Items.golden_apple,'S', Items.sugar,'G', Blocks.glass,'E',Items.emerald);
    }

    @Override
    public boolean hasDetails(ItemStack itemStack)
    {
        return true;
    }

    @Override
    public void addDetails(ItemStack itemstack, EntityPlayer player, List infos)
    {
        super.addDetails(itemstack, player, infos);
        int damage = itemstack.getItemDamage();
        switch (damage)
        {
            case 0:
                infos.add(MOStringHelper.weaponStatToInfo(Reference.WS_DAMAGE, 1.5f));
                infos.add(MOStringHelper.weaponStatToInfo(Reference.WS_AMMO, 0.5f));
                infos.add(MOStringHelper.weaponStatToInfo(Reference.WS_EFFECT, 0.5f));
                break;
            case 1:
                infos.add(MOStringHelper.weaponStatToInfo(Reference.WS_AMMO, 0.5f));
                infos.add(MOStringHelper.weaponStatToInfo(Reference.WS_DAMAGE, 0.75f));
                infos.add(MOStringHelper.weaponStatToInfo(Reference.WS_FIRE_DAMAGE, 1));
                break;
            case 2:
                infos.add(MOStringHelper.weaponStatToInfo(Reference.WS_EXPLOSION_DAMAGE, 1));
                infos.add(MOStringHelper.weaponStatToInfo(Reference.WS_AMMO, 0.2));
                infos.add(MOStringHelper.weaponStatToInfo(Reference.WS_EFFECT, 0.5));
                infos.add(MOStringHelper.weaponStatToInfo(Reference.WS_FIRE_RATE, 0.15));
                break;
            case 3:
                infos.add(MOStringHelper.weaponStatToInfo(Reference.WS_DAMAGE, 0));
                infos.add(MOStringHelper.weaponStatToInfo(Reference.WS_AMMO, 0.5));
                infos.add(MOStringHelper.weaponStatToInfo(Reference.WS_HEAL, 0.1));
                break;
        }
    }

    @Override
    public int getSlot(ItemStack module)
    {
        return Reference.MODULE_BARREL;
    }

    @Override
    public String getModelPath()
    {
        return null;
    }

    @Override
    public ResourceLocation getModelTexture(ItemStack module) {
        return null;
    }

    @Override
    public String getModelName(ItemStack module) {
        return null;
    }

    @Override
    public float modifyWeaponStat(int statID, ItemStack module, ItemStack weapon, float originalStat)
    {
        int damage = module.getItemDamage();
        switch (damage)
        {
            case 0:
                if (statID == Reference.WS_DAMAGE)
                    return originalStat * 1.5f;
                else if (statID == Reference.WS_AMMO)
                    return originalStat * 0.5f;
                else if (statID == Reference.WS_EFFECT)
                    return originalStat * 0.5f;
                break;
            case 1:
                if (statID == Reference.WS_AMMO)
                    return originalStat * 0.5f;
                if (statID == Reference.WS_DAMAGE)
                    return originalStat * 0.75f;
                if (statID == Reference.WS_FIRE_DAMAGE)
                    return originalStat + 1;
                break;
            case 2:
                if (statID == Reference.WS_EXPLOSION_DAMAGE)
                    return originalStat + 1;
                else if (statID == Reference.WS_AMMO)
                    return originalStat * 0.2f;
                else if (statID == Reference.WS_EFFECT)
                    return originalStat * 0.5f;
                else if (statID == Reference.WS_FIRE_RATE)
                    return originalStat * 0.15f;
                break;
            case 3:
                if (statID == Reference.WS_DAMAGE)
                    return 0;
                else if (statID == Reference.WS_AMMO)
                    return originalStat * 0.5f;
                else if (statID == Reference.WS_HEAL)
                    return originalStat + 0.1f;
                break;
        }
        return originalStat;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs creativeTabs, List list)
    {
        for (int i = 0; i < names.length;i++)
        {
            list.add(new ItemStack(item, 1, i));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int damage)
    {
        if (damage >= 0 && damage < icons.length)
        {
            return icons[damage];
        }
        return null;
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack)
    {
        int damage = itemStack.getItemDamage();
        return this.getUnlocalizedName() + "." + names[damage];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister)
    {
        icons = new IIcon[names.length];

        for (int i = 0; i < names.length;i++)
        {
            icons[i] = iconRegister.registerIcon(Reference.MOD_ID + ":barrel_" + names[i]);
        }
    }
}
