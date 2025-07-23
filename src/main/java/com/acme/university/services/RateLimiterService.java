package com.acme.university.services;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import io.github.bucket4j.Bandwidth;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

@Service
public class RateLimiterService {
    private static final Logger logger = LoggerFactory.getLogger(RateLimiterService.class);

    private final Bucket bucket;

    public RateLimiterService() {
        logger.info("Initializing RateLimiterService");

        Bandwidth limit = Bandwidth.classic(100, Refill.intervally(10, Duration.ofMinutes(1)));
        this.bucket = Bucket4j.builder().addLimit(limit).build();

        logger.debug("RateLimiterService initialized with limit: {} requests per minute", 100);
    }

    public boolean isRateLimited() {
        boolean allowed = bucket.tryConsume(1);
        if (!allowed) {
            logger.debug("Rate limit reached");
        }
        return allowed;
    }
}
