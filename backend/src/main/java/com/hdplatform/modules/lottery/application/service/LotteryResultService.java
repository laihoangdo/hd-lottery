package com.hdplatform.modules.lottery.application.service;

import com.hdplatform.modules.lottery.application.command.CreateLotteryResultCommand;
import com.hdplatform.modules.lottery.application.port.LotteryResultRepository;
import com.hdplatform.modules.lottery.application.command.ReplaceLotteryPrizesCommand;
import com.hdplatform.modules.lottery.domain.aggregate.LotteryResult;
import com.hdplatform.modules.lottery.domain.aggregate.LotteryResultId;
import com.hdplatform.modules.lottery.domain.valueobject.LotteryRegion;
import com.hdplatform.modules.lottery.domain.valueobject.PrizeTier;
import com.hdplatform.modules.lottery.domain.valueobject.WinningNumber;
import com.hdplatform.shared.domain.ClockProvider;
import com.hdplatform.shared.exception.ConflictException;
import com.hdplatform.shared.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class LotteryResultService {
    private final LotteryResultRepository repository;
    private final ClockProvider clock;

    @Transactional
    public LotteryResult createDraft(CreateLotteryResultCommand command) {
        String provinceCode = command.provinceCode().trim().toUpperCase(Locale.ROOT);
        if (repository.existsByProvinceAndDate(provinceCode, command.drawDate())) {
            throw new ConflictException(
                    "LOTTERY_RESULT_EXISTS", "Lottery result already exists");
        }
        List<PrizeTier> prizes = command.prizes().stream()
                .map(prize -> new PrizeTier(prize.code(), prize.numbers().stream()
                        .map(WinningNumber::of).toList()))
                .toList();
        return repository.save(LotteryResult.draft(
                LotteryResultId.newId(), provinceCode, command.provinceName(),
                command.region(), command.drawDate(), prizes, clock.now()));
    }

    @Transactional
    public LotteryResult publish(LotteryResultId id) {
        LotteryResult result = repository.findById(id).orElseThrow(this::notFound);
        result.publish(clock.now());
        return repository.save(result);
    }

    @Transactional
    public LotteryResult startDrawing(LotteryResultId id) {
        LotteryResult result = repository.findById(id).orElseThrow(this::notFound);
        result.startDrawing(clock.now());
        return repository.save(result);
    }

    @Transactional
    public LotteryResult replacePrizes(LotteryResultId id, ReplaceLotteryPrizesCommand command) {
        LotteryResult result = repository.findById(id).orElseThrow(this::notFound);
        if (result.getVersion() != command.expectedVersion()) {
            throw new ConflictException(
                    "LOTTERY_RESULT_VERSION_CONFLICT",
                    "Lottery result was updated by another request");
        }
        result.replacePrizes(command.prizes().stream()
                .map(prize -> new PrizeTier(prize.code(), prize.numbers().stream()
                        .map(WinningNumber::of).toList())).toList(), clock.now());
        return repository.save(result);
    }

    @Transactional(readOnly = true)
    public List<LotteryResult> published(LocalDate date, LotteryRegion region) {
        return repository.findPublished(date, region);
    }

    @Transactional(readOnly = true)
    public List<LotteryResult> live(LocalDate date, LotteryRegion region) {
        return repository.findLive(date, region);
    }

    private NotFoundException notFound() {
        return new NotFoundException("LOTTERY_RESULT_NOT_FOUND", "Lottery result not found");
    }
}
