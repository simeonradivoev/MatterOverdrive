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

import cofh.lib.util.helpers.BlockHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import matteroverdrive.Reference;
import matteroverdrive.blocks.includes.MOMatterEnergyStorageBlock;
import matteroverdrive.client.render.block.MOBlockRenderer;
import matteroverdrive.handler.ConfigurationHandler;
import matteroverdrive.init.MatterOverdriveIcons;
import matteroverdrive.tile.TileEntityMachineDecomposer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockDecomposer extends MOMatterEnergyStorageBlock
{
	public IIcon iconTop;
	
	public BlockDecomposer(Material material, String name)
	{
		super(material, name, true, true);
        setHardness(20.0F);
        this.setResistance(9.0f);
        this.setHarvestLevel("pickaxe",2);
        setHasGui(true);
	}

	
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister)
	{
		this.iconTop = iconRegister.registerIcon(Reference.MOD_ID + ":" + "decomposer_top");
	}


    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side)
    {
        int metadata = world.getBlockMetadata(x, y, z);

        if(side == metadata)
        {
            return GetIconBasedOnMatter(world,x,y,z);
        }

        return getIcon(side,metadata);
    }

    @Override
    public IIcon getIcon(int side,int metadata)
    {
        if(side == BlockHelper.getAboveSide(metadata))
        {
            return this.iconTop;
        }
        else if(side == metadata)
        {
            return MatterOverdriveIcons.Matter_tank_empty;
        }

        return MatterOverdriveIcons.YellowStripes;
    }
    
    @Override
	 public boolean canPlaceTorchOnTop(World world, int x, int y, int z)
	 {
		 return true;
	 }
	 
	 public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side)
	 {
		 return true;
	 }
	 
	 @Override
		public TileEntity createNewTileEntity(World world, int meta)
		{
			return new TileEntityMachineDecomposer();
		}

    @Override
    public int getRenderType()
    {
        return MOBlockRenderer.renderID;
    }

    @Override
    public void onConfigChanged(ConfigurationHandler config)
    {
        super.onConfigChanged(config);
        config.initMachineCategory(getUnlocalizedName());
        TileEntityMachineDecomposer.MATTER_STORAGE = config.getMachineInt(getUnlocalizedName(),"storage.matter",1024,String.format("How much matter can the %s hold",getLocalizedName()));
        TileEntityMachineDecomposer.ENERGY_STORAGE = config.getMachineInt(getUnlocalizedName(),"storage.energy",512000,String.format("How much energy can the %s hold",getLocalizedName()));
        TileEntityMachineDecomposer.DECEOPOSE_SPEED_PER_MATTER = config.getMachineInt(getUnlocalizedName(),"speed.decompose",80,"The speed in ticks, of decomposing. (per matter)");
        TileEntityMachineDecomposer.DECOMPOSE_ENERGY_PER_MATTER = config.getMachineInt(getUnlocalizedName(),"cost.decompose",8000,"Decomposing cost per matter");

    }

}
