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

import cofh.lib.util.helpers.BlockHelper;
import cofh.lib.util.helpers.MathHelper;
import cofh.lib.util.position.BlockPosition;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import matteroverdrive.Reference;
import matteroverdrive.api.IScannable;
import matteroverdrive.api.inventory.UpgradeTypes;
import matteroverdrive.api.matter.IMatterDatabase;
import matteroverdrive.api.matter.IMatterPatternStorage;
import matteroverdrive.api.network.IMatterNetworkClient;
import matteroverdrive.blocks.BlockPatternStorage;
import matteroverdrive.data.Inventory;
import matteroverdrive.data.inventory.DatabaseSlot;
import matteroverdrive.data.inventory.PatternStorageSlot;
import matteroverdrive.init.MatterOverdriveItems;
import matteroverdrive.items.MatterScanner;
import matteroverdrive.matter_network.MatterNetworkPacket;
import matteroverdrive.matter_network.MatterNetworkPacketQueue;
import matteroverdrive.matter_network.components.MatterNetworkComponentPatternStorage;
import matteroverdrive.util.MatterDatabaseHelper;
import matteroverdrive.util.MatterHelper;
import matteroverdrive.util.MatterNetworkHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;

/**
 * Created by Simeon on 3/27/2015.
 */
public class TileEntityMachinePatternStorage extends MOTileEntityMachineEnergy implements IMatterDatabase, IMatterNetworkClient,IScannable
{
    public static final int TASK_PROCESS_DELAY = 40;
    public static int ENERGY_CAPACITY = 64000;
    public static int ENERGY_TRANSFER = 128;
    public int input_slot;
    public int[] pattern_storage_slots;
    private MatterNetworkPacketQueue taskQueueProcessing;
    private MatterNetworkComponentPatternStorage networkComponent;

    public TileEntityMachinePatternStorage()
    {
        super(4);
        this.energyStorage.setCapacity(ENERGY_CAPACITY);
        this.energyStorage.setMaxExtract(ENERGY_TRANSFER);
        this.energyStorage.setMaxReceive(ENERGY_TRANSFER);
        this.taskQueueProcessing = new MatterNetworkPacketQueue(this,1);
        redstoneMode = Reference.MODE_REDSTONE_LOW;
        networkComponent = new MatterNetworkComponentPatternStorage(this);
    }

    @Override
    public void updateEntity()
    {
        super.updateEntity();

        if(!worldObj.isRemote) {
            if (energyStorage.getEnergyStored() > 0) {
                manageLinking();
            }
        }
        else
        {
            if (isActive() && random.nextFloat() < 0.2f && getBlockType(BlockPatternStorage.class) != null && getBlockType(BlockPatternStorage.class).hasVentParticles)
            {
                SpawnVentParticles(0.03f, ForgeDirection.getOrientation(BlockHelper.getOppositeSide(worldObj.getBlockMetadata(xCoord, yCoord, zCoord))), 1);
            }
        }
    }

    @Override
    protected void RegisterSlots(Inventory inventory)
    {
        pattern_storage_slots = new int[6];
        input_slot = inventory.AddSlot(new DatabaseSlot(true));

        for (int i = 0;i < pattern_storage_slots.length;i++)
        {
            pattern_storage_slots[i] = inventory.AddSlot(new PatternStorageSlot(false));
        }

        super.RegisterSlots(inventory);
    }

    protected void manageLinking()
    {
        if(MatterHelper.isMatterScanner(inventory.getStackInSlot(input_slot)))
        {
            MatterScanner.link(worldObj, xCoord, yCoord, zCoord, inventory.getStackInSlot(input_slot));
        }
    }

    @Override
    public boolean isAffectedByUpgrade(UpgradeTypes type)
    {
        return type == UpgradeTypes.PowerStorage || type == UpgradeTypes.PowerUsage;
    }

    @Override
    public void addInfo(World world, double x, double y, double z, List<String> infos)
    {
        int patternCount = 0;
        for (ItemStack patternDrive : getPatternStorageList())
        {
            if (patternDrive != null) {
                patternCount += MatterOverdriveItems.pattern_drive.getItemsAsNBT(patternDrive).tagCount();
            }
        }
        if (patternCount > 0)
        {
            infos.add(patternCount + "xPatterns");
        }else
        {
            infos.add("No Patterns.");
        }

    }

    //region NBT
    @Override
    public void writeCustomNBT(NBTTagCompound nbt)
    {
        super.writeCustomNBT(nbt);
        taskQueueProcessing.writeToNBT(nbt);
    }

    @Override
    public void readCustomNBT(NBTTagCompound nbt)
    {
        super.readCustomNBT(nbt);
        taskQueueProcessing.readFromNBT(nbt);
    }
    //endregion

    //region Database functions
    @Override
    public NBTTagList getItemsAsNBT()
    {
        NBTTagList list = new NBTTagList();
        for (int i = 0;i < pattern_storage_slots.length;i++)
        {
            if(MatterHelper.isMatterPatternStorage(inventory.getStackInSlot(pattern_storage_slots[i])))
            {
                NBTTagList l = MatterDatabaseHelper.GetItemsTagList(inventory.getStackInSlot(pattern_storage_slots[i]));
                if(l != null)
                {
                    for (int t = 0;t < l.tagCount();t++)
                    {
                        list.appendTag(l.getCompoundTagAt(t));
                    }
                }
            }
        }
        return list;
    }

    @Override
    public ItemStack[] getItems() {
        return new ItemStack[0];
    }

