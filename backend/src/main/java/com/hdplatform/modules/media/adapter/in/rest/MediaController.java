package com.hdplatform.modules.media.adapter.in.rest;

import com.hdplatform.modules.media.application.service.MediaService;
import com.hdplatform.modules.media.domain.aggregate.MediaAssetId;
import com.hdplatform.modules.tenant.adapter.in.web.TenantRequired;
import com.hdplatform.shared.authorization.AuthorizationExpressions;
import com.hdplatform.shared.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@TenantRequired
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/media")
@PreAuthorize(AuthorizationExpressions.MEDIA_ASSET_WRITE)
public class MediaController {
    private final MediaService mediaService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<MediaAssetResponse> upload(@RequestParam("file") MultipartFile file)
            throws IOException {
        return ApiResponse.success(MediaAssetResponse.from(mediaService.upload(
                file.getOriginalFilename(), file.getContentType(), file.getSize(),
                file.getInputStream())));
    }

    @PostMapping("/{id}/publish")
    public ApiResponse<MediaAssetResponse> publish(@PathVariable UUID id) {
        return ApiResponse.success(MediaAssetResponse.from(
                mediaService.publish(MediaAssetId.of(id))));
    }
}
