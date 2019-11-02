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
 * This class will default to 'UTC' where a TimeZone is not present in the source date.
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
	@Converter(from = java.sql.Date.class, to = Date.class)
	public Function<java.sql.Date, Date> SQLDateToUtilDate() {
		return from -> from;
	}

	@Bean
	@Converter(from = Timestamp.class, to = Date.class)
	public Function<Timestamp, Date> SQLTimestampToUtilDate() {
		return from -> UtilDateToUtilDate().apply(from);
	}

}
