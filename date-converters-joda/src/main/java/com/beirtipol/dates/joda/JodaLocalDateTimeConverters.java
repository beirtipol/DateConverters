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

package com.beirtipol.dates.joda;

import com.beirtipol.dates.Converter;
import com.beirtipol.dates.Converters;
import org.joda.time.LocalTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.datatype.XMLGregorianCalendar;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.function.Function;

@Component
public class JodaLocalDateTimeConverters {

    @Autowired
    private Converters converters;

    @Converter(from = org.joda.time.LocalDateTime.class, to = LocalDate.class)
    public Function<org.joda.time.LocalDateTime, LocalDate> jodaLocalDateTimeToLocalDate() {
        return from -> converters.from(jodaLocalDateTimeToLocalDateTime().apply(from), LocalDate.class);
    }

    @Converter(from = org.joda.time.LocalDateTime.class, to = LocalDateTime.class)
    public Function<org.joda.time.LocalDateTime, LocalDateTime> jodaLocalDateTimeToLocalDateTime() {
        return from -> LocalDateTime.of(from.getYear(), from.getMonthOfYear(), from.getDayOfMonth(), from.getHourOfDay(), from.getMinuteOfHour(), from.getSecondOfMinute(), from.getMillisOfSecond() * 1000000);
    }

    @Converter(from = org.joda.time.LocalDateTime.class, to = ZonedDateTime.class)
    public Function<org.joda.time.LocalDateTime, ZonedDateTime> jodaLocalDateTimeToZonedDateTime() {
        return from -> converters.from(jodaLocalDateTimeToLocalDateTime().apply(from), ZonedDateTime.class);
    }

    @Converter(from = org.joda.time.LocalDateTime.class, to = Date.class)
    public Function<org.joda.time.LocalDateTime, Date> jodaLocalDateTimeToUtilDate() {
        return from -> converters.from(jodaLocalDateTimeToLocalDateTime().apply(from), Date.class);
    }

    @Converter(from = org.joda.time.LocalDateTime.class, to = java.sql.Date.class)
    public Function<org.joda.time.LocalDateTime, java.sql.Date> jodaLocalDateTimeToSQLDate() {
        return from -> converters.from(jodaLocalDateTimeToLocalDateTime().apply(from), java.sql.Date.class);
    }

    @Converter(from = org.joda.time.LocalDateTime.class, to = XMLGregorianCalendar.class)
    public Function<org.joda.time.LocalDateTime, XMLGregorianCalendar> jodaLocalDateTimeToXMLDate() {
        return from -> converters.from(jodaLocalDateTimeToLocalDateTime().apply(from), XMLGregorianCalendar.class);
    }

    @Converter(from = org.joda.time.LocalDateTime.class, to = Timestamp.class)
    public Function<org.joda.time.LocalDateTime, Timestamp> jodaLocalDateTimeToSQLTimestamp() {
        return from -> converters.from(jodaLocalDateTimeToLocalDateTime().apply(from), Timestamp.class);
    }

    @Converter(from = org.joda.time.LocalDateTime.class, to = Calendar.class)
    public Function<org.joda.time.LocalDateTime, Calendar> jodaLocalDateTimeToCalendar() {
        return from -> converters.from(jodaLocalDateTimeToLocalDateTime().apply(from), Calendar.class);
    }

    @Converter(from = Calendar.class, to = org.joda.time.LocalDateTime.class)
    public Function<Calendar, org.joda.time.LocalDateTime> calendartoJodaLocalDateTime() {
        return from -> localDateTimeToJodaLocalDateTime().apply(converters.from(from, LocalDateTime.class));
    }

    @Converter(from = XMLGregorianCalendar.class, to = org.joda.time.LocalDateTime.class)
    public Function<XMLGregorianCalendar, org.joda.time.LocalDateTime> xmlDatetoJodaLocalDateTime() {
        return from -> localDateTimeToJodaLocalDateTime().apply(converters.from(from, LocalDateTime.class));
    }

    @Converter(from = LocalDate.class, to = org.joda.time.LocalDateTime.class)
    public Function<LocalDate, org.joda.time.LocalDateTime> localDateTimetoJodaLocalDateTime() {
        return from -> localDateTimeToJodaLocalDateTime().apply(converters.from(from, LocalDateTime.class));
    }

    @Converter(from = ZonedDateTime.class, to = org.joda.time.LocalDateTime.class)
    public Function<ZonedDateTime, org.joda.time.LocalDateTime> zonedDateTimetoJodaLocalDateTime() {
        return from -> localDateTimeToJodaLocalDateTime().apply(converters.from(from, LocalDateTime.class));
    }

    @Converter(from = LocalDateTime.class, to = org.joda.time.LocalDateTime.class)
    public Function<LocalDateTime, org.joda.time.LocalDateTime> localDateTimeToJodaLocalDateTime() {
        return from -> new org.joda.time.LocalDateTime(from.getYear(), from.getMonthValue(), from.getDayOfMonth(), from.getHour(), from.getMinute(), from.getSecond(), from.getNano() / 1000000);
    }

    @Converter(from = Date.class, to = org.joda.time.LocalDateTime.class)
    public Function<Date, org.joda.time.LocalDateTime> utilDatetoJodaLocalDateTime() {
        return from -> localDateTimeToJodaLocalDateTime().apply(converters.from(from, LocalDateTime.class));
    }

    @Converter(from = java.sql.Date.class, to = org.joda.time.LocalDateTime.class)
    public Function<java.sql.Date, org.joda.time.LocalDateTime> sqlDatetoJodaLocalDateTime() {
        return from -> localDateTimeToJodaLocalDateTime().apply(converters.from(from, LocalDateTime.class));
    }

    @Converter(from = Timestamp.class, to = org.joda.time.LocalDateTime.class)
    public Function<Timestamp, org.joda.time.LocalDateTime> sqlTimestamptoJodaLocalDateTime() {
        return from -> localDateTimeToJodaLocalDateTime().apply(converters.from(from, LocalDateTime.class));
    }

    @Converter(from = org.joda.time.LocalDateTime.class, to = org.joda.time.LocalDateTime.class)
    public Function<org.joda.time.LocalDateTime, org.joda.time.LocalDateTime> jodaLocalDateTimetoJodaLocalDateTime() {
        return from -> from;
    }

    @Converter(from = org.joda.time.LocalDate.class, to = org.joda.time.LocalDateTime.class)
    public Function<org.joda.time.LocalDate, org.joda.time.LocalDateTime> jodaLocalDatetoJodaLocalDateTime() {
        return from -> from.toLocalDateTime(LocalTime.MIDNIGHT);
    }

    @Converter(from = org.joda.time.DateTime.class, to = org.joda.time.LocalDateTime.class)
    public Function<org.joda.time.DateTime, org.joda.time.LocalDateTime> jodaDateTimetoJodaLocalDateTime() {
        return from -> from.toLocalDateTime();
    }

}
