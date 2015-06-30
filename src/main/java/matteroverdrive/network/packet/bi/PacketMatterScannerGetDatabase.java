package matteroverdrive.network.packet.bi;

import cofh.lib.util.position.BlockPosition;
import matteroverdrive.api.matter.IMatterDatabase;
import matteroverdrive.gui.GuiMatterScanner;
import matteroverdrive.network.packet.AbstractBiPacketHandler;
import matteroverdrive.network.packet.TileEntityUpdatePacket;
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
public class PacketMatterScannerGetDatabase extends TileEntityUpdatePacket
{
    NBTTagList list;

    public PacketMatterScannerGetDatabase(){super();}
    public PacketMatterScannerGetDatabase(int x, int y, int z)
    {
        super(x,y,z);
    }
    public PacketMatterScannerGetDatabase(BlockPosition position)
    {
        this(position.x,position.y,position.z);
    }
    public PacketMatterScannerGetDatabase(NBTTagList list)
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
                return new PacketMatterScannerGetDatabase(database.getItemsAsNBT());
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
