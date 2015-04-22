package com.MO.MatterOverdrive.network.packet.client;

import com.MO.MatterOverdrive.network.packet.TileEntityUpdatePacket;
import com.MO.MatterOverdrive.tile.MOTileEntityMachineEnergy;
import com.MO.MatterOverdrive.tile.MOTileEntityMachineMatter;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

/**
 * Created by Simeon on 4/22/2015.
 */
public class PacketMatterUpdate extends TileEntityUpdatePacket
{
    private int matter = 0;

    public PacketMatterUpdate(){}

    public PacketMatterUpdate(MOTileEntityMachineMatter entityMachineEnergy)
    {
        super(entityMachineEnergy.xCoord,entityMachineEnergy.yCoord,entityMachineEnergy.zCoord);
        matter = entityMachineEnergy.getMatterStored();
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        super.fromBytes(buf);
        matter = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        super.toBytes(buf);
        buf.writeInt(matter);
    }

    public static class ClientHandler extends AbstractClientPacketHandler<PacketMatterUpdate>
    {
        public ClientHandler(){}

        @Override
        public IMessage handleClientMessage(EntityPlayer player, PacketMatterUpdate message, MessageContext ctx)
        {
            if (player != null && player.worldObj != null) {
                TileEntity tileEntity = player.worldObj.getTileEntity(message.x, message.y, message.z);

                if (tileEntity != null && tileEntity instanceof MOTileEntityMachineMatter) {
                    ((MOTileEntityMachineMatter) tileEntity).setMatterStored(message.matter);
                }
            }
            return null;
        }
    }
}
