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

package matteroverdrive.world;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.entity.EntityVillagerMadScientist;
import matteroverdrive.init.MatterOverdriveBlocks;
import matteroverdrive.init.MatterOverdriveItems;
import matteroverdrive.tile.TileEntityTritaniumCrate;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces;

import java.util.List;
import java.util.Random;

/**
 * Created by Simeon on 5/30/2015.
 */
public class MadScientistHouse extends StructureVillagePieces.Village
{
    private boolean hasMadeChest;
    int villagersSpawned;
    private static final String __OBFID = "CL_00000517";

    public MadScientistHouse() {}

    public MadScientistHouse(StructureVillagePieces.Start start, int p_i2094_2_, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
    {
        super(start, p_i2094_2_);
        this.coordBaseMode = coordBaseMode;
        this.boundingBox = boundingBox;
    }

    public static MadScientistHouse func_74898_a(StructureVillagePieces.Start start, List list, Random p_74898_2_, int x, int y, int z, EnumFacing p_74898_6_, int p_74898_7_)
    {
        StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, 9, 9, 6, p_74898_6_);
        return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(list, structureboundingbox) == null ? new MadScientistHouse(start, p_74898_7_, p_74898_2_, structureboundingbox, p_74898_6_) : null;
    }

    protected void writeStructureToNBT(NBTTagCompound p_143012_1_)
    {
        super.writeStructureToNBT(p_143012_1_);
        p_143012_1_.setBoolean("Chest", this.hasMadeChest);
    }

    protected void readStructureFromNBT(NBTTagCompound p_143011_1_)
    {
        super.readStructureFromNBT(p_143011_1_);
        this.hasMadeChest = p_143011_1_.getBoolean("Chest");
    }

