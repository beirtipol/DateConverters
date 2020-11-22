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
import org.springframework.beans.factory.annotation.Autowired;
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
 * As {@link LocalDate} does not have any TimeZone information, it does not try to do anything clever with the temporals
 * it attempts to parse. Where possible, it will extract the year/month/day parts of the temporal.
 *
 * @author beirtipol@gmail.com
 */
@Component
public class LocalDateConverters {
    @Autowired
    private ZonedDateTimeConverters zonedDateTimeConverters;

    @Converter(from = XMLGregorianCalendar.class, to = LocalDate.class)
    public Function<XMLGregorianCalendar, LocalDate> XMLDateToLocalDate() {
        return from -> ZonedDateTimeToLocalDate().apply(from.toGregorianCalendar().toZonedDateTime());
    }

    @Converter(from = LocalDate.class, to = LocalDate.class)
    public Function<LocalDate, LocalDate> LocalDateToLocalDate() {
        return from -> from;
    }

    @Converter(from = ZonedDateTime.class, to = LocalDate.class)
    public Function<ZonedDateTime, LocalDate> ZonedDateTimeToLocalDate() {
        return ZonedDateTime::toLocalDate;
    }

    @Converter(from = LocalDateTime.class, to = LocalDate.class)
    public Function<LocalDateTime, LocalDate> LocalDateTimeToLocalDate() {
        return LocalDateTime::toLocalDate;
    }

    @Converter(from = java.util.Date.class, to = LocalDate.class)
    public Function<Date, LocalDate> UtilDateToLocalDate() {
        return from -> zonedDateTimeConverters.UtilDateToZonedDateTime().apply(from).toLocalDate();
    }

    @Converter(from = {Calendar.class, GregorianCalendar.class}, to = LocalDate.class)
    public Function<Calendar, LocalDate> CalendarToLocalDate() {
        return from -> zonedDateTimeConverters.CalendarToZonedDateTime().apply(from).toLocalDate();
    }

    @Converter(from = java.sql.Date.class, to = LocalDate.class)
    public Function<java.sql.Date, LocalDate> SQLDateToLocalDate() {
        return from -> zonedDateTimeConverters.SQLDateToZonedDateTime().apply(from).toLocalDate();
    }

    @Converter(from = Timestamp.class, to = LocalDate.class)
    public Function<Timestamp, LocalDate> SQLTimestampToLocalDate() {
        return from -> UtilDateToLocalDate().apply(from);
    }
}