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

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.function.Function;

import javax.xml.datatype.XMLGregorianCalendar;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.beirtipol.dates.Converter;
import com.beirtipol.dates.ThreeTenDates;

/**
 * As {@link LocalDateTime} does not have any TimeZone information, it does not try to do anything clever with the
 * temporals it attempts to parse. Where possible, it will extract the year/month/day parts of the temporal.
 *
 * @author beirtipol@gmail.com
 */
@Component
public class LocalDateTimeConverters {

    @Bean
    @Converter(from = XMLGregorianCalendar.class, to = LocalDateTime.class)
    public Function<XMLGregorianCalendar, LocalDateTime> XMLDateToLocalDateTime() {
        return from -> ZonedDateTimeToLocalDateTime().apply(from.toGregorianCalendar().toZonedDateTime());
    }

    @Bean
    @Converter(from = LocalDate.class, to = LocalDateTime.class)
    public Function<LocalDate, LocalDateTime> LocalDateToLocalDateTime() {
        return LocalDate::atStartOfDay;
    }

    @Bean
    @Converter(from = ZonedDateTime.class, to = LocalDateTime.class)
    public Function<ZonedDateTime, LocalDateTime> ZonedDateTimeToLocalDateTime() {
        return ZonedDateTime::toLocalDateTime;
    }

    @Bean
    @Converter(from = LocalDateTime.class, to = LocalDateTime.class)
    public Function<LocalDateTime, LocalDateTime> LocalDateTimeTimeToLocalDateTime() {
        return from -> from;
    }

    @Bean
    @Converter(from = java.util.Date.class, to = LocalDateTime.class)
    public Function<Date, LocalDateTime> UtilDateToLocalDateTime() {
        return from -> from.toInstant().atZone(ThreeTenDates.UTC).toLocalDateTime();
    }

    @Bean
    @Converter(from = {Calendar.class, GregorianCalendar.class}, to = LocalDateTime.class)
    public Function<Calendar, LocalDateTime> CalendarToLocalDateTime() {
        return from -> from.toInstant().atZone(ThreeTenDates.UTC).toLocalDateTime();
    }

    @Bean
    @Converter(from = java.sql.Date.class, to = LocalDateTime.class)
    public Function<java.sql.Date, LocalDateTime> SQLDateToLocalDateTime() {
        return from -> LocalDateTime.ofInstant(Instant.ofEpochMilli(from.getTime()), ThreeTenDates.UTC);
    }

    @Bean
    @Converter(from = Timestamp.class, to = LocalDateTime.class)
    public Function<Timestamp, LocalDateTime> SQLTimestampToLocalDateTime() {
        return from -> UtilDateToLocalDateTime().apply(from);
    }

}
