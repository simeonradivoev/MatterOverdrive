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

package matteroverdrive.util;

import cofh.api.energy.IEnergyContainerItem;
import matteroverdrive.Reference;
import matteroverdrive.api.weapon.IWeapon;
import matteroverdrive.api.weapon.IWeaponModule;
import matteroverdrive.data.WeightedRandomItemStack;
import matteroverdrive.init.MatterOverdriveItems;
import matteroverdrive.items.weapon.EnergyWeapon;
import matteroverdrive.items.weapon.module.WeaponModuleBarrel;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.WeightedRandom;

import java.util.*;

/**
 * Created by Simeon on 11/16/2015.
 */
public class WeaponFactory
{
    public static final int MAX_LOOT_LEVEL = 3;
    private Random random;
    public List<WeightedRandomItemStack> weapons;
    public List<WeightedRandomWeaponModule> barrelModules;
    public List<WeightedRandomWeaponModule> batteryModules;
    public List<WeightedRandomWeaponModule> otherModules;

    public WeaponFactory()
    {
        this.random = new Random();
        this.barrelModules = new ArrayList<>();
        this.batteryModules = new ArrayList<>();
        this.otherModules = new ArrayList<>();
        this.weapons = new ArrayList<>();
    }

    public void initModules()
    {
        barrelModules.add(new WeightedRandomWeaponModule(null,200,0,MAX_LOOT_LEVEL));
        barrelModules.add(new WeightedRandomWeaponModule(new ItemStack(MatterOverdriveItems.weapon_module_barrel,1, WeaponModuleBarrel.DAMAGE_BARREL_ID),100,1,MAX_LOOT_LEVEL));
        barrelModules.add(new WeightedRandomWeaponModule(new ItemStack(MatterOverdriveItems.weapon_module_barrel,1, WeaponModuleBarrel.FIRE_BARREL_ID),10,1,MAX_LOOT_LEVEL));
        barrelModules.add(new WeightedRandomWeaponModule(new ItemStack(MatterOverdriveItems.weapon_module_barrel,1, WeaponModuleBarrel.EXPLOSION_BARREL_ID),5,2,MAX_LOOT_LEVEL));

        batteryModules.add(new WeightedRandomWeaponModule(new ItemStack(MatterOverdriveItems.battery),100,1,MAX_LOOT_LEVEL));
        batteryModules.add(new WeightedRandomWeaponModule(new ItemStack(MatterOverdriveItems.hc_battery),20,1,MAX_LOOT_LEVEL));

        otherModules.add(new WeightedRandomWeaponModule(null,300,0,MAX_LOOT_LEVEL));
        otherModules.add(new WeightedRandomWeaponModule(new ItemStack(MatterOverdriveItems.sniperScope),10,1,MAX_LOOT_LEVEL));
    }

    public void initWeapons()
    {
        weapons.add(new WeightedRandomItemStack(new ItemStack(MatterOverdriveItems.phaserRifle),70));
        weapons.add(new WeightedRandomItemStack(new ItemStack(MatterOverdriveItems.omniTool),30));
        weapons.add(new WeightedRandomItemStack(new ItemStack(MatterOverdriveItems.plasmaShotgun),10));
        weapons.add(new WeightedRandomItemStack(new ItemStack(MatterOverdriveItems.ionSniper),5));
    }

    public ItemStack getRandomDecoratedEnergyWeapon(WeaponGenerationContext context)
    {
        ItemStack weapon = getRandomEnergyWeapon(context);
        context.setWeaponStack(weapon);
        decorateWeapon(weapon,context);
        if (context.fullCharge)
        {
            ((EnergyWeapon)weapon.getItem()).rechargeFully(weapon);
        }
        return weapon;
    }

    public ItemStack getRandomEnergyWeapon(WeaponGenerationContext context)
    {
        ItemStack weapon;
        weapon = ((WeightedRandomItemStack)WeightedRandom.getRandomItem(random,weapons)).getStack();
        if (context.fullCharge)
        {
            ((EnergyWeapon)weapon.getItem()).rechargeFully(weapon);
        }
        return weapon;
    }

    public void decorateWeapon(ItemStack weapon,WeaponGenerationContext context)
    {
        WeightedRandomWeaponModule barrelModule = null;
        if (context.barrel) {
            barrelModule = getRandomModule(random, barrelModules, context);
            if (barrelModule != null && barrelModule.getWeaponModule() != null) {
                WeaponHelper.setModuleAtSlot(barrelModule.getModuleSlot(), weapon, barrelModule.getWeaponModule());
            }
        }

        if (context.battery) {
            WeightedRandomWeaponModule battery = getRandomModule(random, batteryModules, context);
            if (battery != null && battery.getWeaponModule() != null) {
                WeaponHelper.setModuleAtSlot(battery.getModuleSlot(), weapon, battery.getWeaponModule());
            }
        }

        if (context.other)
        {
            WeightedRandomWeaponModule other = getRandomModule(random,otherModules,context);
            if (other != null)
            {
                if (other.getWeaponModule() != null)
                {
                    WeaponHelper.setModuleAtSlot(other.getModuleSlot(),weapon,other.getWeaponModule());
                }
            }else
            {
                if (barrelModule != null && barrelModule.weaponModule != null)
                {
                    setColorModuleBasedOnBarrel(weapon,barrelModule.weaponModule);
                }
            }
        }

        if (context.legendary)
        {
            modifyToLegendary(weapon,context);
        }
    }

