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

package matteroverdrive.init;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import matteroverdrive.api.dialog.IDialogRegistry;
import matteroverdrive.dialog.DialogMessage;
import matteroverdrive.dialog.DialogMessageBack;
import matteroverdrive.dialog.DialogMessageQuit;
import matteroverdrive.entity.EntityVillagerMadScientist;
import matteroverdrive.handler.ConfigurationHandler;

/**
 * Created by Simeon on 8/13/2015.
 */
public class MatterOverdriveDialogs
{
    public static DialogMessage backMessage;
    public static DialogMessage quitMessage;

    public static void init(FMLPreInitializationEvent event,ConfigurationHandler configurationHandler,IDialogRegistry registry)
    {
        backMessage = new DialogMessageBack("").loadQuestionFromLocalization("dialog.generic.back.questions");
        quitMessage = new DialogMessageQuit("").loadQuestionFromLocalization("dialog.generic.quit.questions");

        if(event.getSide() == Side.CLIENT)
        {
            backMessage.setHoloIcon("mini_quit");
            quitMessage.setHoloIcon("mini_quit");
        }


        EntityVillagerMadScientist.registerDialogMessages(registry,event.getSide());
    }
}
