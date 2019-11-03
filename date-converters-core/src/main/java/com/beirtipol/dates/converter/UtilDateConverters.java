package com.beirtipol.dates.converter;

import java.sql.Timestamp;
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
 * This contains conversions for {@link java.util.Date}, {@link java.sql.Date} and {@link Timestamp} as they share a
 * hierarchy and some common code
 * 
 * @author beirtipol@gmail.com
 *
 */
@Component
public class UtilDateConverters {

	@Bean
	@Converter(from = XMLGregorianCalendar.class, to = Date.class)
	public Function<XMLGregorianCalendar, Date> XMLDateToUtilDate() {
		return from -> from.toGregorianCalendar().getTime();
	}

	@Bean
	@Converter(from = LocalDate.class, to = Date.class)
	public Function<LocalDate, Date> LocalDateToUtilDate() {
		return from -> LocalDateTimeToUtilDate().apply(from.atStartOfDay());
	}

	@Bean
	@Converter(from = ZonedDateTime.class, to = Date.class)
	public Function<ZonedDateTime, Date> ZonedDateTimeToUtilDate() {
		return from -> Date.from(from.toInstant());
	}

	@Bean
	@Converter(from = LocalDateTime.class, to = Date.class)
	public Function<LocalDateTime, Date> LocalDateTimeToUtilDate() {
		return from -> ZonedDateTimeToUtilDate().apply(from.atZone(ThreeTenDates.UTC));
	}

	@Bean
	@Converter(from = java.util.Date.class, to = Date.class)
	public Function<Date, Date> UtilDateToUtilDate() {
		return from -> from;
	}

	@Bean
	@Converter(from = { Calendar.class, GregorianCalendar.class }, to = java.util.Date.class)
	public Function<Calendar, Date> CalendarToUtilDate() {
		return from -> from.getTime();
	}

	@Bean
	@Converter(from = { Calendar.class, GregorianCalendar.class }, to = java.sql.Date.class)
	public Function<Calendar, java.sql.Date> CalendarToSqlDate() {
		return from -> UtilDateToSQLDate().apply(from.getTime());
	}

	@Bean
	@Converter(from = { Calendar.class, GregorianCalendar.class }, to = java.sql.Timestamp.class)
	public Function<Calendar, java.sql.Timestamp> CalendarToSqlTimestamp() {
		return from -> UtilDateToSQLTimestamp().apply(from.getTime());
	}

	@Bean
	@Converter(from = java.sql.Date.class, to = Date.class)
	public Function<java.sql.Date, Date> SQLDateToUtilDate() {
		return from -> from;
	}

	@Bean
	@Converter(from = Timestamp.class, to = Date.class)
	public Function<Timestamp, Date> SQLTimestampToUtilDate() {
		return from -> UtilDateToUtilDate().apply(from);
	}

	@Bean
	@Converter(from = XMLGregorianCalendar.class, to = java.sql.Date.class)
	public Function<XMLGregorianCalendar, java.sql.Date> XMLDateToSQLDate() {
		return from -> UtilDateToSQLDate().apply(XMLDateToUtilDate().apply(from));
	}

	@Bean
	@Converter(from = LocalDate.class, to = java.sql.Date.class)
	public Function<LocalDate, java.sql.Date> LocalDateToSQLDate() {
		return from -> LocalDateTimeToSQLDate().apply(from.atStartOfDay());
	}

	@Bean
	@Converter(from = ZonedDateTime.class, to = java.sql.Date.class)
	public Function<ZonedDateTime, java.sql.Date> ZonedDateTimeToSQLDate() {
		return from -> UtilDateToSQLDate().apply(ZonedDateTimeToUtilDate().apply(from));
	}

	@Bean
	@Converter(from = LocalDateTime.class, to = java.sql.Date.class)
	public Function<LocalDateTime, java.sql.Date> LocalDateTimeToSQLDate() {
		return from -> ZonedDateTimeToSQLDate().apply(from.atZone(ThreeTenDates.UTC));
	}

	@Bean
	@Converter(from = java.util.Date.class, to = java.sql.Date.class)
	public Function<Date, java.sql.Date> UtilDateToSQLDate() {
		return from -> new java.sql.Date(from.getTime());
	}

	@Bean
	@Converter(from = java.sql.Date.class, to = java.sql.Date.class)
	public Function<java.sql.Date, java.sql.Date> SQLDateToSQLDate() {
		return from -> from;
	}

	@Bean
	@Converter(from = Timestamp.class, to = java.sql.Date.class)
	public Function<Timestamp, java.sql.Date> SQLTimestampToSQLDate() {
		return from -> UtilDateToSQLDate().apply(from);
	}

	@Bean
	@Converter(from = XMLGregorianCalendar.class, to = Timestamp.class)
	public Function<XMLGregorianCalendar, Timestamp> XMLDateToSQLTimestamp() {
		return from -> ZonedDateTimeToSQLTimestamp().apply(from.toGregorianCalendar().toZonedDateTime());
	}

	@Bean
	@Converter(from = LocalDate.class, to = Timestamp.class)
	public Function<LocalDate, Timestamp> LocalDateToSQLTimestamp() {
		return from -> LocalDateTimeToSQLTimestamp().apply(from.atStartOfDay());
	}

	@Bean
	@Converter(from = ZonedDateTime.class, to = Timestamp.class)
	public Function<ZonedDateTime, Timestamp> ZonedDateTimeToSQLTimestamp() {
		return from -> Timestamp.from(ZonedDateTimeToUtilDate().apply(from).toInstant());
	}

	@Bean
	@Converter(from = LocalDateTime.class, to = Timestamp.class)
	public Function<LocalDateTime, Timestamp> LocalDateTimeToSQLTimestamp() {
		return from -> ZonedDateTimeToSQLTimestamp().apply(from.atZone(ThreeTenDates.UTC));
	}

	@Bean
	@Converter(from = java.util.Date.class, to = Timestamp.class)
	public Function<Date, Timestamp> UtilDateToSQLTimestamp() {
		return from -> Timestamp.from(from.toInstant());
	}

	@Bean
	@Converter(from = java.sql.Date.class, to = Timestamp.class)
	public Function<java.sql.Date, Timestamp> SQLDateToSQLTimestamp() {
		return from -> new Timestamp(from.getTime());
	}

	@Bean
	@Converter(from = Timestamp.class, to = Timestamp.class)
	public Function<Timestamp, Timestamp> SQLTimestampToSQLTimestamp() {
		return from -> from;
	}

}
