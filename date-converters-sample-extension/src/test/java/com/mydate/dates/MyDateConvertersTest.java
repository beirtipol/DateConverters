package com.mydate.dates;

import com.beirtipol.dates.converter.DateConvertersTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith({SpringExtension.class})
@SpringBootTest
public class MyDateConvertersTest extends DateConvertersTest {
    @BeforeEach
    protected void setup() {
        super.setup();
        expectedResults.put(MyDate.class, new MyDate(2019, 9, 1));
    }
}
