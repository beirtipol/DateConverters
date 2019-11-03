# FAQ

## What is this?

A library for providing conversion between a thing and another thing, specifically dates, but not restricted to that.

## Why did you write it?

I found myself working on a project that used every conceivable type of Date class, ranging from straight java.util.Calendar to joda Dates, a sprinkling of XMLGregorianCalendar for old JAXB implementations, an occasional usage of java.time.* and then some third-party closed-source libraries that had their own implementation of Dates.

Our biggest problem was we had no consistent way to convert between all these types. There were many DateUtils, DatesUtils, XMLDateUtils but they all did things slightly differently.

## Why would I use it?

I'd guess you're here because you've also found yourself doing a lot of date conversions. I found that the easiest way to perform a big refactor was to try and get everything in to the same date api, whatever one you choose (obviously java.util.time.*) and the easy way to do that is to convert a few classes at a time, rather than going big bang. As you're able to autowire just a single 'Converters' class in, you can easily remove it as you replace the code with a more up to date api.

## How do I use it?

Currently awaiting approval by Sonatype so that it can be published to maven central. For now, you can just download and run 'mvn install' to deploy to your own local repository. You only need depend on 'date-converters-core'.

## How is it written?

I used Spring 5 and a bit of Spring Boot to provide simple autowiring. My first iterations of this depended on many interfaces and extensions of interfaces. If you wanted to write a converter for a new type, you were forced to write a converter between your new type and every other existing type. When you wanted to merge multiple type converters together, things got really messy.

## How does it work?

Simple! Out of the box, you get a converter for all core java 8+ Date types:

* java.util.Date
* java.sql.Date
* java.sql.Timestamp
* javax.xml.datatype.XMLGregorianCalendar
* java.time.LocalDate
* java.time.LocalDateTime
* java.time.ZonedDateTime

You just @Autowire an org.beirtipol.Converters in to your class and ask it to convert 'from' whatever type in to whatever type you want. Due to some magic, you get type-safe conversion and null-safety (if you give null, you get null, your problem).

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

## What about tests?

This project makes heavy use of JUnit 5 @ParameterisedTests. This allows the test class to be quite minimal, but easily extended. Have a look at [DateConvertersTest](https://github.com/beirtipol/date-converters/blob/master/date-converters-core-tests/src/main/java/com/beirtipol/dates/converter/DateConvertersTest.java) for the base implementation and [MyDateConvertersTest](https://github.com/beirtipol/date-converters/blob/master/date-converters-sample-extension/src/test/java/com/mydate/dates/MyDateConvertersTest.java) for a sample extension.

Tests are generated for each combination of 'from' Object, 'to' Class and Timezone in order to verify that converting works in the same way regardless of what timezone you are in. Last time I checked, **38,344** Unit tests were generated to run across all these iterations. While a sheer number of tests doesn't guarantee that this code is perfect, it does mean that each line is hit thousands of times in different ways to try all known combinations of state.  

Jacoco is set up to check coverage across all implementations of converters. Other than the odd bit of Exception Handling and unused hashCode(), it's guaranteed to have 100% coverage. I'd like to say you can view the results on the project's [CircleCI](https://app.circleci.com/github/beirtipol/date-converters/pipelines), but I haven't yet managed to get the jacoco data published in an easily-viewed format.

## How do I provide my own implementation of a converter?

You need to write a method in a spring-annotated class (like @Component) which follows this signature:

    @Bean
    @Converter(from = SomeDateType.class, to = SomeOtherDateType.class)
    public Function<SomeDateType, SomeOtherDateType> SomeDateTypeToSomeOtherDateType() {
        return from -> //convert 'from' in to 'to';
    }
    
The @Bean simply makes the method discoverable by Spring. The @Converter annotation allows the com.beirtipol.dates.Converter class to determine what the 'from' and 'to' types are so that it can index them and find a converter when you ask it to do a 'from'.

## This @Converter stuff looks pretty generic, you could use it for things other than dates?
Yeah, I could. I don't have a good use-case right now though! You should be able to provide any conversion you want though.