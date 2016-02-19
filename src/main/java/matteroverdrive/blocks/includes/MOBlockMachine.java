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

package matteroverdrive.blocks.includes;

import cofh.api.block.IDismantleable;
import matteroverdrive.api.IMOTileEntity;
import matteroverdrive.data.Inventory;
import matteroverdrive.data.inventory.Slot;
import matteroverdrive.handler.ConfigurationHandler;
import matteroverdrive.items.includes.MOMachineBlockItem;
import matteroverdrive.machines.MOTileEntityMachine;
import matteroverdrive.util.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;

/**
 * Created by Simeon on 4/5/2015.
 */
public abstract class MOBlockMachine extends MOBlockContainer implements IDismantleable, IConfigSubscriber
{
    public float volume = 1;
    public boolean hasGui;

    public MOBlockMachine(Material material, String name)
    {
        super(material, name);
    }

    @Override
    protected void registerBlock()
    {
        GameRegistry.registerBlock(this, MOMachineBlockItem.class, this.getUnlocalizedName().substring(5));
    }

    public boolean doNormalDrops(World world, int x, int y, int z)
    {
        return false;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        super.onBlockPlacedBy(worldIn,pos, state, placer,stack);

        IMOTileEntity entity = (IMOTileEntity) worldIn.getTileEntity(pos);
        if (entity != null) {
            try {
                entity.readFromPlaceItem(stack);
            } catch (Exception e) {
                e.printStackTrace();
                MOLog.log(Level.ERROR,"Could not load settings from placing item",e);
            }

            entity.onPlaced(worldIn, placer);
        }
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        //drops inventory
        Inventory inventory = getInventory(worldIn,pos);
        if (inventory != null)
        {
            MatterHelper.DropInventory(worldIn, inventory, pos);
        }

        super.breakBlock(worldIn,pos,state);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        return MachineHelper.canOpenMachine(worldIn,pos,playerIn,hasGui,getUnlocalizedMessage(0));
    }

    protected String getUnlocalizedMessage(int type)
    {
        switch (type)
        {
            case 0:
                return "alert.no_rights";
            default:
                return  "alert.no_access_default";
        }
    }

    @Override
    public boolean removedByPlayer(World world, BlockPos pos, EntityPlayer player, boolean willHarvest)
    {
        if (MachineHelper.canRemoveMachine(world,player,pos,willHarvest))
        {
            return world.setBlockToAir(pos);
        }
        return false;
    }

    public ItemStack getNBTDrop(World world, BlockPos blockPos, IMOTileEntity te)
    {
        IBlockState state = world.getBlockState(blockPos);
        ItemStack itemStack = new ItemStack(this, 1,damageDropped(state));
        if(te != null)
            te.writeToDropItem(itemStack);
        return itemStack;
    }

    public boolean hasGui() {
        return hasGui;
    }

    public void setHasGui(boolean hasGui) {
        this.hasGui = hasGui;
    }

    @Override
    public ArrayList<ItemStack> dismantleBlock(EntityPlayer player, World world, BlockPos pos, boolean returnDrops)
    {
        ArrayList<ItemStack> items = new ArrayList<>();
        ItemStack blockItem = getNBTDrop(world, pos, (IMOTileEntity) world.getTileEntity(pos));
        Inventory inventory = getInventory(world,pos);
        items.add(blockItem);

        //remove any items from the machine inventory so that breakBlock doesn't duplicate the items
        if (inventory != null) {
            for (int i1 = 0; i1 < inventory.getSizeInventory(); ++i1) {
                Slot slot = inventory.getSlot(i1);
                ItemStack itemstack = slot.getItem();

                if (itemstack != null)
                {
                    if (slot.keepOnDismantle())
                    {
                        slot.setItem(null);
                    }
                }
            }
        }

        IBlockState blockState = world.getBlockState(pos);
        boolean flag = blockState.getBlock().removedByPlayer(world, pos,player, true);
        super.breakBlock(world, pos,blockState);

        if (flag)
        {
            blockState.getBlock().onBlockDestroyedByPlayer(world, pos,blockState);
        }

        if (!returnDrops)
        {
            dropBlockAsItem(world, pos, blockState,0);
        }
        else
        {
            MOInventoryHelper.insertItemStackIntoInventory(player.inventory, blockItem, EnumFacing.DOWN);
        }

        return items;
    }

    protected Inventory getInventory(World world,BlockPos pos)
    {
        if (world.getTileEntity(pos) instanceof MOTileEntityMachine) {
            MOTileEntityMachine machine = (MOTileEntityMachine) world.getTileEntity(pos);
            return machine.getInventoryContainer();
        }
        return null;
    }

    @Override
    public boolean canDismantle(EntityPlayer player, World world, BlockPos pos)
    {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof MOTileEntityMachine)
        {
            if (player.capabilities.isCreativeMode || !((MOTileEntityMachine) tileEntity).hasOwner())
            {
                return true;
            }else
            {
                if (((MOTileEntityMachine) tileEntity).getOwner().equals(player.getGameProfile().getId()))
                {
                    return true;
                }else
                {
                    if (world.isRemote) {
                        ChatComponentText message = new ChatComponentText(EnumChatFormatting.GOLD + "[Matter Overdrive] " + EnumChatFormatting.RED + MOStringHelper.translateToLocal("alert.no_rights.dismantle").replace("$0",getLocalizedName()));
                        message.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED));
                        player.addChatMessage(message);
                    }
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onConfigChanged(ConfigurationHandler config)
    {
        config.initMachineCategory(getUnlocalizedName());
        volume = (float)config.getMachineDouble(getUnlocalizedName(), "volume", 1, "The volume of the Machine");
    }
}
