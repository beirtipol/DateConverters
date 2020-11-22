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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
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
 * Converters for {@link XMLGregorianCalendar}. I despise this class almost as much as the core {@link Calendar}
 *
 * @author beirtipol@gmail.com
 */
@Component
public class XMLDateConverters {
    private static final Logger LOG = LoggerFactory.getLogger(XMLDateConverters.class);

    @Autowired
    private ZonedDateTimeConverters zonedDateTimeConverter;

    private DatatypeFactory dt;

    public XMLDateConverters() {
        try {
            dt = DatatypeFactory.newInstance();
        } catch (DatatypeConfigurationException e) {
            LOG.error("SEVERE: Cannot instantiate DatatypeFactory.", e);
        }
    }

    @Converter(from = XMLGregorianCalendar.class, to = XMLGregorianCalendar.class)
    public Function<XMLGregorianCalendar, XMLGregorianCalendar> XMLDateToXMLGregorianCalendar() {
        return from -> ZonedDateTimeToXMLGregorianCalendar().apply(from.toGregorianCalendar().toZonedDateTime());
    }

    @Converter(from = LocalDate.class, to = XMLGregorianCalendar.class)
    public Function<LocalDate, XMLGregorianCalendar> LocalDateToXMLGregorianCalendar() {
        return from -> LocalDateTimeToXMLGregorianCalendar().apply(from.atStartOfDay());
    }

    @Converter(from = ZonedDateTime.class, to = XMLGregorianCalendar.class)
    public Function<ZonedDateTime, XMLGregorianCalendar> ZonedDateTimeToXMLGregorianCalendar() {
        return from -> dt.newXMLGregorianCalendar(GregorianCalendar.from(from));
    }

    @Converter(from = LocalDateTime.class, to = XMLGregorianCalendar.class)
    public Function<LocalDateTime, XMLGregorianCalendar> LocalDateTimeToXMLGregorianCalendar() {
        return from -> ZonedDateTimeToXMLGregorianCalendar().apply(from.atZone(ThreeTenDates.UTC));
    }

    @Converter(from = java.util.Date.class, to = XMLGregorianCalendar.class)
    public Function<Date, XMLGregorianCalendar> UtilDateToXMLGregorianCalendar() {
        return from -> dt.newXMLGregorianCalendar(GregorianCalendar.from(zonedDateTimeConverter.UtilDateToZonedDateTime().apply(from)));
    }

    @Converter(from = {Calendar.class, GregorianCalendar.class}, to = XMLGregorianCalendar.class)
    public Function<Calendar, XMLGregorianCalendar> CalendarToXMLGregorianCalendar() {
        return from -> {
            GregorianCalendar cal = new GregorianCalendar(from.getTimeZone());
            cal.setTime(from.getTime());
            return dt.newXMLGregorianCalendar(cal);
        };
    }

    @Converter(from = java.sql.Date.class, to = XMLGregorianCalendar.class)
    public Function<java.sql.Date, XMLGregorianCalendar> SQLDateToXMLGregorianCalendar() {
        return from -> dt.newXMLGregorianCalendar(GregorianCalendar.from(zonedDateTimeConverter.SQLDateToZonedDateTime().apply(from)));
    }

    @Converter(from = Timestamp.class, to = XMLGregorianCalendar.class)
    public Function<Timestamp, XMLGregorianCalendar> SQLTimestampToXMLGregorianCalendar() {
        return from -> UtilDateToXMLGregorianCalendar().apply(from);
    }

}
