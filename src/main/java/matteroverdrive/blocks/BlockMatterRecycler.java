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

import matteroverdrive.blocks.includes.MOMatterEnergyStorageBlock;
import matteroverdrive.tile.TileEntityMachineMatterRecycler;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

/**
 * Created by Simeon on 5/15/2015.
 */
public class BlockMatterRecycler extends MOMatterEnergyStorageBlock<TileEntityMachineMatterRecycler>
{

	public BlockMatterRecycler(Material material, String name)
	{
		super(material, name, true, true);
		setHasRotation();
		setHardness(20.0F);
		this.setResistance(9.0f);
		this.setHarvestLevel("pickaxe", 2);
		setHasGui(true);
	}

    /*@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister)
    {
        this.iconTop = iconRegister.registerIcon(Reference.MOD_ID + ":decomposer_top");
        this.iconSideAnim = iconRegister.registerIcon(Reference.MOD_ID + ":recycler_side_anim");
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta)
    {
        if (side == MOBlockHelper.getAboveSide(meta))
        {
            return iconTop;
        }

        return MatterOverdriveIcons.Recycler;
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess blockAccess, int x, int y, int z, int side)
    {
        if (side != MOBlockHelper.getAboveSide(blockAccess.getBlockMetadata(x, y, z)))
        {
            if (blockAccess.getTileEntity(x, y, z) instanceof TileEntityMachineMatterRecycler)
            {
                if (((TileEntityMachineMatterRecycler) blockAccess.getTileEntity(x, y, z)).isActive())
                {
                    return iconSideAnim;
                }
            }
        }
        return this.getIcon(side, blockAccess.getBlockMetadata(x, y, z));
    }*/

	@Override
	public Class<TileEntityMachineMatterRecycler> getTileEntityClass()
	{
		return TileEntityMachineMatterRecycler.class;
	}

	@Nonnull
	@Override
	public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState meta)
	{
		return new TileEntityMachineMatterRecycler();
	}
/*
	@Override
    public int getRenderType()
    {
        return MOBlockRenderer.renderID;
    }*/
}
