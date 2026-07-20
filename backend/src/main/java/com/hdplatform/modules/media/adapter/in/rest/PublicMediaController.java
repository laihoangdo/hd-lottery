package com.hdplatform.modules.media.adapter.in.rest;

import com.hdplatform.modules.media.application.service.MediaDownload;
import com.hdplatform.modules.media.application.service.MediaService;
import com.hdplatform.modules.media.domain.aggregate.MediaAssetId;
import com.hdplatform.modules.analytics.application.AnalyticsEvents;
import com.hdplatform.modules.analytics.application.service.AnalyticsTracker;
import com.hdplatform.modules.tenant.application.context.TenantContextHolder;
import com.hdplatform.modules.tenant.adapter.in.web.TenantRequired;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.CacheControl;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.UUID;

@RestController
@TenantRequired
@RequiredArgsConstructor
@RequestMapping("/api/v1/public/media")
public class PublicMediaController {
    private final MediaService mediaService;
    private final AnalyticsTracker analyticsTracker;

    @GetMapping("/{id}")
    public ResponseEntity<InputStreamResource> download(@PathVariable UUID id) throws IOException {
        MediaDownload media = mediaService.downloadPublic(MediaAssetId.of(id));
        analyticsTracker.recordBestEffort(
                TenantContextHolder.requireCurrent().tenantId(), AnalyticsEvents.MEDIA_DOWNLOAD);
        ContentDisposition disposition = ContentDisposition.inline()
                .filename(media.originalName(), StandardCharsets.UTF_8)
                .build();
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(media.contentType()))
                .contentLength(media.sizeBytes())
                .eTag('"' + media.checksumSha256() + '"')
                .cacheControl(CacheControl.maxAge(Duration.ofMinutes(5)).cachePublic())
                .header(HttpHeaders.CONTENT_DISPOSITION, disposition.toString())
                .body(new InputStreamResource(media.content()));
    }
}
