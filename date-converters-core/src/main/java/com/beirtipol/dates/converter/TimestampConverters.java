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
public class TimestampConverters {
	@Autowired
	private UtilDateConverters utilDateConverter;

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
		return from -> Timestamp.from(utilDateConverter.ZonedDateTimeToUtilDate().apply(from).toInstant());
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
		return from -> Timestamp.from(from.toInstant());
	}

	@Bean
	@Converter(from = Timestamp.class, to = Timestamp.class)
	public Function<Timestamp, Timestamp> SQLTimestampToSQLTimestamp() {
		return from -> from;
	}

}
