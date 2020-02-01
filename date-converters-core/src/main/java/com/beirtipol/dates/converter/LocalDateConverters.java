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
 * As {@link LocalDate} does not have any TimeZone information, it does not try to do anything clever with the temporals
 * it attempts to parse. Where possible, it will extract the year/month/day parts of the temporal.
 * 
 * @author beirtipol@gmail.com
 *
 */
@Component
public class LocalDateConverters {

	@Bean
	@Converter(from = XMLGregorianCalendar.class, to = LocalDate.class)
	public Function<XMLGregorianCalendar, LocalDate> XMLDateToLocalDate() {
		return from -> ZonedDateTimeToLocalDate().apply(from.toGregorianCalendar().toZonedDateTime());
	}

	@Bean
	@Converter(from = LocalDate.class, to = LocalDate.class)
	public Function<LocalDate, LocalDate> LocalDateToLocalDate() {
		return from -> from;
	}

	@Bean
	@Converter(from = ZonedDateTime.class, to = LocalDate.class)
	public Function<ZonedDateTime, LocalDate> ZonedDateTimeToLocalDate() {
		return ZonedDateTime::toLocalDate;
	}

	@Bean
	@Converter(from = LocalDateTime.class, to = LocalDate.class)
	public Function<LocalDateTime, LocalDate> LocalDateTimeToLocalDate() {
		return LocalDateTime::toLocalDate;
	}

	@Bean
	@Converter(from = java.util.Date.class, to = LocalDate.class)
	public Function<Date, LocalDate> UtilDateToLocalDate() {
		return from -> from.toInstant().atZone(ThreeTenDates.UTC).toLocalDate();
	}

	@Bean
	@Converter(from = { Calendar.class, GregorianCalendar.class }, to = LocalDate.class)
	public Function<Calendar, LocalDate> CalendarToLocalDate() {
		return from -> from.toInstant().atZone(ThreeTenDates.UTC).toLocalDate();
	}

	@Bean
	@Converter(from = java.sql.Date.class, to = LocalDate.class)
	public Function<java.sql.Date, LocalDate> SQLDateToLocalDate() {
		return from -> Instant.ofEpochMilli(from.getTime()).atZone(ThreeTenDates.UTC).toLocalDate();
	}

	@Bean
	@Converter(from = Timestamp.class, to = LocalDate.class)
	public Function<Timestamp, LocalDate> SQLTimestampToLocalDate() {
		return from -> UtilDateToLocalDate().apply(from);
	}

}
