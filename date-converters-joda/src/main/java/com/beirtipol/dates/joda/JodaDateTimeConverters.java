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
import org.joda.time.DateTimeZone;
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
public class JodaDateTimeConverters {

    @Autowired
    private Converters converters;

    @Converter(from = org.joda.time.DateTime.class, to = LocalDate.class)
    public Function<org.joda.time.DateTime, LocalDate> jodaDateTimeToLocalDate() {
        return from -> converters.from(jodaDateTimeToDateTime().apply(from), LocalDate.class);
    }

    @Converter(from = org.joda.time.DateTime.class, to = LocalDateTime.class)
    public Function<org.joda.time.DateTime, LocalDateTime> jodaDateTimeToDateTime() {
        return from -> LocalDateTime.of(from.getYear(), from.getMonthOfYear(), from.getDayOfMonth(), from.getHourOfDay(), from.getMinuteOfHour(), from.getSecondOfMinute(), from.getMillisOfSecond() * 1000000);
    }

    @Converter(from = org.joda.time.DateTime.class, to = ZonedDateTime.class)
    public Function<org.joda.time.DateTime, ZonedDateTime> jodaDateTimeToZonedDateTime() {
        return from -> converters.from(jodaDateTimeToDateTime().apply(from), ZonedDateTime.class);
    }

    @Converter(from = org.joda.time.DateTime.class, to = Date.class)
    public Function<org.joda.time.DateTime, Date> jodaDateTimeToUtilDate() {
        return from -> converters.from(jodaDateTimeToDateTime().apply(from), Date.class);
    }

    @Converter(from = org.joda.time.DateTime.class, to = java.sql.Date.class)
    public Function<org.joda.time.DateTime, java.sql.Date> jodaDateTimeToSQLDate() {
        return from -> converters.from(jodaDateTimeToDateTime().apply(from), java.sql.Date.class);
    }

    @Converter(from = org.joda.time.DateTime.class, to = XMLGregorianCalendar.class)
    public Function<org.joda.time.DateTime, XMLGregorianCalendar> jodaDateTimeToXMLDate() {
        return from -> converters.from(jodaDateTimeToDateTime().apply(from), XMLGregorianCalendar.class);
    }

    @Converter(from = org.joda.time.DateTime.class, to = Timestamp.class)
    public Function<org.joda.time.DateTime, Timestamp> jodaDateTimeToSQLTimestamp() {
        return from -> converters.from(jodaDateTimeToDateTime().apply(from), Timestamp.class);
    }

    @Converter(from = org.joda.time.DateTime.class, to = Calendar.class)
    public Function<org.joda.time.DateTime, Calendar> jodaDateTimeToCalendar() {
        return from -> converters.from(jodaDateTimeToDateTime().apply(from), Calendar.class);
    }

    @Converter(from = Calendar.class, to = org.joda.time.DateTime.class)
    public Function<Calendar, org.joda.time.DateTime> calendartoJodaDateTime() {
        return from -> zonedDateTimeToJodaDateTime().apply(converters.from(from, ZonedDateTime.class));
    }

    @Converter(from = XMLGregorianCalendar.class, to = org.joda.time.DateTime.class)
    public Function<XMLGregorianCalendar, org.joda.time.DateTime> xmlDatetoJodaDateTime() {
        return from -> zonedDateTimeToJodaDateTime().apply(converters.from(from, ZonedDateTime.class));
    }

    @Converter(from = LocalDate.class, to = org.joda.time.DateTime.class)
    public Function<LocalDate, org.joda.time.DateTime> localDateTimetoJodaDateTime() {
        return from -> zonedDateTimeToJodaDateTime().apply(converters.from(from, ZonedDateTime.class));
    }

    @Converter(from = ZonedDateTime.class, to = org.joda.time.DateTime.class)
    public Function<ZonedDateTime, org.joda.time.DateTime> zonedDateTimeToJodaDateTime() {
        return from -> new org.joda.time.DateTime(from.getYear(), from.getMonthValue(), from.getDayOfMonth(), from.getHour(), from.getMinute(), from.getSecond(), from.getNano() / 1000000, DateTimeZone.forID(from.getZone().toString()));
    }

    @Converter(from = LocalDateTime.class, to = org.joda.time.DateTime.class)
    public Function<LocalDateTime, org.joda.time.DateTime> localDateTimeToJodaDateTime() {
        return from -> zonedDateTimeToJodaDateTime().apply(converters.from(from, ZonedDateTime.class));
    }

    @Converter(from = Date.class, to = org.joda.time.DateTime.class)
    public Function<Date, org.joda.time.DateTime> utilDatetoJodaDateTime() {
        return from -> zonedDateTimeToJodaDateTime().apply(converters.from(from, ZonedDateTime.class));
    }

    @Converter(from = java.sql.Date.class, to = org.joda.time.DateTime.class)
    public Function<java.sql.Date, org.joda.time.DateTime> sqlDatetoJodaDateTime() {
        return from -> zonedDateTimeToJodaDateTime().apply(converters.from(from, ZonedDateTime.class));
    }

    @Converter(from = Timestamp.class, to = org.joda.time.DateTime.class)
    public Function<Timestamp, org.joda.time.DateTime> sqlTimestamptoJodaDateTime() {
        return from -> zonedDateTimeToJodaDateTime().apply(converters.from(from, ZonedDateTime.class));
    }

    @Converter(from = org.joda.time.DateTime.class, to = org.joda.time.DateTime.class)
    public Function<org.joda.time.DateTime, org.joda.time.DateTime> jodaDateTimetoJodaDateTime() {
        return from -> from;
    }

    @Converter(from = org.joda.time.LocalDate.class, to = org.joda.time.DateTime.class)
    public Function<org.joda.time.LocalDate, org.joda.time.DateTime> jodaLocalDatetoJodaDateTime() {
        return from -> from.toLocalDateTime(LocalTime.MIDNIGHT).toDateTime().withZoneRetainFields(DateTimeZone.UTC);
    }

    @Converter(from = org.joda.time.LocalDateTime.class, to = org.joda.time.DateTime.class)
    public Function<org.joda.time.LocalDateTime, org.joda.time.DateTime> jodaLocalDateTimetoJodaDateTime() {
        return from -> from.toDateTime().withZoneRetainFields(DateTimeZone.UTC);
    }

}
