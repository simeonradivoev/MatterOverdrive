package com.MO.MatterOverdrive.data;

import com.MO.MatterOverdrive.api.matter.IMatterDatabase;
import com.MO.MatterOverdrive.api.matter.IMatterNetworkConnection;
import com.MO.MatterOverdrive.tile.TileEntityMachineNetworkController;
import net.minecraft.nbt.NBTTagCompound;
import scala.Int;

import java.io.*;
import java.util.*;

/**
 * Created by Simeon on 3/11/2015.
 */
public class MatterNetwork
{
    TileEntityMachineNetworkController controller;
    List<IMatterNetworkConnection> connections;

    public MatterNetwork(TileEntityMachineNetworkController controller)
    {
        connections = new ArrayList<IMatterNetworkConnection>();
        this.controller = controller;
    }

    public Map<Integer,Integer> toMap()
    {
        Map<Integer,Integer> map = new HashMap<Integer, Integer>(0);
        for (IMatterNetworkConnection connection : connections)
        {
            if(map.containsKey(connection.getID()))
            {
                map.put(connection.getID(),map.get(connection.getID()) + 1);
            }else
            {
                map.put(connection.getID(),1);
            }
        }
        return map;
    }

    public void writeInfoMapToNBT(NBTTagCompound nbt)
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = null;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(toMap());
            nbt.setByteArray("ConnectionsInfo",bos.toByteArray());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static Map<Integer,Integer> infoMapFromNBT(NBTTagCompound nbt)
    {
        if(nbt != null && nbt.hasKey("ConnectionsInfo",7))
        {
            ByteArrayInputStream bis = new ByteArrayInputStream(nbt.getByteArray("ConnectionsInfo"));
            ObjectInput in = null;
            try {
                in = new ObjectInputStream(bis);
                return  (Map<Integer,Integer>)in.readObject();
            }
            catch (ClassNotFoundException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        return null;
    }

    public void Rebuild(IMatterNetworkConnection connection)
    {
        if(controller != null)
            controller.sceduleRebuild();
        else
            System.out.println("No Controller found");
    }

    public void Dissconnect(IMatterNetworkConnection connection)
    {
        if(controller != null) {
            connection.setNetwork(null);
            connections.remove(connection);
            controller.sceduleRebuild();
        } else
            System.out.println("No Controller found");
    }

    public void AddConnection(IMatterNetworkConnection connection)
    {
        connection.setNetwork(this);
        this.connections.add(connection);
    }

    public void Dissasemble()
    {
        for (IMatterNetworkConnection connection : connections)
        {
            connection.setNetwork(null);
        }
        connections.clear();
    }


    public boolean AddConnections(Collection<IMatterNetworkConnection> connections)
    {
        if(connections != null)
        {
            this.connections.addAll(connections);
            return true;
        }

        return false;
    }

    public List<IMatterNetworkConnection> getConnections() {
        return this.connections;
    }

    public IMatterDatabase getFirstMatterDatabase()
    {
        for (int i = 0;i < connections.size();i++)
        {
            if (connections.get(i) instanceof IMatterDatabase)
            {
                return (IMatterDatabase)connections.get(i);
            }
        }
        return null;
    }
}
