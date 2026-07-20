package com.hdplatform.modules.media.application.service;

import java.io.InputStream;

public record MediaDownload(String originalName, String contentType, long sizeBytes,
                            String checksumSha256, InputStream content) {
}
