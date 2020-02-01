/*
 * Copyright (C) 2020  https://github.com/beirtipol
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.beirtipol.dates;

import java.util.TimeZone;

/**
 * Utility class for java.util.Date and related classes
 */
public class UtilDates {
	public static TimeZone	UTC				= TimeZone.getTimeZone("UTC");
	public static int		YEAR_OFFSET		= 1900;
	public static int		MONTH_OFFSET	= 1;
}
