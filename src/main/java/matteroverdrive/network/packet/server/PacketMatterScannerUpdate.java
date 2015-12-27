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

package matteroverdrive.network.packet.server;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import matteroverdrive.data.ItemPattern;
import matteroverdrive.items.MatterScanner;
import matteroverdrive.network.packet.PacketAbstract;
import matteroverdrive.util.MatterHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * Created by Simeon on 3/9/2015.
 */
public class PacketMatterScannerUpdate extends PacketAbstract
{
    private ItemPattern selected;
    private short page;
    //private boolean panelOpen;
    private short slot;

    public PacketMatterScannerUpdate(){}

    public PacketMatterScannerUpdate(ItemStack scanner,short slot)
    {
        selected = MatterScanner.getSelectedAsPattern(scanner);
        if(scanner.hasTagCompound()) {
            this.page = scanner.getTagCompound().getByte(MatterScanner.PAGE_TAG_NAME);
            //this.panelOpen = scanner.getTagCompound().getBoolean(MatterScanner.PANEL_OPEN_TAG_NAME);
        }
        this.slot = slot;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.selected = new ItemPattern(buf);
        this.page = buf.readShort();
        //this.panelOpen = buffer.readBoolean();
        this.slot = buf.readShort();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        selected.writeToBuffer(buf);
        buf.writeShort(this.page);
        //buffer.writeBoolean(this.panelOpen);
        buf.writeShort(slot);
    }

    public static class ServerHandler extends AbstractServerPacketHandler<PacketMatterScannerUpdate>
    {
        public ServerHandler(){}

        @Override
        public IMessage handleServerMessage(EntityPlayer player, PacketMatterScannerUpdate message, MessageContext ctx)
        {
            if (message.slot < player.inventory.getSizeInventory()) {
                ItemStack scanner = player.inventory.getStackInSlot(message.slot);
                if (MatterHelper.isMatterScanner(scanner)) {
                    MatterScanner.setSelected(scanner, message.selected);
                    if (scanner.hasTagCompound()) {
                        scanner.getTagCompound().setShort(MatterScanner.PAGE_TAG_NAME, message.page);
                        //scanner.getTagCompound().setBoolean(MatterScanner.PANEL_OPEN_TAG_NAME,this.panelOpen);
                    }
                }
            }
            return null;
        }
    }
}
