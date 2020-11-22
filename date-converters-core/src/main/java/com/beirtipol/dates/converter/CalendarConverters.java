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

package com.beirtipol.dates.converter;

import com.beirtipol.dates.Converter;
import com.beirtipol.dates.ThreeTenDates;
import com.beirtipol.dates.UtilDates;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.xml.datatype.XMLGregorianCalendar;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.function.Function;

/**
 * Contains converters for {@link Calendar}. It defaults to using GregorianCalendar in all instances. The author does
 * not claim to understand sun.util.BuddhistCalendar or java.util.JapaneseImperialCalendar well enough to
 * provide implementations for those.
 *
 * @author beirtipol@gmail.com
 */
@Component
public class CalendarConverters {
    @Converter(from = {Calendar.class, GregorianCalendar.class}, to = Calendar.class)
    public Function<Calendar, Calendar> CalendarToCalendar() {
        return from -> from;
    }

    @Converter(from = {XMLGregorianCalendar.class}, to = Calendar.class)
    public Function<XMLGregorianCalendar, Calendar> XMLGregorianCalendarToCalendar() {
        return from -> {
            Calendar result = GregorianCalendar.getInstance(UtilDates.UTC);
            result.setTimeInMillis(from.toGregorianCalendar().getTimeInMillis());
            return result;
        };
    }


    @Converter(from = LocalDate.class, to = Calendar.class)
    public Function<LocalDate, Calendar> LocalDateToCalendar() {
        return from -> LocalDateTimeToCalendar().apply(from.atStartOfDay());
    }

    @Converter(from = LocalDateTime.class, to = Calendar.class)
    public Function<LocalDateTime, Calendar> LocalDateTimeToCalendar() {
        return from -> ZonedDateTimeToCalendar().apply(from.atZone(ThreeTenDates.UTC));
    }

    @Converter(from = ZonedDateTime.class, to = Calendar.class)
    public Function<ZonedDateTime, Calendar> ZonedDateTimeToCalendar() {
        return from -> {
            Calendar result = GregorianCalendar.getInstance(UtilDates.UTC);
            result.setTimeInMillis(from.toInstant().toEpochMilli());
            return result;
        };
    }

    @Converter(from = java.util.Date.class, to = Calendar.class)
    public Function<Date, Calendar> UtilDateToCalendar() {
        return from -> {
            Calendar result = GregorianCalendar.getInstance(UtilDates.UTC);
            result.setTime(from);
            return result;
        };
    }

    @Converter(from = java.sql.Date.class, to = Calendar.class)
    public Function<java.sql.Date, Calendar> SQLDateToCalendar() {
        return from -> UtilDateToCalendar().apply(from);
    }

    @Converter(from = Timestamp.class, to = Calendar.class)
    public Function<Timestamp, Calendar> SQLTimestampToCalendar() {
        return from -> UtilDateToCalendar().apply(from);
    }

}