    /**
     * second Part of Structure generating, this for example places Spiderwebs, Mob Spawners, it closes
     * Mineshafts at the end, it adds Fences..
     */
    public boolean addComponentParts(World world, Random random, StructureBoundingBox boundingBox)
    {
        if (this.field_143015_k < 0)
        {
            this.field_143015_k = this.getAverageGroundLevel(world, boundingBox);

            if (this.field_143015_k < 0)
            {
                return true;
            }

            this.boundingBox.offset(0, this.field_143015_k - this.boundingBox.maxY + 9 - 1, 0);
        }

        this.fillWithBlocks(world, boundingBox, 1, 1, 1, 7, 5, 4, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
        this.fillWithBlocks(world, boundingBox, 0, 0, 0, 8, 0, 5, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
        this.fillWithBlocks(world, boundingBox, 0, 5, 0, 8, 5, 5, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
        this.fillWithBlocks(world, boundingBox, 0, 6, 1, 8, 6, 4, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
        this.fillWithBlocks(world, boundingBox, 0, 7, 2, 8, 7, 3, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
        int i = this.getMetadataWithOffset(Blocks.oak_stairs, 3);
        int j = this.getMetadataWithOffset(Blocks.oak_stairs, 2);
        int k;
        int l;

        for (k = -1; k <= 2; ++k)
        {
            for (l = 0; l <= 8; ++l)
            {
                this.setBlockState(world, Blocks.oak_stairs.getStateFromMeta(i), l, 6 + k, k, boundingBox);
                this.setBlockState(world, Blocks.oak_stairs.getStateFromMeta(j), l, 6 + k, 5 - k, boundingBox);
            }
        }

        this.fillWithBlocks(world, boundingBox, 0, 1, 0, 0, 1, 5, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
        this.fillWithBlocks(world, boundingBox, 1, 1, 5, 8, 1, 5, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
        this.fillWithBlocks(world, boundingBox, 8, 1, 0, 8, 1, 4, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
        this.fillWithBlocks(world, boundingBox, 2, 1, 0, 7, 1, 0, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
        this.fillWithBlocks(world, boundingBox, 0, 2, 0, 0, 4, 0, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
        this.fillWithBlocks(world, boundingBox, 0, 2, 5, 0, 4, 5, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
        this.fillWithBlocks(world, boundingBox, 8, 2, 5, 8, 4, 5, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
        this.fillWithBlocks(world, boundingBox, 8, 2, 0, 8, 4, 0, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
        this.fillWithBlocks(world, boundingBox, 0, 2, 1, 0, 4, 4, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
        this.fillWithBlocks(world, boundingBox, 1, 2, 5, 7, 4, 5, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
        this.fillWithBlocks(world, boundingBox, 8, 2, 1, 8, 4, 4, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
        this.fillWithBlocks(world, boundingBox, 1, 2, 0, 7, 4, 0, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
        this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 4, 2, 0, boundingBox);
        this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 5, 2, 0, boundingBox);
        this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 6, 2, 0, boundingBox);
        this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 4, 3, 0, boundingBox);
        this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 5, 3, 0, boundingBox);
        this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 6, 3, 0, boundingBox);
        this.setBlockState(world, Blocks.glass_pane.getDefaultState(),0, 2, 2, boundingBox);
        this.setBlockState(world, Blocks.glass_pane.getDefaultState(),0, 2, 3, boundingBox);
        this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 0, 3, 2, boundingBox);
        this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 0, 3, 3, boundingBox);
        this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 8, 2, 2, boundingBox);
        this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 8, 2, 3, boundingBox);
        this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 8, 3, 2, boundingBox);
        this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 8, 3, 3, boundingBox);
        this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 2, 2, 5, boundingBox);
        this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 3, 2, 5, boundingBox);
        this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 5, 2, 5, boundingBox);
        this.setBlockState(world, Blocks.glass_pane.getDefaultState(), 6, 2, 5, boundingBox);
        this.fillWithBlocks(world, boundingBox, 1, 4, 1, 7, 4, 1, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
        this.fillWithBlocks(world, boundingBox, 1, 4, 4, 7, 4, 4, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
        this.fillWithBlocks(world, boundingBox, 1, 3, 4, 7, 3, 4, Blocks.bookshelf.getDefaultState(), Blocks.bookshelf.getDefaultState(), false);
        this.setBlockState(world, Blocks.planks.getDefaultState(), 7, 1, 4, boundingBox);
        this.setBlockState(world, Blocks.oak_stairs.getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, 0)), 7, 1, 3, boundingBox);
        k = this.getMetadataWithOffset(Blocks.oak_stairs, 3);
        this.setBlockState(world, Blocks.oak_stairs.getStateFromMeta(k), 6, 1, 4, boundingBox);
        this.setBlockState(world, Blocks.oak_stairs.getStateFromMeta(k), 5, 1, 4, boundingBox);
        this.setBlockState(world, Blocks.oak_stairs.getStateFromMeta(k), 4, 1, 4, boundingBox);
        this.setBlockState(world, Blocks.oak_stairs.getStateFromMeta(k), 3, 1, 4, boundingBox);
        this.setBlockState(world, Blocks.oak_fence.getDefaultState(), 6, 1, 3, boundingBox);
        this.setBlockState(world, Blocks.wooden_pressure_plate.getDefaultState(), 6, 2, 3, boundingBox);
        this.setBlockState(world, Blocks.oak_fence.getDefaultState(), 4, 1, 3, boundingBox);
        this.setBlockState(world, Blocks.wooden_pressure_plate.getDefaultState(), 4, 2, 3, boundingBox);
        this.setBlockState(world, MatterOverdriveBlocks.inscriber.getDefaultState(), 7, 1, 1, boundingBox);

        if (!this.hasMadeChest)
        {
            this.hasMadeChest = true;
            BlockPos pos = new BlockPos(this.getXWithOffset(1, 4),this.getYWithOffset(1),this.getZWithOffset(1, 4));

            if (boundingBox.isVecInside(pos))
            {
                world.setBlockState(pos, MatterOverdriveBlocks.tritaniumCrate.getDefaultState(), 2);
                TileEntityTritaniumCrate tileentitycrate = (TileEntityTritaniumCrate)world.getTileEntity(pos);
                tileentitycrate.getInventory().addItem(MatterOverdrive.questFactory.generateQuestStack(random, MatterOverdrive.quests.getQuestByName("gmo")).getContract());
                ItemStack scanner = new ItemStack(MatterOverdriveItems.dataPad);
                scanner.setStackDisplayName("Mad Scientist's Data Pad");
                MatterOverdriveItems.dataPad.addToScanWhitelist(scanner,Blocks.carrots);
                MatterOverdriveItems.dataPad.addToScanWhitelist(scanner,Blocks.potatoes);
                MatterOverdriveItems.dataPad.addToScanWhitelist(scanner,Blocks.wheat);
                scanner.getTagCompound().setBoolean("Destroys",true);
                scanner.getTagCompound().setBoolean("nogui",true);
                tileentitycrate.getInventory().addItem(scanner);
                return true;
            }
            else
            {
                return false;
            }
        }

        this.setBlockState(world, Blocks.air.getDefaultState(), 1, 1, 0, boundingBox);
        this.setBlockState(world, Blocks.air.getDefaultState(), 1, 2, 0, boundingBox);
        this.placeDoorCurrentPosition(world, boundingBox, random, 1, 1, 0,EnumFacing.getHorizontal(this.getMetadataWithOffset(Blocks.oak_door, 1)));

        if (this.getBlockStateFromPos(world, 1, 0, -1, boundingBox).getBlock().getMaterial() == Material.air && this.getBlockStateFromPos(world, 1, -1, -1, boundingBox).getBlock().getMaterial() != Material.air)
        {
            this.setBlockState(world, Blocks.stone_stairs.getStateFromMeta(this.getMetadataWithOffset(Blocks.stone_stairs, 3)), 1, 0, -1, boundingBox);
        }

        for (l = 0; l < 6; ++l)
        {
            for (int i1 = 0; i1 < 9; ++i1)
            {
                this.clearCurrentPositionBlocksUpwards(world, i1, 9, l, boundingBox);
                this.replaceAirAndLiquidDownwards(world, Blocks.cobblestone.getDefaultState(), i1, -1, l, boundingBox);
            }
        }

        spawnVillagers(world, boundingBox, 2, 3, 2, 1);
        return true;
    }

    @Override
    protected void spawnVillagers(World world, StructureBoundingBox structureBoundingBox, int x, int y, int z, int count)
    {
        if (this.villagersSpawned < count)
        {
            for (int i1 = this.villagersSpawned; i1 < count; ++i1)
            {
                int j1 = this.getXWithOffset(x + i1, z);
                int k1 = this.getYWithOffset(y);
                int l1 = this.getZWithOffset(x + i1, z);

                if (!structureBoundingBox.isVecInside(new Vec3i(j1, k1, l1)))
                {
                    break;
                }

                ++this.villagersSpawned;
                EntityVillagerMadScientist madScientist = new EntityVillagerMadScientist(world);
                madScientist.setLocationAndAngles((double) j1 + 0.5D, (double) k1, (double) l1 + 0.5D, 0.0F, 0.0F);
                world.spawnEntityInWorld(madScientist);
            }
        }
    }

    /**
     * Returns the villager type to spawn in this component, based on the number of villagers already spawned.
     */
    protected int getVillagerType(int p_74888_1_)
    {
        return 666;
    }
}
