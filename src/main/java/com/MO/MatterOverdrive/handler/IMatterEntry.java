package com.MO.MatterOverdrive.handler;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

public interface IMatterEntry 
{
	public int getMatter();
	public boolean isBlock();
	public boolean isItem();
	public String getName();
}
