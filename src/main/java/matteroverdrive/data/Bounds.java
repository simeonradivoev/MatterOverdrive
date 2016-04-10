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

package matteroverdrive.data;

/**
 * Created by Simeon on 8/30/2015.
 */
public class Bounds
{
	private int minX;
	private int minY;
	private int maxX;
	private int maxY;

	public Bounds(int minX, int minY, int maxX, int maxY)
	{
		this.minX = minX;
		this.minY = minY;
		this.maxX = maxX;
		this.maxY = maxY;
	}

	public void extendMin(int minX, int minY)
	{
		this.minX = Math.min(minX, this.minX);
		this.minY = Math.min(minY, this.minY);
	}

	public void extendMinBy(int minX, int minY)
	{
		this.minX += minX;
		this.minY += minY;
	}

	public void extendMax(int maxX, int maxY)
	{
		this.maxX = Math.max(maxX, this.maxX);
		this.maxY = Math.max(maxY, this.maxY);
	}

	public void extendMaxBy(int maxX, int maxY)
	{
		this.maxX += maxX;
		this.maxY += maxY;
	}

	public void extend(int minX, int minY, int maxX, int maxY)
	{
		extendMin(minX, minY);
		extendMax(maxX, maxY);
	}

	public void extend(Bounds bounds)
	{
		extendMin(bounds.minX, bounds.minY);
		extendMax(bounds.maxX, bounds.maxY);
	}

	public int getMinX()
	{
		return minX;
	}

	public void setMinX(int minX)
	{
		this.minX = minX;
	}

	public int getMinY()
	{
		return minY;
	}

	public void setMinY(int minY)
	{
		this.minY = minY;
	}

	public int getMaxY()
	{
		return maxY;
	}

	public void setMaxY(int maxY)
	{
		this.maxY = maxY;
	}

	public int getMaxX()
	{
		return maxX;
	}

	public void setMaxX(int maxX)
	{
		this.maxX = maxX;
	}

	public int getWidth()
	{
		return maxX - minX;
	}

	public int getHeight()
	{
		return maxY - minY;
	}
}
