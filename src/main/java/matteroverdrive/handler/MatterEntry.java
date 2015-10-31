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

import matteroverdrive.api.matter.IMatterEntry;

import java.io.Serializable;

public class MatterEntry implements Serializable, IMatterEntry
{
	private byte type;
	private int matter;
	private String name;
	private boolean calculated;

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
    public void setMatter(int matter){this.matter = matter;}

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

    @Override
	public String getName()
	{
		return name;
	}

	public void setCalculated(boolean calculated)
	{
		this.calculated = calculated;
	}

    @Override
	public boolean getCalculated()
	{
		return calculated;
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
