package com.MO.MatterOverdrive.network.packet.client;

import com.MO.MatterOverdrive.network.packet.TileEntityUpdatePacket;
import com.MO.MatterOverdrive.tile.MOTileEntityMachineEnergy;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

/**
 * Created by Simeon on 4/22/2015.
 */
public class PacketPowerUpdate extends TileEntityUpdatePacket
{
    int energy;

    public PacketPowerUpdate(){}

    public PacketPowerUpdate(MOTileEntityMachineEnergy entityMachineEnergy)
    {
        super(entityMachineEnergy.xCoord,entityMachineEnergy.yCoord,entityMachineEnergy.zCoord);
        energy = entityMachineEnergy.getEnergyStorage().getEnergyStored();
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        super.fromBytes(buf);
        energy = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        super.toBytes(buf);
        buf.writeInt(energy);
    }

    public static class ClientHandler extends AbstractClientPacketHandler<PacketPowerUpdate>
    {

        public ClientHandler(){}

        @Override
        public IMessage handleClientMessage(EntityPlayer player, PacketPowerUpdate message, MessageContext ctx)
        {
            TileEntity tileEntity = player.worldObj.getTileEntity(message.x,message.y,message.z);
            if (tileEntity instanceof MOTileEntityMachineEnergy)
            {
                ((MOTileEntityMachineEnergy) tileEntity).setEnergyStored(message.energy);
            }
            return null;
        }
    }
}
