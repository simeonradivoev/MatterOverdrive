package com.MO.MatterOverdrive.tile;

import cofh.lib.util.TimeTracker;
import cofh.lib.util.position.BlockPosition;
import com.MO.MatterOverdrive.api.matter.IMatterNetworkConnection;
import com.MO.MatterOverdrive.data.MatterNetwork;
import com.MO.MatterOverdrive.util.MatterNetworkHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Map;

/**
 * Created by Simeon on 3/11/2015.
 */
public class TileEntityMachineNetworkController extends MOTileEntityMachine implements IMatterNetworkConnection
{
    public static final int REBUILD_UPDATE_DELAY = 50;
    private boolean needsRebuild = true;
    private TimeTracker timer;
    @SideOnly(Side.CLIENT)
    public Map<Integer,Integer> connectionsInfoMap;

    public TileEntityMachineNetworkController()
    {
        network = new MatterNetwork(this);
        timer = new TimeTracker();
    }

    @Override
    public boolean canConnectToNetwork(ForgeDirection direction)
    {
        return true;
    }

    @Override
    public void readCustomNBT(NBTTagCompound nbt)
    {
        super.readCustomNBT(nbt);
        connectionsInfoMap = MatterNetwork.infoMapFromNBT(nbt);
    }

    @Override
    public void  writeCustomNBT(NBTTagCompound nbt)
    {
        super.writeCustomNBT(nbt);
        network.writeInfoMapToNBT(nbt);
    }

    @Override
    public void updateEntity()
    {
        if(!worldObj.isRemote)
        {
            if (timer.hasDelayPassed(this.worldObj, REBUILD_UPDATE_DELAY))
            {
                if(needsRebuild)
                {
                    rebuild();
                    needsRebuild = false;
                    forceClientUpdate = true;
                }
            }
        }

        super.updateEntity();
    }

    @Override
    public String getSound() {
        return null;
    }

    @Override
    public boolean hasSound() {
        return false;
    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public float soundVolume() { return 0;}

    @Override
    public void onAdded()
    {

    }

    @Override
    public void onDestroyed()
    {
        network.Dissasemble();
    }

    public void sceduleRebuild()
    {
        needsRebuild = true;
    }

    protected void rebuild()
    {
        if(!worldObj.isRemote)
        {
            System.out.println("Rebuilding Network");
            network.Dissasemble();
            buildConnections(this);
        }
    }

    public void buildConnections(IMatterNetworkConnection entity)
    {
        for (int i = 0; i < 6; i++)
            {
                ForgeDirection dir = ForgeDirection.values()[i];
                BlockPosition pos = entity.getPosition();
                pos.x += dir.offsetX;
                pos.y += dir.offsetY;
                pos.z += dir.offsetZ;
                TileEntity ec = worldObj.getTileEntity(pos.x, pos.y, pos.z);

                if (ec instanceof IMatterNetworkConnection)
                {
                    IMatterNetworkConnection connection = (IMatterNetworkConnection) ec;
                    System.out.println(connection.getNetwork());

                    if (MatterNetworkHelper.canEstablishConnection(entity,connection,dir) && connection.getNetwork() == null)
                    {
                        System.out.println("Found connection " + connection.getClass().getName() + " at: [" + pos.x + "," + pos.y + "," + pos.z + "]");
                        this.network.AddConnection(connection);
                        buildConnections(connection);
                    }
                }
            }
    }

}
