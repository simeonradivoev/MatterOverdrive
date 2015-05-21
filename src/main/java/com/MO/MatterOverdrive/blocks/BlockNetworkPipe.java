package com.MO.MatterOverdrive.blocks;

import cofh.api.block.IDismantleable;
import cofh.lib.util.helpers.BlockHelper;
import cofh.lib.util.helpers.InventoryHelper;
import com.MO.MatterOverdrive.tile.IMOTileEntity;
import com.MO.MatterOverdrive.tile.pipes.TileEntityNetworkPipe;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.ArrayList;

/**
 * Created by Simeon on 3/15/2015.
 */
public class BlockNetworkPipe extends BlockPipe implements IDismantleable
{

    public BlockNetworkPipe(Material material, String name)
    {
        super(material, name);
        setHardness(10.0F);
        this.setResistance(9.0f);
        setRotationType(BlockHelper.RotationType.PREVENT);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return new TileEntityNetworkPipe();
    }

    @Override
    public ArrayList<ItemStack> dismantleBlock(EntityPlayer player, World world, int x, int y, int z, boolean returnDrops)
    {
        ArrayList<ItemStack> items = new ArrayList<ItemStack>();
        Block block = world.getBlock(x, y, z);
        int l = world.getBlockMetadata(x, y, z);

        if (!returnDrops)
        {
            block.harvestBlock(world,player,x,y,z,l);
            block.removedByPlayer(world, player, x, y, z, true);
        }
        else
        {
            block.removedByPlayer(world, player, x, y, z, true);
            block.breakBlock(world, x, y, z, block, l);
            for (ItemStack itemStack : getDrops(world,x,y,z,l,0))
                InventoryHelper.insertItemStackIntoInventory(player.inventory, itemStack, 0);
        }

        return items;
    }

    @Override
    public boolean canDismantle(EntityPlayer player, World world, int x, int y, int z)
    {
        return true;
    }
}
