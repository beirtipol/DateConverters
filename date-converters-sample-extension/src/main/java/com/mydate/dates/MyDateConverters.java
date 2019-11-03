package com.mydate.dates;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.function.Function;

import javax.xml.datatype.XMLGregorianCalendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.beirtipol.dates.Converter;
import com.beirtipol.dates.Converters;
import com.beirtipol.dates.UtilDates;

@SuppressWarnings("deprecation")
@Component
public class MyDateConverters {

	@Autowired
	private Converters converters;

	@Bean
	@Converter(from = MyDate.class, to = LocalDate.class)
	public Function<MyDate, LocalDate> MyDateToLocalDate() {
		return from -> LocalDate.of(from.getYear(), from.getMonth(), from.getDay());
	}

	@Bean
	@Converter(from = MyDate.class, to = LocalDateTime.class)
	public Function<MyDate, LocalDateTime> MyDateToLocalDateTime() {
		return from -> converters.from(MyDateToLocalDate().apply(from), LocalDateTime.class);
	}

	@Bean
	@Converter(from = MyDate.class, to = ZonedDateTime.class)
	public Function<MyDate, ZonedDateTime> MyDateToZonedDateTime() {
		return from -> converters.from(MyDateToLocalDate().apply(from), ZonedDateTime.class);
	}

	@Bean
	@Converter(from = MyDate.class, to = java.util.Date.class)
	public Function<MyDate, java.util.Date> MyDateToUtilDate() {
		return from -> converters.from(MyDateToLocalDate().apply(from), java.util.Date.class);
	}

	@Bean
	@Converter(from = MyDate.class, to = java.sql.Date.class)
	public Function<MyDate, java.sql.Date> MyDateToSQLDate() {
		return from -> converters.from(MyDateToLocalDate().apply(from), java.sql.Date.class);
	}

	@Bean
	@Converter(from = MyDate.class, to = XMLGregorianCalendar.class)
	public Function<MyDate, XMLGregorianCalendar> MyDateToXMLDate() {
		return from -> converters.from(MyDateToLocalDate().apply(from), XMLGregorianCalendar.class);
	}

	@Bean
	@Converter(from = MyDate.class, to = Timestamp.class)
	public Function<MyDate, Timestamp> MyDateToSQLTimestamp() {
		return from -> converters.from(MyDateToLocalDate().apply(from), Timestamp.class);
	}

	@Bean
	@Converter(from = MyDate.class, to = Calendar.class)
	public Function<MyDate, Calendar> MyDateToCalendar() {
		return from -> converters.from(MyDateToLocalDate().apply(from), Calendar.class);
	}

	@Bean
	@Converter(from = Calendar.class, to = MyDate.class)
	public Function<Calendar, MyDate> CalendarToMyDate() {
		return from -> LocalDateToMyDate().apply(converters.from(from, LocalDate.class));
	}

	@Bean
	@Converter(from = XMLGregorianCalendar.class, to = MyDate.class)
	public Function<XMLGregorianCalendar, MyDate> XMLDateToMyDate() {
		return from -> new MyDate(from.getYear(), from.getMonth(), from.getDay());
	}

	@Bean
	@Converter(from = LocalDate.class, to = MyDate.class)
	public Function<LocalDate, MyDate> LocalDateToMyDate() {
		return from -> new MyDate(from.getYear(), from.getMonthValue(), from.getDayOfMonth());
	}

	@Bean
	@Converter(from = ZonedDateTime.class, to = MyDate.class)
	public Function<ZonedDateTime, MyDate> ZonedDateTimeToMyDate() {
		return from -> new MyDate(from.getYear(), from.getMonthValue(), from.getDayOfMonth());
	}

	@Bean
	@Converter(from = LocalDateTime.class, to = MyDate.class)
	public Function<LocalDateTime, MyDate> LocalDateTimeToMyDate() {
		return from -> new MyDate(from.getYear(), from.getMonthValue(), from.getDayOfMonth());
	}

	@Bean
	@Converter(from = java.util.Date.class, to = MyDate.class)
	public Function<Date, MyDate> UtilDateToMyDate() {
		return from -> new MyDate(from.getYear() + UtilDates.YEAR_OFFSET, from.getMonth() + UtilDates.MONTH_OFFSET, from.getDate());
	}

	@Bean
	@Converter(from = java.sql.Date.class, to = MyDate.class)
	public Function<java.sql.Date, MyDate> SQLDateToMyDate() {
		return from -> new MyDate(from.getYear() + UtilDates.YEAR_OFFSET, from.getMonth() + UtilDates.MONTH_OFFSET, from.getDate());
	}

	@Bean
	@Converter(from = Timestamp.class, to = MyDate.class)
	public Function<Timestamp, MyDate> SQLTimestampToMyDate() {
		return from -> new MyDate(from.getYear() + UtilDates.YEAR_OFFSET, from.getMonth() + UtilDates.MONTH_OFFSET, from.getDate());
	}

	@Bean
	@Converter(from = MyDate.class, to = MyDate.class)
	public Function<MyDate, MyDate> MyDateToMyDate() {
		return from -> from;
	}

}