    public void setColorModuleBasedOnBarrel(ItemStack weapon,ItemStack barrel)
    {
        if (barrel.getItem() instanceof WeaponModuleBarrel)
        {
            if (barrel.getItemDamage() == WeaponModuleBarrel.EXPLOSION_BARREL_ID)
            {
                //gold
                WeaponHelper.setModuleAtSlot(1, weapon, new ItemStack(MatterOverdriveItems.weapon_module_color,1, 6));
            }else if (barrel.getItemDamage() == WeaponModuleBarrel.FIRE_BARREL_ID)
            {
                //red
                WeaponHelper.setModuleAtSlot(1, weapon, new ItemStack(MatterOverdriveItems.weapon_module_color,1));
            }else if (barrel.getItemDamage() == WeaponModuleBarrel.DAMAGE_BARREL_ID)
            {
                //blue
                WeaponHelper.setModuleAtSlot(1, weapon, new ItemStack(MatterOverdriveItems.weapon_module_color,1, 2));
            }
        }
    }

    public void modifyToLegendary(ItemStack weapon,WeaponGenerationContext context)
    {
        weapon.setStackDisplayName("\u272a " + EnumChatFormatting.GOLD + MOStringHelper.translateToLocal("rarity.legendary") + " " + weapon.getDisplayName());

        int damageLevel = random.nextInt(context.level+1);
        if (damageLevel > 0)
            weapon.getTagCompound().setFloat(EnergyWeapon.CUSTOM_DAMAGE_MULTIPLY_TAG,1 + 0.1f*damageLevel);

        int accuracyLevel = random.nextInt(context.level+1);
        if (accuracyLevel > 0)
            weapon.getTagCompound().setFloat(EnergyWeapon.CUSTOM_ACCURACY_MULTIPLY_TAG,1f - (0.1f*accuracyLevel));

        int shootCooldownLevel = random.nextInt(context.level+1);
        if (shootCooldownLevel > 0)
            weapon.getTagCompound().setFloat(EnergyWeapon.CUSTOM_SPEED_MULTIPLY_TAG,1f - (0.05f*shootCooldownLevel));

        int rangeLevel = random.nextInt(context.level+1);
        if (rangeLevel > 0)
            weapon.getTagCompound().setFloat(EnergyWeapon.CUSTOM_RANGE_MULTIPLY_TAG,1f + 0.15f*rangeLevel);
    }

    public void setSeed(long seed)
    {
        random.setSeed(seed);
    }

    //region random calculation
    public static int getTotalModulesWeight(Collection<WeightedRandomWeaponModule> collection,WeaponGenerationContext context)
    {
        int i = 0;

        for (WeightedRandomWeaponModule module : collection)
        {
            if (module.fits(context))
            {
                i += module.itemWeight;
            }
        }

        return i;
    }

    public static WeightedRandomWeaponModule getItem(Collection<WeightedRandomWeaponModule> par1Collection, int weight,WeaponGenerationContext context)
    {
        int j = weight;
        Iterator<WeightedRandomWeaponModule> iterator = par1Collection.iterator();
        WeightedRandomWeaponModule module = null;

        do
        {
            if (!iterator.hasNext())
            {
                return null;
            }

            WeightedRandomWeaponModule temp = iterator.next();
            if (temp.fits(context)) {
                module = temp;
                j -= temp.itemWeight;
            }
        }
        while (j >= 0);

        return module;
    }

    public static WeightedRandomWeaponModule getRandomModule(Random random, Collection collection,WeaponGenerationContext context)
    {
        return getItem(collection,random.nextInt(getTotalModulesWeight(collection,context)+1),context);
    }
    //endregion

    //region Classes
    public static class WeaponGenerationContext
    {
        public final int level;
        public Entity entity;
        public boolean legendary;
        public boolean fullCharge = true;
        public boolean barrel = true;
        public boolean battery = true;
        public boolean other = true;
        public ItemStack weaponStack;

        public WeaponGenerationContext(int level)
        {
            this.level = level;
            this.weaponStack = weaponStack;
        }

        public WeaponGenerationContext(int level,Entity entity)
        {
            this(level);
            this.entity = entity;
        }

        public WeaponGenerationContext(int level,Entity entity,boolean legendary)
        {
            this(level,entity);
            this.legendary = legendary;
        }

        public void setWeaponStack(ItemStack weaponStack)
        {
            this.weaponStack = weaponStack;
        }
    }

    public static class WeightedRandomWeaponModule extends WeightedRandom.Item
    {
        private int minLevel;
        private int maxLevel;
        private boolean legendary;
        private int slotID;
        private final ItemStack weaponModule;

        public WeightedRandomWeaponModule(ItemStack weaponModule,int weight,int minLevel,int maxLevel)
        {
            super(weight);
            if (weaponModule != null)
            {
                if (weaponModule.getItem() instanceof IWeaponModule) {
                    this.slotID = ((IWeaponModule) weaponModule.getItem()).getSlot(weaponModule);
                }else if (weaponModule.getItem() instanceof IEnergyContainerItem)
                {
                    this.slotID = Reference.MODULE_BATTERY;
                }
            }
            this.weaponModule = weaponModule;
            this.minLevel = minLevel;
            this.maxLevel = maxLevel;
        }

        public int getModuleSlot()
        {
            return slotID;
        }

        public ItemStack getWeaponModule()
        {
            if (this.weaponModule != null)
                return this.weaponModule.copy();
            return null;
        }

        public boolean fits(WeaponGenerationContext context)
        {
            boolean weaponSupportModule = false;
            if (context.weaponStack != null && context.weaponStack.getItem() instanceof IWeapon)
            {
                weaponSupportModule = context.weaponStack.getItem() instanceof IWeapon && ((IWeapon) context.weaponStack.getItem()).supportsModule(context.weaponStack,weaponModule);
            }

            if (legendary && !context.legendary)
            {
                return weaponSupportModule;
            }
            return context.level >= minLevel && context.level <= maxLevel && weaponSupportModule;

        }
    }
    //endregion
}
