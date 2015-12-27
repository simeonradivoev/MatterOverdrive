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

package matteroverdrive.network.packet.client;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import matteroverdrive.api.matter.IMatterDatabase;
import matteroverdrive.data.BlockPos;
import matteroverdrive.data.ItemPattern;
import matteroverdrive.network.packet.TileEntityUpdatePacket;
import matteroverdrive.tile.TileEntityMachinePatternMonitor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Simeon on 4/26/2015.
 */
public class PacketPatternMonitorSync extends TileEntityUpdatePacket
{
    List<ItemPattern> patterns;

    public PacketPatternMonitorSync(){super();}
    public PacketPatternMonitorSync(TileEntityMachinePatternMonitor monitor)
    {
        super(monitor);
        loadPatternsFromDatabases(monitor.getWorldObj(),monitor.getDatabases());
    }

    public void loadPatternsFromDatabases(World world,HashSet<BlockPos> databases)
    {
        patterns = new ArrayList<>();
        for (BlockPos pos : databases)
        {
            TileEntity tileEntity = pos.getTileEntity(world);
            if (tileEntity instanceof IMatterDatabase)
            {
                List<ItemPattern> itemStacks = ((IMatterDatabase) tileEntity).getPatterns();
                patterns.addAll(itemStacks);
            }
        }
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        super.fromBytes(buf);
        int size = buf.readInt();
        patterns = new ArrayList<>();
        for (int i = 0; i < size;i++)
        {
            patterns.add(new ItemPattern(buf));
        }
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        super.toBytes(buf);
        buf.writeInt(patterns.size());
        for (ItemPattern pattern : patterns)
        {
            pattern.writeToBuffer(buf);
        }
    }

    public static class ClientHandler extends AbstractClientPacketHandler<PacketPatternMonitorSync>
    {
        @Override
        public IMessage handleClientMessage(EntityPlayer player, PacketPatternMonitorSync message, MessageContext ctx)
        {
            TileEntity entity = message.getTileEntity(player.worldObj);

            if (entity != null && entity instanceof TileEntityMachinePatternMonitor) {

                TileEntityMachinePatternMonitor monitor = (TileEntityMachinePatternMonitor)entity;
                monitor.setGuiPatterns(message.patterns);
                monitor.forceSearch(true);
            }
            return null;
        }
    }
}
