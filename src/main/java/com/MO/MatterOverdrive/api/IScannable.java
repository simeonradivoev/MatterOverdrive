package com.MO.MatterOverdrive.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by Simeon on 5/11/2015.
 */
public interface IScannable
{
    void addInfo(World world,double x,double y,double z,List<String> infos);
    void onScan(World world,double x,double y,double z,EntityPlayer player,ItemStack scanner);
}
