package com.acme.university;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
@EnableCaching
public class UniversityApplication {
    private static final Logger logger = LoggerFactory.getLogger(UniversityApplication.class);

    public static void main(String[] args) {// Load .env variables into system properties BEFORE Spring starts
        logger.info("Starting UniversityApplication...");
        Dotenv dotenv = Dotenv.configure()
                .directory("./")
                .ignoreIfMissing()
                .load();

        dotenv.entries().forEach(entry ->
                System.setProperty(entry.getKey(), entry.getValue())
        );

        logger.debug("Loaded .env variables and set system properties");
        SpringApplication.run(UniversityApplication.class, args);
        logger.info("UniversityApplication started successfully.");
    }

}
