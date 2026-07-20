package com.hdplatform.modules.lottery.adapter.out.persistence;

import com.hdplatform.modules.lottery.domain.valueobject.LotteryRegion;
import com.hdplatform.modules.lottery.domain.valueobject.LotteryResultStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;
import com.fasterxml.jackson.databind.JsonNode;

@Entity
@Table(name = "lottery_results")
@Getter @Setter @NoArgsConstructor
public class LotteryResultEntity {
    @Id private UUID id;
    @Column(name = "province_code", nullable = false, length = 20) private String provinceCode;
    @Column(name = "province_name", nullable = false, length = 100) private String provinceName;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 20) private LotteryRegion region;
    @Column(name = "draw_date", nullable = false) private LocalDate drawDate;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "prizes_json", nullable = false, columnDefinition = "jsonb") private JsonNode prizesJson;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 20) private LotteryResultStatus status;
    @Column(name = "published_at") private Instant publishedAt;
    @Column(name = "created_at", nullable = false, updatable = false) private Instant createdAt;
    @Column(name = "updated_at", nullable = false) private Instant updatedAt;
    @Version @Column(nullable = false) private long version;
}
