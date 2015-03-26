package com.MO.MatterOverdrive.network.packet;

import com.MO.MatterOverdrive.tile.pipes.TileEntityPipe;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

/**
 * Created by Simeon on 3/6/2015.
 */
public class PacketPipeUpdate extends TileEntityUpdatePacket
{

    public PacketPipeUpdate()
    {

    }

    public PacketPipeUpdate(int x, int y, int z)
    {
        super(x, y, z);
    }

    @Override
    public void handleClientSide(EntityPlayer player)
    {
        super.handleClientSide(player);
        TileEntity e = player.worldObj.getTileEntity(this.x,this.y,this.z);
        if(e instanceof TileEntityPipe)
        {
            ((TileEntityPipe)player.worldObj.getTileEntity(this.x,this.y,this.z)).queueUpdate();
        }
    }

    @Override
    public void handleServerSide(EntityPlayer player)
    {
        super.handleServerSide(player);
    }
}
