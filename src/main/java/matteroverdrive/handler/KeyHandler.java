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

package matteroverdrive.handler;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.api.android.IBionicStat;
import matteroverdrive.entity.AndroidPlayer;
import matteroverdrive.items.MatterScanner;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

/**
 * Created by Simeon on 3/6/2015.
 */
@SideOnly(Side.CLIENT)
public class KeyHandler
{
    public static  final int MATTER_SCANNER_KEY = 0;
    public static final int ABILITY_USE_KEY = 1;
    public static final int ABILITY_SWITCH_KEY = 2;
    private static  final String[] keyDesc = {"Open Matter Scanner GUI","Android Ability key","Android Switch Ability key"};
    private  static  final  int[] keyValues = {Keyboard.KEY_C,Keyboard.KEY_X,Keyboard.KEY_TAB};
    private  final KeyBinding[] keys;

    public  KeyHandler()
    {
        keys = new KeyBinding[keyValues.length];
        for(int i = 0;i < keys.length;i++)
        {
            keys[i] = new KeyBinding(keyDesc[i],keyValues[i],"Matter Overdrive");
            ClientRegistry.registerKeyBinding(keys[i]);
        }
    }

    @SubscribeEvent
    public  void onKeyInput(InputEvent.KeyInputEvent event)
    {
        if(!FMLClientHandler.instance().isGUIOpen(GuiChat.class))
        {
            int key = Keyboard.getEventKey();
            boolean isDown = Keyboard.getEventKeyState();

            //Matter Scanner key
            if(isDown && keys[MATTER_SCANNER_KEY].getKeyCode() == key)
            {
                //send packet to open gui
                MatterScanner.DisplayGuiScreen();
            }

            manageBiostats(event);
        }
    }

    public void manageBiostats(InputEvent.KeyInputEvent event)
    {
        AndroidPlayer androidPlayer = AndroidPlayer.get(FMLClientHandler.instance().getClientPlayerEntity());
        if (androidPlayer.isAndroid()) {
            androidPlayer.onKeyInput(event);

            for (IBionicStat stat : MatterOverdrive.statRegistry.getStats()) {
                int level = androidPlayer.getUnlockedLevel(stat);
                if (level > 0 && stat.isEnabled(androidPlayer, level)) {
                    stat.onKeyPress(androidPlayer, androidPlayer.getUnlockedLevel(stat), Keyboard.getEventKey(), Keyboard.getEventKeyState());
                }
            }
        }
    }

    public KeyBinding getBinding(int id)
    {
        return keys[id];
    }
}
