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

import cofh.lib.util.TimeTracker;
import cofh.lib.util.helpers.BlockHelper;
import cofh.lib.util.position.BlockPosition;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.api.inventory.UpgradeTypes;
import matteroverdrive.api.matter.IMatterDatabase;
import matteroverdrive.api.network.IMatterNetworkClient;
import matteroverdrive.api.network.IMatterNetworkDispatcher;
import matteroverdrive.matter_network.MatterNetworkPacket;
import matteroverdrive.matter_network.MatterNetworkTaskQueue;
import matteroverdrive.matter_network.components.MatterNetworkComponentPatternMonitor;
import matteroverdrive.matter_network.packets.MatterNetworkRequestPacket;
import matteroverdrive.matter_network.packets.MatterNetwrokResponcePacket;
import matteroverdrive.matter_network.tasks.MatterNetworkTaskReplicatePattern;
import matteroverdrive.network.packet.client.PacketPatternMonitorSync;
import matteroverdrive.util.MatterNetworkHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.HashSet;

/**
 * Created by Simeon on 4/26/2015.
 */
public class TileEntityMachinePatternMonitor extends MOTileEntityMachineEnergy implements IMatterNetworkDispatcher, IMatterNetworkClient
{
    public static final int BROADCAST_WEATING_DELAY = 80;
    public static final int SEARCH_DELAY = 120;
    public static final int VALIDATE_DELAY = 120;
    public static final int TASK_QUEUE_SIZE = 16;
    HashSet<BlockPosition> databases;
    MatterNetworkTaskQueue<MatterNetworkTaskReplicatePattern> taskQueue;
    TimeTracker searchDelayTracker;
    private MatterNetworkComponentPatternMonitor networkComponent;

    public TileEntityMachinePatternMonitor()
    {
        super(4);
        taskQueue = new MatterNetworkTaskQueue<>(this,TASK_QUEUE_SIZE);
        databases = new HashSet<>();
        searchDelayTracker = new TimeTracker();
        networkComponent = new MatterNetworkComponentPatternMonitor(this);
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
        return false;
    }

    @Override
    public float soundVolume() {
        return 0;
    }

    public boolean isAffectedByUpgrade(UpgradeTypes type)
    {
        return false;
    }

    //region Matter Network Functions

    @Override
    public int onNetworkTick(World world, TickEvent.Phase phase)
    {
        return networkComponent.onNetworkTick(world,phase);
    }

    @Override
    public MatterNetworkTaskQueue<MatterNetworkTaskReplicatePattern> getQueue(int id)
    {
        return taskQueue;
    }

    public void SyncDatabasesWithClient()
    {
        MatterOverdrive.packetPipeline.sendToAllAround(new PacketPatternMonitorSync(this), this, 64);
    }

    @Override
    public void writeCustomNBT(NBTTagCompound nbt)
    {
        super.writeCustomNBT(nbt);
        taskQueue.writeToNBT(nbt);
    }

    @Override
    protected void onActiveChange() {

    }

    @Override
    public void readCustomNBT(NBTTagCompound nbt)
    {
        super.readCustomNBT(nbt);
        taskQueue.readFromNBT(nbt);
    }

    @Override
    protected void onAwake(Side side) {

    }

    @Override
    public BlockPosition getPosition() {
        return new BlockPosition(xCoord,yCoord,zCoord);
    }

    @Override
    public boolean canConnectFromSide(ForgeDirection side)
    {
        int meta = worldObj.getBlockMetadata(xCoord,yCoord,zCoord);
        return BlockHelper.getOppositeSide(meta) == side.ordinal();
    }

    //endregion

    public void queueRequest(int[] request)
    {
        networkComponent.queueRequest(request);
    }

    public HashSet<BlockPosition> getDatabases()
    {
        return databases;
    }

    public void setDatabases(HashSet<BlockPosition> blockPositions)
    {
        databases = blockPositions;
    }

    public void forceSearch(boolean refresh)
    {
        networkComponent.setNeedsSearchRefresh(refresh);
    }

    public void queueSearch()
    {
        if (searchDelayTracker.hasDelayPassed(worldObj,SEARCH_DELAY))
        {
            forceSearch(true);
        }
    }

    public boolean needsRefresh()
    {
        return networkComponent.getNeedsSearchRefresh();
    }

    @Override
    public boolean canPreform(MatterNetworkPacket packet)
    {
        return networkComponent.canPreform(packet);
    }

    @Override
    public void queuePacket(MatterNetworkPacket packet, ForgeDirection from)
    {
        networkComponent.queuePacket(packet,from);
    }

    @Override
    public boolean shouldRenderInPass(int pass)
    {
        return pass == 1;
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
