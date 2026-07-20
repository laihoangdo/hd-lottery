package com.hdplatform.modules.platformcatalog.adapter.out;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "website_templates", uniqueConstraints =
        @UniqueConstraint(name = "uk_website_templates_id_vertical", columnNames = {"id", "vertical_id"}))
@Getter @Setter @NoArgsConstructor
public class WebsiteTemplateEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID verticalId;
    @Column(nullable = false, unique = true, length = 100) private String code;
    @Column(nullable = false, length = 150) private String name;
    @JdbcTypeCode(SqlTypes.JSON) @Column(nullable = false, columnDefinition = "jsonb") private JsonNode layoutConfig;
    @JdbcTypeCode(SqlTypes.JSON) @Column(nullable = false, columnDefinition = "jsonb") private JsonNode defaultColors;
    @Column(nullable = false) private boolean active;
    @Column(nullable = false) private Instant createdAt;
    @Column(nullable = false) private Instant updatedAt;
}
