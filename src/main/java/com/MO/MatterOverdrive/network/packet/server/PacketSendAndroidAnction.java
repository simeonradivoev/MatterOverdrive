package com.MO.MatterOverdrive.network.packet.server;

import com.MO.MatterOverdrive.entity.AndroidPlayer;
import com.MO.MatterOverdrive.handler.AndroidStatRegistry;
import com.MO.MatterOverdrive.network.packet.PacketAbstract;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Created by Simeon on 6/9/2015.
 */
public class PacketSendAndroidAnction extends PacketAbstract
{
    public static final int ACTION_SHIELD = 0;
    int action = 0;
    boolean state;
    int options;

    public PacketSendAndroidAnction()
    {

    }

    public PacketSendAndroidAnction(int action,boolean state)
    {
        this(action,state,0);
    }

    public PacketSendAndroidAnction(int action,boolean state,int options)
    {
        this.action = action;
        this.state = state;
        this.options = options;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        action = buf.readInt();
        state = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(action);
        buf.writeBoolean(state);
    }

    public static class ServerHandler extends AbstractServerPacketHandler<PacketSendAndroidAnction>
    {

        @Override
        public IMessage handleServerMessage(EntityPlayer player, PacketSendAndroidAnction message, MessageContext ctx)
        {
            AndroidPlayer androidPlayer = AndroidPlayer.get(player);
            if (androidPlayer != null)
            {
                if (message.action == ACTION_SHIELD)
                {
                    AndroidStatRegistry.shield.setShield(androidPlayer,message.state);
                }
            }
            return null;
        }
    }
}
