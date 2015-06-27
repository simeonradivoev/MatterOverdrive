package com.MO.MatterOverdrive.network.packet.client;

import com.MO.MatterOverdrive.MatterOverdrive;
import com.MO.MatterOverdrive.network.packet.PacketAbstract;
import com.MO.MatterOverdrive.starmap.GalaxyClient;
import com.MO.MatterOverdrive.starmap.data.Galaxy;
import com.MO.MatterOverdrive.starmap.data.Planet;
import com.MO.MatterOverdrive.starmap.data.Quadrant;
import com.MO.MatterOverdrive.starmap.data.Star;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by Simeon on 6/15/2015.
 */
public class PacketUpdatePlanet extends PacketAbstract
{
    int planetID;
    int starID;
    int quadrantID;
    boolean updateHomeworlds;
    NBTTagCompound planetData;

    public PacketUpdatePlanet()
    {

    }

    public PacketUpdatePlanet(Planet planet)
    {
        this(planet,false);
    }

    public PacketUpdatePlanet(Planet planet,boolean updateHomeworlds)
    {
        planetID = planet.getId();
        starID = planet.getStar().getId();
        quadrantID = planet.getStar().getQuadrant().getId();
        planetData = new NBTTagCompound();
        this.updateHomeworlds = updateHomeworlds;
        planet.writeToNBT(planetData);
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        planetID = buf.readInt();
        starID = buf.readInt();
        quadrantID = buf.readInt();
        updateHomeworlds = buf.readBoolean();
        planetData = ByteBufUtils.readTag(buf);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(planetID);
        buf.writeInt(starID);
        buf.writeInt(quadrantID);
        buf.writeBoolean(updateHomeworlds);
        ByteBufUtils.writeTag(buf,planetData);
    }

    public static class ClientHandler extends AbstractClientPacketHandler<PacketUpdatePlanet>
    {

        @Override
        public IMessage handleClientMessage(EntityPlayer player, PacketUpdatePlanet message, MessageContext ctx)
        {
            Galaxy galaxy = GalaxyClient.getInstance().getTheGalaxy();
            if (galaxy != null)
            {
                Quadrant quadrant = galaxy.quadrant(message.quadrantID);
                if (quadrant != null) {
                    Star star = quadrant.star(message.starID);
                    if (star != null) {
                        Planet planet = star.planet(message.planetID);
                        if (planet == null) {
                            planet = new Planet();
                            planet.readFromNBT(message.planetData,null);
                            star.addPlanet(planet);
                        } else {
                            planet.readFromNBT(message.planetData,null);
                        }
                    }
                }

                if (message.updateHomeworlds)
                {
                    GalaxyClient.getInstance().loadClaimedPlanets();
                }
            }
            return null;
        }
    }
}
