package com.MO.MatterOverdrive.tile.pipes;

import cofh.lib.util.TimeTracker;
import com.MO.MatterOverdrive.api.matter.IMatterConnection;
import com.MO.MatterOverdrive.api.matter.IMatterHandler;
import com.MO.MatterOverdrive.data.MatterStorage;
import com.MO.MatterOverdrive.util.MatterHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Simeon on 3/7/2015.
 */
public class TileEntityMatterPipe extends  TileEntityPipe<IMatterConnection> implements IMatterConnection, IMatterHandler
{
    public  static  final int MATTER_EXTRACT = 1;
    public  static  final int MATTER_INPUT = 1;
    public  static  final int TRANSFER_SPEED = 20;
    public  ForgeDirection lastDir = ForgeDirection.WEST;
    private MatterStorage storage;
    public  static Random rand = new Random();
    TimeTracker t;

    @SideOnly(Side.CLIENT)
    private boolean matterVisible;

    public TileEntityMatterPipe()
    {
        super(IMatterConnection.class);
        t = new TimeTracker();
        storage = new MatterStorage(1,MATTER_EXTRACT,MATTER_INPUT);
    }

    @Override
    public  void updateEntity()
    {
        super.updateEntity();
        if(!worldObj.isRemote)
        {
            if (t.hasDelayPassed(worldObj, TRANSFER_SPEED))
            {
                Transfer();
            }
        }
    }

    public  void  Transfer()
    {
        if (getMatterStored() > 0)
        {
            List<WeightedDirection> validSides = getWeightedValidSides(lastDir);

            for (WeightedDirection dir : validSides)
            {
                if(getMatterStored() <= 0)
                    return;

                TileEntity e = worldObj.getTileEntity(xCoord + dir.dir.offsetX, yCoord + dir.dir.offsetY, zCoord + dir.dir.offsetZ);

                if (e != null && e instanceof IMatterHandler)
                {
                    IMatterHandler to = (IMatterHandler)e;
                    int transferAmount = MatterHelper.Transfer(dir.dir,MATTER_EXTRACT,this,to);
                }
            }
        }
    }

    private List<WeightedDirection> getWeightedValidSides(ForgeDirection transferDir)
    {
        List<WeightedDirection> validSides = new ArrayList<WeightedDirection>(connections.length);
        ForgeDirection transferDirOp = MatterHelper.opposite(transferDir);

        for (int i = 0; i < connections.length; i++) {
            if (connections[i] != null) {
                if (connections[i] == transferDir) {
                    validSides.add(new WeightedDirection(connections[i], 2.0f));
                } else if (connections[i] == transferDirOp) {
                    validSides.add(new WeightedDirection(connections[i], 0.0f));
                } else {
                    validSides.add(new WeightedDirection(connections[i], 0.5f + rand.nextFloat()));
                }
            }
        }

        if(validSides.size() > 1)
            WeightedDirection.Sort(validSides);

        return validSides;
    }

    @Override
    public void  writeToNBT(NBTTagCompound comp)
    {
        super.writeToNBT(comp);
        if(!worldObj.isRemote)
        {
            storage.writeToNBT(comp);
            comp.setByte("transfer_dir", (byte) this.lastDir.ordinal());
        }
    }

    @Override
    public  void  readFromNBT(NBTTagCompound comp)
    {
        super.readFromNBT(comp);
        storage.readFromNBT(comp);
        if (comp.hasKey("transfer_dir"))
        {
            lastDir = ForgeDirection.values()[comp.getByte("transfer_dir")];
        }
    }

    @Override
    public boolean canConnectFrom(ForgeDirection dir)
    {
        return true;
    }

    @SideOnly(Side.CLIENT)
    public  boolean matterVisible()
    {
        return this.matterVisible;
    }

    @SideOnly(Side.CLIENT)
    public  boolean setMatterVisible(boolean matterVisible)
    {
        return this.matterVisible = matterVisible;
    }

    @Override
    public int getMatterStored()
    {
        return storage.getMatterStored();
    }

    @Override
    public int getMatterCapacity()
    {
        return storage.getCapacity();
    }

    @Override
    public int receiveMatter(ForgeDirection side, int amount, boolean simulate)
    {
        int received = storage.receiveMatter(side,amount,simulate);
        if(!simulate)
        {
            if(received > 0)
            {
                //MatterOverdrive.packetPipeline.sendToAll(new PacketMatterPipeUpdate(xCoord,yCoord,zCoord,getMatterStored() > 0));
                worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                lastDir = side;
                t.markTime(worldObj);
            }

        }
        return received;
    }

    @Override
    public int extractMatter(ForgeDirection direction, int amount, boolean simulate)
    {
        int matterExtracted =  storage.extractMatter(direction,amount,simulate);
        if(!simulate)
        {
            //MatterOverdrive.packetPipeline.sendToAll(new PacketMatterPipeUpdate(xCoord, yCoord, zCoord, getMatterStored() > 0));
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        }
        return matterExtracted;
    }

    @Override
    public Packet getDescriptionPacket()
    {
        NBTTagCompound connections = new NBTTagCompound();
        connections.setBoolean("matterVisible",getMatterStored() > 0);
        return new S35PacketUpdateTileEntity(xCoord,yCoord,zCoord,1,connections);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
    {
        if(worldObj.isRemote) {
            this.setMatterVisible(pkt.func_148857_g().getBoolean("matterVisible"));
        }
    }
}
