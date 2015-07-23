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
import cpw.mods.fml.relauncher.SideOnly;
import matteroverdrive.Reference;
import matteroverdrive.api.inventory.UpgradeTypes;
import matteroverdrive.data.Inventory;
import matteroverdrive.data.inventory.BionicSlot;
import matteroverdrive.data.inventory.EnergySlot;
import matteroverdrive.data.inventory.ModuleSlot;
import matteroverdrive.entity.AndroidPlayer;
import matteroverdrive.machines.MOTileEntityMachine;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

/**
 * Created by Simeon on 5/27/2015.
 */
public class TileEntityAndroidStation extends MOTileEntityMachine
{
    public int HEAD_SLOT;
    public int ARMS_SLOT;
    public int LEGS_SLOT;
    public int CHEST_SLOT;
    public int OTHER_SLOT;
    public int BATTERY_SLOT;

    public TileEntityAndroidStation()
    {
        super(0);
    }

    @Override
    protected void onAwake(Side side) {

    }

    @Override
    protected void RegisterSlots(Inventory inventory)
    {
        HEAD_SLOT = inventory.AddSlot(new BionicSlot(false, Reference.BIONIC_HEAD));
        ARMS_SLOT = inventory.AddSlot(new BionicSlot(false,Reference.BIONIC_ARMS));
        LEGS_SLOT = inventory.AddSlot(new ModuleSlot(false,Reference.BIONIC_LEGS));
        CHEST_SLOT = inventory.AddSlot(new ModuleSlot(false,Reference.BIONIC_CHEST));
        OTHER_SLOT = inventory.AddSlot(new ModuleSlot(false,Reference.BIONIC_OTHER));
        BATTERY_SLOT = inventory.AddSlot(new EnergySlot(false));
        redstoneMode = Reference.MODE_REDSTONE_LOW;
        super.RegisterSlots(inventory);
    }

    @Override
    protected void registerComponents() {

    }

    public IInventory getActiveInventory()
    {
        return inventory;
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
    public boolean isActive() {
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
    public boolean shouldRenderInPass(int pass)
    {
        return pass == 1;
    }

    //region Inventory Functions
    @Override
    public ItemStack getStackInSlot(int slot)
    {
        return super.getStackInSlot(slot);
    }

    @Override
    public ItemStack decrStackSize(int slot, int size)
    {
        return super.decrStackSize(slot, size);
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot)
    {
        return super.getStackInSlotOnClosing(slot);
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack itemStack)
    {
        super.setInventorySlotContents(slot,itemStack);
    }
    //endregion

    //region Upgrades
    @Override
    public boolean isAffectedByUpgrade(UpgradeTypes type)
    {
        return false;
    }
    //endregion

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox()
    {
        return AxisAlignedBB.getBoundingBox(xCoord,yCoord,zCoord,xCoord + 1,yCoord + 3,zCoord + 1);
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player)
    {
        return AndroidPlayer.get(player) != null && AndroidPlayer.get(player).isAndroid() && super.isUseableByPlayer(player);
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
}
