package com.beirtipol.dates.converter;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.function.Function;

import javax.xml.datatype.XMLGregorianCalendar;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.beirtipol.dates.Converter;
import com.beirtipol.dates.ThreeTenDates;

/**
 * As {@link LocalDateTimeTime} does not have any TimeZone information, it does not try to do anything clever with the
 * temporals
 * it attempts to parse. Where possible, it will extract the year/month/day parts of the temporal.
 * 
 * @author beirtipol@gmail.com
 *
 */
@Component
public class LocalDateTimeConverters {

	@Bean
	@Converter(from = XMLGregorianCalendar.class, to = LocalDateTime.class)
	public Function<XMLGregorianCalendar, LocalDateTime> XMLDateToLocalDateTime() {
		return from -> ZonedDateTimeToLocalDateTime().apply(from.toGregorianCalendar().toZonedDateTime());
	}

	@Bean
	@Converter(from = LocalDate.class, to = LocalDateTime.class)
	public Function<LocalDate, LocalDateTime> LocalDateToLocalDateTime() {
		return from -> from.atStartOfDay();
	}

	@Bean
	@Converter(from = ZonedDateTime.class, to = LocalDateTime.class)
	public Function<ZonedDateTime, LocalDateTime> ZonedDateTimeToLocalDateTime() {
		return from -> from.toLocalDateTime();
	}

	@Bean
	@Converter(from = LocalDateTime.class, to = LocalDateTime.class)
	public Function<LocalDateTime, LocalDateTime> LocalDateTimeTimeToLocalDateTime() {
		return from -> from;
	}

	@Bean
	@Converter(from = java.util.Date.class, to = LocalDateTime.class)
	public Function<Date, LocalDateTime> UtilDateToLocalDateTime() {
		return from -> from.toInstant().atZone(ThreeTenDates.UTC).toLocalDateTime();
	}

	@Bean
	@Converter(from = java.sql.Date.class, to = LocalDateTime.class)
	public Function<java.sql.Date, LocalDateTime> SQLDateToLocalDateTime() {
		return from -> LocalDateToLocalDateTime().apply(from.toLocalDate());
	}

	@Bean
	@Converter(from = Timestamp.class, to = LocalDateTime.class)
	public Function<Timestamp, LocalDateTime> SQLTimestampToLocalDateTime() {
		return from -> UtilDateToLocalDateTime().apply(from);
	}

}
