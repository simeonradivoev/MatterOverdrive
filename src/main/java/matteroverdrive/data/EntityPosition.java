package matteroverdrive.data;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;

/**
 * Created by Simeon on 5/4/2015.
 */
public class EntityPosition
{
    int entityID;
    public double x,y,z;

    public EntityPosition(int entityID, double x, double y, double z)
    {
        this.entityID = entityID;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public EntityPosition(Entity entity)
    {
        entityID = entity.getEntityId();
        x = entity.posX;
        y = entity.posY;
        z = entity.posZ;
    }

    public EntityPosition(ByteBuf buf)
    {
        entityID = buf.readInt();
        x = buf.readDouble();
        y = buf.readDouble();
        z = buf.readDouble();
    }

    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(entityID);
        buf.writeDouble(x);
        buf.writeDouble(y);
        buf.writeDouble(z);
    }

    public int getEntityID()
    {
        return entityID;
    }
}
