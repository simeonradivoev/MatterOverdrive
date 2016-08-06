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

package matteroverdrive.items.food;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.entity.android_player.AndroidPlayer;
import matteroverdrive.entity.player.MOPlayerCapabilityProvider;
import matteroverdrive.util.MOStringHelper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.util.List;

/**
 * Created by Simeon on 7/12/2015.
 */
public class AndroidPill extends MOItemFood
{
	public static final String[] names = new String[] {"red", "blue", "yellow"};

	public AndroidPill(String name)
	{
		super(name, 0, 0, false);
		setAlwaysEdible();
		hasSubtypes = true;
	}

	@Override
	public int getMetadata(int damage)
	{
		return damage;
	}

	@Override
	public void addInformation(ItemStack itemstack, EntityPlayer player, List infos, boolean p_77624_4_)
	{
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
		{
			String[] infoList = MOStringHelper.translateToLocal(getUnlocalizedName(itemstack) + ".details").split("/n");
			for (String info : infoList)
			{
				infos.add(TextFormatting.GRAY + info);
			}
		}
		else
		{
			infos.add(MOStringHelper.MORE_INFO);
		}

		if (itemstack.getItemDamage() == 2)
		{
			AndroidPlayer androidPlayer = MOPlayerCapabilityProvider.GetAndroidCapability(player);
			if (androidPlayer != null && androidPlayer.isAndroid())
			{
				infos.add(TextFormatting.GREEN + "XP:" + androidPlayer.getResetXPRequired() + "l");
			}
			else
			{
				infos.add(TextFormatting.RED + "Not an Android.");
			}
		}
	}

	public void addToDunguns()
	{
		// TODO: 3/24/2016 Find new way of adding dungon loot
		//ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CORRIDOR).addItem(new WeightedRandomChestContent(new ItemStack(this,1),1,1,1));
	}

	@Override
	public String getUnlocalizedName(ItemStack itemStack)
	{
		return getUnlocalizedName() + "_" + names[MathHelper.clamp_int(itemStack.getItemDamage(), 0, names.length - 1)];
	}

    /*@Override
	@SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister)
    {
        this.itemIcon = iconRegister.registerIcon(Reference.MOD_ID + ":" + "pill_bottom");
        this.overlay = iconRegister.registerIcon(Reference.MOD_ID + ":" + "pill_top");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses()
    {
        return true;
    }*/

	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs creativeTabs, List list)
	{
		list.add(new ItemStack(item, 1, 0));
		list.add(new ItemStack(item, 1, 1));
		list.add(new ItemStack(item, 1, 2));
	}

    /*@Override
	public int getRenderPasses(int metadata)
    {
        return 2;
    }*/

    /*@Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamageForRenderPass(int damage, int pass)
    {
        if (pass == 1)
        {
            return overlay;
        }else
        {
            return itemIcon;
        }
    }*/

   /* @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player)
    {
        AndroidPlayer androidPlayer = AndroidPlayer.get(player);
        if (itemStack.getItemDamage() >= 1)
        {
            if (!androidPlayer.isTurning() && androidPlayer.isAndroid())
            {
                player.setItemInUse(itemStack, this.getMaxItemUseDuration(itemStack));
            }
        }
        else
        {
            if (!androidPlayer.isAndroid() && !androidPlayer.isTurning())
            {
                player.setItemInUse(itemStack, this.getMaxItemUseDuration(itemStack));
            }
        }
        return itemStack;
    }*/

	public void register()
	{
		setCreativeTab(MatterOverdrive.tabMatterOverdrive_food);
		GameRegistry.register(this, new ResourceLocation(Reference.MOD_ID, this.getUnlocalizedName().substring(5)));
	}

	@Override
	protected void onFoodEaten(ItemStack itemStack, World world, EntityPlayer player)
	{
		if (world.isRemote)
		{
			return;
		}

		AndroidPlayer androidPlayer = MOPlayerCapabilityProvider.GetAndroidCapability(player);
		if (itemStack.getItemDamage() == 0)
		{
			androidPlayer.startConversion();
		}
		else if (itemStack.getItemDamage() == 1)
		{
			androidPlayer.setAndroid(false);
		}
		else if (itemStack.getItemDamage() == 2)
		{
			if (!androidPlayer.isTurning() && androidPlayer.isAndroid())
			{
				int xpLevels = androidPlayer.resetUnlocked();
				player.addExperienceLevel(xpLevels);
			}
		}
	}
}
