package com.hdplatform.shared.domain;

import java.time.Instant;

public interface ClockProvider {

    Instant now();

}