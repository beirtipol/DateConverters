package com.beirtipol.dates.excel;


import org.springframework.context.annotation.ComponentScan;

import com.beirtipol.dates.Converters;

/**
 * You are required to include this class in your @ComponentScan in order to ensure correct
 * initialization
 * 
 * @author beirtipol@gmail.com
 *
 */
@ComponentScan(basePackageClasses = { Converters.class, ExcelDateConverters.class })
public class ExcelDatesConfiguration {

}
