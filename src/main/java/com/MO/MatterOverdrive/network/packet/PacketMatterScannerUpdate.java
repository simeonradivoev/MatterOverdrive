package com.MO.MatterOverdrive.network.packet;

import com.MO.MatterOverdrive.items.MatterScanner;
import com.MO.MatterOverdrive.util.MatterDatabaseHelper;
import com.MO.MatterOverdrive.util.MatterHelper;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.CharSet;
import org.lwjgl.Sys;

import java.nio.charset.Charset;

/**
 * Created by Simeon on 3/9/2015.
 */
public class PacketMatterScannerUpdate extends AbstractPacket
{
    private String selectedIndex;
    private short slot;

    public PacketMatterScannerUpdate()
    {

    }

    public PacketMatterScannerUpdate(ItemStack scanner,short slot)
    {
        selectedIndex = MatterScanner.getSelectedIndex(scanner);
        this.slot = slot;
    }

    @Override
    public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
    {
        byte[] bytes = selectedIndex.getBytes(Charset.forName("UTF-8"));
        buffer.writeInt(bytes.length);
        buffer.writeBytes(bytes);
        buffer.writeShort(slot);
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
    {
        int bytesLength = buffer.readInt();
        this.selectedIndex = buffer.readBytes(bytesLength).toString(Charset.forName("UTF-8"));
        this.slot = buffer.readShort();
    }

    @Override
    public void handleClientSide(EntityPlayer player) {

    }

    @Override
    public void handleServerSide(EntityPlayer player)
    {
        ItemStack scanner = player.inventory.getStackInSlot(slot);
        if(MatterHelper.isMatterScanner(scanner))
        {
            MatterScanner.setSelectedIndex(scanner, selectedIndex);
        }
    }
}
