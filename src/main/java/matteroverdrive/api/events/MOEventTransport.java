package matteroverdrive.api.events;

import matteroverdrive.api.transport.TransportLocation;
import net.minecraft.entity.Entity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraftforge.event.entity.EntityEvent;

/**
 * Created by Simeon on 12/30/2015.
 */
public class MOEventTransport extends EntityEvent
{
    public final TransportLocation destination;
    public final ChunkCoordinates source;

    public MOEventTransport(ChunkCoordinates source, TransportLocation destination, Entity event)
    {
        super(event);
        this.source = source;
        this.destination = destination;
    }

    @Override
    public boolean isCancelable()
    {
        return true;
    }
}
