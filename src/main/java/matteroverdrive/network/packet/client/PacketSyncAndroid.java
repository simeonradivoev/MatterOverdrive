package matteroverdrive.network.packet.client;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
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
    NBTTagCompound data;
    int syncPart;
    int playerID;
    boolean others;

    public PacketSyncAndroid()
    {
        data = new NBTTagCompound();
    }

    public PacketSyncAndroid(AndroidPlayer player,int syncPart,boolean others)
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
            default:
                data = new NBTTagCompound();
                player.saveNBTData(data);
        }
        this.syncPart = syncPart;
        this.playerID = player.getPlayer().getEntityId();
        this.others = others;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        data = ByteBufUtils.readTag(buf);
        syncPart = buf.readInt();
        playerID = buf.readInt();
        others = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        ByteBufUtils.writeTag(buf,data);
        buf.writeInt(syncPart);
        buf.writeInt(playerID);
        buf.writeBoolean(others);
    }

    public static class ClientHandler extends AbstractClientPacketHandler<PacketSyncAndroid>
    {
        @Override
        public IMessage handleClientMessage(EntityPlayer player, PacketSyncAndroid message, MessageContext ctx)
        {
            Entity entity = player.worldObj.getEntityByID(message.playerID);
            if (!message.others)
            {
                entity = player;
            }

            if (entity instanceof EntityPlayer) {
                EntityPlayer source = (EntityPlayer)entity;
                if (source != null) {
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
                            default:
                                ex.loadNBTData(message.data);
                        }
                    }
                }
            }
            return null;
        }
    }
}
