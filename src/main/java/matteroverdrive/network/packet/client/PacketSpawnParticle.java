package matteroverdrive.network.packet.client;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import matteroverdrive.client.render.RenderParticlesHandler;
import matteroverdrive.fx.AndroidTeleportParticle;
import matteroverdrive.fx.ShockwaveParticle;
import matteroverdrive.network.packet.PacketAbstract;
import matteroverdrive.proxy.ClientProxy;
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
    float size;

    public PacketSpawnParticle()
    {

    }

    public PacketSpawnParticle(String particleType, double x, double y, double z, int count, int blending)
    {
        this(particleType,x,y,z,count,blending,0);
    }

    public PacketSpawnParticle(String particleType, double x, double y, double z, int count, int blending,float size)
    {
        this.particleType = particleType;
        this.x = x;
        this.y = y;
        this.z = z;
        this.count = count;
        this.blending = blending;
        this.size = size;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        particleType = ByteBufUtils.readUTF8String(buf);
        x = buf.readDouble();
        y = buf.readDouble();
        z = buf.readDouble();
        count = buf.readInt();
        blending = buf.readByte();
        size = buf.readableBytes();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf,particleType);
        buf.writeDouble(x);
        buf.writeDouble(y);
        buf.writeDouble(z);
        buf.writeInt(count);
        buf.writeByte(blending);
        buf.writeFloat(size);
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
            }else if (message.particleType.equalsIgnoreCase("shockwave"))
            {
                particle = new ShockwaveParticle(world,message.x,message.y,message.z,message.size);
            }

            if (particle != null) {
                ClientProxy.renderHandler.getRenderParticlesHandler().addEffect(particle, RenderParticlesHandler.Blending.values()[message.blending]);
            }
        }
    }
}
