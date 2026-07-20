package com.hdplatform.modules.lottery.domain.service;

import com.hdplatform.modules.lottery.domain.aggregate.LotteryResult;
import com.hdplatform.modules.lottery.domain.valueobject.LotteryRegion;
import com.hdplatform.modules.lottery.domain.valueobject.LotteryResultStatus;
import com.hdplatform.shared.domain.exception.DomainException;

import java.util.ArrayList;
import java.util.List;

public final class TicketChecker {
    private TicketChecker() {
    }

    public static TicketCheckResult check(LotteryResult result, String ticketNumber) {
        if (result.getStatus() != LotteryResultStatus.PUBLISHED) {
            throw new DomainException("Only published result can be used to check a ticket");
        }
        String ticket = ticketNumber == null ? "" : ticketNumber.trim();
        int expectedLength = result.getRegion() == LotteryRegion.NORTH ? 5 : 6;
        if (!ticket.matches("[0-9]{" + expectedLength + "}")) {
            throw new DomainException(
                    "Ticket number must contain exactly " + expectedLength + " digits");
        }
        List<TicketCheckResult.PrizeMatch> matches = new ArrayList<>();
        result.getPrizes().forEach(prize -> prize.numbers().forEach(winningNumber -> {
            if (ticket.endsWith(winningNumber.value())) {
                matches.add(new TicketCheckResult.PrizeMatch(
                        prize.code(), winningNumber.value()));
            }
        }));
        return new TicketCheckResult(ticket, !matches.isEmpty(), matches);
    }
}
