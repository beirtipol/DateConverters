package com.beirtipol.dates.converter;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.function.Function;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
public class XMLDateConverters {
	private static final Logger		LOG	= LoggerFactory.getLogger(XMLDateConverters.class);

	@Autowired
	private ZonedDateTimeConverters	zonedDateTimeConverter;

	private DatatypeFactory			dt;

	public XMLDateConverters() {
		try {
			dt = DatatypeFactory.newInstance();
		} catch (DatatypeConfigurationException e) {
			LOG.error("SEVERE: Cannot instantiate DatatypeFactory.", e);
		}
	}

	@Bean
	@Converter(from = XMLGregorianCalendar.class, to = XMLGregorianCalendar.class)
	public Function<XMLGregorianCalendar, XMLGregorianCalendar> XMLDateToXMLGregorianCalendar() {
		return from -> ZonedDateTimeToXMLGregorianCalendar().apply(from.toGregorianCalendar().toZonedDateTime());
	}

	@Bean
	@Converter(from = LocalDate.class, to = XMLGregorianCalendar.class)
	public Function<LocalDate, XMLGregorianCalendar> LocalDateToXMLGregorianCalendar() {
		return from -> LocalDateTimeToXMLGregorianCalendar().apply(from.atStartOfDay());
	}

	@Bean
	@Converter(from = ZonedDateTime.class, to = XMLGregorianCalendar.class)
	public Function<ZonedDateTime, XMLGregorianCalendar> ZonedDateTimeToXMLGregorianCalendar() {
		return from -> dt.newXMLGregorianCalendar(GregorianCalendar.from(from));
	}

	@Bean
	@Converter(from = LocalDateTime.class, to = XMLGregorianCalendar.class)
	public Function<LocalDateTime, XMLGregorianCalendar> LocalDateTimeToXMLGregorianCalendar() {
		return from -> ZonedDateTimeToXMLGregorianCalendar().apply(from.atZone(ThreeTenDates.UTC));
	}

	@Bean
	@Converter(from = java.util.Date.class, to = XMLGregorianCalendar.class)
	public Function<Date, XMLGregorianCalendar> UtilDateToXMLGregorianCalendar() {
		return from -> dt.newXMLGregorianCalendar(GregorianCalendar.from(zonedDateTimeConverter.UtilDateToZonedDateTime().apply(from)));
	}

	@Bean
	@Converter(from = java.sql.Date.class, to = XMLGregorianCalendar.class)
	public Function<java.sql.Date, XMLGregorianCalendar> SQLDateToXMLGregorianCalendar() {
		return from -> dt.newXMLGregorianCalendar(GregorianCalendar.from(zonedDateTimeConverter.SQLDateToZonedDateTime().apply(from)));
	}

	@Bean
	@Converter(from = Timestamp.class, to = XMLGregorianCalendar.class)
	public Function<Timestamp, XMLGregorianCalendar> SQLTimestampToXMLGregorianCalendar() {
		return from -> UtilDateToXMLGregorianCalendar().apply(from);
	}

}
