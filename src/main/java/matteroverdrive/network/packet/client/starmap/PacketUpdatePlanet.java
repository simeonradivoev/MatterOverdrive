package matteroverdrive.network.packet.client.starmap;

import io.netty.buffer.ByteBuf;
import matteroverdrive.gui.GuiStarMap;
import matteroverdrive.network.packet.PacketAbstract;
import matteroverdrive.network.packet.client.AbstractClientPacketHandler;
import matteroverdrive.starmap.GalaxyClient;
import matteroverdrive.starmap.data.Galaxy;
import matteroverdrive.starmap.data.Planet;
import matteroverdrive.starmap.data.Quadrant;
import matteroverdrive.starmap.data.Star;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
		this(planet, false);
	}

	public PacketUpdatePlanet(Planet planet, boolean updateHomeworlds)
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
		ByteBufUtils.writeTag(buf, planetData);
	}

	public static class ClientHandler extends AbstractClientPacketHandler<PacketUpdatePlanet>
	{
		@SideOnly(Side.CLIENT)
		@Override
		public void handleClientMessage(EntityPlayerSP player, PacketUpdatePlanet message, MessageContext ctx)
		{
			Galaxy galaxy = GalaxyClient.getInstance().getTheGalaxy();
			if (galaxy != null)
			{
				Quadrant quadrant = galaxy.quadrant(message.quadrantID);
				if (quadrant != null)
				{
					Star star = quadrant.star(message.starID);
					if (star != null)
					{
						Planet planet = star.planet(message.planetID);
						if (planet == null)
						{
							planet = new Planet();
							planet.readFromNBT(message.planetData, null);
							star.addPlanet(planet);
						}
						else
						{
							planet.readFromNBT(message.planetData, null);
						}
						notifyChange(planet);
					}
				}

				if (message.updateHomeworlds)
				{
					GalaxyClient.getInstance().loadClaimedPlanets();
				}
			}
		}

		@SideOnly(Side.CLIENT)
		private void notifyChange(Planet planet)
		{
			if (Minecraft.getMinecraft().currentScreen instanceof GuiStarMap)
			{
				GuiStarMap guiStarMap = (GuiStarMap)Minecraft.getMinecraft().currentScreen;
				guiStarMap.onPlanetChange(planet);
			}
		}
	}
}
