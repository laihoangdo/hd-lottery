package com.hdplatform.modules.lottery.application.service;

import com.hdplatform.modules.lottery.application.command.CreateLotteryResultCommand;
import com.hdplatform.modules.lottery.application.command.PrizeTierCommand;
import com.hdplatform.modules.lottery.application.command.ReplaceLotteryPrizesCommand;
import com.hdplatform.modules.lottery.domain.aggregate.LotteryResultId;
import com.hdplatform.modules.lottery.application.port.LotteryResultRepository;
import com.hdplatform.modules.lottery.domain.aggregate.LotteryResult;
import com.hdplatform.modules.lottery.domain.aggregate.LotteryResultTest;
import com.hdplatform.modules.lottery.domain.valueobject.LotteryRegion;
import com.hdplatform.shared.exception.ConflictException;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class LotteryResultServiceTest {
    private final LotteryResultRepository repository = mock(LotteryResultRepository.class);
    private final LotteryResultService service = new LotteryResultService(
            repository, () -> Instant.parse("2026-07-20T00:00:00Z"));

    @Test
    void creates_one_canonical_result_without_tenant_duplication() {
        LocalDate date = LocalDate.of(2026, 7, 20);
        when(repository.existsByProvinceAndDate("HANOI", date)).thenReturn(false);
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        CreateLotteryResultCommand command = new CreateLotteryResultCommand(
                "hanoi", "Hà Nội", LotteryRegion.NORTH, date,
                LotteryResultTest.northPrizes().stream()
                        .map(prize -> new PrizeTierCommand(prize.code(), prize.numbers().stream()
                                .map(number -> number.value()).toList()))
                        .toList());

        LotteryResult result = service.createDraft(command);

        assertThat(result.getProvinceCode()).isEqualTo("HANOI");
        verify(repository).existsByProvinceAndDate("HANOI", date);
    }

    @Test
    void rejects_duplicate_province_and_draw_date() {
        LocalDate date = LocalDate.of(2026, 7, 20);
        when(repository.existsByProvinceAndDate("HANOI", date)).thenReturn(true);
        CreateLotteryResultCommand command = new CreateLotteryResultCommand(
                "HANOI", "Hà Nội", LotteryRegion.NORTH, date, java.util.List.of());

        assertThatThrownBy(() -> service.createDraft(command))
                .isInstanceOf(ConflictException.class)
                .hasMessage("Lottery result already exists");
    }

    @Test
    void rejects_stale_realtime_update_version() {
        LotteryResult result = LotteryResult.restore(
                LotteryResultId.newId(), "HANOI", "Hà Nội", LotteryRegion.NORTH,
                LocalDate.of(2026, 7, 20), java.util.List.of(),
                com.hdplatform.modules.lottery.domain.valueobject.LotteryResultStatus.DRAWING,
                null, 3, Instant.parse("2026-07-20T00:00:00Z"),
                Instant.parse("2026-07-20T00:01:00Z"));
        when(repository.findById(result.getId())).thenReturn(Optional.of(result));

        assertThatThrownBy(() -> service.replacePrizes(
                result.getId(), new ReplaceLotteryPrizesCommand(2, java.util.List.of())))
                .isInstanceOf(ConflictException.class)
                .hasMessage("Lottery result was updated by another request");
    }
}
