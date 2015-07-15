package matteroverdrive.api.network;

import cofh.lib.util.position.BlockPosition;
import matteroverdrive.Reference;
import matteroverdrive.util.MOStringHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import java.util.List;
import java.util.UUID;

/**
 * Created by Simeon on 4/19/2015.
 */
public abstract class MatterNetworkTask
{
    long id;
    String name;
    BlockPosition senderPos;
    boolean isAlive;

    String unlocalizedName;
    MatterNetworkTaskState state;

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
        id = UUID.randomUUID().getMostSignificantBits();
    }
    public MatterNetworkTaskState getState()
    {
        return state;
    }
    public void setState(MatterNetworkTaskState state)
    {
        this.state = state;
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
            this.state = MatterNetworkTaskState.get(compound.getInteger("State"));
            this.isAlive = compound.getBoolean("isAlive");
            this.id = compound.getLong("id");
        }
    }
    public void writeToNBT(NBTTagCompound compound)
    {
        if (compound != null)
        {
            senderPos.writeToNBT(compound);
            compound.setInteger("State",state.ordinal());
            compound.setBoolean("isAlive",isAlive);
            compound.setLong("id",id);
        }
    }
    public void addInfo(List<String> list)
    {
        list.add(getColorForState(state) + "[ " + MOStringHelper.translateToLocal("task.state." + getState() + ".name") + " ]");
        String descriptionUnlocilized = "task." + getUnlocalizedName() + ".state." + state + ".description";
        String[] infos;
        if(MOStringHelper.hasTranslation(descriptionUnlocilized))
        {
            infos = MOStringHelper.translateToLocal(descriptionUnlocilized).split("\n");
        }
        else
        {
            infos = MOStringHelper.translateToLocal("task.state." + state + ".description").split("\n");
        }

        for (int i = 0;i < infos.length;i++)
        {
            list.add(infos[i]);
        }
    }

    private EnumChatFormatting getColorForState(MatterNetworkTaskState state)
    {
        switch (state)
        {
            case WAITING:
                return EnumChatFormatting.AQUA;
            case QUEUED:
                return EnumChatFormatting.BLUE;
            case PROCESSING:
                return EnumChatFormatting.YELLOW;
            case FINISHED:
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
        if (world != null && senderPos != null)
        {
            TileEntity entity = senderPos.getTileEntity(world);
            if (entity instanceof IMatterNetworkDispatcher) {
                return (IMatterNetworkDispatcher)entity;
            }
        }
        return null;
    }
    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean isAlive) {
        this.isAlive = isAlive;
    }
    public long getId(){return id;}
    //endregion
}
