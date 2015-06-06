package com.MO.MatterOverdrive.handler;

import com.MO.MatterOverdrive.api.inventory.IBionicStat;
import com.MO.MatterOverdrive.entity.AndroidPlayer;
import com.MO.MatterOverdrive.items.MatterScanner;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

/**
 * Created by Simeon on 3/6/2015.
 */
public class KeyHandler
{
    private static  final int MATTER_SCANNER_KEY = 0;
    public static final int TELEPORT_KEY = 1;
    private static  final String[] keyDesc = {"Open Matter Scanner GUI","Ender Teleporter Android Ability"};
    private  static  final  int[] keyValues = {Keyboard.KEY_C,Keyboard.KEY_X};
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

        for (IBionicStat stat : AndroidStatRegistry.stats.values())
        {
            int level = androidPlayer.getUnlockedLevel(stat);
            if (level > 0 && stat.isEnabled(androidPlayer,level)) {
                stat.onKeyPress(androidPlayer, androidPlayer.getUnlockedLevel(stat), Keyboard.getEventKey(), Keyboard.getEventKeyState());
            }
        }
    }

    public KeyBinding getBinding(int id)
    {
        return keys[id];
    }
}
