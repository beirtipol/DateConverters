package com.beirtipol.dates;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation allows you to tag a method returning a {@link java.util.function.Function} which is responsible for converting input in to a return value of the declared type.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Converter {
	/**
	 *
	 * @return an array of Classes of objects which this Function can convert 'from'. You need to be careful to ensure all classes are in the same type hierarchy or you will end up with runtime Class exceptions
	 */
	public Class<?>[] from();

	/**
	 *
	 * @return the class of object which this Function can convert 'to'
	 */
	public Class<?> to();
}
