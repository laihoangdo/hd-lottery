package com.hdplatform.modules.media.adapter.out.persistence;

import com.hdplatform.modules.media.domain.valueobject.MediaStatus;
import com.hdplatform.modules.media.domain.valueobject.MediaVisibility;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "media_assets")
@Getter @Setter @NoArgsConstructor
public class MediaAssetEntity {
    @Id private UUID id;
    @Column(name = "tenant_id", nullable = false, updatable = false) private UUID tenantId;
    @Column(name = "object_key", nullable = false, unique = true, length = 500) private String objectKey;
    @Column(name = "original_name", nullable = false, length = 255) private String originalName;
    @Column(name = "content_type", nullable = false, length = 100) private String contentType;
    @Column(name = "size_bytes", nullable = false) private long sizeBytes;
    @Column(name = "checksum_sha256", nullable = false, length = 64) private String checksumSha256;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 20) private MediaStatus status;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 20) private MediaVisibility visibility;
    @Column(name = "created_at", nullable = false, updatable = false) private Instant createdAt;
    @Column(name = "updated_at", nullable = false) private Instant updatedAt;
}
