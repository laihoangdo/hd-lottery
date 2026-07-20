package com.hdplatform.modules.lottery.domain.service;

import com.hdplatform.modules.lottery.domain.valueobject.PrizeTierCode;

import java.util.List;

public record TicketCheckResult(String ticketNumber, boolean winner, List<PrizeMatch> matches) {
    public TicketCheckResult {
        matches = List.copyOf(matches);
        if (winner != !matches.isEmpty()) {
            throw new IllegalArgumentException("winner must match prize results");
        }
    }

    public record PrizeMatch(PrizeTierCode prize, String winningNumber) {
    }
}
