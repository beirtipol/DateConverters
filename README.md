# DateConverters
Library for providing conversion between any date types

See below for sample usage

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
