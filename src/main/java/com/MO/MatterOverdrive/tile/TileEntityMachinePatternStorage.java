package com.MO.MatterOverdrive.tile;

import cofh.lib.util.TimeTracker;
import cofh.lib.util.helpers.BlockHelper;
import cofh.lib.util.helpers.MathHelper;
import cofh.lib.util.position.BlockPosition;
import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.api.matter.IMatterDatabase;
import com.MO.MatterOverdrive.api.matter.IMatterPatternStorage;
import com.MO.MatterOverdrive.api.network.*;
import com.MO.MatterOverdrive.blocks.BlockPatternStorage;
import com.MO.MatterOverdrive.data.Inventory;
import com.MO.MatterOverdrive.data.inventory.DatabaseSlot;
import com.MO.MatterOverdrive.data.inventory.PatternStorageSlot;
import com.MO.MatterOverdrive.util.MatterNetworkHelper;
import cpw.mods.fml.relauncher.Side;
import com.MO.MatterOverdrive.matter_network.MatterNetworkPacket;
import com.MO.MatterOverdrive.matter_network.packets.MatterNetworkRequestPacket;
import com.MO.MatterOverdrive.matter_network.packets.MatterNetworkTaskPacket;
import com.MO.MatterOverdrive.matter_network.MatterNetworkPacketQueue;
import com.MO.MatterOverdrive.matter_network.packets.MatterNetwrokResponcePacket;
import com.MO.MatterOverdrive.matter_network.tasks.MatterNetworkTaskStorePattern;
import com.MO.MatterOverdrive.items.MatterScanner;
import com.MO.MatterOverdrive.util.MatterDatabaseHelper;
import com.MO.MatterOverdrive.util.MatterHelper;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by Simeon on 3/27/2015.
 */
public class TileEntityMachinePatternStorage extends MOTileEntityMachineEnergy implements IMatterDatabase, IMatterNetworkClient,IMatterNetworkConnectionProxy
{
    public static final int TASK_PROCESS_DELAY = 40;
    public static final int ENERGY_CAPACITY = 64000;
    public static final int ENERGY_TRANSFER = 128;
    public int input_slot;
    public int[] pattern_storage_slots;
    private MatterNetworkPacketQueue taskQueueProcessing;
    private TimeTracker taskProcessingTracker;

    public TileEntityMachinePatternStorage()
    {
        super(4);
        this.energyStorage.setCapacity(ENERGY_CAPACITY);
        this.energyStorage.setMaxExtract(ENERGY_TRANSFER);
        this.energyStorage.setMaxReceive(ENERGY_TRANSFER);
        this.taskQueueProcessing = new MatterNetworkPacketQueue(this,1);
        taskProcessingTracker = new TimeTracker();
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
            if (isActive() && random.nextFloat() < 0.2f && getBlockType(BlockPatternStorage.class).hasVentParticles)
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
    public void writeCustomNBT(NBTTagCompound nbt)
    {
        super.writeCustomNBT(nbt);
        taskQueueProcessing.writeToNBT(worldObj, nbt);
    }

    @Override
    public void readCustomNBT(NBTTagCompound nbt)
    {
        super.readCustomNBT(nbt);
        taskQueueProcessing.readFromNBT(worldObj, nbt);
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
    public boolean isActive()
    {
        return energyStorage.getEnergyStored() > 0;
    }

    @Override
    public float soundVolume() {
        return 0;
    }

    @Override
    protected void onAwake(Side side)
    {
        if (side.isServer()) {
            MatterNetworkHelper.broadcastConnection(worldObj, this);
        }
    }

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
        if (packet instanceof MatterNetworkTaskPacket) {
            if (((MatterNetworkTaskPacket) packet).getTask(worldObj) instanceof MatterNetworkTaskStorePattern) {
                MatterNetworkTaskStorePattern task = (MatterNetworkTaskStorePattern) ((MatterNetworkTaskPacket) packet).getTask(worldObj);
                return addItem(task.getItemStack(), task.getProgress(), true, null);
            }
        }
        else if (packet instanceof MatterNetworkRequestPacket)
        {
            MatterNetworkRequestPacket requestPacket = (MatterNetworkRequestPacket)packet;
            return requestPacket.getRequestType() == Reference.PACKET_REQUEST_CONNECTION
                    || requestPacket.getRequestType() == Reference.PACKET_REQUEST_PATTERN_SEARCH
                    || requestPacket.getRequestType() == Reference.PACKET_REQUEST_NEIGHBOR_CONNECTION;
        }
        return false;
    }

    @Override
    public void queuePacket(MatterNetworkPacket packet,ForgeDirection from)
    {
        if (packet != null && packet.isValid(worldObj))
        {
            if (packet instanceof MatterNetworkTaskPacket) {
                if (taskProcessingTracker.hasDelayPassed(worldObj, TASK_PROCESS_DELAY) && ((MatterNetworkTaskPacket) packet).getTask(worldObj).getState() <= Reference.TASK_STATE_QUEUED) {
                    MatterNetworkTask task = ((MatterNetworkTaskPacket) packet).getTask(worldObj);
                    if (task instanceof MatterNetworkTaskStorePattern) {
                        if (addItem(((MatterNetworkTaskStorePattern) task).getItemStack(), ((MatterNetworkTaskStorePattern) task).getProgress(), false, null)) {
                            //if the task is finished and the item is in the database
                            task.setState(Reference.TASK_STATE_FINISHED);
                        } else {
                            //if the item could not be added to the database for some reason, and has passed the canProcess check
                            //then reset the task and set it to waiting
                            task.setState(Reference.TASK_STATE_WAITING);
                        }
                    }
                }
            }
            else if (packet instanceof MatterNetworkRequestPacket)
            {
                manageRequests((MatterNetworkRequestPacket)packet,from);
            }
        }
    }

    private void manageRequests(MatterNetworkRequestPacket packet,ForgeDirection from)
    {
        if (packet.getRequestType() == Reference.PACKET_REQUEST_PATTERN_SEARCH)
        {
            if (packet.getRequest() instanceof int[]) {
                int[] array = (int[]) packet.getRequest();
                NBTTagCompound tagCompound = getItemAsNBT(new ItemStack(Item.getItemById(array[0]), 1, array[1]));
                if (tagCompound != null && packet.getSender(worldObj) instanceof IMatterNetworkClient) {
                    ((IMatterNetworkClient) packet.getSender(worldObj)).queuePacket(new MatterNetwrokResponcePacket(this, Reference.PACKET_RESPONCE_VALID,packet.getRequestType(), tagCompound,packet.getSenderPort()), ForgeDirection.UNKNOWN);
                }
            }
        }else if (packet.getRequestType() == Reference.PACKET_REQUEST_NEIGHBOR_CONNECTION || packet.getRequestType() == Reference.PACKET_REQUEST_CONNECTION)
        {
            MatterNetworkHelper.handleConnectionRequestPacket(worldObj,this,packet,from);
        }
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
    public IMatterNetworkConnection getMatterNetworkConnection()
    {
        return this;
    }

    @Override
    public int onNetworkTick(World world,TickEvent.Phase phase)
    {
        return 0;
    }
    //endregion
}
