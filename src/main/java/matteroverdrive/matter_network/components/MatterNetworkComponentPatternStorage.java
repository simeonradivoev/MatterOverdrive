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

package matteroverdrive.matter_network.components;

import cofh.lib.util.TimeTracker;
import cofh.lib.util.position.BlockPosition;
import cpw.mods.fml.common.gameevent.TickEvent;
import matteroverdrive.Reference;
import matteroverdrive.api.network.IMatterNetworkClient;
import matteroverdrive.api.network.MatterNetworkTask;
import matteroverdrive.api.network.MatterNetworkTaskState;
import matteroverdrive.matter_network.MatterNetworkPacket;
import matteroverdrive.matter_network.packets.MatterNetworkRequestPacket;
import matteroverdrive.matter_network.packets.MatterNetworkTaskPacket;
import matteroverdrive.matter_network.packets.MatterNetworkResponsePacket;
import matteroverdrive.matter_network.tasks.MatterNetworkTaskStorePattern;
import matteroverdrive.tile.TileEntityMachinePatternStorage;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by Simeon on 7/15/2015.
 */
public class MatterNetworkComponentPatternStorage extends MatterNetworkComponentClient
{
    private TileEntityMachinePatternStorage patternStorage;
    private TimeTracker taskProcessingTracker;

    public MatterNetworkComponentPatternStorage(TileEntityMachinePatternStorage patternStorage)
    {
        this.patternStorage = patternStorage;
        taskProcessingTracker = new TimeTracker();
    }

    @Override
    public boolean canPreform(MatterNetworkPacket packet) {
        if (patternStorage.getRedstoneActive()) {
            if (packet instanceof MatterNetworkTaskPacket) {
                if (((MatterNetworkTaskPacket) packet).getTask(patternStorage.getWorldObj()) instanceof MatterNetworkTaskStorePattern) {
                    MatterNetworkTaskStorePattern task = (MatterNetworkTaskStorePattern) ((MatterNetworkTaskPacket) packet).getTask(patternStorage.getWorldObj());
                    return patternStorage.addItem(task.getItemStack(), task.getProgress(), true, null);
                }
            } else if (packet instanceof MatterNetworkRequestPacket) {
                MatterNetworkRequestPacket requestPacket = (MatterNetworkRequestPacket) packet;
                return requestPacket.getRequestType() == Reference.PACKET_REQUEST_CONNECTION
                        || requestPacket.getRequestType() == Reference.PACKET_REQUEST_PATTERN_SEARCH
                        || requestPacket.getRequestType() == Reference.PACKET_REQUEST_NEIGHBOR_CONNECTION;
            }
        }
        return false;
    }

    @Override
    public void queuePacket(MatterNetworkPacket packet, ForgeDirection from)
    {
        if (packet != null && packet.isValid(patternStorage.getWorldObj()))
        {
            if (packet instanceof MatterNetworkTaskPacket) {
                if (taskProcessingTracker.hasDelayPassed(patternStorage.getWorldObj(), patternStorage.TASK_PROCESS_DELAY) && ((MatterNetworkTaskPacket) packet).getTask(patternStorage.getWorldObj()).getState().belowOrEqual(MatterNetworkTaskState.QUEUED)) {
                    MatterNetworkTask task = ((MatterNetworkTaskPacket) packet).getTask(patternStorage.getWorldObj());
                    if (task instanceof MatterNetworkTaskStorePattern) {
                        if (patternStorage.addItem(((MatterNetworkTaskStorePattern) task).getItemStack(), ((MatterNetworkTaskStorePattern) task).getProgress(), false, null)) {
                            //if the task is finished and the item is in the database
                            task.setState(MatterNetworkTaskState.FINISHED);
                        } else {
                            //if the item could not be added to the database for some reason, and has passed the canProcess check
                            //then reset the task and set it to waiting
                            task.setState(MatterNetworkTaskState.WAITING);
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
                NBTTagCompound tagCompound = patternStorage.getItemAsNBT(new ItemStack(Item.getItemById(array[0]), 1, array[1]));
                if (tagCompound != null && packet.getSender(patternStorage.getWorldObj()) instanceof IMatterNetworkClient) {
                    ((IMatterNetworkClient) packet.getSender(patternStorage.getWorldObj())).queuePacket(new MatterNetworkResponsePacket(patternStorage, Reference.PACKET_RESPONCE_VALID,packet.getRequestType(), tagCompound,packet.getSenderPort()), ForgeDirection.UNKNOWN);
                }
            }
        }else if (packet.getRequestType() == Reference.PACKET_REQUEST_NEIGHBOR_CONNECTION || packet.getRequestType() == Reference.PACKET_REQUEST_CONNECTION)
        {
            manageRequestPackets(patternStorage,patternStorage.getWorldObj(),packet,from);
        }
    }

    @Override
    public BlockPosition getPosition() {
        return patternStorage.getPosition();
    }

    @Override
    public boolean canConnectFromSide(ForgeDirection side) {
        return patternStorage.canConnectFromSide(side);
    }

    @Override
    public int onNetworkTick(World world, TickEvent.Phase phase) {
        return 0;
    }
}
