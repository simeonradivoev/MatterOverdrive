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

import matteroverdrive.blocks.includes.MOBlockMachine;
import matteroverdrive.machines.analyzer.TileEntityMachineMatterAnalyzer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

/**
 * Created by Simeon on 3/16/2015.
 */
public class BlockMatterAnalyzer extends MOBlockMachine<TileEntityMachineMatterAnalyzer>
{
	public static float MACHINE_VOLUME;
/*    private IIcon iconTop;
	private IIcon iconFront;
    private IIcon iconFronAnim;*/

	public BlockMatterAnalyzer(Material material, String name)
	{
		super(material, name);
		setHasRotation();
		setHardness(20.0F);
		this.setResistance(5.0f);
		this.setHarvestLevel("pickaxe", 2);
		setHasGui(true);
	}

	@Override
	public Class<TileEntityMachineMatterAnalyzer> getTileEntityClass()
	{
		return TileEntityMachineMatterAnalyzer.class;
	}

	@Nonnull
	@Override
	public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state)
	{
		return new TileEntityMachineMatterAnalyzer();
	}

    /*@Override
	@SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        this.iconFront = iconRegister.registerIcon(Reference.MOD_ID + ":" + "analyzer_front");
        this.iconTop = iconRegister.registerIcon(Reference.MOD_ID + ":" + "analyzer_top");
        this.iconFronAnim = iconRegister.registerIcon(Reference.MOD_ID + ":" + "analyzer_front_anim");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int metadata)
    {
        if(side == 1)
        {
            return this.iconTop;
        }
        else if(side == metadata)
        {
            return this.iconFront;
        }
        else if(side == MOBlockHelper.getOppositeSide(metadata))
        {
            return MatterOverdriveIcons.Network_port_square;
        }
        else if (side == MOBlockHelper.getLeftSide(metadata) || side == MOBlockHelper.getRightSide(metadata))
        {
            return MatterOverdriveIcons.Vent2;
        }

        return MatterOverdriveIcons.Base;
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess blockAccess, int x, int y, int z, int side)
    {
        if (side == blockAccess.getBlockMetadata(x, y, z))
        {
            if (blockAccess.getTileEntity(x, y, z) instanceof TileEntityMachineMatterAnalyzer)
            {
                if (((TileEntityMachineMatterAnalyzer) blockAccess.getTileEntity(x, y, z)).isActive())
                {
                    return this.iconFronAnim;
                }
            }
        }
        return this.getIcon(side, blockAccess.getBlockMetadata(x, y, z));
    }

    @Override
    public int getRenderType()
    {
        return MOBlockRenderer.renderID;
    }*/
}
