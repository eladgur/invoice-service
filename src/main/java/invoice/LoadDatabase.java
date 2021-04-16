package invoice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

@Configuration
class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(InvoiceRepository repository) {

        return args -> {
            log.info("Preloading " + repository.save(new Invoice("1", 14.0f, new Date(121, 12, 1), "asd", "awesomeComp", "e@awesomeComp.com")));
            log.info("Preloading " + repository.save(new Invoice("2", 54.0f, new Date(121, 11, 1), "aa", "ddd", "e@ddd.com")));
        };
    }
}