CircleCI Build: [![CircleCI](https://circleci.com/gh/beirtipol/date-converters.svg?style=svg)](https://circleci.com/gh/beirtipol/date-converters)
![Java CI with Maven](https://github.com/beirtipol/date-converters/workflows/Java%20CI%20with%20Maven/badge.svg)

# FAQ

## What is this?

A library for providing conversion between a thing and another thing, specifically dates, but not restricted to that.

## Why did you write it?

I found myself working on a project that used every conceivable type of Date class, ranging from straight `java.util.Calendar` to joda Dates, a sprinkling of `XMLGregorianCalendar` for old JAXB implementations, an occasional usage of `java.time.*` and then some third-party closed-source libraries that had their own implementation of Dates.

Our biggest problem was we had no consistent way to convert between all these types. There were many DateUtils, DatesUtils, XMLDateUtils but they all did things slightly differently which led to inconsistencies, especially around daylight savings.

## Why would I use it?

I'd guess you're here because you've also found yourself doing a lot of date conversions. I found that the easiest way to perform a big refactor was to try and get everything in to the same date api, whatever one you choose (obviously java.util.time.*) and the easy way to do that is to convert a few classes at a time, rather than going big bang. As you're able to autowire just a single 'Converters' class in, you can easily remove it as you replace the code with a more up to date api.

## How do I use it?

Just add a dependency on date-converters-core in your pom.xml/gradle build file. The project is published to the Sonatype Nexus central repository.

    <dependency>
        <groupId>com.beirtipol</groupId>
        <artifactId>date-converters-core</artifactId>
        <version>1.0-RELEASE</version>
    </dependency>
    
This project is built using JDK11 but the code is intended to be compatible with JDK8 if you're stuck unable to upgrade just yet so you can download the source and redeploy your own version to your local repository.

## How does it work?

Simple! Out of the box, you get a converter for all core java 8+ Date types:

* `java.util.Calendar`
* `java.util.GregorianCalendar`
* `java.util.Date`
* `java.sql.Date`
* `java.sql.Timestamp`
* `javax.xml.datatype.XMLGregorianCalendar`
* `java.time.LocalDate`
* `java.time.LocalDateTime`
* `java.time.ZonedDateTime`

In addition, extensions are provided for:

* Excel `java.util.Double`
* Joda:
    * `org.joda.time.LocalDate`
    * `org.joda.time.LocalDateTime`
    * `org.joda.time.DateTime`

You just `@Autowire` an `org.beirtipol.Converters` in to your class and ask it to convert 'from' whatever type in to whatever type you want. Due to some magic, you get type-safe conversion and null-safety (if you give null, you get null, your problem).

e.g.

    // No casting, no type-safety warnings, just a result
    LocalDate now = converters.from( ZonedDateTime.now(), LocalDate.class );

Here's how you'd autowire a simple application. You can see a full example in the date-converters-sample-extension module.

    @SpringBootApplication
    @ComponentScan(basePackageClasses = { Converters.class })
    public class DemoApplication implements CommandLineRunner {
        private static final Logger 	LOG    = LoggerFactory.getLogger(DemoApplication.class);
        @Autowired
        private Converters 				converters;
    
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

## What about Timezones?

I'm glad you asked. If you asked, it means you probably know what you're doing already, which is a great start. TimeZone conversion is arbitrary at best. All of the core conversion classes will keep the 'Instant' where possible when converting between dates. i.e. if the 'from' and 'to' Class support a Timezone, they will represent the same point on the timeline. If however, you are converting from a timezoned object to a local object, the 'from' date is converted to UTC and then the timezone is stripped off. This may not be what you want to do, in which case you should provide your own implementation. More on that later.

## What about dates before 1582?

Now we're talking. I've tried to implement a 'sane' conversion for dates before this time but it becomes very messy and very difficult very quickly.

- Borders changed, a lot. TimeZones as we know them did not exist
- Leap years were applied differently depending on the country. This means that 'LocalDate' becomes quite meaningless if you attempt to convert it to a date that has 'timezone' information.

My suggestions for dealing with historic dates are:

- Avoid them. 
- If you cannot avoid them, stick to a single date library.
- Do not perform calculations (like adding or removing days) as things like the Leap year issue will hit you.
- Just pretend they didn't exist.

## What about tests?

This project makes heavy use of JUnit 5 `@ParameterizedTest`. This allows the test class to be quite minimal, but easily extended. Have a look at [DateConvertersTest](https://github.com/beirtipol/date-converters/blob/master/date-converters-core-tests/src/main/java/com/beirtipol/dates/converter/DateConvertersTest.java) and [DateTimeConvertersTest](https://github.com/beirtipol/date-converters/blob/master/date-converters-core-tests/src/main/java/com/beirtipol/dates/converter/DateTimeConvertersTest.java) for the base implementation and [MyDateConvertersTest](https://github.com/beirtipol/date-converters/blob/master/date-converters-sample-extension/src/test/java/com/mydate/dates/MyDateConvertersTest.java) for a sample extension.

Tests are generated for each combination of 'from' Object, 'to' Class and Timezone in order to verify that converting works in the same way regardless of what timezone you are in. Last time I checked, **38,344** Unit tests were generated to run across all these iterations. While a sheer number of tests doesn't guarantee that this code is perfect, it does mean that each line is hit thousands of times in different ways to try all known combinations of state.  

Jacoco is set up to check coverage across all implementations of converters. Other than the odd bit of Exception Handling and unused hashCode(), it's guaranteed to have 100% coverage. I'd like to say you can view the results on the project's [CircleCI](https://app.circleci.com/github/beirtipol/date-converters/pipelines), but I haven't yet managed to get the jacoco data published in an easily-viewed format.

## How do I provide my own implementation of a converter?

You need to write a method in a spring-annotated class (like `@Component`) which follows this signature:

    @Converter(from = SomeDateType.class, to = SomeOtherDateType.class)
    public Function<SomeDateType, SomeOtherDateType> SomeDateTypeToSomeOtherDateType() {
        return from -> //convert 'from' in to 'to';
    }
    
The `@Converter` annotation allows the com.beirtipol.dates.Converter class to determine what the 'from' and 'to' types are so that it can index them and find a converter when you ask it to do a 'from'. This annotation is also a Spring `@Bean` which makes it discoverable

## What about dates before the Julian -> Gregorian cutover?
Now you're talking. This library currently does not handle these very well. If you are attempting to convert between java.util.* and java.time.* for dates pre 1582, things get very weird. You'll notice days being added or subtracted. This is due to the many changes that happened before time got standardised. 

I would suggest you avoid converting dates that are that old. If you really need to use dates that old, you should work purely with a java.time.* and avoid any conversions to/from java.util.*

For more 'light' reading,
- https://www.timeanddate.com/calendar/julian-calendar.html
- https://docs.oracle.com/javase/8/docs/api/java/util/GregorianCalendar.html
- This snippet from the source code of java.util.GregorianCalendar:
"Likewise, with the Julian calendar, we assume a consistent 4-year leap year rule, even though the historical pattern of leap years is irregular, being every 3 years from 45 BCE through 9 BCE, then every 4 years from 8 CE onwards, with no leap years in-between.  Thus date computations and functions such as isLeapYear() are not intended to be historically accurate."
    

## This @Converter stuff looks pretty generic, you could use it for things other than dates?
Yeah, I could. I don't have a good use-case right now though! You should be able to provide any conversion you want though.

## How do you keep an eye on performance
When you're using libraries like this in financial software (cos really, where else will you have such an array of different date types), then you need to make sure it's fast. I've been using YourKit professionally for years and they kindly offered me an open source license for giving them a mention on this project.

![Yourkit Logo](https://www.yourkit.com/images/yklogo.png)

YourKit supports open source projects with innovative and intelligent tools for monitoring and profiling Java and .NET applications.
YourKit is the creator of [YourKit Java Profiler](https://www.yourkit.com/java/profiler/), [YourKit .NET Profiler](https://www.yourkit.com/.net/profiler/), and [YourKit YouMonitor](https://www.yourkit.com/youmonitor/)

## Licensing?
I'm not precious. It's released under GPL3. Do what you like with it. Copy it, rip it apart, extend it, make it better. I would ask that you let me know if you've done any of that, but you don't have to.

Cheers,

Beirti
