package com.MO.MatterOverdrive.handler;

import cpw.mods.fml.common.registry.GameRegistry;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class MatterEntry
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
	
}
