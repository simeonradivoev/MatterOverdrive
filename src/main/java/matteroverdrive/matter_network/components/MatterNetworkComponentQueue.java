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

import matteroverdrive.data.matter_network.IMatterNetworkEvent;
import matteroverdrive.tile.TileEntityMachinePacketQueue;

/**
 * Created by Simeon on 7/15/2015.
 */
public class MatterNetworkComponentQueue extends MatterNetworkComponentClient<TileEntityMachinePacketQueue>
{

	public static final int[] directions = {0, 1, 2, 3, 4, 5};

	public MatterNetworkComponentQueue(TileEntityMachinePacketQueue queue)
	{
		super(queue);
	}

	@Override
	public void onNetworkEvent(IMatterNetworkEvent event)
	{

	}

    /*@Override
	public boolean canPreform(MatterNetworkPacket packet)
    {
        return rootClient.getRedstoneActive();
    }

    @Override
    public void queuePacket(MatterNetworkPacket packet)
    {
        if (canPreform(packet) && packet.isValid(getWorldObj()))
        {
            if (getPacketQueue(0).queue(packet))
            {
                packet.addToPath(rootClient);
                packet.tickAlive(getWorldObj(),true);
                MatterOverdrive.packetPipeline.sendToAllAround(new PacketSendQueueFlash(rootClient), rootClient, 32);
            }
        }
    }

    @Override
    protected void executePacket(MatterNetworkPacket packet)
    {

    }*/

    /*protected int handlePacketBroadcast(World world,MatterNetworkPacket packet)
    {
        int broadcastCount = 0;
        for (int direction : directions)
        {
            if (MatterNetworkHelper.broadcastPacketInDirection(world, packet, rootClient, EnumFacing.VALUES[direction]))
            {
                broadcastCount++;
            }
        }
        return broadcastCount;
    }*/

    /*@Override
    public int onNetworkTick(World world, TickEvent.Phase phase)
    {
        int broadcastCount = 0;
        if (phase == TickEvent.Phase.END)
        {
            for (int i = 0;i < getPacketQueueCount();i++) {
                getPacketQueue(i).tickAllAlive(world, true);

                MatterNetworkPacket packet = getPacketQueue(i).dequeue();
                if (packet != null) {
                    if (packet.isValid(world)) {

                        broadcastCount += handlePacketBroadcast(world, packet);
                    }
                }
            }
        }
        return broadcastCount;
    }*/
}
