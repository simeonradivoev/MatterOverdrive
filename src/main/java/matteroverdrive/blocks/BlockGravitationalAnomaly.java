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

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import matteroverdrive.api.IScannable;
import matteroverdrive.blocks.includes.MOBlockContainer;
import matteroverdrive.handler.ConfigurationHandler;
import matteroverdrive.tile.TileEntityGravitationalAnomaly;
import matteroverdrive.util.IConfigSubscriber;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by Simeon on 5/11/2015.
 */
public class BlockGravitationalAnomaly extends MOBlockContainer implements IScannable, IConfigSubscriber
{
    public BlockGravitationalAnomaly(Material material, String name)
    {
        super(material, name);
        setBlockBounds(0.3f,0.3f,0.3f,0.6f,0.6f,0.6f);
        setBlockUnbreakable();
        setResistance(6000000.0F);
        disableStats();
    }
    @Override
    public boolean isNormalCube()
    {
        return false;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
    {
        return null;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z)
    {
        this.setBlockBoundsBasedOnState(world, x, y, z);
        return super.getSelectedBoundingBoxFromPool(world, x, y, z);
    }

    @Override
    public MovingObjectPosition collisionRayTrace(World world, int x, int y, int z, Vec3 vector1, Vec3 vector2)
    {
        this.setBlockBoundsBasedOnState(world, x, y, z);
        return super.collisionRayTrace(world, x, y, z, vector1, vector2);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
    {
        TileEntity tileEntity = world.getTileEntity(x,y,z);
        if (tileEntity != null && tileEntity instanceof TileEntityGravitationalAnomaly)
        {
            double range = ((TileEntityGravitationalAnomaly) tileEntity).getEventHorizon();
            range = Math.max(range,0.4);
            float rangeMin = (float)(0.5 - (range/2));
            float rangeMax = (float)(0.5 + (range/2));
            setBlockBounds(rangeMin, rangeMin, rangeMin, rangeMax, rangeMax, rangeMax);
        }
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }
    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityGravitationalAnomaly();
    }

    @Override
    public void addInfo(World world, double x, double y, double z, List<String> infos)
    {
        TileEntity tileEntity = world.getTileEntity((int)x, (int)y, (int)z);

        if (tileEntity != null && tileEntity instanceof TileEntityGravitationalAnomaly)
        {
            ((TileEntityGravitationalAnomaly) tileEntity).addInfo(world, x, y, z, infos);
        }
    }

    @Override
    public void onScan(World world, double x, double y, double z, EntityPlayer player, ItemStack scanner)
    {

    }

    @Override
    public boolean canEntityDestroy(IBlockAccess world, int x, int y, int z, Entity entity)
    {
        return false;
    }

    @Override
    public int getRenderType()
    {
        return -1;
    }

    @Override
    public void onConfigChanged(ConfigurationHandler config)
    {
        TileEntityGravitationalAnomaly.BLOCK_ENTETIES = config.getBool(ConfigurationHandler.KEY_GRAVITATIONAL_ANOMALY_BLOCK_ENTITIES, ConfigurationHandler.CATEGORY_SERVER + "." + getUnlocalizedName().substring(5), true, "Should the blocks drop entities or be directly consumed when destroyed by the gravitational anomaly");
        TileEntityGravitationalAnomaly.FALLING_BLOCKS = config.getBool(ConfigurationHandler.KEY_GRAVITATIONAL_ANOMALY_FALLING_BLOCKS, ConfigurationHandler.CATEGORY_SERVER + "." + getUnlocalizedName().substring(5), true, "Should blocks be turned into falling blocks when broken");
        TileEntityGravitationalAnomaly.VANILLA_FLUIDS = config.getBool(ConfigurationHandler.KEY_GRAVITATIONAL_ANOMALY_VANILLA_FLUIDS, ConfigurationHandler.CATEGORY_SERVER + "." + getUnlocalizedName().substring(5), true, "Should vanilla fluid block such as water and lava be consumed by the anomaly");
        TileEntityGravitationalAnomaly.FORGE_FLUIDS = config.getBool(ConfigurationHandler.KEY_GRAVITATIONAL_ANOMALY_FORGE_FLUIDS, ConfigurationHandler.CATEGORY_SERVER + "." + getUnlocalizedName().substring(5), true, "Should other mod fluid blocks be consumed by the anomaly");
        TileEntityGravitationalAnomaly.BLOCK_DESTRUCTION = config.getBool("block destruction",ConfigurationHandler.CATEGORY_SERVER + "." + getUnlocalizedName().substring(5), true, "Should the gravitational anomaly destroy blocks");
        TileEntityGravitationalAnomaly.GRAVITATION = config.getBool("gravitational pull",ConfigurationHandler.CATEGORY_SERVER + "." + getUnlocalizedName().substring(5), true, "Should the gravitational entity pull entities towards it");
    }
}
