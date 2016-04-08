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

package matteroverdrive.api.network;

import com.mojang.realmsclient.gui.ChatFormatting;
import matteroverdrive.util.MOStringHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Created by Simeon on 4/19/2015.
 * This class is used by all machines that send commands to other machines, or just have internal tasks that need to be completed.
 * This is just an Entity class that holds information. Mainly in the form of NBT. Processing of the tasks is left to the Machines.
 */
public abstract class MatterNetworkTask
{
    /**
     * The individual ID of each Task
     */
    long id;

    /**
     * Name of the task.
     * This is none localized name. It's different from the unlocalized name of the task.
     * This is used to display the name of the task in GUI.
     */
    String name;

    /**
     * Unlocalized name of the task.
     * This is mainly used to access the description from the lang file.
     */
    String unlocalizedName;

    /**
     * The current sate of the task.
     * This is used to determine what is currently being done with the task.
     */
    MatterNetworkTaskState state = MatterNetworkTaskState.UNKNOWN;

    /**
     * Default constructor
     */
    public MatterNetworkTask()
    {
        init();
    }

    /**
     * Initialization method.
     * Used to assign the individual ID from a random UUID.
     */
    protected void init()
    {
        id = UUID.randomUUID().getMostSignificantBits();
    }

    /**
     * Used to determine if a task is valid.
     * This is called each time the task is accessed.
     * It checks if the sender/creator is valid.
     * It is crustal that the sender is valid, because task are stored in the sender.
     * If this returns false the task is usually discarded.
     * @param world the world of the sender/creator. This is where the creator must reside.
     * @return is the task valid.
     */
    public boolean isValid(World world)
    {
        return true;
    }
    //region NBT

    /**
     * Read the NBT data of the task.
     * @param compound the NBT data.
     */
    public void readFromNBT(NBTTagCompound compound)
    {
        if (compound != null) {
            this.state = MatterNetworkTaskState.get(compound.getInteger("State"));
            this.id = compound.getLong("id");
        }
    }

    /**
     * Writes the data to the NBT.
     * This is mainly used to store data on the task itself.
     * For example {@link matteroverdrive.matter_network.tasks.MatterNetworkTaskReplicatePattern} stores the pattern info in the task's NBT.
     * All the processing is left to the Machines.
     * @param compound the NBT tag.
     */
    public void writeToNBT(NBTTagCompound compound)
    {
        if (compound != null)
        {
            compound.setInteger("State",state.ordinal());
            compound.setLong("id",id);
        }
    }
    //endregion

    /**
     * This method is called by the tooltip of the task.
     * All the lines in list will be displayed on the task's tooltip.
     * @param list the list of tooltip lines.
     *             You must add the lines in this list in order for them to be displayed.
     */
    public void addInfo(List<String> list)
    {
        list.add(getColorForState(state) + "[ " + MOStringHelper.translateToLocal("task.state." + getState() + ".name") + " ]");
        String unlocalizedDescription = "task." + getUnlocalizedName() + ".state." + state + ".description";
        String[] infos;
        if(MOStringHelper.hasTranslation(unlocalizedDescription))
        {
            infos = MOStringHelper.translateToLocal(unlocalizedDescription).split("\n");
        }
        else
        {
            infos = MOStringHelper.translateToLocal("task.state." + state + ".description").split("\n");
        }

		Collections.addAll(list, infos);
    }

    /**
     * @param state the state of the task.
     * @return the chat color based on the state.
     */
    private ChatFormatting getColorForState(MatterNetworkTaskState state)
    {
        switch (state)
        {
            case WAITING:
                return ChatFormatting.AQUA;
            case QUEUED:
                return ChatFormatting.BLUE;
            case PROCESSING:
                return ChatFormatting.YELLOW;
            case FINISHED:
                return ChatFormatting.GREEN;
            default:
                return ChatFormatting.GRAY;
        }
    }

    //region Setters and getters

    /**
     * Gets the name of the task.
     * Not to be confused with the unlocalized name of the task.
     * @return the name of the task.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the task.
     * @param name the new name of the task.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the unlocalized name of the task
     * @return the unlocalized name of the task.
     */
    public String getUnlocalizedName() {
        return unlocalizedName;
    }

    /**
     * Sets the unlocalized name of the task.
     * @param unlocalizedName the new unlocalized name of the task.
     */
    public void setUnlocalizedName(String unlocalizedName) {
        this.unlocalizedName = unlocalizedName;
    }

    /**
     * Gets the individual id of the task.
     * @return the current state of the task.
     */
    public long getId()
	{
		return id;
	}

    /**
     * Gets the current state of the task.
     * @return the current state of the task.
     */
    public MatterNetworkTaskState getState()
    {
        return state;
    }

    /**
     * Sets the current state of the task.
     * @param state the new state of the task.
     */
    public void setState(MatterNetworkTaskState state)
    {
        this.state = state;
    }
    //endregion
}
