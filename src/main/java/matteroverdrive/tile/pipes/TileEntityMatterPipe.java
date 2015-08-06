/*
 * This file is part of Matter Overdrive
 * Copyright (c) 2015., Simeon Radivoev, All rights reserved.
 *
 * Matter Overdrive is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Matter Overdrive is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Matter Overdrive.  If not, see <http://www.gnu.org/licenses>.
 */

package matteroverdrive.tile.pipes;

import cofh.lib.util.TimeTracker;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import matteroverdrive.api.matter.IMatterConnection;
import matteroverdrive.api.matter.IMatterHandler;
import matteroverdrive.data.MatterStorage;
import matteroverdrive.machines.MachineNBTCategory;
import matteroverdrive.util.MatterHelper;
import matteroverdrive.util.math.MOMathHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;

/**
 * Created by Simeon on 3/7/2015.
 */
public class TileEntityMatterPipe extends  TileEntityPipe implements IMatterConnection, IMatterHandler
{
    public  static  final int MATTER_STORAGE = 32;
    public  static  final int TRANSFER_SPEED = 20;
    public  ForgeDirection lastDir = ForgeDirection.WEST;
    private MatterStorage storage;
    public  static Random rand = new Random();
    TimeTracker t;

    @SideOnly(Side.CLIENT)
    private boolean matterVisible;

    public TileEntityMatterPipe()
    {
        t = new TimeTracker();
        storage = new MatterStorage(MATTER_STORAGE);
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

    @Override
    public boolean canConnectTo(TileEntity entity,ForgeDirection direction)
    {
        return entity instanceof IMatterConnection && ((IMatterConnection) entity).canConnectFrom(direction);
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
                    int transferAmount = MatterHelper.Transfer(dir.dir,MATTER_STORAGE,this,to);
                }
            }
        }
    }

    private List<WeightedDirection> getWeightedValidSides(ForgeDirection transferDir)
    {
        int connections = getConnections();
        List<WeightedDirection> validSides = new ArrayList<>(6);
        ForgeDirection transferDirOp = MatterHelper.opposite(transferDir);

        for (int i = 0; i < 6; i++)
        {
            if (MOMathHelper.getBoolean(connections,i))
            {
                if (ForgeDirection.values()[i] == transferDir)
                {
                    validSides.add(new WeightedDirection(ForgeDirection.values()[i], 2.0f));
                } else if (ForgeDirection.values()[i] == transferDirOp) {
                    validSides.add(new WeightedDirection(ForgeDirection.values()[i], 0.0f));
                } else {
                    validSides.add(new WeightedDirection(ForgeDirection.values()[i], 0.5f + rand.nextFloat()));
                }
            }
        }

        if(validSides.size() > 1)
            WeightedDirection.Sort(validSides);

        return validSides;
    }

    @Override
    public void writeCustomNBT(NBTTagCompound comp, EnumSet<MachineNBTCategory> categories)
    {
        if(!worldObj.isRemote && categories.contains(MachineNBTCategory.DATA))
        {
            storage.writeToNBT(comp);
            comp.setByte("transfer_dir", (byte) this.lastDir.ordinal());
        }
    }

    @Override
    public  void  readCustomNBT(NBTTagCompound comp, EnumSet<MachineNBTCategory> categories)
    {
        if (categories.contains(MachineNBTCategory.DATA)) {
            storage.readFromNBT(comp);
            if (comp.hasKey("transfer_dir")) {
                lastDir = ForgeDirection.values()[comp.getByte("transfer_dir")];
            }
        }
    }

    @Override
    protected void onAwake(Side side) {

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
        NBTTagCompound tagCompound = new NBTTagCompound();
        tagCompound.setInteger("Connections",getConnections());
        tagCompound.setBoolean("matterVisible", getMatterStored() > 0);
        return new S35PacketUpdateTileEntity(xCoord,yCoord,zCoord,1,tagCompound);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
    {
        super.onDataPacket(net,pkt);
        if(worldObj.isRemote) {
            this.setMatterVisible(pkt.func_148857_g().getBoolean("matterVisible"));
        }
    }

    @Override
    public void onAdded(World world, int x, int y, int z) {

    }

    @Override
    public void onPlaced(World world, EntityLivingBase entityLiving) {

    }

    @Override
    public void onDestroyed() {

    }

    @Override
    public void writeToDropItem(ItemStack itemStack) {

    }

    @Override
    public void readFromPlaceItem(ItemStack itemStack) {

    }
}
