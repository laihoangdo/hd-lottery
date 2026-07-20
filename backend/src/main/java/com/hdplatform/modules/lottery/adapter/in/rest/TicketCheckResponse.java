package com.hdplatform.modules.lottery.adapter.in.rest;

import com.hdplatform.modules.lottery.domain.service.TicketCheckResult;
import com.hdplatform.modules.lottery.domain.valueobject.PrizeTierCode;

import java.util.List;

public record TicketCheckResponse(boolean winner, List<PrizeMatchResponse> matches) {
    static TicketCheckResponse from(TicketCheckResult result) {
        return new TicketCheckResponse(result.winner(), result.matches().stream()
                .map(match -> new PrizeMatchResponse(match.prize(), match.winningNumber()))
                .toList());
    }

    public record PrizeMatchResponse(PrizeTierCode prize, String winningNumber) {
    }
}
