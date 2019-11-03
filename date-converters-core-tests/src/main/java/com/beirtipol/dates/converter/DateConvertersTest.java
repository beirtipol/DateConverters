package com.beirtipol.dates.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Stream;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.beirtipol.dates.Converters;
import com.beirtipol.dates.ThreeTenDates;
import com.beirtipol.dates.UtilDates;

/**
 * In order to automatically generate tests for your own converters, you just need to sublcass this class, keep the same annotations, and override 'setup()' to add your own expected values.
 *
 * @author beirtipol@gmail.com
 */
@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith({SpringExtension.class})
@SpringBootTest
public class DateConvertersTest {
    protected Map<Class<?>, Object> expectedResults = new HashMap<>();

    @Autowired
    protected Converters converters;

    private static DatatypeFactory dtFactory;

    @BeforeAll
    private static void beforeClass() throws Exception {
        dtFactory = DatatypeFactory.newInstance();
    }

    @BeforeEach
    protected void setup() {
		LocalDate  expectedLocalDate = LocalDate.of(2019, 9, 1);
		LocalTime expectedLocalTime = LocalTime.of(0, 0);
		LocalDateTime expectedLocalDateTime = LocalDateTime.of(expectedLocalDate, expectedLocalTime);
		ZonedDateTime expectedZonedDateTime = expectedLocalDateTime.atZone(ThreeTenDates.UTC);
		Calendar  expectedGregorianCalendar = GregorianCalendar.getInstance(UtilDates.UTC);
        expectedGregorianCalendar.setTimeInMillis(expectedZonedDateTime.toInstant().toEpochMilli());
		Date   expectedUtilDate = expectedGregorianCalendar.getTime();
		XMLGregorianCalendar expectedXMLDate = dtFactory.newXMLGregorianCalendar(2019, 9, 1, 0, 0, 0, 0, 0);
		java.sql.Date  expectedSQLDate = new java.sql.Date(expectedUtilDate.getTime());
		Timestamp  expectedSQLTimestamp = new Timestamp(expectedUtilDate.getTime());
        expectedResults.put(LocalDate.class, expectedLocalDate);
        expectedResults.put(LocalDateTime.class, expectedLocalDateTime);
        expectedResults.put(ZonedDateTime.class, expectedZonedDateTime);
        expectedResults.put(Date.class, expectedUtilDate);
        expectedResults.put(java.sql.Date.class, expectedSQLDate);
        expectedResults.put(XMLGregorianCalendar.class, expectedXMLDate);
        expectedResults.put(Timestamp.class, expectedSQLTimestamp);
        expectedResults.put(Calendar.class, expectedGregorianCalendar);
    }

	/**
	 * @return a stream of the keys of expectedResults
	 */
	protected Stream<Class<?>> supportedClasses() {
		// setup() is not called before this provider is invoked when this test is subclassed
    	setup();
        return expectedResults.keySet().stream();
    }

	/**
	 * @return all available system timezones. This could be overridden if required.
	 */
	protected Stream<String> systemTimeZones() {
        return ZoneId.getAvailableZoneIds().stream();
    }

    /**
     * @return a flatMapped stream of all expectedResult keys and values along with every System timezone
     * to test conversion from all types to all types in all regions. At time of writing, this results in 38,336 tests! This might seem excessive,
     * but during development, this helped to turn up some nasty edge cases in conversion between types where one has Timezone information
     * and the other does not.
     */
    protected Stream<Arguments> testData() {
        // setup() is not called before this provider is invoked
        setup();
        Supplier<Stream<Object>> valueStream = () -> expectedResults.values().stream();
        Supplier<Stream<String>> timezoneStream = () -> systemTimeZones();
        return expectedResults.keySet().stream()// Every Class
                .flatMap(key -> valueStream.get().map(value -> Arguments.of(value, key)))// Every Value
                .flatMap(arg -> timezoneStream.get().map(tz -> Arguments.of(arg.get()[0], arg.get()[1], tz)));// Every TimeZone
    }


    @ParameterizedTest(name = "Convert {0} to {1} when System is set to {2}")
    @MethodSource("testData")
    public void fromAnythingToAnything(Object from, Class<?> to, String systemTimezone) {
        TimeZone.setDefault(TimeZone.getTimeZone(systemTimezone));
        Object result = converters.from(from, to);
        assertEquals(expectedResults.get(to), result, "Failed conversion from " + from.getClass());
    }

	/**
	 * Verify that all classes handle null correctly. This should be a moot point as Converters is set up to fail-fast and
	 * return null if found, but this will cover us in case of changes.
	 * @param clazz attempted 'to' class for converting null
	 */
	@ParameterizedTest
    @MethodSource("supportedClasses")
    public void fromNull_isNull(Class<?> clazz) {
        assertNull(converters.from(null, clazz));
    }
}
