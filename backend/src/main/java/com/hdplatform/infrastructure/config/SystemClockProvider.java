package com.hdplatform.infrastructure.config;

import com.hdplatform.shared.domain.ClockProvider;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class SystemClockProvider implements ClockProvider {

    @Override
    public Instant now() {
        return Instant.now();
    }

}