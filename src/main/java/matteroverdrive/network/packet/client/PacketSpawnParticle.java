package matteroverdrive.network.packet.client;

import matteroverdrive.client.render.RenderParticlesHandler;
import matteroverdrive.fx.AndroidTeleportParticle;
import matteroverdrive.network.packet.PacketAbstract;
import matteroverdrive.proxy.ClientProxy;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/**
 * Created by Simeon on 6/2/2015.
 */
public class PacketSpawnParticle extends PacketAbstract
{
    String particleType;
    double x,y,z;
    int count;
    int blending;

    public PacketSpawnParticle()
    {

    }

    public PacketSpawnParticle(String particleType, double x, double y, double z, int count, int blending)
    {
        this.particleType = particleType;
        this.x = x;
        this.y = y;
        this.z = z;
        this.count = count;
        this.blending = blending;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        particleType = ByteBufUtils.readUTF8String(buf);
        x = buf.readDouble();
        y = buf.readDouble();
        z = buf.readDouble();
        count = buf.readInt();
        blending = buf.readByte();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf,particleType);
        buf.writeDouble(x);
        buf.writeDouble(y);
        buf.writeDouble(z);
        buf.writeInt(count);
        buf.writeByte(blending);
    }

    public static class ClientHandler extends AbstractClientPacketHandler<PacketSpawnParticle>
    {
        @Override
        public IMessage handleClientMessage(EntityPlayer player, PacketSpawnParticle message, MessageContext ctx)
        {
            spawnParticle(player.worldObj,message);
            return null;
        }

        @SideOnly(Side.CLIENT)
        public void spawnParticle(World world,PacketSpawnParticle message)
        {
            EntityFX particle = null;
            if (message.particleType.equalsIgnoreCase("teleport"))
            {
                particle = new AndroidTeleportParticle(world,message.x,message.y,message.z);
            }

            if (particle != null) {
                ClientProxy.renderHandler.getRenderParticlesHandler().addEffect(particle, RenderParticlesHandler.Blending.values()[message.blending]);
            }
        }
    }
}
