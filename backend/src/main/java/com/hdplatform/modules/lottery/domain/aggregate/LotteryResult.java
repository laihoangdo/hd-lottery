package com.hdplatform.modules.lottery.domain.aggregate;

import com.hdplatform.modules.lottery.domain.valueobject.*;
import com.hdplatform.shared.domain.AuditableEntity;
import com.hdplatform.shared.domain.exception.DomainException;

import java.time.Instant;
import java.time.LocalDate;
import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public final class LotteryResult extends AuditableEntity<LotteryResultId> {
    private final String provinceCode;
    private final String provinceName;
    private final LotteryRegion region;
    private final LocalDate drawDate;
    private final Map<PrizeTierCode, PrizeTier> prizes;
    private LotteryResultStatus status;
    private Instant publishedAt;
    private long version;

    private LotteryResult(LotteryResultId id, String provinceCode, String provinceName,
                          LotteryRegion region, LocalDate drawDate, List<PrizeTier> prizes,
                          LotteryResultStatus status, Instant publishedAt, long version) {
        super(id);
        this.provinceCode = validProvinceCode(provinceCode);
        this.provinceName = validProvinceName(provinceName);
        this.region = Objects.requireNonNull(region);
        this.drawDate = Objects.requireNonNull(drawDate);
        this.prizes = new EnumMap<>(PrizeTierCode.class);
        prizes.forEach(prize -> {
            if (this.prizes.put(prize.code(), prize) != null) {
                throw new DomainException("Duplicate prize tier: " + prize.code());
            }
        });
        this.status = Objects.requireNonNull(status);
        this.publishedAt = publishedAt;
        if (version < 0) throw new IllegalArgumentException("version cannot be negative");
        this.version = version;
        validatePartialPrizeStructure(this.prizes);
    }

    public static LotteryResult draft(LotteryResultId id, String provinceCode,
                                      String provinceName, LotteryRegion region,
                                      LocalDate drawDate, List<PrizeTier> prizes, Instant now) {
        LotteryResult result = new LotteryResult(id, provinceCode, provinceName,
                region, drawDate, prizes, LotteryResultStatus.DRAFT, null, 0);
        result.markCreated(Objects.requireNonNull(now));
        return result;
    }

    public static LotteryResult restore(LotteryResultId id, String provinceCode,
                                        String provinceName, LotteryRegion region,
                                        LocalDate drawDate, List<PrizeTier> prizes,
                                        LotteryResultStatus status, Instant publishedAt,
                                        long version, Instant createdAt, Instant updatedAt) {
        LotteryResult result = new LotteryResult(id, provinceCode, provinceName,
                region, drawDate, prizes, status, publishedAt, version);
        result.createdAt = createdAt;
        result.updatedAt = updatedAt;
        return result;
    }

    public void publish(Instant now) {
        if (status == LotteryResultStatus.PUBLISHED) return;
        validateTraditionalPrizeStructure();
        status = LotteryResultStatus.PUBLISHED;
        publishedAt = Objects.requireNonNull(now);
        markUpdated(now);
    }

    public void startDrawing(Instant now) {
        if (status == LotteryResultStatus.PUBLISHED) {
            throw new DomainException("Published result is immutable");
        }
        if (status == LotteryResultStatus.DRAFT) {
            status = LotteryResultStatus.DRAWING;
            markUpdated(Objects.requireNonNull(now));
        }
    }

    public void replacePrizes(List<PrizeTier> replacement, Instant now) {
        if (status == LotteryResultStatus.PUBLISHED) {
            throw new DomainException("Published result is immutable");
        }
        EnumMap<PrizeTierCode, PrizeTier> updated = new EnumMap<>(PrizeTierCode.class);
        replacement.forEach(prize -> {
            if (updated.put(prize.code(), prize) != null) {
                throw new DomainException("Duplicate prize tier: " + prize.code());
            }
        });
        validatePartialPrizeStructure(updated);
        prizes.clear();
        prizes.putAll(updated);
        markUpdated(Objects.requireNonNull(now));
    }

    private void validatePartialPrizeStructure(Map<PrizeTierCode, PrizeTier> candidate) {
        Map<PrizeTierCode, PrizeRule> rules = rules();
        candidate.forEach((code, tier) -> {
            PrizeRule rule = rules.get(code);
            if (rule == null || tier.numbers().size() > rule.count()
                    || tier.numbers().stream()
                    .anyMatch(number -> number.value().length() != rule.digits())) {
                throw new DomainException("Invalid partial data for prize " + code);
            }
        });
    }

    private void validateTraditionalPrizeStructure() {
        Map<PrizeTierCode, PrizeRule> rules = rules();
        if (!prizes.keySet().equals(rules.keySet())) {
            throw new DomainException("Prize tiers are incomplete for region " + region);
        }
        rules.forEach((code, rule) -> {
            PrizeTier tier = prizes.get(code);
            if (tier.numbers().size() != rule.count()
                    || tier.numbers().stream().anyMatch(number -> number.value().length() != rule.digits())) {
                throw new DomainException("Invalid number count or length for prize " + code);
            }
        });
    }

    private Map<PrizeTierCode, PrizeRule> rules() {
        return region == LotteryRegion.NORTH ? northRules() : centralAndSouthRules();
    }

    private static Map<PrizeTierCode, PrizeRule> northRules() {
        return Map.of(
                PrizeTierCode.SPECIAL, new PrizeRule(1, 5),
                PrizeTierCode.FIRST, new PrizeRule(1, 5),
                PrizeTierCode.SECOND, new PrizeRule(2, 5),
                PrizeTierCode.THIRD, new PrizeRule(6, 5),
                PrizeTierCode.FOURTH, new PrizeRule(4, 4),
                PrizeTierCode.FIFTH, new PrizeRule(6, 4),
                PrizeTierCode.SIXTH, new PrizeRule(3, 3),
                PrizeTierCode.SEVENTH, new PrizeRule(4, 2));
    }

    private static Map<PrizeTierCode, PrizeRule> centralAndSouthRules() {
        return Map.ofEntries(
                Map.entry(PrizeTierCode.SPECIAL, new PrizeRule(1, 6)),
                Map.entry(PrizeTierCode.FIRST, new PrizeRule(1, 5)),
                Map.entry(PrizeTierCode.SECOND, new PrizeRule(1, 5)),
                Map.entry(PrizeTierCode.THIRD, new PrizeRule(2, 5)),
                Map.entry(PrizeTierCode.FOURTH, new PrizeRule(7, 5)),
                Map.entry(PrizeTierCode.FIFTH, new PrizeRule(1, 4)),
                Map.entry(PrizeTierCode.SIXTH, new PrizeRule(3, 4)),
                Map.entry(PrizeTierCode.SEVENTH, new PrizeRule(1, 3)),
                Map.entry(PrizeTierCode.EIGHTH, new PrizeRule(1, 2)));
    }

    private static String validProvinceCode(String value) {
        if (value == null || !value.trim().toUpperCase(Locale.ROOT).matches("[A-Z0-9_]{2,20}")) {
            throw new DomainException("Invalid province code");
        }
        return value.trim().toUpperCase(Locale.ROOT);
    }

    private static String validProvinceName(String value) {
        if (value == null || value.isBlank() || value.trim().length() > 100) {
            throw new DomainException("Invalid province name");
        }
        return value.trim();
    }

    private record PrizeRule(int count, int digits) {}

    public String getProvinceCode() { return provinceCode; }
    public String getProvinceName() { return provinceName; }
    public LotteryRegion getRegion() { return region; }
    public LocalDate getDrawDate() { return drawDate; }
    public List<PrizeTier> getPrizes() { return List.copyOf(prizes.values()); }
    public LotteryResultStatus getStatus() { return status; }
    public Instant getPublishedAt() { return publishedAt; }
    public long getVersion() { return version; }
}
