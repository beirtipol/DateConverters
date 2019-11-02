package com.mydate.dates;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.ComponentScan;

import com.beirtipol.dates.DatesConfiguration;

@SpringBootConfiguration
@ComponentScan(basePackageClasses = { DatesConfiguration.class, MyDateConverters.class })
public class MyDatesConfiguration {

}
