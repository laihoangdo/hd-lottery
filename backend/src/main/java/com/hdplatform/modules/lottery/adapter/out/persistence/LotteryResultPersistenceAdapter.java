package com.hdplatform.modules.lottery.adapter.out.persistence;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.hdplatform.modules.lottery.application.port.LotteryResultRepository;
import com.hdplatform.modules.lottery.domain.aggregate.LotteryResult;
import com.hdplatform.modules.lottery.domain.aggregate.LotteryResultId;
import com.hdplatform.modules.lottery.domain.valueobject.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class LotteryResultPersistenceAdapter implements LotteryResultRepository {
    private final LotteryResultJpaRepository repository;
    private final ObjectMapper objectMapper;

    @Override
    public LotteryResult save(LotteryResult result) {
        return toAggregate(repository.saveAndFlush(toEntity(result)));
    }

    @Override
    public Optional<LotteryResult> findById(LotteryResultId id) {
        return repository.findById(id.getValue()).map(this::toAggregate);
    }

    @Override
    public boolean existsByProvinceAndDate(String provinceCode, LocalDate drawDate) {
        return repository.existsByProvinceCodeAndDrawDate(provinceCode, drawDate);
    }

    @Override
    public List<LotteryResult> findPublished(LocalDate drawDate, LotteryRegion region) {
        return repository.findByDrawDateAndRegionAndStatusOrderByProvinceName(
                        drawDate, region, LotteryResultStatus.PUBLISHED)
                .stream().map(this::toAggregate).toList();
    }

    @Override
    public List<LotteryResult> findLive(LocalDate drawDate, LotteryRegion region) {
        return repository.findByDrawDateAndRegionAndStatusInOrderByProvinceName(
                        drawDate, region,
                        List.of(LotteryResultStatus.DRAWING, LotteryResultStatus.PUBLISHED))
                .stream().map(this::toAggregate).toList();
    }

    @Override
    public Optional<LotteryResult> findPublishedByProvinceAndDate(
            String provinceCode, LocalDate drawDate) {
        return repository.findByProvinceCodeAndDrawDateAndStatus(
                        provinceCode, drawDate, LotteryResultStatus.PUBLISHED)
                .map(this::toAggregate);
    }

    private LotteryResultEntity toEntity(LotteryResult result) {
        LotteryResultEntity entity = new LotteryResultEntity();
        entity.setId(result.getId().getValue());
        entity.setProvinceCode(result.getProvinceCode());
        entity.setProvinceName(result.getProvinceName());
        entity.setRegion(result.getRegion());
        entity.setDrawDate(result.getDrawDate());
        entity.setPrizesJson(writePrizes(result.getPrizes()));
        entity.setStatus(result.getStatus());
        entity.setPublishedAt(result.getPublishedAt());
        entity.setCreatedAt(result.getCreatedAt());
        entity.setUpdatedAt(result.getUpdatedAt());
        entity.setVersion(result.getVersion());
        return entity;
    }

    private LotteryResult toAggregate(LotteryResultEntity entity) {
        return LotteryResult.restore(LotteryResultId.of(entity.getId()),
                entity.getProvinceCode(), entity.getProvinceName(), entity.getRegion(),
                entity.getDrawDate(), readPrizes(entity.getPrizesJson()), entity.getStatus(),
                entity.getPublishedAt(), entity.getVersion(),
                entity.getCreatedAt(), entity.getUpdatedAt());
    }

    private JsonNode writePrizes(List<PrizeTier> prizes) {
        return objectMapper.valueToTree(prizes.stream()
                .map(prize -> new PrizeJson(prize.code(), prize.numbers().stream()
                        .map(WinningNumber::value).toList())).toList());
    }

    private List<PrizeTier> readPrizes(JsonNode json) {
        try {
            List<PrizeJson> values = objectMapper.convertValue(
                    json, new TypeReference<List<PrizeJson>>() {});
            return values.stream().map(value -> new PrizeTier(
                    value.code(), value.numbers().stream().map(WinningNumber::of).toList()))
                    .toList();
        } catch (IllegalArgumentException exception) {
            throw new IllegalStateException("Unable to deserialize lottery prizes", exception);
        }
    }

    private record PrizeJson(PrizeTierCode code, List<String> numbers) {}
}
