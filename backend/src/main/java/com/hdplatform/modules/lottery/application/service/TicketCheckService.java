package com.hdplatform.modules.lottery.application.service;

import com.hdplatform.modules.lottery.application.command.CheckTicketCommand;
import com.hdplatform.modules.lottery.application.port.LotteryResultRepository;
import com.hdplatform.modules.lottery.domain.aggregate.LotteryResult;
import com.hdplatform.modules.lottery.domain.service.TicketCheckResult;
import com.hdplatform.modules.lottery.domain.service.TicketChecker;
import com.hdplatform.shared.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class TicketCheckService {
    private final LotteryResultRepository repository;

    @Transactional(readOnly = true)
    public TicketCheckResult check(CheckTicketCommand command) {
        String provinceCode = command.provinceCode().trim().toUpperCase(Locale.ROOT);
        LotteryResult result = repository.findPublishedByProvinceAndDate(
                        provinceCode, command.drawDate())
                .orElseThrow(() -> new NotFoundException(
                        "LOTTERY_RESULT_NOT_FOUND",
                        "Published lottery result not found"));
        return TicketChecker.check(result, command.ticketNumber());
    }
}
