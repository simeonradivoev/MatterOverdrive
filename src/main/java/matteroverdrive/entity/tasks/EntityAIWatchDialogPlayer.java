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

package matteroverdrive.entity.tasks;

import matteroverdrive.api.dialog.IDialogNpc;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Created by Simeon on 8/11/2015.
 */
public class EntityAIWatchDialogPlayer extends EntityAIWatchClosest
{
    private IDialogNpc npc;

    public EntityAIWatchDialogPlayer(IDialogNpc dialogNpc)
    {
        super(dialogNpc.getEntity(), EntityPlayer.class, 8.0F);
        this.npc = dialogNpc;
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        if (this.npc.getDialogPlayer() != null)
        {
            this.closestEntity = this.npc.getDialogPlayer();
            return true;
        }
        else
        {
            return false;
        }
    }
}
