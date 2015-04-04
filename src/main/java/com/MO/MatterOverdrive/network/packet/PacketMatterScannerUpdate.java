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
    private short page;
    //private boolean panelOpen;
    private short slot;

    public PacketMatterScannerUpdate()
    {

    }

    public PacketMatterScannerUpdate(ItemStack scanner,short slot)
    {
        selected = MatterScanner.getSelectedAsNBT(scanner);
        if(scanner.hasTagCompound()) {
            this.page = scanner.getTagCompound().getByte(MatterScanner.PAGE_TAG_NAME);
            //this.panelOpen = scanner.getTagCompound().getBoolean(MatterScanner.PANEL_OPEN_TAG_NAME);
        }
        this.slot = slot;
    }

    @Override
    public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
    {
        ByteBufUtils.writeTag(buffer, selected);
        buffer.writeShort(this.page);
        //buffer.writeBoolean(this.panelOpen);
        buffer.writeShort(slot);
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
    {
        this.selected = ByteBufUtils.readTag(buffer);
        this.page = buffer.readShort();
        //this.panelOpen = buffer.readBoolean();
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
            if(scanner.hasTagCompound())
            {
                scanner.getTagCompound().setShort(MatterScanner.PAGE_TAG_NAME, this.page);
                //scanner.getTagCompound().setBoolean(MatterScanner.PANEL_OPEN_TAG_NAME,this.panelOpen);
            }
        }
    }
}
