package com.MO.MatterOverdrive.tile;

import cofh.lib.util.position.BlockPosition;
import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.api.matter.IMatterNetworkConnection;
import com.MO.MatterOverdrive.data.MatterNetwork;
import com.MO.MatterOverdrive.sound.MachineSound;
import com.MO.MatterOverdrive.util.MatterNetworkHelper;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

/**
 * Created by Simeon on 3/11/2015.
 */
public abstract class MOTileEntityMachine extends MOTileEntity implements IMOTileEntity, IMatterNetworkConnection
{
    //client syncs
    protected boolean lastActive;

    protected MachineSound sound;
    private final ResourceLocation soundRes;
    protected MatterNetwork network;

    protected boolean redstoneState;
    protected boolean redstoneStateDirty = true;

    protected boolean forceClientUpdate;

    public MOTileEntityMachine()
    {
        soundRes = getSoundFor(getSound());
    }

    @Override
    public void updateEntity()
    {
        if(worldObj.isRemote)
        {
            manageSound();

            if(forceClientUpdate)
            {
                updateBlock();
                forceClientUpdate = false;
            }

            return;
        }

        manageRedstoneState();
        manageClientSync();

        if(lastActive != isActive())
        {

        }
    }

    private static ResourceLocation getSoundFor(String sound)
    {
        return sound == null ? null : new ResourceLocation(Reference.MOD_ID + ":" + sound);
    }
    public abstract String getSound();
    public abstract boolean hasSound();
    public abstract boolean isActive();
    public abstract float soundVolume();

    @SideOnly(Side.CLIENT)
    protected void manageSound()
    {
        if(hasSound())
        {
            if (isActive() && !isInvalid()) {
                if (sound == null) {
                    sound = new MachineSound(soundRes, xCoord, yCoord, zCoord, soundVolume(), 1);
                    FMLClientHandler.instance().getClient().getSoundHandler().playSound(sound);

                }
            } else if (sound != null) {
                sound.stopPlaying();
                sound = null;
            }
        }
    }

    @Override
    public void readCustomNBT(NBTTagCompound nbt)
    {
        redstoneState = nbt.getBoolean("redstoneState");
        forceClientUpdate = nbt.getBoolean("forceClientUpdate");
    }

    @Override
    public void  writeCustomNBT(NBTTagCompound nbt)
    {
        nbt.setBoolean("redstoneState",redstoneState);
        nbt.setBoolean("forceClientUpdate", forceClientUpdate);
        forceClientUpdate = false;
    }

    @Override
    public Packet getDescriptionPacket()
    {
        System.out.println("Sending Packet To Client");
        NBTTagCompound syncData = new NBTTagCompound();
        writeCustomNBT(syncData);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, syncData);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
    {
        System.out.println("Receiving Packet From Server");
        NBTTagCompound syncData = pkt.func_148857_g();
        if(syncData != null)
        {
            readCustomNBT(syncData);
        }
    }

    protected void manageRedstoneState()
    {
        if(redstoneStateDirty)
        {
            boolean flag = redstoneState;
            redstoneState = worldObj.getBlockPowerInput(xCoord,yCoord,zCoord) > 0;
            redstoneStateDirty = false;
            if(flag != redstoneState)
                forceClientUpdate = true;

        }
    }

    protected void manageClientSync()
    {
        if(forceClientUpdate)
        {
            updateBlock();
            markDirty();
        }
    }

    @Override
    public void invalidate()
    {
        super.invalidate();
        if(worldObj.isRemote)
        {
            manageSound();
        }
    }

    @Override
    public void onAdded()
    {
        MatterNetworkHelper.tryConnectToNetwork(this.worldObj, this, true);
    }

    @Override
    public void onDestroyed()
    {
        MatterNetworkHelper.disconnectFromNetwork(worldObj,this,true);
    }

    @Override
    public void onNeighborBlockChange()
    {
        redstoneStateDirty = true;
    }

    @Override
    public BlockPosition getPosition()
    {
        return new BlockPosition(xCoord,yCoord,zCoord);
    }

    @Override
    public MatterNetwork getNetwork()
    {
        return network;
    }

    public boolean setNetwork(MatterNetwork network)
    {
        this.network = network;
        return true;
    }

    @Override
    public int getID()
    {
        return Block.getIdFromBlock(this.getBlockType());
    }
}
