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

package matteroverdrive.api;

import matteroverdrive.api.exceptions.CoreInaccessibleException;

import java.lang.reflect.Field;

/**
 * Created by Simeon on 7/20/2015.
 */
public class MOApi
{
	private static final String CORE_API_CLASS = "matteroverdrive.core.Api";
	private static final String CORE_API_FIELD = "INSTANCE";
	private static final IMOApi API_INSTANCE;

	static
	{
		try
		{
			final Class<?> apiClass = Class.forName(CORE_API_CLASS);
			final Field apiField = apiClass.getField(CORE_API_FIELD);
			API_INSTANCE = (IMOApi)apiField.get(apiClass);
		}
		catch (ClassNotFoundException e)
		{
			throw new CoreInaccessibleException("MatterOverdrive API tried  to access the %s class, without it being declared", CORE_API_CLASS);
		}
		catch (IllegalAccessException e)
		{
			throw new CoreInaccessibleException("MatterOverdrive API tried to access the %s field in %s without it being declared.", CORE_API_FIELD, CORE_API_CLASS);
		}
		catch (NoSuchFieldException e)
		{
			throw new CoreInaccessibleException("MatterOverdrive API tried to access the %s field in %s without enough access permissions.", CORE_API_FIELD, CORE_API_CLASS);
		}
	}

	public static IMOApi instance()
	{
		return API_INSTANCE;
	}
}
