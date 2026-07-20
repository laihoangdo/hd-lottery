package com.hdplatform.modules.analytics.application.query;

import java.time.LocalDate;

public record DailyEventCount(LocalDate date, long count) {
}
