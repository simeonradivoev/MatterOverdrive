package com.MO.MatterOverdrive.network.packet;

import com.MO.MatterOverdrive.util.MatterDatabaseHelper;
import com.MO.MatterOverdrive.util.MatterHelper;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.lwjgl.Sys;

/**
 * Created by Simeon on 3/9/2015.
 */
public class PacketMatterScannerUpdate extends AbstractPacket
{
    private int selectedIndex;
    private short slot;

    public PacketMatterScannerUpdate()
    {

    }

    public PacketMatterScannerUpdate(ItemStack scanner,short slot)
    {
        selectedIndex = MatterDatabaseHelper.GetSelectedIndex(scanner);
        this.slot = slot;
    }

    @Override
    public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
    {
        buffer.writeInt(selectedIndex);
        buffer.writeShort(slot);
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
    {
       this.selectedIndex = buffer.readInt();
        this.slot = buffer.readShort();
    }

    @Override
    public void handleClientSide(EntityPlayer player) {

    }

    @Override
    public void handleServerSide(EntityPlayer player)
    {
        ItemStack scanner = player.inventory.getStackInSlot(slot);
        if(MatterHelper.isDatabaseItem(scanner))
        {
            MatterDatabaseHelper.SetSelectedIndex(scanner,selectedIndex);
        }
    }
}
