package com.demo.booking.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.ZonedDateTime;
import java.util.Optional;

@Configuration
@EnableJpaAuditing(dateTimeProviderRef = "auditingZonedDateTimeProvider")
public class JpaAuditingConfig {

    @Bean
    public DateTimeProvider auditingZonedDateTimeProvider() {
        return () -> Optional.of(ZonedDateTime.now());
    }
}