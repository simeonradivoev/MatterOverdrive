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

package matteroverdrive.tile;

import cpw.mods.fml.relauncher.Side;
import matteroverdrive.api.inventory.UpgradeTypes;
import matteroverdrive.data.MachineMatterStorage;
import matteroverdrive.machines.MachineNBTCategory;
import matteroverdrive.machines.fusionReactorController.TileEntityMachineFusionReactorController;
import matteroverdrive.multiblock.IMultiBlockTile;
import matteroverdrive.multiblock.IMultiBlockTileStructure;
import matteroverdrive.multiblock.MultiBlockTileStructureMachine;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.EnumSet;

/**
 * Created by Simeon on 10/30/2015.
 */
public class TileEntityFusionReactorPart extends MOTileEntityMachineMatter implements IMultiBlockTile
{
    private IMultiBlockTileStructure structure;
    private TileEntityMachineFusionReactorController fusionReactorController;

    public TileEntityFusionReactorPart()
    {
        super(0);
        energyStorage.setCapacity(0);
        energyStorage.setMaxTransfer(0);
    }

    @Override
    public String getSound() {
        return null;
    }

    @Override
    public boolean hasSound() {
        return false;
    }

    @Override
    public boolean getServerActive() {
        return false;
    }

    @Override
    public float soundVolume() {
        return 0;
    }

    @Override
    protected void onActiveChange() {

    }

    @Override
    public void onAdded(World world, int x, int y, int z) {

    }

    @Override
    public void onPlaced(World world, EntityLivingBase entityLiving) {

    }

    @Override
    public void onDestroyed() {

    }

    public MachineMatterStorage getMatterStorage()
    {
        if (fusionReactorController != null)
        {
            return fusionReactorController.getMatterStorage();
        }
        return null;
    }

    @Override
    public boolean isAffectedByUpgrade(UpgradeTypes type) {
        return false;
    }

    @Override
    protected void onAwake(Side side) {

    }

    @Override
    public boolean canJoinMultiBlockStructure(IMultiBlockTileStructure structure)
    {
        return getMultiBlockHandler() == null && structure instanceof MultiBlockTileStructureMachine && ((MultiBlockTileStructureMachine) structure).getMachine() instanceof TileEntityMachineFusionReactorController;
    }

    @Override
    public boolean isMultiblockInvalid() {
        return isInvalid();
    }

    @Override
    public IMultiBlockTileStructure getMultiBlockHandler() {
        return structure;
    }

    @Override
    public void setMultiBlockTileStructure(IMultiBlockTileStructure structure)
    {
        this.structure = structure;
        if (structure == null)
        {
            fusionReactorController = null;
        }else if (structure instanceof MultiBlockTileStructureMachine)
        {
            fusionReactorController = (TileEntityMachineFusionReactorController)((MultiBlockTileStructureMachine)structure).getMachine();
        }
    }

    @Override
    public void writeCustomNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories, boolean toDisk)
    {

    }

    @Override
    public void readCustomNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories)
    {

    }
}
