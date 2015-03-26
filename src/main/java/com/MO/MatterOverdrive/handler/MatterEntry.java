package com.MO.MatterOverdrive.handler;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;

public class MatterEntry implements IMatterEntry
{
	byte type;
	int matter;
    String name;
	
	public MatterEntry(Item item,int matter)
	{
		this.matter = matter;
		this.name = Item.itemRegistry.getNameForObject(item);
		this.type = 1;
	}
	
	public MatterEntry(Block block,int matter)
	{
        this.name = Block.blockRegistry.getNameForObject(block);
		this.type = 2;
		this.matter = matter;
	}

    public MatterEntry(String entry,int matter)
    {
        this.name = entry;
        this.type = 0;
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
