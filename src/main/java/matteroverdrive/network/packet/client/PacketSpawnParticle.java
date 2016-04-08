package matteroverdrive.network.packet.client;

import io.netty.buffer.ByteBuf;
import matteroverdrive.client.render.RenderParticlesHandler;
import matteroverdrive.fx.AndroidTeleportParticle;
import matteroverdrive.fx.Lightning;
import matteroverdrive.fx.ShockwaveParticle;
import matteroverdrive.network.packet.PacketAbstract;
import matteroverdrive.proxy.ClientProxy;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Simeon on 6/2/2015.
 */
public class PacketSpawnParticle extends PacketAbstract
{
    String particleType;
    double[] coordinates;
    int count;
    RenderParticlesHandler.Blending blending;
    float size;

    public PacketSpawnParticle()
    {

    }

    public PacketSpawnParticle(String particleType, double x, double y, double z, int count, RenderParticlesHandler.Blending blending)
    {
        this(particleType,x,y,z,count,blending,0);
    }

    public PacketSpawnParticle(String particleType, double x, double y, double z, int count, RenderParticlesHandler.Blending blending,float size)
    {
        this(particleType,new double[]{x,y,z},count,blending,size);
    }

    public PacketSpawnParticle(String particleType, double[] coordinates, int count, RenderParticlesHandler.Blending blending, float size)
    {
        this.particleType = particleType;
        this.coordinates = coordinates;
        this.count = count;
        this.blending = blending;
        this.size = size;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        particleType = ByteBufUtils.readUTF8String(buf);
        int coordSize = buf.readUnsignedByte();
        coordinates = new double[coordSize];
        for (int i = 0;i < coordSize;i++)
        {
            coordinates[i] = buf.readDouble();
        }
        count = buf.readInt();
        blending = RenderParticlesHandler.Blending.values()[buf.readUnsignedByte()];
        size = buf.readUnsignedByte();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf,particleType);
        buf.writeByte(coordinates.length);
        for (int i = 0;i < coordinates.length;i++)
        {
            buf.writeDouble(coordinates[i]);
        }
        buf.writeInt(count);
        buf.writeByte(blending.ordinal());
        buf.writeFloat(size);
    }

    public static class ClientHandler extends AbstractClientPacketHandler<PacketSpawnParticle>
    {
        @SideOnly(Side.CLIENT)
        @Override
        public void handleClientMessage(EntityPlayerSP player, PacketSpawnParticle message, MessageContext ctx)
        {
            spawnParticle(player.worldObj,message);
        }

        @SideOnly(Side.CLIENT)
        public void spawnParticle(World world,PacketSpawnParticle message)
        {
            EntityFX particle = null;
            if (message.particleType.equalsIgnoreCase("teleport"))
            {
                particle = new AndroidTeleportParticle(world,message.coordinates[0],message.coordinates[1],message.coordinates[2]);
            }else if (message.particleType.equalsIgnoreCase("shockwave"))
            {
                particle = new ShockwaveParticle(world,message.coordinates[0],message.coordinates[1],message.coordinates[2],message.size);
            }else if (message.particleType.equalsIgnoreCase("lightning"))
            {
                if (message.coordinates.length > 7)
                {
                    particle = new Lightning(world, new Vec3d(message.coordinates[0], message.coordinates[1], message.coordinates[2]), new Vec3d(message.coordinates[3], message.coordinates[4], message.coordinates[5]), (float) message.coordinates[6], (float) message.coordinates[7]);
                }else if (message.coordinates.length > 5)
                {
                    particle = new Lightning(world, new Vec3d(message.coordinates[0], message.coordinates[1], message.coordinates[2]), new Vec3d(message.coordinates[3], message.coordinates[4], message.coordinates[5]));
                }
            }

            if (particle != null) {
                ClientProxy.renderHandler.getRenderParticlesHandler().addEffect(particle, message.blending);
            }
        }
    }
}
