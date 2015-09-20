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

package matteroverdrive.network.packet.client;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.api.android.IBionicStat;
import matteroverdrive.entity.AndroidPlayer;
import matteroverdrive.network.packet.PacketAbstract;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by Simeon on 5/26/2015.
 */
public class PacketSyncAndroid extends PacketAbstract
{
    public static final int SYNC_ALL = -1;
    public static final int SYNC_BATTERY = 0;
    public static final int SYNC_EFFECTS = 1;
    public static final int SYNC_STATS = 2;
    public static final int SYNC_ACTIVE_ABILITY = 3;
    public static final int SYNC_INVENTORY = 4;
    NBTTagCompound data;
    int syncPart;
    int playerID;

    public PacketSyncAndroid()
    {
        data = new NBTTagCompound();
    }

    public PacketSyncAndroid(AndroidPlayer player,int syncPart)
    {
        switch (syncPart)
        {
            case SYNC_BATTERY:
                if (player.getStackInSlot(player.ENERGY_SLOT) != null)
                {
                    data = new NBTTagCompound();
                    player.getStackInSlot(player.ENERGY_SLOT).writeToNBT(data);
                }
                break;
            case SYNC_EFFECTS:
                data = player.getEffects();
                break;
            case SYNC_STATS:
                data = player.getUnlocked();
                break;
            case SYNC_ACTIVE_ABILITY:
                data = new NBTTagCompound();
                if (player.getActiveStat() != null)
                {
                    data.setString("Ability",player.getActiveStat().getUnlocalizedName());
                }
                break;
            case SYNC_INVENTORY:
                data = new NBTTagCompound();
                player.getInventory().writeToNBT(data);
                break;
            default:
                data = new NBTTagCompound();
                player.saveNBTData(data);
        }
        this.syncPart = syncPart;
        this.playerID = player.getPlayer().getEntityId();
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        data = ByteBufUtils.readTag(buf);
        syncPart = buf.readInt();
        playerID = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        ByteBufUtils.writeTag(buf,data);
        buf.writeInt(syncPart);
        buf.writeInt(playerID);
    }

    public static class ClientHandler extends AbstractClientPacketHandler<PacketSyncAndroid>
    {
        @Override
        public IMessage handleClientMessage(EntityPlayer player, PacketSyncAndroid message, MessageContext ctx)
        {
            Entity entity = player.worldObj.getEntityByID(message.playerID);

            if (entity instanceof EntityPlayer) {
                EntityPlayer source = (EntityPlayer) entity;
                AndroidPlayer ex = AndroidPlayer.get(source);

                if (ex != null) {
                    switch (message.syncPart) {
                        case SYNC_BATTERY:
                            if (ex.getStackInSlot(ex.ENERGY_SLOT) != null) {
                                ex.getStackInSlot(ex.ENERGY_SLOT).readFromNBT(message.data);
                            }
                            break;
                        case SYNC_EFFECTS:
                            ex.setEffects(message.data);
                            break;
                        case SYNC_STATS:
                            ex.setUnlocked(message.data);
                            break;
                        case SYNC_ACTIVE_ABILITY:
                            if (message.data.hasKey("Ability")) {
                                IBionicStat stat = MatterOverdrive.statRegistry.getStat(message.data.getString("Ability"));
                                ex.setActiveStat(stat);
                            } else {
                                ex.setActiveStat(null);
                            }
                            break;
                        case SYNC_INVENTORY:
                            ex.getInventory().readFromNBT(message.data);
                            break;
                        default:
                            ex.loadNBTData(message.data);
                    }
                }
            }
            return null;
        }
    }
}
