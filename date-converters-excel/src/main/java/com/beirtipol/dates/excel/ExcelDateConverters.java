package com.beirtipol.dates.excel;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.function.Function;

import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.poi.ss.usermodel.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.beirtipol.dates.Converter;
import com.beirtipol.dates.Converters;

@Component
public class ExcelDateConverters {

	@Autowired
	private Converters converters;

	@Bean
	@Converter(from = Double.class, to = LocalDate.class)
	public Function<Double, LocalDate> DoubleToLocalDate() {
		return from -> DateUtil.getLocalDateTime(from).toLocalDate();
	}

	@Bean
	@Converter(from = Double.class, to = LocalDateTime.class)
	public Function<Double, LocalDateTime> DoubleToLocalDateTime() {
		return from -> DateUtil.getLocalDateTime(from);
	}

	@Bean
	@Converter(from = Double.class, to = ZonedDateTime.class)
	public Function<Double, ZonedDateTime> DoubleToZonedDateTime() {
		return from -> converters.from(DoubleToLocalDateTime().apply(from), ZonedDateTime.class);
	}

	@Bean
	@Converter(from = Double.class, to = java.util.Date.class)
	public Function<Double, java.util.Date> DoubleToUtilDate() {
		return from -> converters.from(DoubleToLocalDateTime().apply(from), java.util.Date.class);
	}

	@Bean
	@Converter(from = Double.class, to = java.sql.Date.class)
	public Function<Double, java.sql.Date> DoubleToSQLDate() {
		return from -> converters.from(DoubleToLocalDateTime().apply(from), java.sql.Date.class);
	}

	@Bean
	@Converter(from = Double.class, to = XMLGregorianCalendar.class)
	public Function<Double, XMLGregorianCalendar> DoubleToXMLDate() {
		return from -> converters.from(DoubleToLocalDateTime().apply(from), XMLGregorianCalendar.class);
	}

	@Bean
	@Converter(from = Double.class, to = Timestamp.class)
	public Function<Double, Timestamp> DoubleToSQLTimestamp() {
		return from -> converters.from(DoubleToLocalDateTime().apply(from), Timestamp.class);
	}

	@Bean
	@Converter(from = Double.class, to = Calendar.class)
	public Function<Double, Calendar> DoubleToCalendar() {
		return from -> converters.from(DoubleToLocalDateTime().apply(from), Calendar.class);
	}

	@Bean
	@Converter(from = Calendar.class, to = Double.class)
	public Function<Calendar, Double> CalendarToDouble() {
		return from -> DateUtil.getExcelDate(from, false);
	}

	@Bean
	@Converter(from = XMLGregorianCalendar.class, to = Double.class)
	public Function<XMLGregorianCalendar, Double> XMLDateToDouble() {
		return from -> DateUtil.getExcelDate(converters.from(from, Calendar.class), false);
	}

	@Bean
	@Converter(from = LocalDate.class, to = Double.class)
	public Function<LocalDate, Double> LocalDateToDouble() {
		return from -> DateUtil.getExcelDate(from);
	}

	@Bean
	@Converter(from = ZonedDateTime.class, to = Double.class)
	public Function<ZonedDateTime, Double> ZonedDateTimeToDouble() {
		return from -> DateUtil.getExcelDate(from.toLocalDateTime());
	}

	@Bean
	@Converter(from = LocalDateTime.class, to = Double.class)
	public Function<LocalDateTime, Double> LocalDateTimeToDouble() {
		return from -> DateUtil.getExcelDate(from);
	}

	@Bean
	@Converter(from = java.util.Date.class, to = Double.class)
	public Function<Date, Double> UtilDateToDouble() {
		return from -> DateUtil.getExcelDate(converters.from(from, LocalDateTime.class));
	}

	@Bean
	@Converter(from = java.sql.Date.class, to = Double.class)
	public Function<java.sql.Date, Double> SQLDateToDouble() {
		return from -> DateUtil.getExcelDate(converters.from(from, LocalDate.class));
	}

	@Bean
	@Converter(from = Timestamp.class, to = Double.class)
	public Function<Timestamp, Double> SQLTimestampToDouble() {
		return from -> DateUtil.getExcelDate(converters.from(from, LocalDateTime.class));
	}

	@Bean
	@Converter(from = Double.class, to = Double.class)
	public Function<Double, Double> DoubleToDouble() {
		return from -> from;
	}

}
