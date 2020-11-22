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
import org.springframework.beans.factory.annotation.Autowired;
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
 * This contains conversions for {@link java.util.Date}, {@link java.sql.Date} and {@link Timestamp} as they share a
 * hierarchy and some common code
 *
 * @author beirtipol@gmail.com
 */
@Component
public class UtilDateConverters {
    @Autowired
    private CalendarConverters calendarConverters;

    @Converter(from = XMLGregorianCalendar.class, to = Date.class)
    public Function<XMLGregorianCalendar, Date> XMLDateToUtilDate() {
        return from -> calendarConverters.XMLGregorianCalendarToCalendar().apply(from).getTime();
    }

    @Converter(from = LocalDate.class, to = Date.class)
    public Function<LocalDate, Date> LocalDateToUtilDate() {
        return from -> LocalDateTimeToUtilDate().apply(from.atStartOfDay());
    }

    @Converter(from = ZonedDateTime.class, to = Date.class)
    public Function<ZonedDateTime, Date> ZonedDateTimeToUtilDate() {
        return from -> Date.from(calendarConverters.ZonedDateTimeToCalendar().apply(from).toInstant());
    }

    @Converter(from = LocalDateTime.class, to = Date.class)
    public Function<LocalDateTime, Date> LocalDateTimeToUtilDate() {
        return from -> ZonedDateTimeToUtilDate().apply(from.atZone(ThreeTenDates.UTC));
    }

    @Converter(from = java.util.Date.class, to = Date.class)
    public Function<Date, Date> UtilDateToUtilDate() {
        return from -> from;
    }

    @Converter(from = {Calendar.class, GregorianCalendar.class}, to = java.util.Date.class)
    public Function<Calendar, Date> CalendarToUtilDate() {
        return Calendar::getTime;
    }

    @Converter(from = {Calendar.class, GregorianCalendar.class}, to = java.sql.Date.class)
    public Function<Calendar, java.sql.Date> CalendarToSqlDate() {
        return from -> UtilDateToSQLDate().apply(from.getTime());
    }

    @Converter(from = {Calendar.class, GregorianCalendar.class}, to = java.sql.Timestamp.class)
    public Function<Calendar, java.sql.Timestamp> CalendarToSqlTimestamp() {
        return from -> UtilDateToSQLTimestamp().apply(from.getTime());
    }

    @Converter(from = java.sql.Date.class, to = Date.class)
    public Function<java.sql.Date, Date> SQLDateToUtilDate() {
        return from -> from;
    }

    @Converter(from = Timestamp.class, to = Date.class)
    public Function<Timestamp, Date> SQLTimestampToUtilDate() {
        return from -> UtilDateToUtilDate().apply(from);
    }

    @Converter(from = XMLGregorianCalendar.class, to = java.sql.Date.class)
    public Function<XMLGregorianCalendar, java.sql.Date> XMLDateToSQLDate() {
        return from -> UtilDateToSQLDate().apply(XMLDateToUtilDate().apply(from));
    }

    @Converter(from = LocalDate.class, to = java.sql.Date.class)
    public Function<LocalDate, java.sql.Date> LocalDateToSQLDate() {
        return from -> LocalDateTimeToSQLDate().apply(from.atStartOfDay());
    }

    @Converter(from = ZonedDateTime.class, to = java.sql.Date.class)
    public Function<ZonedDateTime, java.sql.Date> ZonedDateTimeToSQLDate() {
        return from -> UtilDateToSQLDate().apply(ZonedDateTimeToUtilDate().apply(from));
    }

    @Converter(from = LocalDateTime.class, to = java.sql.Date.class)
    public Function<LocalDateTime, java.sql.Date> LocalDateTimeToSQLDate() {
        return from -> ZonedDateTimeToSQLDate().apply(from.atZone(ThreeTenDates.UTC));
    }

    @Converter(from = java.util.Date.class, to = java.sql.Date.class)
    public Function<Date, java.sql.Date> UtilDateToSQLDate() {
        return from -> new java.sql.Date(from.getTime());
    }

    @Converter(from = java.sql.Date.class, to = java.sql.Date.class)
    public Function<java.sql.Date, java.sql.Date> SQLDateToSQLDate() {
        return from -> from;
    }

    @Converter(from = Timestamp.class, to = java.sql.Date.class)
    public Function<Timestamp, java.sql.Date> SQLTimestampToSQLDate() {
        return from -> UtilDateToSQLDate().apply(from);
    }

    @Converter(from = XMLGregorianCalendar.class, to = Timestamp.class)
    public Function<XMLGregorianCalendar, Timestamp> XMLDateToSQLTimestamp() {
        return from -> ZonedDateTimeToSQLTimestamp().apply(from.toGregorianCalendar().toZonedDateTime());
    }

    @Converter(from = LocalDate.class, to = Timestamp.class)
    public Function<LocalDate, Timestamp> LocalDateToSQLTimestamp() {
        return from -> LocalDateTimeToSQLTimestamp().apply(from.atStartOfDay());
    }

    @Converter(from = ZonedDateTime.class, to = Timestamp.class)
    public Function<ZonedDateTime, Timestamp> ZonedDateTimeToSQLTimestamp() {
        return from -> Timestamp.from(ZonedDateTimeToUtilDate().apply(from).toInstant());
    }

    @Converter(from = LocalDateTime.class, to = Timestamp.class)
    public Function<LocalDateTime, Timestamp> LocalDateTimeToSQLTimestamp() {
        return from -> ZonedDateTimeToSQLTimestamp().apply(from.atZone(ThreeTenDates.UTC));
    }

    @Converter(from = java.util.Date.class, to = Timestamp.class)
    public Function<Date, Timestamp> UtilDateToSQLTimestamp() {
        return from -> Timestamp.from(from.toInstant());
    }

    @Converter(from = java.sql.Date.class, to = Timestamp.class)
    public Function<java.sql.Date, Timestamp> SQLDateToSQLTimestamp() {
        return from -> new Timestamp(from.getTime());
    }

    @Converter(from = Timestamp.class, to = Timestamp.class)
    public Function<Timestamp, Timestamp> SQLTimestampToSQLTimestamp() {
        return from -> from;
    }

}
