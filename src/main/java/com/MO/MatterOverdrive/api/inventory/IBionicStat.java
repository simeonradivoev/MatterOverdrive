package com.MO.MatterOverdrive.api.inventory;

import com.MO.MatterOverdrive.entity.AndroidPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;

import java.util.List;

/**
 * Created by Simeon on 5/27/2015.
 */
public interface IBionicStat
{
    String getUnlocalizedName();
    String getDisplayName(AndroidPlayer androidPlayer,int level);
    boolean canBeUnlocked(AndroidPlayer android,int level);
    void onAndroidUpdate(AndroidPlayer android, int level);
    void onKeyPress(AndroidPlayer androidPlayer, int level, int keycode, boolean down);
    void onLivingEvent(AndroidPlayer androidPlayer,int level,LivingEvent event);
    void onUnlock(AndroidPlayer android,int level);
    void onTooltip(AndroidPlayer android, int level, List<String> list, int mouseX, int mouseY);
    void changeAndroidStats(AndroidPlayer androidPlayer,int level,boolean enabled);
    IBionicStat getRoot();
    List<ItemStack> getRequiredItems();
    boolean isEnabled(AndroidPlayer android, int level);
    boolean isActive(AndroidPlayer androidPlayer,int level);
    boolean showOnHud(AndroidPlayer android,int level);
    ResourceLocation getIcon(int level);
    int maxLevel();
}
