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

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Utility class for java.util.Date and related classes
 */
public class UtilDates {
    public static TimeZone UTC           = TimeZone.getTimeZone("UTC");
    public static TimeZone EUROPE_LONDON = TimeZone.getTimeZone("Europe/London");
    public static int      YEAR_OFFSET   = 1900;
    public static int      MONTH_OFFSET  = 1;

    /**
     * Helper method for creating java.util.Date with the same syntax and indexing as java.time.ZonedDateTime
     *
     * @param year         The cardinal year as humans see it. No need to manually offset by 1900
     * @param month        The cardinal month, 1-indexed rather than zero-indexed
     * @param day          The day of the month
     * @param hour
     * @param minute
     * @param second
     * @param milliseconds
     * @param timezone
     * @return a java.util.Date with the specified fields
     */
    public static Date newUtilDate(int year, int month, int day, int hour, int minute, int second, int milliseconds, TimeZone timezone) {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.set(year, month - 1, day, hour, minute, second);
        calendar.set(Calendar.MILLISECOND, milliseconds);
        calendar.setTimeZone(timezone);
        return calendar.getTime();
    }

    /**
     * Add a number of temporal units to a Date
     *
     * @param date
     * @param units
     * @param field The {@link Calendar} field referencing the temporal unit to adjust, e.g. {@link Calendar#DAY_OF_YEAR}
     * @return
     */
    public static Date plus(Date date, int units, int field) {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(date);
        calendar.set(field, calendar.get(field) + units);
        return calendar.getTime();
    }

    /**
     * Add a number of days to a Date
     *
     * @param date
     * @param days can be positive or negative
     * @return
     */
    public static Date plusDays(Date date, int days) {
        return plus(date, days, Calendar.DAY_OF_YEAR);
    }


    /**
     * Add a number of months to a Date
     *
     * @param date
     * @param months can be positive or negative
     * @return
     */
    public static Date plusMonths(Date date, int months) {
        return plus(date, months, Calendar.MONTH);
    }

    /**
     * Add a number of years to a Date
     *
     * @param date
     * @param years can be positive or negative
     * @return
     */
    public static Date plusYears(Date date, int years) {
        return plus(date, years, Calendar.YEAR);
    }
}
