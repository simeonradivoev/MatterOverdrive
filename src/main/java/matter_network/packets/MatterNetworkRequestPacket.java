package matter_network.packets;

import com.MO.MatterOverdrive.api.network.IMatterNetworkConnection;
import matter_network.MatterNetworkPacket;
import net.minecraft.world.World;

/**
 * Created by Simeon on 4/27/2015.
 */
public class MatterNetworkRequestPacket extends MatterNetworkPacket
{
    int requestType;
    Object request;

    public MatterNetworkRequestPacket(){super();}
    public MatterNetworkRequestPacket(IMatterNetworkConnection sender,int requestType,Object request)
    {
        super(sender.getPosition());
        this.requestType = requestType;
        this.request = request;
    }

    @Override
    public boolean isValid(World world)
    {
        return true;
    }

    @Override
    public String getName()
    {
        return "Request Packet";
    }

    public int getRequestType() {
        return requestType;
    }

    public Object getRequest() {
        return request;
    }
}
