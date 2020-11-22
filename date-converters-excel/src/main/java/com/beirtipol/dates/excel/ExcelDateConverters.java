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

package com.beirtipol.dates.excel;

import com.beirtipol.dates.Converter;
import com.beirtipol.dates.Converters;
import org.apache.poi.ss.usermodel.DateUtil;
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
public class ExcelDateConverters {

    @Autowired
    private Converters converters;

    @Converter(from = Double.class, to = LocalDate.class)
    public Function<Double, LocalDate> DoubleToLocalDate() {
        return from -> DateUtil.getLocalDateTime(from).toLocalDate();
    }

    @Converter(from = Double.class, to = LocalDateTime.class)
    public Function<Double, LocalDateTime> DoubleToLocalDateTime() {
        return DateUtil::getLocalDateTime;
    }

    @Converter(from = Double.class, to = ZonedDateTime.class)
    public Function<Double, ZonedDateTime> DoubleToZonedDateTime() {
        return from -> converters.from(DoubleToLocalDateTime().apply(from), ZonedDateTime.class);
    }

    @Converter(from = Double.class, to = java.util.Date.class)
    public Function<Double, java.util.Date> DoubleToUtilDate() {
        return from -> converters.from(DoubleToLocalDateTime().apply(from), java.util.Date.class);
    }

    @Converter(from = Double.class, to = java.sql.Date.class)
    public Function<Double, java.sql.Date> DoubleToSQLDate() {
        return from -> converters.from(DoubleToLocalDateTime().apply(from), java.sql.Date.class);
    }

    @Converter(from = Double.class, to = XMLGregorianCalendar.class)
    public Function<Double, XMLGregorianCalendar> DoubleToXMLDate() {
        return from -> converters.from(DoubleToLocalDateTime().apply(from), XMLGregorianCalendar.class);
    }

    @Converter(from = Double.class, to = Timestamp.class)
    public Function<Double, Timestamp> DoubleToSQLTimestamp() {
        return from -> converters.from(DoubleToLocalDateTime().apply(from), Timestamp.class);
    }

    @Converter(from = Double.class, to = Calendar.class)
    public Function<Double, Calendar> DoubleToCalendar() {
        return from -> converters.from(DoubleToLocalDateTime().apply(from), Calendar.class);
    }

    @Converter(from = Calendar.class, to = Double.class)
    public Function<Calendar, Double> CalendarToDouble() {
        return from -> DateUtil.getExcelDate(from, false);
    }

    @Converter(from = XMLGregorianCalendar.class, to = Double.class)
    public Function<XMLGregorianCalendar, Double> XMLDateToDouble() {
        return from -> DateUtil.getExcelDate(converters.from(from, Calendar.class), false);
    }

    @Converter(from = LocalDate.class, to = Double.class)
    public Function<LocalDate, Double> LocalDateToDouble() {
        return DateUtil::getExcelDate;
    }

    @Converter(from = ZonedDateTime.class, to = Double.class)
    public Function<ZonedDateTime, Double> ZonedDateTimeToDouble() {
        return from -> DateUtil.getExcelDate(from.toLocalDateTime());
    }

    @Converter(from = LocalDateTime.class, to = Double.class)
    public Function<LocalDateTime, Double> LocalDateTimeToDouble() {
        return DateUtil::getExcelDate;
    }

    @Converter(from = java.util.Date.class, to = Double.class)
    public Function<Date, Double> UtilDateToDouble() {
        return from -> DateUtil.getExcelDate(converters.from(from, LocalDateTime.class));
    }

    @Converter(from = java.sql.Date.class, to = Double.class)
    public Function<java.sql.Date, Double> SQLDateToDouble() {
        return from -> DateUtil.getExcelDate(converters.from(from, LocalDateTime.class));
    }

    @Converter(from = Timestamp.class, to = Double.class)
    public Function<Timestamp, Double> SQLTimestampToDouble() {
        return from -> DateUtil.getExcelDate(converters.from(from, LocalDateTime.class));
    }

    @Converter(from = Double.class, to = Double.class)
    public Function<Double, Double> DoubleToDouble() {
        return from -> from;
    }

}
