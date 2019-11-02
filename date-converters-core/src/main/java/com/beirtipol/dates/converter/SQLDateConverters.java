package com.beirtipol.dates.converter;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.function.Function;

import javax.xml.datatype.XMLGregorianCalendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.beirtipol.dates.Converter;
import com.beirtipol.dates.ThreeTenDates;

/**
 * 
 * @author beirtipol@gmail.com
 *
 */
@Component
public class SQLDateConverters {

	@Autowired
	private UtilDateConverters utilDateConverter;

	@Bean
	@Converter(from = XMLGregorianCalendar.class, to = java.sql.Date.class)
	public Function<XMLGregorianCalendar, java.sql.Date> XMLDateToSQLDate() {
		return from -> UtilDateToSQLDate().apply(utilDateConverter.XMLDateToUtilDate().apply(from));
	}

	@Bean
	@Converter(from = LocalDate.class, to = java.sql.Date.class)
	public Function<LocalDate, java.sql.Date> LocalDateToSQLDate() {
		return from -> LocalDateTimeToSQLDate().apply(from.atStartOfDay());
	}

	@Bean
	@Converter(from = ZonedDateTime.class, to = java.sql.Date.class)
	public Function<ZonedDateTime, java.sql.Date> ZonedDateTimeToSQLDate() {
		return from -> UtilDateToSQLDate().apply(utilDateConverter.ZonedDateTimeToUtilDate().apply(from));
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

}
