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

package matteroverdrive.client.data;

/**
 * Created by Simeon on 12/22/2015.
 */
public class Color extends Number
{
	public static final Color WHITE = new Color(255, 255, 255);
	public static final Color RED = new Color(255, 0, 0);
	public static final Color GREEN = new Color(0, 255, 0);
	public static final Color BLUE = new Color(0, 0, 255);
	private final int color;

	public Color(int color)
	{
		this.color = color;
	}

	public Color(int r, int g, int b)
	{
		this(r, g, b, 255);
	}

	public Color(int r, int g, int b, int a)
	{
		this(b & 255 | (g & 255) << 8 | (r & 255) << 16 | (a & 255) << 24);
	}

	public Color multiplyWithoutAlpha(float multiply)
	{
		return multiply(multiply, multiply, multiply, 1f);
	}

	public Color multiplyWithAlpha(float multiply)
	{
		return multiply(multiply, multiply, multiply, multiply);
	}

	public Color multiply(float rm, float gm, float bm, float am)
	{
		return new Color((int)(getIntR() * rm), (int)(getIntG() * gm), (int)(getIntB() * bm), (int)(getIntA() * am));
	}

	public Color add(Color color)
	{
		return new Color(getIntR() + color.getIntR(), getIntG() + color.getIntG(), getIntB() + color.getIntB(), getIntA() + color.getIntA());
	}

	public Color subtract(Color color)
	{
		return new Color(getIntR() - color.getIntR(), getIntG() - color.getIntG(), getIntB() - color.getIntB(), getIntA() - color.getIntA());
	}

	//region INT getters and setters

	public int getIntR()
	{
		return this.color >> 16 & 255;
	}

	public int getIntG()
	{
		return this.color >> 8 & 255;
	}

	public int getIntB()
	{
		return this.color & 255;
	}

	public int getIntA()
	{
		return this.color >> 24 & 255;
	}

	public int getColor()
	{
		return color;
	}

	//endregion
	//region FLOAT setters and getters
	public float getFloatR()
	{
		return (float)getIntR() / 255f;
	}

	public float getFloatG()
	{
		return (float)getIntG() / 255f;
	}

	public float getFloatB()
	{
		return (float)getIntB() / 255f;
	}

	public float getFloatA()
	{
		return (float)getIntA() / 255f;
	}

	@Override
	public int intValue()
	{
		return color;
	}

	@Override
	public long longValue()
	{
		return (long)color;
	}

	@Override
	public float floatValue()
	{
		return (float)color;
	}

	@Override
	public double doubleValue()
	{
		return (double)color;
	}
	//endregion
}
