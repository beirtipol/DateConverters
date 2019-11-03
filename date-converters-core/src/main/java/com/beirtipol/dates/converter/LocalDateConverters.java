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
		return from -> LocalDate.ofInstant(Instant.ofEpochMilli(from.getTime()), ThreeTenDates.UTC);
	}

	@Bean
	@Converter(from = Timestamp.class, to = LocalDate.class)
	public Function<Timestamp, LocalDate> SQLTimestampToLocalDate() {
		return from -> UtilDateToLocalDate().apply(from);
	}

}
