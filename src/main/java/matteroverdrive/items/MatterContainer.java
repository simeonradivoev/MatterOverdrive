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

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import matteroverdrive.Reference;
import matteroverdrive.init.MatterOverdriveBlocks;
import matteroverdrive.init.MatterOverdriveItems;
import matteroverdrive.items.includes.MOBaseItem;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

/**
 * Created by Simeon on 8/20/2015.
 */
public class MatterContainer extends MOBaseItem
{
    IIcon centerFill;
    IIcon bottomFill;
    boolean isFull;

    public MatterContainer(String name,boolean isFull) {
        super(name);
        this.isFull = isFull;
        setMaxStackSize(8);
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister)
    {
        this.itemIcon = iconRegister.registerIcon(Reference.MOD_ID + ":" + "container");
        centerFill = iconRegister.registerIcon(Reference.MOD_ID + ":" + "container_center_fill");
        bottomFill = iconRegister.registerIcon(Reference.MOD_ID + ":" + "container_bottom_fill");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses()
    {
        return isFull;
    }

    @Override
    public int getRenderPasses(int metadata)
    {
        return isFull ? 3 : 1;
    }

    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer entityPlayer)
    {
        MovingObjectPosition movingobjectposition = this.getMovingObjectPositionFromPlayer(world, entityPlayer, !isFull);

        if (movingobjectposition == null)
        {
            return itemStack;
        }
        else
        {
            if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
            {
                int i = movingobjectposition.blockX;
                int j = movingobjectposition.blockY;
                int k = movingobjectposition.blockZ;

                if (!world.canMineBlock(entityPlayer, i, j, k))
                {
                    return itemStack;
                }

                if (!isFull)
                {
                    if (!entityPlayer.canPlayerEdit(i, j, k, movingobjectposition.sideHit, itemStack))
                    {
                        return itemStack;
                    }

                    Block block = world.getBlock(i, j, k);
                    int l = world.getBlockMetadata(i, j, k);

                    if (block == MatterOverdriveBlocks.blockMatterPlasma && l == 0)
                    {
                        world.setBlockToAir(i, j, k);
                        return this.darinFluid(itemStack, entityPlayer, MatterOverdriveItems.matterContainerFull);
                    }
                }
                else
                {
                    if (!this.isFull)
                    {
                        return new ItemStack(MatterOverdriveItems.matterContainer);
                    }

                    if (movingobjectposition.sideHit == 0)
                    {
                        --j;
                    }

                    if (movingobjectposition.sideHit == 1)
                    {
                        ++j;
                    }

                    if (movingobjectposition.sideHit == 2)
                    {
                        --k;
                    }

                    if (movingobjectposition.sideHit == 3)
                    {
                        ++k;
                    }

                    if (movingobjectposition.sideHit == 4)
                    {
                        --i;
                    }

                    if (movingobjectposition.sideHit == 5)
                    {
                        ++i;
                    }

                    if (!entityPlayer.canPlayerEdit(i, j, k, movingobjectposition.sideHit, itemStack))
                    {
                        return itemStack;
                    }

                    if (this.tryPlaceContainedLiquid(world, i, j, k) && !entityPlayer.capabilities.isCreativeMode)
                    {
                        itemStack.stackSize--;
                        if (itemStack.stackSize > 1) {
                            if (!entityPlayer.inventory.addItemStackToInventory(new ItemStack(MatterOverdriveItems.matterContainer))) {
                                entityPlayer.dropPlayerItemWithRandomChoice(new ItemStack(MatterOverdriveItems.matterContainer), false);
                            }
                        }else if (itemStack.stackSize <= 0)
                        {
                            return new ItemStack(MatterOverdriveItems.matterContainer);
                        }
                    }
                }
            }
        }
        return itemStack;
    }

    private ItemStack darinFluid(ItemStack itemStack, EntityPlayer entityPlayer, Item item)
    {
        if (entityPlayer.capabilities.isCreativeMode)
        {
            return itemStack;
        }
        else if (--itemStack.stackSize <= 0)
        {
            return new ItemStack(item);
        }
        else
        {
            if (!entityPlayer.inventory.addItemStackToInventory(new ItemStack(item)))
            {
                entityPlayer.dropPlayerItemWithRandomChoice(new ItemStack(item, 1, 0), false);
            }

            return itemStack;
        }
    }

    public boolean tryPlaceContainedLiquid(World world, int x, int y, int z)
    {
        if (!isFull)
        {
            return false;
        }
        else
        {
            Material material = world.getBlock(x, y, z).getMaterial();

            if (!world.isAirBlock(x, y, z))
            {
                return false;
            }
            else
            {
                if (!world.isRemote && !material.isLiquid())
                {
                    world.func_147480_a(x, y, z, true);
                }

                world.setBlock(x, y, z, MatterOverdriveBlocks.blockMatterPlasma, 0, 3);

                return true;
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamageForRenderPass(int damage, int pass)
    {
        if (pass == 1 && isFull)
        {
            return centerFill;
        }else if (pass == 2 && isFull)
        {
            return bottomFill;
        }else
        {
            return itemIcon;
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack itemStack, int pass)
    {
        if (pass == 1 && isFull)
        {
            return Reference.COLOR_MATTER.getColor();
        }else if (pass == 2 && isFull)
        {
            return Reference.COLOR_YELLOW_STRIPES.getColor();
        }
        return super.getColorFromItemStack(itemStack,pass);
    }
}
