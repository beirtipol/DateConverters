package com.beirtipol.dates.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.beirtipol.dates.Converters;
import com.beirtipol.dates.ThreeTenDates;
import com.beirtipol.dates.UtilDates;

@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith({ SpringExtension.class })
@SpringBootTest
public class DateConvertersTest {

	private ZonedDateTime			expectedZonedDateTime;
	private LocalDateTime			expectedLocalDateTime;
	private LocalDate				expectedLocalDate;
	private LocalTime				expectedLocalTime;
	private Date					expectedUtilDate;
	private XMLGregorianCalendar	expectedXMLDate;
	private java.sql.Date			expectedSQLDate;
	private Timestamp				expectedSQLTimestamp;

	protected Map<Class<?>, Object>	expectedResults	= new HashMap<>();

	@Autowired
	protected Converters			DATES;

	private static DatatypeFactory	dtFactory;

	@BeforeAll
	private static void beforeClass() throws Exception {
		dtFactory = DatatypeFactory.newInstance();
	}

	@BeforeEach
	protected void setup() {
		expectedLocalDate = LocalDate.of(2019, 9, 1);
		expectedLocalTime = LocalTime.of(0, 0);
		expectedLocalDateTime = LocalDateTime.of(expectedLocalDate, expectedLocalTime);
		expectedZonedDateTime = expectedLocalDateTime.atZone(ThreeTenDates.UTC);
		GregorianCalendar expectedUtilDateCalendar = new GregorianCalendar(2019, Calendar.SEPTEMBER, 1);
		expectedUtilDateCalendar.setTimeZone(UtilDates.UTC);
		expectedUtilDate = expectedUtilDateCalendar.getTime();
		expectedXMLDate = dtFactory.newXMLGregorianCalendar(2019, 9, 1, 0, 0, 0, 0, 0);
		expectedSQLDate = new java.sql.Date(expectedUtilDate.getTime());
		expectedSQLTimestamp = new Timestamp(expectedUtilDate.getTime());
		expectedResults.put(LocalDate.class, expectedLocalDate);
		expectedResults.put(LocalDateTime.class, expectedLocalDateTime);
		expectedResults.put(ZonedDateTime.class, expectedZonedDateTime);
		expectedResults.put(Date.class, expectedUtilDate);
		expectedResults.put(java.sql.Date.class, expectedSQLDate);
		expectedResults.put(XMLGregorianCalendar.class, expectedXMLDate);
		expectedResults.put(Timestamp.class, expectedSQLTimestamp);
	}

	protected Stream<Class<?>> supportedClasses() {
		return Stream.of(LocalDate.class, LocalDateTime.class, ZonedDateTime.class, java.util.Date.class, java.sql.Date.class, Timestamp.class, XMLGregorianCalendar.class);
	}

	protected Stream<String> systemTimeZones() {
		return ZoneId.getAvailableZoneIds().stream();
	}

	@ParameterizedTest
	@MethodSource("supportedClasses")
	public void fromLocalDateTime(Class<?> clazz) {
		Object result = DATES.from(expectedLocalDateTime, clazz);
		assertEquals(expectedResults.get(clazz), result);
	}

	@ParameterizedTest
	@MethodSource("supportedClasses")
	public void fromLocalDate(Class<?> clazz) {
		Object result = DATES.from(expectedLocalDate, clazz);
		assertEquals(expectedResults.get(clazz), result);
	}

	@ParameterizedTest
	@MethodSource("supportedClasses")
	public void fromZonedDateTime(Class<?> clazz) {
		Object result = DATES.from(expectedZonedDateTime, clazz);
		assertEquals(expectedResults.get(clazz), result);
	}

	@ParameterizedTest
	@MethodSource("supportedClasses")
	public void fromUtilDate(Class<?> clazz) {
		Object result = DATES.from(expectedUtilDate, clazz);
		assertEquals(expectedResults.get(clazz), result);
	}

	@ParameterizedTest
	@MethodSource("supportedClasses")
	public void fromTimestamp(Class<?> clazz) {
		Object result = DATES.from(expectedSQLTimestamp, clazz);
		assertEquals(expectedResults.get(clazz), result);
	}

	@ParameterizedTest
	@MethodSource("supportedClasses")
	public void fromNullLocalDateTime_isNull(Class<?> clazz) {
		assertNull(DATES.from((LocalDateTime) null, clazz));
	}

	@ParameterizedTest
	@MethodSource("supportedClasses")
	public void fromNullUtilDate_isNull(Class<?> clazz) {
		assertNull(DATES.from((LocalDateTime) null, clazz));
	}

	@ParameterizedTest
	@MethodSource("supportedClasses")
	public void fromNullZonedDateTime_isNull(Class<?> clazz) {
		assertNull(DATES.from((ZonedDateTime) null, clazz));
	}

	@ParameterizedTest
	@MethodSource("supportedClasses")
	public void fromNullXMLDate_isNull(Class<?> clazz) {
		assertNull(DATES.from((XMLGregorianCalendar) null, clazz));
	}

	@ParameterizedTest
	@MethodSource("supportedClasses")
	public void fromNullSQLDate_isNull(Class<?> clazz) {
		assertNull(DATES.from((java.sql.Date) null, clazz));
	}

	@ParameterizedTest
	@MethodSource("supportedClasses")
	public void fromNullSQLTimestamp_isNull(Class<?> clazz) {
		assertNull(DATES.from((Timestamp) null, LocalDate.class));
	}

	@ParameterizedTest
	@MethodSource("systemTimeZones")
	public void fromZonedDateTime(String timeZone) {
		ZonedDateTime zdt = ZonedDateTime.of(LocalDateTime.of(expectedLocalDate, expectedLocalTime), ZoneId.of(timeZone));
		assertEquals(expectedLocalDate, DATES.from(zdt, LocalDate.class));
	}
}
