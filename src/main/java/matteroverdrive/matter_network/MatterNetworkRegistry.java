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

package matteroverdrive.matter_network;

import matteroverdrive.api.network.MatterNetworkTask;
import matteroverdrive.matter_network.packets.MatterNetworkBroadcastPacket;
import matteroverdrive.matter_network.packets.MatterNetworkRequestPacket;
import matteroverdrive.matter_network.packets.MatterNetworkTaskPacket;
import matteroverdrive.matter_network.packets.MatterNetworkResponsePacket;
import matteroverdrive.matter_network.tasks.MatterNetworkTaskReplicatePattern;
import matteroverdrive.matter_network.tasks.MatterNetworkTaskStorePattern;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Created by Simeon on 4/27/2015.
 */
public class MatterNetworkRegistry
{
    public static List<Class> packetTypes = new ArrayList<>();
    public static List<Class> taskTypes = new ArrayList<>();

    public static int registerPacket(Class<? extends MatterNetworkPacket> packetClass)
    {
        packetTypes.add(packetClass);
        return packetTypes.size()-1;
    }

    public static int registerTask(Class<? extends MatterNetworkTask> taskClass)
    {
        taskTypes.add(taskClass);
        return packetTypes.size()-1;
    }

    public static void register()
    {
        registerPacket(MatterNetworkTaskPacket.class);
        registerPacket(MatterNetworkRequestPacket.class);
        registerPacket(MatterNetworkBroadcastPacket.class);
        registerPacket(MatterNetworkResponsePacket.class);

        registerTask(MatterNetworkTaskReplicatePattern.class);
        registerTask(MatterNetworkTaskStorePattern.class);
    }

    public static int getPacketID(Class<? extends MatterNetworkPacket> type) throws NoSuchElementException {
        for (int i = 0;i < packetTypes.size();i++)
        {
            if (type.equals(packetTypes.get(i)))
                return i;
        }
        throw new NoSuchElementException(String.format("Packet %s was not registered",type));
    }

    public static int getTaskID(Class<? extends MatterNetworkTask> type) throws NoSuchElementException {
        for (int i = 0;i < taskTypes.size();i++)
        {
            if (type.equals(taskTypes.get(i)))
                return i;
        }
        throw new NoSuchElementException(String.format("Task %s was not registered",type));
    }

    public static Class getPacketClass(int id)
    {
        return packetTypes.get(id);
    }
    public static Class getTaskClass(int id){return taskTypes.get(id);}
}
