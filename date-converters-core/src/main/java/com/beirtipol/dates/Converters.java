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

import com.beirtipol.dates.converter.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.type.MethodMetadata;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * {@link Converters} will gather all bean methods which declare the annotation {@link Converter} and index them by the
 * 'from' and 'to' types declared in the annotation. It has a single useful method
 * {@link Converters#from(Object, Class)} which allows passing in any {@link Object} along with the desired return type
 * {@link Class} and will search for an appropriate {@link Converter} to apply the conversion.
 * <p>
 * A number of core {@link Converter} beans are provided by this project
 *
 * @author beirtipol@gmail.com
 * @see LocalDateConverters
 * @see LocalDateTimeConverters
 * @see ZonedDateTimeConverters
 * @see UtilDateConverters
 * @see XMLDateConverters
 */
@SuppressWarnings({"rawtypes", "unchecked"})
@SpringBootConfiguration
@ComponentScan
@Component
public class Converters implements BeanPostProcessor {
    private static final Logger LOG = LoggerFactory.getLogger(Converters.class);
    @Autowired
    private BeanFactory beanFactory;

    private final Map<ConverterKey, Function> converters = new HashMap<>();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        BeanDefinitionRegistry registry = (BeanDefinitionRegistry) beanFactory;
        if (registry.containsBeanDefinition(beanName)) {
            BeanDefinition beanDefinition = registry.getBeanDefinition(beanName);
            if (beanDefinition.getSource() instanceof MethodMetadata) {
                MethodMetadata beanMethod = (MethodMetadata) beanDefinition.getSource();
                String annotationType = Converter.class.getName();
                if (beanMethod.isAnnotated(annotationType)) {
                    MultiValueMap<String, Object> attribs = beanMethod.getAllAnnotationAttributes(annotationType);
                    assert attribs != null;
                    Class<?>[] froms = (Class<?>[]) attribs.get("from").get(0);
                    Class<?> to = (Class<?>) attribs.get("to").get(0);
                    Arrays.stream(froms).forEach(from -> {
                        ConverterKey key = new ConverterKey(from, to);
                        converters.put(key, (Function) bean);
                    });

                }
            }
        }

        return bean;
    }

    public <T> T from(Object from, Class<T> to) {
        if (from == null) {
            return null;
        }

        // In the case of java.util.Calendar, we may not have created converters for all types.
        Class<?> fromClass = from.getClass();
        Function converter = getConverter(fromClass, to);
        while (converter == null && fromClass.getSuperclass() != Object.class) {
            fromClass = fromClass.getSuperclass();
            converter = getConverter(fromClass, to);
        }
        if (converter == null) {
            throw new NoSuchBeanDefinitionException(to, String.format("No bean available to convert from %s to %s", from.getClass(), to));
        }
        if (fromClass != from.getClass() && LOG.isDebugEnabled()) {
            LOG.debug(String.format("No direct converter found between %s and %s. Attempting to convert from %s to %s instead.", from.getClass(), to, fromClass, to));
        }
        return (T) converter.apply(from);
    }

    private <T> Function getConverter(Class<?> from, Class<T> to) {
        Function converter;
        ConverterKey key = new ConverterKey(from, to);
        converter = converters.get(key);
        return converter;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
