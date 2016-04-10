/*
 * This file is part of Matter Overdrive
 * Copyright (c) 2015., Simeon Radivoev, All rights reserved.
 *
 * Matter Overdrive is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Matter Overdrive is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Matter Overdrive.  If not, see <http://www.gnu.org/licenses>.
 */

package matteroverdrive.network.packet.bi;

import io.netty.buffer.ByteBuf;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.api.weapon.WeaponShot;
import matteroverdrive.items.weapon.EnergyWeapon;
import matteroverdrive.network.packet.AbstractBiPacketHandler;
import matteroverdrive.network.packet.PacketAbstract;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Simeon on 7/25/2015.
 */
public class PacketFirePlasmaShot extends PacketAbstract
{
	WeaponShot shot;
	private int sender;
	private Vec3d position;
	private Vec3d direction;

	public PacketFirePlasmaShot()
	{
	}

	public PacketFirePlasmaShot(int sender, Vec3d pos, Vec3d dir, WeaponShot shot)
	{
		this.shot = shot;
		this.sender = sender;
		this.position = pos;
		this.direction = dir;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.shot = new WeaponShot(buf);
		this.sender = buf.readInt();
		this.position = new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
		this.direction = new Vec3d(buf.readFloat(), buf.readFloat(), buf.readFloat());
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		shot.writeTo(buf);
		buf.writeInt(sender);
		buf.writeDouble(position.xCoord);
		buf.writeDouble(position.yCoord);
		buf.writeDouble(position.zCoord);
		buf.writeFloat((float)direction.xCoord);
		buf.writeFloat((float)direction.yCoord);
		buf.writeFloat((float)direction.zCoord);
	}

	public WeaponShot getShot()
	{
		return shot;
	}

	public static class BiHandler extends AbstractBiPacketHandler<PacketFirePlasmaShot>
	{
		@SideOnly(Side.CLIENT)
		@Override
		public void handleClientMessage(EntityPlayerSP player, PacketFirePlasmaShot message, MessageContext ctx)
		{
			// TODO: 3/26/2016 Add support of Off hand
			if (player.getEntityId() != message.sender)
			{
				Entity entity = player.worldObj.getEntityByID(message.sender);
				if (entity != null && entity instanceof EntityLivingBase)
				{
					EntityLivingBase livingBase = (EntityLivingBase)entity;
					if (livingBase.getHeldItem(EnumHand.MAIN_HAND) != null && livingBase.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof EnergyWeapon)
					{
						((EnergyWeapon)livingBase.getHeldItem(EnumHand.MAIN_HAND).getItem()).onClientShot(livingBase.getHeldItem(EnumHand.MAIN_HAND), livingBase, message.position, message.direction, message.shot);
					}
				}

			}
		}

		@Override
		public void handleServerMessage(EntityPlayerMP player, PacketFirePlasmaShot message, MessageContext ctx)
		{
			handleServerShot(player, message, 0);
			MatterOverdrive.packetPipeline.sendToAllAround(message, player, message.shot.getRange() + 64);
		}

		public void handleServerShot(EntityPlayer player, PacketFirePlasmaShot shot, int delay)
		{
			ItemStack heldItem = player.getHeldItem(EnumHand.MAIN_HAND);
			if (heldItem != null && heldItem.getItem() instanceof EnergyWeapon && ((EnergyWeapon)heldItem.getItem()).canFire(player.getHeldItem(EnumHand.MAIN_HAND), player.worldObj, player))
			{
				((EnergyWeapon)heldItem.getItem()).onServerFire(heldItem, player, shot.shot, shot.position, shot.direction, delay);
			}
		}
	}
}
