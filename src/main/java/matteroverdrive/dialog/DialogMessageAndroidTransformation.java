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

import matteroverdrive.api.dialog.IDialogNpc;
import matteroverdrive.entity.player.AndroidPlayer;
import matteroverdrive.init.MatterOverdriveItems;
import matteroverdrive.util.MOStringHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

/**
 * Created by Simeon on 8/10/2015.
 */
public class DialogMessageAndroidTransformation extends DialogMessage
{
    public DialogMessageAndroidTransformation(){super();}
    public DialogMessageAndroidTransformation(String message, String question) {
        super(message, question);
    }

    public DialogMessageAndroidTransformation(String message) {
        super(message);
    }

    @Override
    public boolean canInteract(IDialogNpc npc, EntityPlayer player)
    {
        boolean[] hasParts = new boolean[4];
        int[] slots = new int[4];

        for (int i = 0; i < player.inventory.getSizeInventory();i++)
        {
            if(player.inventory.getStackInSlot(i) != null && player.inventory.getStackInSlot(i).getItem() == MatterOverdriveItems.androidParts)
            {
                int damage = player.inventory.getStackInSlot(i).getItemDamage();
                if (damage < hasParts.length)
                {
                    hasParts[damage] = true;
                    slots[damage] = i;
                }
            }
        }

        for (boolean hasPart : hasParts) {
            if (!hasPart) {
                return false;
            }
        }

        return true;
    }

    @Override
    public void onInteract(IDialogNpc npc, EntityPlayer player)
    {
        boolean[] hasParts = new boolean[4];
        int[] slots = new int[4];

        for (int i = 0; i < player.inventory.getSizeInventory();i++)
        {
            if(player.inventory.getStackInSlot(i) != null && player.inventory.getStackInSlot(i).getItem() == MatterOverdriveItems.androidParts)
            {
                int damage = player.inventory.getStackInSlot(i).getItemDamage();
                if (damage < hasParts.length)
                {
                    hasParts[damage] = true;
                    slots[damage] = i;
                }
            }
        }

        for (boolean hasPart : hasParts) {
            if (!hasPart) {
                if (!player.worldObj.isRemote) {
                    ChatComponentText componentText = new ChatComponentText(EnumChatFormatting.GOLD + "<Mad Scientist>" + EnumChatFormatting.RED + MOStringHelper.translateToLocal("entity.mad_scientist.line.fail." + player.getRNG().nextInt(4)));
                    componentText.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED));
                    player.addChatMessage(componentText);
                }
                return;
            }
        }

        if (!player.worldObj.isRemote) {
            for (int slot : slots) {
                player.inventory.decrStackSize(slot, 1);
            }
        }

        AndroidPlayer.get(player).startConversion();
        player.closeScreen();
    }

    @Override
    public boolean isVisible(IDialogNpc npc, EntityPlayer player)
    {
        return AndroidPlayer.get(player) == null || !AndroidPlayer.get(player).isAndroid();
    }
}
