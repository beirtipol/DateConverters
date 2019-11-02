package com.mydate.dates;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.beirtipol.dates.converter.DateConvertersTest;

@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith({ SpringExtension.class })
@SpringBootTest
public class MyDateConvertersTest extends DateConvertersTest {

	private MyDate expectedMyDate;

	@BeforeEach
	protected void setup() {
		super.setup();
		expectedMyDate = new MyDate(2019, 9, 1);
		expectedResults.put(MyDate.class, expectedMyDate);
	}

	protected Stream<Class<?>> supportedClasses() {
		return Stream.concat(super.supportedClasses(), Stream.of(MyDate.class));
	}

	@ParameterizedTest
	@MethodSource("supportedClasses")
	public void fromMyDate(Class<?> clazz) {
		Object result = DATES.from(expectedMyDate, clazz);
		assertEquals(expectedResults.get(clazz), result);
	}
}
