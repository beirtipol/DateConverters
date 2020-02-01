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

package com.mydate.dates;

import java.time.LocalDate;
import java.time.ZonedDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.beirtipol.dates.Converters;

@SpringBootApplication
@ComponentScan(basePackageClasses = { Converters.class, MyDateConverters.class })
public class DemoApplication implements CommandLineRunner {
	private static final Logger	LOG	= LoggerFactory.getLogger(DemoApplication.class);
	@Autowired
	private Converters			converters;

	@Override
	public void run(String... args) {
		ZonedDateTime now = ZonedDateTime.now();

		LOG.info("Demonstrating Conversions");
		LOG.info("Now (ZonedDateTime): " + now);
		LOG.info("As LocalDate: " + converters.from(now, LocalDate.class));
		LOG.info("As java.util.Date: " + converters.from(now, java.util.Date.class));
	}

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
}
