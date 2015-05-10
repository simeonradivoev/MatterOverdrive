package com.MO.MatterOverdrive.network.packet.bi;

import cofh.lib.util.position.BlockPosition;
import com.MO.MatterOverdrive.api.matter.IMatterDatabase;
import com.MO.MatterOverdrive.gui.GuiMatterScanner;
import com.MO.MatterOverdrive.handler.GuiHandler;
import com.MO.MatterOverdrive.items.MatterScanner;
import com.MO.MatterOverdrive.network.packet.AbstractBiPacketHandler;
import com.MO.MatterOverdrive.network.packet.PacketAbstract;
import com.MO.MatterOverdrive.network.packet.TileEntityUpdatePacket;
import com.MO.MatterOverdrive.network.packet.client.AbstractClientPacketHandler;
import com.MO.MatterOverdrive.network.packet.server.AbstractServerPacketHandler;
import com.MO.MatterOverdrive.util.MatterDatabaseHelper;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;

/**
 * Created by Simeon on 5/5/2015.
 */
public class PacketGetDatabase extends TileEntityUpdatePacket
{
    NBTTagList list;

    public PacketGetDatabase(){super();}
    public PacketGetDatabase(int x,int y,int z)
    {
        super(x,y,z);
    }
    public PacketGetDatabase(BlockPosition position)
    {
        this(position.x,position.y,position.z);
    }
    public PacketGetDatabase(NBTTagList list)
    {
        this.list = list;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        super.fromBytes(buf);
        NBTTagCompound tagCompound = ByteBufUtils.readTag(buf);
        list = tagCompound.getTagList("List",10);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        super.toBytes(buf);
        NBTTagCompound tagCompound = new NBTTagCompound();
        if (list != null)
            tagCompound.setTag("List",list);

        ByteBufUtils.writeTag(buf, tagCompound);
    }

    public static class Handler extends AbstractBiPacketHandler<PacketGetDatabase>
    {

        public Handler(){}

        @Override
        public IMessage handleServerMessage(EntityPlayer player, PacketGetDatabase message, MessageContext ctx)
        {
            TileEntity tileEntity = message.getTileEntity(player.worldObj);
            if (tileEntity instanceof IMatterDatabase)
            {
                IMatterDatabase database = (IMatterDatabase) tileEntity;
                return new PacketGetDatabase(database.getItemsAsNBT());
            }

            return null;
        }

        @Override
        public IMessage handleClientMessage(EntityPlayer player, PacketGetDatabase message, MessageContext ctx)
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
