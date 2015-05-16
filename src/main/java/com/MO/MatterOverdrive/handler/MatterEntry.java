package com.MO.MatterOverdrive.handler;

import cpw.mods.fml.common.registry.GameRegistry;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.io.Serializable;

public class MatterEntry implements Serializable
{
	byte type;
	int matter;
    String name;
	boolean calculated;

    public MatterEntry(String entry,int matter,byte type)
    {
        this.name = entry;
        this.type = type;
        this.matter = matter;
    }

	public int getMatter() 
	{
		return matter;
	}

	public boolean isBlock()
	{
		return type == 2;
	}

	public boolean isItem()
	{
		return this.type == 1;
	}

	public byte getType()
	{
		return type;
	}

	public String getName()
	{
		return name;
	}

	public void setCalculated(boolean calculated)
	{
		this.calculated = calculated;
	}

	@Override
	public boolean equals(Object o)
	{
		if (o == null) return false;
		if (o == this) return true;
		if (!(o instanceof MatterEntry)) return false;
		MatterEntry entry = (MatterEntry)o;
		if(entry.name.equals(name) && entry.matter == matter && entry.calculated == calculated && entry.type == type)
			return true;
		return false;
	}
	
}
