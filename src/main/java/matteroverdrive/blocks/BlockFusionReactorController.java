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

import matteroverdrive.blocks.includes.MOBlock;
import matteroverdrive.blocks.includes.MOBlockMachine;
import matteroverdrive.handler.ConfigurationHandler;
import matteroverdrive.machines.fusionReactorController.TileEntityMachineFusionReactorController;
import matteroverdrive.util.MOBlockHelper;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

/**
 * Created by Simeon on 5/14/2015.
 */
public class BlockFusionReactorController extends MOBlockMachine
{
    public BlockFusionReactorController(Material material, String name)
    {
        super(material, name);
        setHardness(30.0F);
        this.setResistance(10.0f);
        this.setHarvestLevel("pickaxe", 2);
        setHasGui(true);
        lightValue = 10;
        setRotationType(MOBlockHelper.RotationType.SIX_WAY);
        setHasRotation();
    }

   /* @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta)
    {
        if (side == meta)
        {
            return MatterOverdriveIcons.Monitor_back;
        }
        else if (side == MOBlockHelper.getRightSide(meta) || side == MOBlockHelper.getLeftSide(meta))
        {
            return MatterOverdriveBlocks.decomposer.iconTop;
        }
        else
        {
            return MatterOverdriveIcons.YellowStripes;
        }
    }*/

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
    {
        return new TileEntityMachineFusionReactorController();
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        EnumFacing l = BlockPistonBase.getFacingFromEntity(worldIn,pos,placer);
        worldIn.setBlockState(pos,state.withProperty(MOBlock.PROPERTY_DIRECTION,l),2);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (tileEntity instanceof TileEntityMachineFusionReactorController) {
            if (((TileEntityMachineFusionReactorController) tileEntity).isValidStructure())
            {
                return super.onBlockActivated(worldIn,pos,state,playerIn, side, hitX, hitY, hitZ);
            }
        }
        return false;
    }

    @Override
    public void onConfigChanged(ConfigurationHandler config)
    {
        super.onConfigChanged(config);
        TileEntityMachineFusionReactorController.ENERGY_STORAGE = config.getMachineInt(getUnlocalizedName(), "storage.energy", 100000000, String.format("How much energy can the %s hold", getLocalizedName()));
        TileEntityMachineFusionReactorController.MATTER_STORAGE = config.getMachineInt(getUnlocalizedName(), "storage.matter", 2048, String.format("How much matter can the %s hold", getLocalizedName()));
        TileEntityMachineFusionReactorController.ENERGY_PER_TICK = config.getMachineInt(getUnlocalizedName(), "output.energy", 2048, "The Energy Output per tick. Dependant on the size of the anomaly as well");
        TileEntityMachineFusionReactorController.MATTER_DRAIN_PER_TICK = (float)config.getMachineDouble(getUnlocalizedName(), "drain.matter", 1D / 80D, "How much matter is drained per tick. Dependant on the size of the anomaly as well");
        TileEntityMachineFusionReactorController.MAX_GRAVITATIONAL_ANOMALY_DISTANCE = config.getMachineInt(getUnlocalizedName(), "distance.anomaly", 3, "The maximum distance of the anomaly");
        TileEntityMachineFusionReactorController.STRUCTURE_CHECK_DELAY = config.getMachineInt(getUnlocalizedName(), "check.delay", 40, "The time delay between each structure check");
    }
}
