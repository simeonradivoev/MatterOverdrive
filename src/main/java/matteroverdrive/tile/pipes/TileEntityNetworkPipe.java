package matteroverdrive.tile.pipes;

import cofh.lib.util.position.BlockPosition;
import matteroverdrive.Reference;
import matteroverdrive.api.network.IMatterNetworkCable;
import matteroverdrive.api.network.IMatterNetworkConnection;
import matteroverdrive.api.network.IMatterNetworkConnectionProxy;
import matteroverdrive.matter_network.MatterNetworkPacket;
import matteroverdrive.matter_network.packets.MatterNetworkTaskPacket;
import matteroverdrive.util.MatterNetworkHelper;
import matteroverdrive.util.math.MOMathHelper;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by Simeon on 3/15/2015.
 */
public class TileEntityNetworkPipe extends TileEntityPipe implements IMatterNetworkCable, IMatterNetworkConnectionProxy {

    @Override
    public boolean canConnectTo(TileEntity entity, ForgeDirection direction)
    {
        if (entity instanceof IMatterNetworkConnectionProxy)
        {
            if (entity instanceof TileEntityNetworkPipe)
            {
                TileEntityNetworkPipe networkPipe = (TileEntityNetworkPipe)entity;
                int pipeConnections = networkPipe.getConnections();
                if (MOMathHelper.getBoolean(pipeConnections,direction.ordinal())) {
                    return true;
                }
                else
                {
                    int pipeConnectionsCount = 0;
                    for (int i = 0; i < 6; i++) {
                        pipeConnectionsCount += ((pipeConnections >> i) & 1);
                    }
                    return pipeConnectionsCount < 2;
                }
            }
            else
            {
                return ((IMatterNetworkConnectionProxy) entity).getMatterNetworkConnection().canConnectFromSide(direction);
            }
        }
        return false;
    }

    @Override
    public void onAdded(World world, int x, int y, int z) {

    }

    @Override
    public void onPlaced(World world, EntityLivingBase entityLiving) {

    }

    @Override
    public void onDestroyed()
    {

    }

    @Override
    public void writeToDropItem(ItemStack itemStack) {

    }

    @Override
    public void readFromPlaceItem(ItemStack itemStack) {

    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public void broadcast(MatterNetworkPacket task,ForgeDirection direction)
    {
        if (isValid())
        {
            if (task instanceof MatterNetworkTaskPacket && ((MatterNetworkTaskPacket) task).getTask(worldObj).getState() > Reference.TASK_STATE_WAITING)
                return;

            for (int i = 0; i < 6; i++)
            {
                if (direction.getOpposite().ordinal() != i)
                    MatterNetworkHelper.broadcastTaskInDirection(worldObj, task, this, ForgeDirection.getOrientation(i));
            }
        }
    }

    @Override
    public BlockPosition getPosition() {
        return new BlockPosition(this);
    }

    @Override
    public boolean canConnectFromSide(ForgeDirection side)
    {
        return MOMathHelper.getBoolean(getConnections(),side.ordinal());
    }

    @Override
    public void updateSides(boolean notify)
    {
        int connections = 0;
        int connectionCount = 0;

        for (int i = 0; i < 6; i++) {
            TileEntity t = this.worldObj.getTileEntity(ForgeDirection.values()[i].offsetX + this.xCoord, ForgeDirection.values()[i].offsetY + this.yCoord, ForgeDirection.values()[i].offsetZ + this.zCoord);

            if (connectionCount < 2 && canConnectTo(t, ForgeDirection.getOrientation(ForgeDirection.OPPOSITES[i])))
            {
                connections |= ForgeDirection.values()[i].flag;
                connectionCount++;
            }
        }

        this.setConnections(connections, notify);
    }

    @Override
    public IMatterNetworkConnection getMatterNetworkConnection() {
        return this;
    }

    @Override
    public int onNetworkTick(World world, TickEvent.Phase phase)
    {
        return 0;
    }

    @Override
    public void writeCustomNBT(NBTTagCompound nbt) {

    }

    @Override
    public void readCustomNBT(NBTTagCompound nbt) {

    }

    @Override
    protected void onAwake(Side side) {

    }
}
