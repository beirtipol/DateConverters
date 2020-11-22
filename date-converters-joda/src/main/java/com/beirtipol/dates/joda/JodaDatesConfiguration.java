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

package com.beirtipol.dates.joda;


import com.beirtipol.dates.Converters;
import org.springframework.context.annotation.ComponentScan;

/**
 * You are required to include this class in your @ComponentScan in order to ensure correct
 * initialization
 *
 * @author beirtipol@gmail.com
 */
@ComponentScan(basePackageClasses = {Converters.class, JodaLocalDateConverters.class})
public class JodaDatesConfiguration {

}
