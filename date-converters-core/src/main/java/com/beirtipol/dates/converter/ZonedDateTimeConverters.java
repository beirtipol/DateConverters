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
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.function.Function;

/**
 * This always converts to a {@link ZonedDateTime} which is set to UTC, allowing you to convert it to the TimeZone of
 * your choosing by simply calling {@link ZonedDateTime#withZoneSameInstant(ZoneId)}
 *
 * @author beirtipol@gmail.com
 */
@Component
public class ZonedDateTimeConverters {
    private final ThreadLocal<Calendar> calendar = new ThreadLocal<>();

    @Converter(from = XMLGregorianCalendar.class, to = ZonedDateTime.class)
    public Function<XMLGregorianCalendar, ZonedDateTime> XMLDateToZonedDateTime() {
        return from -> {
            Calendar result = GregorianCalendar.getInstance(UtilDates.UTC);
            result.setTimeInMillis(from.toGregorianCalendar().getTimeInMillis());
            return ((GregorianCalendar) result).toZonedDateTime();
        };
    }

    @Converter(from = LocalDate.class, to = ZonedDateTime.class)
    public Function<LocalDate, ZonedDateTime> LocalDateToZonedDateTime() {
        return from -> from.atStartOfDay().atZone(ThreeTenDates.UTC);
    }

    @Converter(from = ZonedDateTime.class, to = ZonedDateTime.class)
    public Function<ZonedDateTime, ZonedDateTime> ZonedDateTimeToZonedDateTime() {
        return from -> from;
    }

    @Converter(from = LocalDateTime.class, to = ZonedDateTime.class)
    public Function<LocalDateTime, ZonedDateTime> LocalDateTimeToZonedDateTime() {
        return from -> from.atZone(ThreeTenDates.UTC);

    }

    @Converter(from = java.util.Date.class, to = ZonedDateTime.class)
    public Function<Date, ZonedDateTime> UtilDateToZonedDateTime() {
        return from -> {
            Calendar cal = Calendar.getInstance();
            cal.clear();
            cal.setTime(from);
            return CalendarToZonedDateTime().apply(cal);
        };
    }

    @Converter(from = {Calendar.class, GregorianCalendar.class}, to = ZonedDateTime.class)
    public Function<Calendar, ZonedDateTime> CalendarToZonedDateTime() {
        return from -> from.getTime().toInstant().atZone(ThreeTenDates.UTC);
    }

    @Converter(from = java.sql.Date.class, to = ZonedDateTime.class)
    public Function<java.sql.Date, ZonedDateTime> SQLDateToZonedDateTime() {
        return from -> UtilDateToZonedDateTime().apply(from);
    }

    @Converter(from = Timestamp.class, to = ZonedDateTime.class)
    public Function<Timestamp, ZonedDateTime> SQLTimestampToZonedDateTime() {
        return from -> UtilDateToZonedDateTime().apply(from);
    }

}
