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

package matteroverdrive.api.dialog;

import matteroverdrive.api.renderer.IDialogShot;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

/**
 * Created by Simeon on 8/9/2015.
 */
public interface IDialogMessage
{
    int getID();
    IDialogMessage getParent(IDialogNpc npc,EntityPlayer player);
    List<IDialogMessage> getOptions(IDialogNpc npc,EntityPlayer player);
    String getMessageText(IDialogNpc npc,EntityPlayer player);
    String getQuestionText(IDialogNpc npc,EntityPlayer player);
    void onInteract(IDialogNpc npc,EntityPlayer player,int option);
    void onInteract(IDialogNpc npc,EntityPlayer player);
    boolean canInteract(IDialogNpc npc,EntityPlayer player);
    boolean isVisible(IDialogNpc npc,EntityPlayer player);
    IDialogShot[] getShots(IDialogNpc npc, EntityPlayer player);
    String getHoloIcon(IDialogNpc npc, EntityPlayer player);
}
