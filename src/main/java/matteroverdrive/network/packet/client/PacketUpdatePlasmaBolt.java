package matteroverdrive.network.packet.client;

import io.netty.buffer.ByteBuf;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.entity.weapon.PlasmaBolt;
import matteroverdrive.handler.weapon.ClientWeaponHandler;
import matteroverdrive.network.packet.PacketAbstract;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Simeon on 1/16/2016.
 */
public class PacketUpdatePlasmaBolt extends PacketAbstract
{
    int boltID;
    double posX;
    float posY;
    double posZ;

    public PacketUpdatePlasmaBolt(){}

    public PacketUpdatePlasmaBolt(int boltID,double posX,double posY,double posZ)
    {
        this.boltID = boltID;
        this.posX = posX;
        this.posY = (float) posY;
        this.posZ = posZ;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        boltID = buf.readInt();
        posX = buf.readDouble();
        posY = buf.readFloat();
        posZ = buf.readDouble();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(boltID);
        buf.writeDouble(posX);
        buf.writeFloat(posY);
        buf.writeDouble(posZ);
    }

    public static class ClientHandler extends AbstractClientPacketHandler<PacketUpdatePlasmaBolt>
    {
        @SideOnly(Side.CLIENT)
        @Override
        public void handleClientMessage(EntityPlayerSP player, PacketUpdatePlasmaBolt message, MessageContext ctx)
        {
            Entity bolt = ((ClientWeaponHandler) MatterOverdrive.proxy.getWeaponHandler()).getPlasmaBolt(message.boltID);
            if (bolt instanceof PlasmaBolt)
            {
                bolt.setPosition(message.posX,message.posY,message.posZ);
            }
        }
    }
}
