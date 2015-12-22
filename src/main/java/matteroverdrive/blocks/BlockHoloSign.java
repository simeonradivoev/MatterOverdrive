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

package matteroverdrive.blocks;

import cofh.api.block.IDismantleable;
import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.api.IMOTileEntity;
import matteroverdrive.client.render.IconConnectedTexture;
import matteroverdrive.client.render.block.MOBlockRenderer;
import matteroverdrive.init.MatterOverdriveIcons;
import matteroverdrive.tile.TileEntityHoloSign;
import matteroverdrive.util.MOInventoryHelper;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.ArrayList;

/**
 * Created by Simeon on 8/15/2015.
 */
public class BlockHoloSign extends BlockCT implements IDismantleable, ITileEntityProvider
{

    public BlockHoloSign(Material material, String name)
    {
        super(material, name);
        float f = 0.25F;
        float f1 = 1.0F;
        this.setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f1, 0.5F + f);
        this.isBlockContainer = true;
        this.setHardness(20f);
    }

    @Override
    public void register()
    {
        super.register();
		GameRegistry.registerTileEntity(TileEntityHoloSign.class, this.getUnlocalizedName().substring(5));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta)
    {
        if (side == meta)
        {
            MatterOverdriveIcons.Monitor_back.setType(0);
            return MatterOverdriveIcons.Monitor_back;
        }
        return MatterOverdriveIcons.Base;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IconConnectedTexture getIconConnectedTexture(int meta, int side)
    {
        return MatterOverdriveIcons.Monitor_back;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        this.blockIcon = MatterOverdriveIcons.Monitor_back;
        iconConnectedTexture = new IconConnectedTexture(this.blockIcon);
    }

    @Override
    public boolean canConnect(IBlockAccess world, Block block, int x, int y, int z) {
        return block instanceof BlockHoloSign;
    }

    @Override
    public boolean isSideCT(IBlockAccess world, int x, int y, int z, int side) {
        return world.getBlockMetadata(x, y, z) == side;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
    {
        return null;
    }

    @Override
    public void setBlockBoundsForItemRender()
    {
        setBounds(-1);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
    {
        int l = world.getBlockMetadata(x, y, z);
        setBounds(l);
    }

    private void setBounds(int meta)
    {
        float f = 0;
        float f1 = 1;
        float f2 = 0.0F;
        float f3 = 1.0F;
        float f4 = 0.125F;
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);

        if (meta == 2)
        {
            this.setBlockBounds(f2, f, 1.0F - f4, f3, f1, 1.0F);
        }

        if (meta == 3)
        {
            this.setBlockBounds(f2, f, 0.0F, f3, f1, f4);
        }

        if (meta == 4)
        {
            this.setBlockBounds(1.0F - f4, f, f2, 1.0F, f1, f3);
        }

        if (meta == 5)
        {
            this.setBlockBounds(0.0F, f, f2, f4, f1, f3);
        }

        if (meta == -1)
        {
            this.setBlockBounds(f2, f, 0.3f, f3, f1, 0.3f + f4);
        }
    }


    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z)
    {
        this.setBlockBoundsBasedOnState(world, x, y, z);
        return super.getSelectedBoundingBoxFromPool(world, x, y, z);
    }

    @Override
    public int getRenderType()
    {
        return MOBlockRenderer.renderID;
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override
    public boolean getBlocksMovement(IBlockAccess world, int x, int y, int z)
    {
        return true;
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return new TileEntityHoloSign();
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block)
    {
        boolean flag;
        int l = world.getBlockMetadata(x, y, z);
        flag = true;

        if (l == 2 && world.getBlock(x, y, z + 1).getMaterial().isSolid())
        {
            flag = false;
        }

        if (l == 3 && world.getBlock(x, y, z - 1).getMaterial().isSolid())
        {
            flag = false;
        }

        if (l == 4 && world.getBlock(x + 1, y, z).getMaterial().isSolid())
        {
            flag = false;
        }

        if (l == 5 && world.getBlock(x - 1, y, z).getMaterial().isSolid())
        {
            flag = false;
        }

        if (flag)
        {
            this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
            world.setBlockToAir(x, y, z);
        }

        super.onNeighborBlockChange(world, x, y, z, block);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ)
    {
        if(!world.isRemote)
        {
            TileEntity tileEntity = world.getTileEntity(x, y, z);
            if (tileEntity instanceof TileEntityHoloSign)
            {
                FMLNetworkHandler.openGui(player, MatterOverdrive.instance, -1, world, x, y, z);
                return true;
            }
        }

        return false;
    }

    @Override
    public ArrayList<ItemStack> dismantleBlock(EntityPlayer player, World world, int x, int y, int z, boolean returnDrops)
    {
        Block block = world.getBlock(x, y, z);
        int l = world.getBlockMetadata(x, y, z);
        boolean flag = block.removedByPlayer(world, player, x, y, z, true);
        ItemStack blockItem = new ItemStack(getItemDropped(l,world.rand,1));

        if (!returnDrops)
        {
            dropBlockAsItem(world, x, y, z, blockItem);
        }
        else
        {
            MOInventoryHelper.insertItemStackIntoInventory(player.inventory, blockItem, 0);
        }

        ArrayList<ItemStack> list = new ArrayList<>();
        list.add(blockItem);
        return list;
    }

    @Override
    public boolean canDismantle(EntityPlayer entityPlayer, World world, int x, int y, int z) {
        return true;
    }

    @Override
    public void onBlockPreDestroy(World world, int x, int y, int z, int meta)
    {
        super.onBlockPreDestroy(world, x, y, z, meta);
        IMOTileEntity tileEntity = (IMOTileEntity)world.getTileEntity(x, y, z);
        if(tileEntity != null)
            tileEntity.onDestroyed();
    }
}
