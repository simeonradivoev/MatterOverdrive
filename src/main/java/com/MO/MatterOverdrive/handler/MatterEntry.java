package com.MO.MatterOverdrive.handler;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class MatterEntry implements IMatterEntry
{
	byte type;
	int matter;
    String name;

    public MatterEntry(String entry,int matter,byte type)
    {
        this.name = entry;
        this.type = type;
        this.matter = matter;
    }
	
	@Override
	public int getMatter() 
	{
		return matter;
	}

	@Override
	public boolean isBlock() 
	{
		return type == 2;
	}

	@Override
	public boolean isItem() 
	{
		return this.type == 1;
	}

	@Override
	public String getName()
	{
		return name;
	}
	
}
