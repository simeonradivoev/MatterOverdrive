package matteroverdrive.network.packet.bi;

import io.netty.buffer.ByteBuf;
import matteroverdrive.network.packet.TileEntityUpdatePacket;

/**
 * Created by Simeon on 6/12/2015.
 */
public class PacketMatterScannerGetPattern extends TileEntityUpdatePacket
{
    int id;
    short damage;
    short scannerSlot;
    short type;

    public PacketMatterScannerGetPattern(){super();}
    public PacketMatterScannerGetPattern(int x, int y, int z)
    {
        super(x,y,z);
    }
    public PacketMatterScannerGetPattern(int x,int y,int z,int id,short damage,short scannerSlot,short type)
    {
        this(x,y,z);
        this.id = id;
        this.damage = damage;
        this.scannerSlot = scannerSlot;
        this.type = type;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        super.fromBytes(buf);
        id = buf.readInt();
        damage = buf.readShort();
        scannerSlot = buf.readShort();
        type = buf.readShort();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        super.toBytes(buf);
        buf.writeInt(id);
        buf.writeShort(damage);
        buf.writeShort(scannerSlot);
        buf.writeShort(type);
    }
}
