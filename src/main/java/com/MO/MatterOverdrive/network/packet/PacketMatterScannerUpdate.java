package com.MO.MatterOverdrive.network.packet;

import com.MO.MatterOverdrive.items.MatterScanner;
import com.MO.MatterOverdrive.util.MatterDatabaseHelper;
import com.MO.MatterOverdrive.util.MatterHelper;
import cpw.mods.fml.common.network.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.apache.commons.lang3.CharSet;
import org.lwjgl.Sys;

import java.nio.charset.Charset;

/**
 * Created by Simeon on 3/9/2015.
 */
public class PacketMatterScannerUpdate extends AbstractPacket
{
    private NBTTagCompound selected;
    private short slot;

    public PacketMatterScannerUpdate()
    {

    }

    public PacketMatterScannerUpdate(ItemStack scanner,short slot)
    {
        selected = MatterScanner.getSelectedAsNBT(scanner);
        this.slot = slot;
    }

    @Override
    public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
    {
        ByteBufUtils.writeTag(buffer, selected);
        buffer.writeShort(slot);
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
    {
        this.selected = ByteBufUtils.readTag(buffer);
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
            MatterScanner.setSelected(scanner, selected);
        }
    }
}
