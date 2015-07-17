package matteroverdrive.matter_network.packets;

import matteroverdrive.api.network.IMatterNetworkConnection;
import matteroverdrive.matter_network.MatterNetworkPacket;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by Simeon on 4/30/2015.
 */
public class MatterNetworkResponsePacket extends MatterNetworkPacket
{
    int responseType;
    int requestType;
    NBTTagCompound response;

    public MatterNetworkResponsePacket(){super();}
    public MatterNetworkResponsePacket(IMatterNetworkConnection sender, int responseType, int requestType, NBTTagCompound response, ForgeDirection port)
    {
        super(sender.getPosition(),port);
        this.responseType = responseType;
        this.requestType = requestType;
        this.response = response;
    }

    @Override
    public boolean isValid(World world)
    {
        return true;
    }

    @Override
    public String getName()
    {
        return "Response Packet";
    }

    public int getResponseType() {
        return responseType;
    }

    public boolean fits(int responseType,int requestType)
    {
        return getResponseType() == responseType && getRequestType() == requestType;
    }

    public int getRequestType(){return requestType;}

    public NBTTagCompound getResponse() {
        return response;
    }
}
