package com.MO.MatterOverdrive.matter_network.packets;

import com.MO.MatterOverdrive.api.network.IMatterNetworkConnection;
import com.MO.MatterOverdrive.matter_network.MatterNetworkPacket;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by Simeon on 4/30/2015.
 */
public class MatterNetwrokResponcePacket extends MatterNetworkPacket
{
    int responceType;
    int requestType;
    NBTTagCompound responce;

    public MatterNetwrokResponcePacket(){super();}
    public MatterNetwrokResponcePacket(IMatterNetworkConnection sender,int responceType,int requestType,NBTTagCompound responce,ForgeDirection port)
    {
        super(sender.getPosition(),port);
        this.responceType = responceType;
        this.requestType = requestType;
        this.responce = responce;
    }

    @Override
    public boolean isValid(World world)
    {
        return true;
    }

    @Override
    public String getName()
    {
        return "Responce Packet";
    }

    public int getResponceType() {
        return responceType;
    }

    public int getRequestType(){return requestType;}

    public NBTTagCompound getResponce() {
        return responce;
    }
}
