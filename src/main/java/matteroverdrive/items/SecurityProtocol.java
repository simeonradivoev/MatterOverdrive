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

import matteroverdrive.items.includes.MOBaseItem;
import matteroverdrive.machines.MOTileEntityMachine;
import matteroverdrive.util.MOStringHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;
import java.util.UUID;

/**
 * Created by Simeon on 5/10/2015.
 */
public class SecurityProtocol extends MOBaseItem
{

	public static final String[] types = new String[] {"empty", "claim", "access", "remove"};

	public SecurityProtocol(String name)
	{
		super(name);
		setMaxStackSize(16);
	}

	@Override
	public int getMetadata(int damage)
	{
		return damage;
	}

	@Override
	public void addDetails(ItemStack itemstack, EntityPlayer player, List<String> infos)
	{
		super.addDetails(itemstack, player, infos);

		if (itemstack.hasTagCompound())
		{
			try
			{
				EntityPlayer entityPlayer = player.worldObj.getPlayerEntityByUUID(UUID.fromString(itemstack.getTagCompound().getString("Owner")));
				if (entityPlayer != null)
				{
					String owner = entityPlayer.getGameProfile().getName();
					infos.add(TextFormatting.YELLOW + "Owner: " + owner);
				}
			}
			catch (Exception e)
			{
				infos.add(TextFormatting.RED + MOStringHelper.translateToLocal(getUnlocalizedName() + ".invalid"));
			}
		}
	}

    /*@Override
	@SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister p_94581_1_)
    {
        icons = new IIcon[types.length];

        for (int i = 0;i < types.length;i++)
        {
            icons[i] = p_94581_1_.registerIcon(this.getIconString() + "_" + types[i]);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int damage)
    {
        return icons[damage];
    }*/

	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return super.getUnlocalizedName(stack) + "." + types[MathHelper.clamp_int(stack.getItemDamage(), 0, types.length)];
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand)
	{
		if (!itemStackIn.hasTagCompound())
		{
			if (playerIn.isSneaking())
			{
				TagCompountCheck(itemStackIn);
				itemStackIn.getTagCompound().setString("Owner", playerIn.getGameProfile().getId().toString());
				itemStackIn.setItemDamage(1);
				return ActionResult.newResult(EnumActionResult.SUCCESS, itemStackIn);
			}
		}
		else if (itemStackIn.getTagCompound().getString("Owner").equals(playerIn.getGameProfile().getId().toString()) || playerIn.capabilities.isCreativeMode)
		{
			if (playerIn.isSneaking())
			{
				int damage = itemStackIn.getItemDamage() + 1;
				if (damage >= types.length)
				{
					damage = 1;
				}

				itemStackIn.setItemDamage(damage);
				return ActionResult.newResult(EnumActionResult.SUCCESS, itemStackIn);
			}
		}

		return ActionResult.newResult(EnumActionResult.PASS, itemStackIn);
	}

	@Override
	public EnumActionResult onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand)
	{
		if (!world.isRemote)
		{
			TileEntity tileEntity = world.getTileEntity(pos);
			if (tileEntity instanceof MOTileEntityMachine)
			{
				if (stack.getItemDamage() == 1)
				{
					if (((MOTileEntityMachine)tileEntity).claim(stack))
					{
						stack.stackSize--;
						return EnumActionResult.SUCCESS;
					}
				}
				else if (stack.getItemDamage() == 3)
				{
					if (((MOTileEntityMachine)tileEntity).unclaim(stack))
					{
						stack.stackSize--;
						return EnumActionResult.SUCCESS;
					}
				}
			}
		}
		return EnumActionResult.FAIL;
	}

	@Override
	public boolean hasDetails(ItemStack stack)
	{
		return stack.hasTagCompound() && stack.getTagCompound().hasKey("Owner");
	}
}
