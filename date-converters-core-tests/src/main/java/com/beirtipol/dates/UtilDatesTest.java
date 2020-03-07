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

package com.beirtipol.dates;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.ZonedDateTime;
import java.util.Date;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith({SpringExtension.class})
@SpringBootTest
public class UtilDatesTest {

    @Autowired
    protected Converters converters;

    @Test
    public void sensibleCreation() throws Exception {
        int year = 2000;
        int month = 9;
        int day = 1;
        int hour = 0;
        int minute = 0;
        int second = 0;
        int milliseconds = 0;

        Date ud = UtilDates.newUtilDate(year, month, day, hour, minute, second, milliseconds, UtilDates.UTC);
        ZonedDateTime zdt = ZonedDateTime.of(year, month, day, hour, minute, second, milliseconds * 1000, ThreeTenDates.UTC);
        Date expected = converters.from(zdt, Date.class);
        Assertions.assertEquals(expected, ud);
    }

    @Test
    public void addDays() throws Exception {
        Date date = UtilDates.newUtilDate(2000, 1, 1, 0, 0, 0, 0, UtilDates.UTC);
        Date plus1 = UtilDates.newUtilDate(2000, 1, 2, 0, 0, 0, 0, UtilDates.UTC);
        Date plus31 = UtilDates.newUtilDate(2000, 2, 1, 0, 0, 0, 0, UtilDates.UTC);
        Date plus366 = UtilDates.newUtilDate(2001, 1, 1, 0, 0, 0, 0, UtilDates.UTC);
        Date minus1 = UtilDates.newUtilDate(1999, 12, 31, 0, 0, 0, 0, UtilDates.UTC);
        Assertions.assertEquals(plus1, UtilDates.plusDays(date, 1));
        Assertions.assertEquals(plus31, UtilDates.plusDays(date, 31));
        Assertions.assertEquals(plus366, UtilDates.plusDays(date, 366));
        Assertions.assertEquals(minus1, UtilDates.plusDays(date, -1));
    }

    @Test
    public void addDaysKeepsTimezone() throws Exception {
        Date date = UtilDates.newUtilDate(2000, 1, 1, 0, 0, 0, 0, UtilDates.EUROPE_LONDON);
        Date plus1 = UtilDates.newUtilDate(2000, 1, 2, 0, 0, 0, 0, UtilDates.EUROPE_LONDON);
        Assertions.assertEquals(plus1, UtilDates.plusDays(date, 1));
    }

    @Test
    public void addMonths() throws Exception {
        Date date = UtilDates.newUtilDate(2000, 1, 1, 0, 0, 0, 0, UtilDates.UTC);
        Date plus1 = UtilDates.newUtilDate(2000, 2, 1, 0, 0, 0, 0, UtilDates.UTC);
        Date plus12 = UtilDates.newUtilDate(2001, 1, 1, 0, 0, 0, 0, UtilDates.UTC);
        Date minus1 = UtilDates.newUtilDate(1999, 12, 1, 0, 0, 0, 0, UtilDates.UTC);
        Assertions.assertEquals(plus1, UtilDates.plusMonths(date, 1));
        Assertions.assertEquals(plus12, UtilDates.plusMonths(date, 12));
        Assertions.assertEquals(minus1, UtilDates.plusMonths(date, -1));
    }

    @Test
    public void addYears() throws Exception {
        Date date = UtilDates.newUtilDate(2000, 1, 1, 0, 0, 0, 0, UtilDates.UTC);
        Date plus1 = UtilDates.newUtilDate(2001, 1, 1, 0, 0, 0, 0, UtilDates.UTC);
        Date minus1 = UtilDates.newUtilDate(1999, 1, 1, 0, 0, 0, 0, UtilDates.UTC);
        Assertions.assertEquals(plus1, UtilDates.plusYears(date, 1));
        Assertions.assertEquals(minus1, UtilDates.plusYears(date, -1));
    }
}
