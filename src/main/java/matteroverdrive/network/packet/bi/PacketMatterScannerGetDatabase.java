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

package matteroverdrive.network.packet.bi;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import matteroverdrive.api.matter.IMatterDatabase;
import matteroverdrive.data.BlockPos;
import matteroverdrive.data.ItemPattern;
import matteroverdrive.gui.GuiMatterScanner;
import matteroverdrive.network.packet.AbstractBiPacketHandler;
import matteroverdrive.network.packet.TileEntityUpdatePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

import java.util.List;

/**
 * Created by Simeon on 5/5/2015.
 */
public class PacketMatterScannerGetDatabase extends TileEntityUpdatePacket
{
    List<ItemPattern> list;

    public PacketMatterScannerGetDatabase(){super();}
    public PacketMatterScannerGetDatabase(int x, int y, int z)
    {
        super(x,y,z);
    }
    public PacketMatterScannerGetDatabase(BlockPos position)
    {
        this(position.x,position.y,position.z);
    }
    public PacketMatterScannerGetDatabase(List<ItemPattern> list)
    {
        this.list = list;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        super.fromBytes(buf);
        int size = buf.readInt();
        for (int i = 0;i < size;i++)
        {
            list.add(new ItemPattern(buf));
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        super.toBytes(buf);
        buf.writeInt(list.size());
        for (ItemPattern pattern : list)
        {
            pattern.writeToBuffer(buf);
        }
    }

    public static class Handler extends AbstractBiPacketHandler<PacketMatterScannerGetDatabase>
    {

        public Handler(){}

        @Override
        public IMessage handleServerMessage(EntityPlayer player, PacketMatterScannerGetDatabase message, MessageContext ctx)
        {
            TileEntity tileEntity = message.getTileEntity(player.worldObj);
            if (tileEntity instanceof IMatterDatabase)
            {
                IMatterDatabase database = (IMatterDatabase) tileEntity;
                return new PacketMatterScannerGetDatabase(database.getPatterns());
            }

            return null;
        }

        @Override
        public IMessage handleClientMessage(EntityPlayer player, PacketMatterScannerGetDatabase message, MessageContext ctx)
        {
            if (Minecraft.getMinecraft().currentScreen instanceof GuiMatterScanner)
            {
                GuiMatterScanner guiMatterScanner = (GuiMatterScanner)Minecraft.getMinecraft().currentScreen;
                guiMatterScanner.UpdatePatternList(message.list);
            }

            return null;
        }
    }
}
