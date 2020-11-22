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
public class JodaLocalDateConverters {

    @Autowired
    private Converters converters;

    @Converter(from = org.joda.time.LocalDate.class, to = LocalDate.class)
    public Function<org.joda.time.LocalDate, LocalDate> jodaLocalDateToLocalDate() {
        return from -> LocalDate.of(from.getYear(), from.getMonthOfYear(), from.getDayOfMonth());
    }

    @Converter(from = org.joda.time.LocalDate.class, to = LocalDateTime.class)
    public Function<org.joda.time.LocalDate, LocalDateTime> jodaLocalDateToLocalDateTime() {
        return from -> converters.from(jodaLocalDateToLocalDate().apply(from), LocalDateTime.class);
    }

    @Converter(from = org.joda.time.LocalDate.class, to = ZonedDateTime.class)
    public Function<org.joda.time.LocalDate, ZonedDateTime> jodaLocalDateToZonedDateTime() {
        return from -> converters.from(jodaLocalDateToLocalDateTime().apply(from), ZonedDateTime.class);
    }

    @Converter(from = org.joda.time.LocalDate.class, to = java.util.Date.class)
    public Function<org.joda.time.LocalDate, java.util.Date> jodaLocalDateToUtilDate() {
        return from -> converters.from(jodaLocalDateToLocalDateTime().apply(from), java.util.Date.class);
    }

    @Converter(from = org.joda.time.LocalDate.class, to = java.sql.Date.class)
    public Function<org.joda.time.LocalDate, java.sql.Date> jodaLocalDateToSQLDate() {
        return from -> converters.from(jodaLocalDateToLocalDateTime().apply(from), java.sql.Date.class);
    }

    @Converter(from = org.joda.time.LocalDate.class, to = XMLGregorianCalendar.class)
    public Function<org.joda.time.LocalDate, XMLGregorianCalendar> jodaLocalDateToXMLDate() {
        return from -> converters.from(jodaLocalDateToLocalDateTime().apply(from), XMLGregorianCalendar.class);
    }

    @Converter(from = org.joda.time.LocalDate.class, to = Timestamp.class)
    public Function<org.joda.time.LocalDate, Timestamp> jodaLocalDateToSQLTimestamp() {
        return from -> converters.from(jodaLocalDateToLocalDateTime().apply(from), Timestamp.class);
    }

    @Converter(from = org.joda.time.LocalDate.class, to = Calendar.class)
    public Function<org.joda.time.LocalDate, Calendar> jodaLocalDateToCalendar() {
        return from -> converters.from(jodaLocalDateToLocalDateTime().apply(from), Calendar.class);
    }

    @Converter(from = Calendar.class, to = org.joda.time.LocalDate.class)
    public Function<Calendar, org.joda.time.LocalDate> calendarToJodaLocalDate() {
        return from -> localDateToJodaLocalDate().apply(converters.from(from, LocalDate.class));
    }

    @Converter(from = XMLGregorianCalendar.class, to = org.joda.time.LocalDate.class)
    public Function<XMLGregorianCalendar, org.joda.time.LocalDate> xmlDateToJodaLocalDate() {
        return from -> localDateToJodaLocalDate().apply(converters.from(from, LocalDate.class));
    }

    @Converter(from = LocalDate.class, to = org.joda.time.LocalDate.class)
    public Function<LocalDate, org.joda.time.LocalDate> localDateToJodaLocalDate() {
        return from -> new org.joda.time.LocalDate(from.getYear(), from.getMonthValue(), from.getDayOfMonth());
    }

    @Converter(from = ZonedDateTime.class, to = org.joda.time.LocalDate.class)
    public Function<ZonedDateTime, org.joda.time.LocalDate> zonedDateTimeToJodaLocalDate() {
        return from -> localDateToJodaLocalDate().apply(converters.from(from, LocalDate.class));
    }

    @Converter(from = LocalDateTime.class, to = org.joda.time.LocalDate.class)
    public Function<LocalDateTime, org.joda.time.LocalDate> localDateTimeToJodaLocalDate() {
        return from -> localDateToJodaLocalDate().apply(converters.from(from, LocalDate.class));
    }

    @Converter(from = java.util.Date.class, to = org.joda.time.LocalDate.class)
    public Function<Date, org.joda.time.LocalDate> utilDateToJodaLocalDate() {
        return from -> localDateToJodaLocalDate().apply(converters.from(from, LocalDate.class));
    }

    @Converter(from = java.sql.Date.class, to = org.joda.time.LocalDate.class)
    public Function<java.sql.Date, org.joda.time.LocalDate> sqlDateToJodaLocalDate() {
        return from -> localDateToJodaLocalDate().apply(converters.from(from, LocalDate.class));
    }

    @Converter(from = Timestamp.class, to = org.joda.time.LocalDate.class)
    public Function<Timestamp, org.joda.time.LocalDate> sqlTimestampToJodaLocalDate() {
        return from -> localDateToJodaLocalDate().apply(converters.from(from, LocalDate.class));
    }

    @Converter(from = org.joda.time.LocalDate.class, to = org.joda.time.LocalDate.class)
    public Function<org.joda.time.LocalDate, org.joda.time.LocalDate> jodaLocalDateToJodaLocalDate() {
        return from -> from;
    }

    @Converter(from = org.joda.time.LocalDateTime.class, to = org.joda.time.LocalDate.class)
    public Function<org.joda.time.LocalDateTime, org.joda.time.LocalDate> jodaLocalDateTimetoJodaLocalDate() {
        return from -> from.toLocalDate();
    }

    @Converter(from = org.joda.time.DateTime.class, to = org.joda.time.LocalDate.class)
    public Function<org.joda.time.DateTime, org.joda.time.LocalDate> jodaDateTimetoJodaLocalDate() {
        return from -> from.toLocalDateTime().toLocalDate();
    }

}
