package com.hdplatform.modules.lottery.domain.aggregate;

import com.hdplatform.modules.lottery.domain.valueobject.*;
import com.hdplatform.shared.domain.exception.DomainException;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class LotteryResultTest {
    private static final Instant NOW = Instant.parse("2026-07-20T00:00:00Z");

    @Test
    void publishes_complete_northern_result() {
        LotteryResult result = draft(LotteryRegion.NORTH, northPrizes());

        result.publish(NOW.plusSeconds(60));

        assertThat(result.getStatus()).isEqualTo(LotteryResultStatus.PUBLISHED);
        assertThat(result.getPublishedAt()).isEqualTo(NOW.plusSeconds(60));
    }

    @Test
    void refuses_incomplete_result() {
        List<PrizeTier> incomplete = new ArrayList<>(northPrizes());
        incomplete.removeLast();
        LotteryResult result = draft(LotteryRegion.NORTH, incomplete);

        assertThatThrownBy(() -> result.publish(NOW.plusSeconds(60)))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("incomplete");
    }

    @Test
    void refuses_wrong_digit_length_for_region() {
        List<PrizeTier> invalid = new ArrayList<>(northPrizes());
        invalid.set(0, tier(PrizeTierCode.SPECIAL, 1, 6));

        assertThatThrownBy(() -> draft(LotteryRegion.NORTH, invalid))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("SPECIAL");
    }

    @Test
    void accepts_partial_prizes_during_drawing_but_keeps_published_result_immutable() {
        LotteryResult result = draft(LotteryRegion.NORTH, List.of());
        result.startDrawing(NOW.plusSeconds(30));
        result.replacePrizes(List.of(tier(PrizeTierCode.SEVENTH, 2, 2)),
                NOW.plusSeconds(60));

        assertThat(result.getStatus()).isEqualTo(LotteryResultStatus.DRAWING);
        assertThat(result.getPrizes()).hasSize(1);

        result.replacePrizes(northPrizes(), NOW.plusSeconds(90));
        result.publish(NOW.plusSeconds(120));
        assertThatThrownBy(() -> result.replacePrizes(List.of(), NOW.plusSeconds(180)))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("immutable");
    }

    @Test
    void invalid_realtime_update_does_not_mutate_existing_prizes() {
        LotteryResult result = draft(LotteryRegion.NORTH,
                List.of(tier(PrizeTierCode.SEVENTH, 1, 2)));

        assertThatThrownBy(() -> result.replacePrizes(
                List.of(tier(PrizeTierCode.SEVENTH, 1, 3)), NOW.plusSeconds(30)))
                .isInstanceOf(DomainException.class);
        assertThat(result.getPrizes().getFirst().numbers().getFirst().value()).hasSize(2);
    }

    private LotteryResult draft(LotteryRegion region, List<PrizeTier> prizes) {
        return LotteryResult.draft(LotteryResultId.newId(), "HANOI", "Hà Nội",
                region, LocalDate.of(2026, 7, 20), prizes, NOW);
    }

    public static List<PrizeTier> northPrizes() {
        return List.of(
                tier(PrizeTierCode.SPECIAL, 1, 5),
                tier(PrizeTierCode.FIRST, 1, 5),
                tier(PrizeTierCode.SECOND, 2, 5),
                tier(PrizeTierCode.THIRD, 6, 5),
                tier(PrizeTierCode.FOURTH, 4, 4),
                tier(PrizeTierCode.FIFTH, 6, 4),
                tier(PrizeTierCode.SIXTH, 3, 3),
                tier(PrizeTierCode.SEVENTH, 4, 2));
    }

    public static PrizeTier tier(PrizeTierCode code, int count, int digits) {
        List<WinningNumber> numbers = java.util.stream.IntStream.range(0, count)
                .mapToObj(index -> WinningNumber.of(
                        String.format("%0" + digits + "d", index + 1)))
                .toList();
        return new PrizeTier(code, numbers);
    }
}
