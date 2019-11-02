package com.mydate.dates;

import org.springframework.context.annotation.ComponentScan;

import com.beirtipol.dates.Converters;

/**
 * You are required to include {@link DatesConfiguration} in your @ComponentScan in order to ensure correct
 * initialization
 * 
 * @author beirtipol@gmail.com
 *
 */
@ComponentScan(basePackageClasses = { Converters.class, MyDateConverters.class })
public class MyDatesConfiguration {

}
