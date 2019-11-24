package com.beirtipol.dates.excel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.beirtipol.dates.converter.DateConvertersTest;

@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith({ SpringExtension.class })
@SpringBootTest
public class ExcelDateConvertersTest extends DateConvertersTest {
	@BeforeEach
	protected void setup() {
		super.setup();
		expectedResults.put(Double.class, 43709.0d);
	}
}