    @Override
    public boolean hasItem(ItemStack item)
    {
        for (int i = 0;i < pattern_storage_slots.length;i++)
        {
            if(MatterHelper.isMatterPatternStorage(inventory.getStackInSlot(pattern_storage_slots[i])))
            {
                boolean hasItem = MatterDatabaseHelper.HasItem(inventory.getStackInSlot(pattern_storage_slots[i]), item);
                if(hasItem)
                    return hasItem;
            }
        }
        return false;
    }

    //increases the progress if the database has the item
    //if it does not have the item it adds it
    @Override
    public boolean addItem(ItemStack itemStack,int amount,boolean simulate,StringBuilder info)
    {
        NBTTagCompound hasItem = null;

        if (!MatterHelper.CanScan(itemStack))
        {
            info.append(EnumChatFormatting.RED + itemStack.getDisplayName() + " cannot be analyzed!");
            return false;
        }

        for (int i = 0;i < pattern_storage_slots.length;i++)
        {
            if(MatterHelper.isMatterPatternStorage(inventory.getStackInSlot(pattern_storage_slots[i])))
            {
                NBTTagCompound hasItemInPatternStorage = MatterDatabaseHelper.GetItemAsNBT(inventory.getStackInSlot(pattern_storage_slots[i]), itemStack);
                if (hasItemInPatternStorage != null)
                {
                    hasItem = hasItemInPatternStorage;
                    break;

                }
            }
        }

        if (hasItem != null)
        {
            int progress = MatterDatabaseHelper.GetProgressFromNBT(hasItem);

            if (progress < MatterDatabaseHelper.MAX_ITEM_PROGRESS)
            {
                if (!simulate)
                {
                    progress = MathHelper.clampI(progress + amount, 0, MatterDatabaseHelper.MAX_ITEM_PROGRESS);
                    MatterDatabaseHelper.SetProgressToNBT(hasItem, (byte) progress);
                    ForceSync();
                }
                if (info != null)
                    info.append(EnumChatFormatting.GREEN + itemStack.getDisplayName() + " added to Pattern Storage. Progress is now at " + progress + "%.");
                return true;
            }
            else
            {
                if (info != null)
                    info.append(EnumChatFormatting.RED + itemStack.getDisplayName() + " is fully analyzed!");
                return false;
            }
        }else
        {
            for (int i = 0;i < pattern_storage_slots.length;i++)
            {
                if (inventory.getStackInSlot(pattern_storage_slots[i]) != null)
                {
                    IMatterPatternStorage storage = (IMatterPatternStorage) inventory.getStackInSlot(pattern_storage_slots[i]).getItem();
                    if (storage.addItem(inventory.getStackInSlot(pattern_storage_slots[i]), itemStack, amount, simulate))
                    {
                        if (!simulate)
                            ForceSync();
                        if (info != null)
                            info.append(EnumChatFormatting.GREEN + itemStack.getDisplayName() + " added to Pattern Storage. Progress is now at " + amount + "%.");
                        return true;
                    }
                }
            }
        }

        if (info != null)
            info.append(EnumChatFormatting.RED + "No space available for '"+itemStack.getDisplayName()+"' !");
        return false;
    }

    @Override
    public NBTTagCompound getItemAsNBT(ItemStack item)
    {
        for (int i = 0;i < pattern_storage_slots.length;i++)
        {
            if(MatterHelper.isMatterPatternStorage(inventory.getStackInSlot(pattern_storage_slots[i])))
            {
                NBTTagCompound hasItem = MatterDatabaseHelper.GetItemAsNBT(inventory.getStackInSlot(pattern_storage_slots[i]), item);
                if(hasItem != null)
                    return hasItem;
            }
        }
        return null;
    }

    @Override
    public ItemStack[] getPatternStorageList()
    {
        ItemStack[] patternsDrives = new ItemStack[pattern_storage_slots.length];
        for (int i = 0;i < pattern_storage_slots.length;i++)
        {
            patternsDrives[i] = getStackInSlot(pattern_storage_slots[i]);
        }
        return patternsDrives;
    }
    //endregion

    //region Invetory Functions
    @Override
    public int[] getAccessibleSlotsFromSide(int side)
    {
        if(side == 1)
        {
            return new int[]{input_slot};
        }
        else
        {
            return pattern_storage_slots;
        }
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack item, int side)
    {
        return true;
    }
    //endregion

    //region Matter Network Functions
    @Override
    public boolean canPreform(MatterNetworkPacket packet)
    {
        return networkComponent.canPreform(packet);
    }

    @Override
    public void queuePacket(MatterNetworkPacket packet,ForgeDirection from)
    {
        networkComponent.queuePacket(packet,from);
    }

    @Override
    public BlockPosition getPosition()
    {
        return new BlockPosition(this);
    }

    @Override
    public boolean canConnectFromSide(ForgeDirection side) {
        return side.ordinal() == worldObj.getBlockMetadata(xCoord,yCoord,zCoord);
    }

    @Override
    public int onNetworkTick(World world,TickEvent.Phase phase)
    {
        return 0;
    }
    //endregion

    //region Events
    @Override
    public void onScan(World world, double x, double y, double z, EntityPlayer player, ItemStack scanner) {

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

    @Override
    protected void onAwake(Side side)
    {
        if (side.isServer()) {
            MatterNetworkHelper.broadcastConnection(worldObj, this);
        }
    }

    @Override
    protected void onActiveChange() {

    }
    //endregion

    //region Getters and Setters
    @Override
    public String getSound() {
        return null;
    }

    @Override
    public boolean hasSound() {
        return false;
    }

    @Override
    public boolean isActive()
    {
        return energyStorage.getEnergyStored() > 0;
    }

    @Override
    public float soundVolume() {
        return 0;
    }
    //endregion
}
