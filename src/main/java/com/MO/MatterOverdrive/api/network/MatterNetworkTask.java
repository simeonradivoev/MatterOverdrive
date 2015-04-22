package com.MO.MatterOverdrive.api.network;

import cofh.lib.util.helpers.StringHelper;
import cofh.lib.util.position.BlockPosition;
import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.data.network.MatterNetworkTaskPacket;
import com.MO.MatterOverdrive.util.MOStringHelper;
import com.google.common.collect.Lists;
import com.sun.istack.internal.NotNull;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import scala.actors.threadpool.Arrays;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by Simeon on 4/19/2015.
 */
public abstract class MatterNetworkTask
{
    String name;
    BlockPosition senderPos;

    String unlocalizedName;
    byte state;

    public MatterNetworkTask()
    {
        init();
    }
    public MatterNetworkTask(IMatterNetworkConnection sender)
    {
        this.senderPos = sender.getPosition();
        init();
    }
    protected void init()
    {

    }
    public byte getState()
    {
        return state;
    }
    public void setState(byte state)
    {
        this.state = state;
    }
    public void setStateWithNotify(World world,byte state)
    {
        IMatterNetworkDispatcher dispatcher = getSender(world);
        if (dispatcher != null)
            dispatcher.onTaskChange(this);
        setState(state);
    }
    public boolean isValid(World world)
    {
        IMatterNetworkDispatcher dispatcher = getSender(world);
        return dispatcher != null;
    }
    public void readFromNBT(NBTTagCompound compound)
    {
        if (compound != null) {
            this.senderPos = new BlockPosition(compound);
            this.state = compound.getByte("State");
        }
    }
    public void writeToNBT(NBTTagCompound compound)
    {
        if (compound != null)
        {
            senderPos.writeToNBT(compound);
            compound.setInteger("State",state);
        }
    }
    public void addInfo(List<String> list)
    {
        list.add(getColorForState(state) + "[ " + MOStringHelper.translateToLocal("task.state." + getState() + ".name") + " ]");
        String descriptionUnlocilized = "task." + getUnlocalizedName() + ".state." + state + ".description";
        if(MOStringHelper.hasTranslation(descriptionUnlocilized))
        {
            list.addAll(Arrays.asList(MOStringHelper.translateToLocal(descriptionUnlocilized).split("\n")));
        }
        else
        {
            list.addAll(Arrays.asList(MOStringHelper.translateToLocal("task.state." + state + ".description").split("\n")));
        }
    }

    private EnumChatFormatting getColorForState(int state)
    {
        switch (state)
        {
            case Reference.TASK_STATE_WAITING:
                return EnumChatFormatting.AQUA;
            case Reference.TASK_STATE_QUEUED:
                return EnumChatFormatting.BLUE;
            case Reference.TASK_STATE_PROCESSING:
                return EnumChatFormatting.YELLOW;
            case Reference.TASK_STATE_FINISHED:
                return EnumChatFormatting.GREEN;
            default:
                return EnumChatFormatting.GRAY;
        }
    }

    //region Setters nad getters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnlocalizedName() {
        return unlocalizedName;
    }

    public void setUnlocalizedName(String unlocalizedName) {
        this.unlocalizedName = unlocalizedName;
    }

    public IMatterNetworkDispatcher getSender(World world)
    {
        if (world != null) {
            TileEntity entity = senderPos.getTileEntity(world);
            if (entity instanceof IMatterNetworkConnectionProxy && ((IMatterNetworkConnectionProxy) entity).getMatterNetworkConnection() instanceof IMatterNetworkDispatcher) {
                return (IMatterNetworkDispatcher) ((IMatterNetworkConnectionProxy) entity).getMatterNetworkConnection();
            }
        }
        return null;
    }
    //endregion
}
