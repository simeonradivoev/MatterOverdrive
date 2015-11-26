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

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import matteroverdrive.Reference;
import matteroverdrive.blocks.includes.MOBlockMachine;
import matteroverdrive.client.render.block.RendererBlockTritaniumCrate;
import matteroverdrive.data.Inventory;
import matteroverdrive.init.MatterOverdriveIcons;
import matteroverdrive.init.MatterOverdriveItems;
import matteroverdrive.tile.TileEntityTritaniumCrate;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.oredict.ShapedOreRecipe;

/**
 * Created by Simeon on 11/5/2015.
 */
public class BlockTritaniumCrate extends MOBlockMachine
{
    private int color;

    public BlockTritaniumCrate(Material material, String name,int color) {
        super(material, name);
        setHardness(20.0F);
        this.setResistance(9.0f);
        setBlockBounds(0, 0, 0, 1, 12 * (1 / 16f), 1);
        this.setHarvestLevel("pickaxe", 2);
        this.color = color;
    }

    public static BlockTritaniumCrate[] createAllColors(Material material, String name)
    {
        BlockTritaniumCrate[] crates = new BlockTritaniumCrate[16];
        for (int i = 0;i < crates.length;i++)
        {
            crates[i] = new BlockTritaniumCrate(material,name + "." + ItemDye.field_150923_a[i], ItemDye.field_150922_c[i]);
        }
        return crates;
    }

    public static void registerAll(BlockTritaniumCrate[] crates,String name)
    {
        for (int i = 0;i < crates.length;i++)
        {
            GameRegistry.registerBlock(crates[i], crates[i].getUnlocalizedName().substring(5));
        }

        GameRegistry.registerTileEntity(new TileEntityTritaniumCrate().getClass(), name);
    }

    public static void registerRecipes(BlockTritaniumCrate[] crates)
    {
        for (int i = 0;i < crates.length;i++)
        {
            ShapedOreRecipe recipe = new ShapedOreRecipe(new ItemStack(crates[i])," D ","TCT"," T ",'D',new ItemStack(Items.dye,1,i),'T', MatterOverdriveItems.tritanium_plate,'C', Blocks.chest);
            GameRegistry.addRecipe(recipe);
        }
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TileEntityTritaniumCrate();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister p_149651_1_)
    {
        return;
    }

    @Override
    public int getRenderType()
    {
        return RendererBlockTritaniumCrate.renderID;
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int p_149691_1_, int p_149691_2_)
    {
        return MatterOverdriveIcons.tritanium_crate_base;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
    {
        setBlockBoundsBasedOnState(world,x,y,z);
        return super.getCollisionBoundingBoxFromPool(world,x,y,z);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
    {
        int l = world.getBlockMetadata(x, y, z);
        setBounds(l);
    }

    private void setBounds(int meta)
    {
        float unit = 1f/16f;
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        ForgeDirection forgeDirection = ForgeDirection.getOrientation(meta);

        if (forgeDirection == ForgeDirection.EAST || forgeDirection == ForgeDirection.WEST)
        {
            this.setBlockBounds(unit*2, 0, 0, 1-unit*2, unit*13, 1);
        }
        else if (forgeDirection == ForgeDirection.SOUTH || forgeDirection == ForgeDirection.NORTH)
        {
            this.setBlockBounds(0, 0, unit*2, 1, unit*13, 1-unit*2);
        }
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityPlayer, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_)
    {
        if (world.isRemote)
        {
            return true;
        }
        else
        {
            TileEntity entity = world.getTileEntity(x,y,z);
            if (entity instanceof TileEntityTritaniumCrate) {
                //FMLNetworkHandler.openGui(entityPlayer, MatterOverdrive.instance, GuiHandler.TRITANIUM_CRATE, world, x, y, z);
                world.playSoundEffect(x,y,z,Reference.MOD_ID + ":" + "crate_open",0.5f,1);
                entityPlayer.displayGUIChest(((TileEntityTritaniumCrate) entity).getInventory());
                return true;
            }
        }
        return false;
    }

    protected Inventory getInventory(World world, int x, int y, int z)
    {
        if (world.getTileEntity(x,y,z) instanceof TileEntityTritaniumCrate) {
            TileEntityTritaniumCrate machine = (TileEntityTritaniumCrate) world.getTileEntity(x, y, z);
            return machine.getInventory();
        }
        return null;
    }

    @SideOnly(Side.CLIENT)
    public int getBlockColor()
    {
        return color;
    }
}
