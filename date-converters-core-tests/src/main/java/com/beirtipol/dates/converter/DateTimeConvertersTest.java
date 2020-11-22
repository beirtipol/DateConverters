/*
 * Copyright (C) 2020  https://github.com/beirtipol
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.beirtipol.dates.converter;

import com.beirtipol.dates.ThreeTenDates;
import com.beirtipol.dates.UtilDates;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith({SpringExtension.class})
@SpringBootTest
public class DateTimeConvertersTest extends DateConvertersTest {
    private static DatatypeFactory dtFactory;

    @BeforeAll
    private static void beforeClass() throws Exception {
        dtFactory = DatatypeFactory.newInstance();
    }

    @Override
    @BeforeEach
    protected void setup() {
        LocalDate expectedLocalDate = LocalDate.of(2019, 9, 1);
        LocalTime expectedLocalTime = LocalTime.of(1, 2, 3, 456000000);
        LocalDateTime expectedLocalDateTime = LocalDateTime.of(expectedLocalDate, expectedLocalTime);
        ZonedDateTime expectedZonedDateTime = expectedLocalDateTime.atZone(ThreeTenDates.UTC);
        Date expectedUtilDate = UtilDates.newUtilDate(2019, 9, 1, 1, 2, 3, 456, UtilDates.UTC);
        Calendar expectedGregorianCalendar = GregorianCalendar.getInstance(UtilDates.UTC);
        expectedGregorianCalendar.setTime(expectedUtilDate);
        XMLGregorianCalendar expectedXMLDate = dtFactory.newXMLGregorianCalendar(2019, 9, 1, 1, 2, 3, 456, 0);
        java.sql.Date expectedSQLDate = new java.sql.Date(expectedUtilDate.getTime());
        Timestamp expectedSQLTimestamp = new Timestamp(expectedUtilDate.getTime());
        expectedResults.put(LocalDateTime.class, expectedLocalDateTime);
        expectedResults.put(ZonedDateTime.class, expectedZonedDateTime);
        expectedResults.put(Date.class, expectedUtilDate);
        expectedResults.put(java.sql.Date.class, expectedSQLDate);
        expectedResults.put(XMLGregorianCalendar.class, expectedXMLDate);
        expectedResults.put(Timestamp.class, expectedSQLTimestamp);
        expectedResults.put(Calendar.class, expectedGregorianCalendar);
    }
}
