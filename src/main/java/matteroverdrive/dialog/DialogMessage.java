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

package matteroverdrive.dialog;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import matteroverdrive.api.dialog.IDialogMessage;
import matteroverdrive.api.dialog.IDialogNpc;
import matteroverdrive.api.events.MOEventDialogInteract;
import matteroverdrive.api.renderer.IDialogShot;
import matteroverdrive.entity.player.MOExtendedProperties;
import matteroverdrive.gui.GuiDialog;
import matteroverdrive.util.MOStringHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simeon on 8/9/2015.
 */
public class DialogMessage implements IDialogMessage
{
    protected String message;
    protected String question;
    protected IDialogMessage parent;
    protected List<IDialogMessage> options;
    @SideOnly(Side.CLIENT)
    protected IDialogShot[] shots;
    @SideOnly(Side.CLIENT)
    protected String holoIcon;

    public DialogMessage()
    {
        init();
    }
    public DialogMessage(String message)
    {
        this(message, message);
    }
    public DialogMessage(String message, String question)
    {
        this.message = message;
        this.question = question;
        init();
    }

    private void init()
    {
        options = new ArrayList<>();
    }

    @Override
    public IDialogMessage getParent(IDialogNpc npc, EntityPlayer player) {
        return parent;
    }

    @Override
    public List<IDialogMessage> getOptions(IDialogNpc npc, EntityPlayer player)
    {
        return options;
    }

    @Override
    public String getMessageText(IDialogNpc npc, EntityPlayer player)
    {
        return formatMessage(message, npc, player);
    }

    @Override
    public String getQuestionText(IDialogNpc npc, EntityPlayer player)
    {
        return formatQuestion(question, npc, player);
    }

    @Override
    public void onOptionsInteract(IDialogNpc npc, EntityPlayer player, int option)
    {
        if (option >= 0 && option < options.size())
        {
            options.get(option).onInteract(npc, player);
        }
    }

    @Override
    public void onInteract(IDialogNpc npc,EntityPlayer player)
    {
        if (npc != null && player != null)
        {
            if (player.worldObj.isRemote) {
                setAsGuiActiveMessage(npc, player);
            }else
            {
                npc.onPlayerInteract(player,this);
                MOEventDialogInteract eventDialogInteract = new MOEventDialogInteract(player,npc,this);
                MinecraftForge.EVENT_BUS.post(eventDialogInteract);
                MOExtendedProperties extendedProperties = MOExtendedProperties.get(player);
                if (extendedProperties != null)
                {
                    extendedProperties.onEvent(eventDialogInteract);
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    protected void setAsGuiActiveMessage(IDialogNpc npc, EntityPlayer player)
    {
        if (Minecraft.getMinecraft().currentScreen instanceof GuiDialog)
        {
            ((GuiDialog) Minecraft.getMinecraft().currentScreen).setCurrentMessage(this);
        }
    }

    @Override
    public boolean canInteract(IDialogNpc npc, EntityPlayer player) {
        return true;
    }

    @Override
    public boolean isVisible(IDialogNpc npc, EntityPlayer player)
    {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IDialogShot[] getShots(IDialogNpc npc, EntityPlayer player) {
        return shots;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getHoloIcon(IDialogNpc npc, EntityPlayer player)
    {
        return holoIcon;
    }

    @SideOnly(Side.CLIENT)
    public void setShots(IDialogShot... shot)
    {
        this.shots = shot;
    }

    public void setParent(IDialogMessage parent)
    {
        this.parent = parent;
    }

    public void addOption(IDialogMessage message)
    {
        this.options.add(message);
    }

    public IDialogMessage getMessage(int id)
    {
        return this.options.get(id);
    }

    public List<IDialogMessage> getOptions()
    {
        return options;
    }

    @SideOnly(Side.CLIENT)
    public DialogMessage setHoloIcon(String holoIcon)
    {
        this.holoIcon = holoIcon; return this;
    }

    public DialogMessage loadMessageFromLocalization(String key)
    {
        message = MOStringHelper.translateToLocal(key);
        return this;
    }

    public DialogMessage loadQuestionFromLocalization(String key)
    {
        question = MOStringHelper.translateToLocal(key);
        return this;
    }

    protected String formatMessage(String text,IDialogNpc npc,EntityPlayer player)
    {
        if (text != null) {
            return String.format(text, player.getDisplayName(), npc.getEntity().getCommandSenderName());
        }
        return text;
    }

    protected String formatQuestion(String text,IDialogNpc npc,EntityPlayer player)
    {
        if (text != null) {
            return String.format(text, player.getDisplayName(), npc.getEntity().getCommandSenderName());
        }
        return text;
    }
}
