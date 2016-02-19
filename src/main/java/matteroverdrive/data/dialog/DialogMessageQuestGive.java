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

package matteroverdrive.data.dialog;

import com.google.gson.JsonObject;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import matteroverdrive.api.dialog.IDialogNpc;
import matteroverdrive.api.dialog.IDialogQuestGiver;
import matteroverdrive.api.quest.QuestStack;
import matteroverdrive.gui.GuiDialog;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Created by Simeon on 11/22/2015.
 */
public class DialogMessageQuestGive extends DialogMessage
{
    QuestStack questStack;
    boolean returnToMain;

    public DialogMessageQuestGive(JsonObject object)
    {
        super(object);
        this.questStack = questStack;
    }

    public DialogMessageQuestGive(String message, QuestStack questStack)
    {
        super(message);
        this.questStack = questStack;
    }

    public DialogMessageQuestGive(String message, String question, QuestStack questStack)
    {
        super(message, question);
        this.questStack = questStack;
    }

    public DialogMessageQuestGive(String[] messages, String[] questions, QuestStack questStack)
    {
        super(messages, questions);
        this.questStack = questStack;
    }

    @Override
    public void onInteract(IDialogNpc npc, EntityPlayer player)
    {
        super.onInteract(npc,player);
        if (npc != null && npc instanceof IDialogQuestGiver && player != null && !player.worldObj.isRemote)
        {
            ((IDialogQuestGiver) npc).giveQuest(this,questStack,player);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected void setAsGuiActiveMessage(IDialogNpc npc, EntityPlayer player)
    {
        if (Minecraft.getMinecraft().currentScreen instanceof GuiDialog)
        {
            ((GuiDialog) Minecraft.getMinecraft().currentScreen).setCurrentMessage(returnToMain ? npc.getStartDialogMessage(player) : this);
        }
    }

    public DialogMessageQuestGive setReturnToMain(boolean returnToMain)
    {
        this.returnToMain = returnToMain;
        return this;
    }
}
