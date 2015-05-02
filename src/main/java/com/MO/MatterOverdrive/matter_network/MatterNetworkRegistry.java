package com.MO.MatterOverdrive.matter_network;

import com.MO.MatterOverdrive.matter_network.packets.MatterNetworkBroadcastPacket;
import com.MO.MatterOverdrive.matter_network.packets.MatterNetworkRequestPacket;
import com.MO.MatterOverdrive.matter_network.packets.MatterNetworkTaskPacket;
import com.MO.MatterOverdrive.matter_network.packets.MatterNetwrokResponcePacket;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simeon on 4/27/2015.
 */
public class MatterNetworkRegistry
{
    public static List<Class<? extends MatterNetworkPacket>> packetTypes = new ArrayList<Class<? extends MatterNetworkPacket>>();

    public static int registerPacket(Class<? extends MatterNetworkPacket> packetClass)
    {
        packetTypes.add(packetClass);
        return packetTypes.size()-1;
    }

    public static void register()
    {
        registerPacket(MatterNetworkTaskPacket.class);
        registerPacket(MatterNetworkRequestPacket.class);
        registerPacket(MatterNetworkBroadcastPacket.class);
        registerPacket(MatterNetwrokResponcePacket.class);
    }

    public static int getPacketID(Class<? extends MatterNetworkPacket> type)
    {
        for (int i = 0;i < packetTypes.size();i++)
        {
            if (type.equals(packetTypes.get(i)))
                return i;
        }
        return 0;
    }

    public static Class<? extends MatterNetworkPacket> getPacketClass(int id)
    {
        return packetTypes.get(id);
    }
}
