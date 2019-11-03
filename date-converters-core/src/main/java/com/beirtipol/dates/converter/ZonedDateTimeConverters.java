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

import com.beirtipol.dates.UtilDates;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.beirtipol.dates.Converter;
import com.beirtipol.dates.ThreeTenDates;

/**
 * All conversions are done through UTC.
 *
 * @author beirtipol@gmail.com
 */
@Component
public class ZonedDateTimeConverters {

    @Bean
    @Converter(from = XMLGregorianCalendar.class, to = ZonedDateTime.class)
    public Function<XMLGregorianCalendar, ZonedDateTime> XMLDateToZonedDateTime() {
        return from -> {
            Calendar result = GregorianCalendar.getInstance(UtilDates.UTC);
            result.setTimeInMillis(from.toGregorianCalendar().getTimeInMillis());
            return ((GregorianCalendar) result).toZonedDateTime();
        };
    }

    @Bean
    @Converter(from = LocalDate.class, to = ZonedDateTime.class)
    public Function<LocalDate, ZonedDateTime> LocalDateToZonedDateTime() {
        return from -> from.atStartOfDay().atZone(ThreeTenDates.UTC);
    }

    @Bean
    @Converter(from = ZonedDateTime.class, to = ZonedDateTime.class)
    public Function<ZonedDateTime, ZonedDateTime> ZonedDateTimeToZonedDateTime() {
        return from -> from;
    }

    @Bean
    @Converter(from = LocalDateTime.class, to = ZonedDateTime.class)
    public Function<LocalDateTime, ZonedDateTime> LocalDateTimeToZonedDateTime() {
        return from -> from.atZone(ThreeTenDates.UTC);

    }

    @Bean
    @Converter(from = java.util.Date.class, to = ZonedDateTime.class)
    public Function<Date, ZonedDateTime> UtilDateToZonedDateTime() {
        return from -> from.toInstant().atZone(ThreeTenDates.UTC);
    }

    @Bean
    @Converter(from = {Calendar.class, GregorianCalendar.class}, to = ZonedDateTime.class)
    public Function<Calendar, ZonedDateTime> CalendarToZonedDateTime() {
        return from -> from.toInstant().atZone(ThreeTenDates.UTC);
    }

    @Bean
    @Converter(from = java.sql.Date.class, to = ZonedDateTime.class)
    public Function<java.sql.Date, ZonedDateTime> SQLDateToZonedDateTime() {
		return from -> ZonedDateTime.ofInstant(Instant.ofEpochMilli(from.getTime()), ThreeTenDates.UTC);
    }

    @Bean
    @Converter(from = Timestamp.class, to = ZonedDateTime.class)
    public Function<Timestamp, ZonedDateTime> SQLTimestampToZonedDateTime() {
        return from -> UtilDateToZonedDateTime().apply(from);
    }

}
