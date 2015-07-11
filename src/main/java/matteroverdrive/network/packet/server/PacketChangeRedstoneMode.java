package matteroverdrive.network.packet.server;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import matteroverdrive.network.packet.TileEntityUpdatePacket;
import matteroverdrive.tile.MOTileEntityMachine;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

/**
 * Created by Simeon on 5/10/2015.
 */
public class PacketChangeRedstoneMode extends TileEntityUpdatePacket
{
    private byte redstoneMode;

    public PacketChangeRedstoneMode(){super();}
    public PacketChangeRedstoneMode(MOTileEntityMachine machine,byte mode)
    {
        super(machine);
        redstoneMode = mode;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        super.fromBytes(buf);
        redstoneMode = buf.readByte();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        super.toBytes(buf);
        buf.writeByte(redstoneMode);
    }

    public static class ServerHandler extends AbstractServerPacketHandler<PacketChangeRedstoneMode>
    {

        @Override
        public IMessage handleServerMessage(EntityPlayer player, PacketChangeRedstoneMode message, MessageContext ctx) {

            TileEntity entity = message.getTileEntity(player.worldObj);
            if (entity instanceof MOTileEntityMachine)
            {
                ((MOTileEntityMachine) entity).setRedstoneMode(message.redstoneMode);
            }
            return null;
        }
    }
}
