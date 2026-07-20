package com.hdplatform.modules.media.adapter.out.storage;

import com.hdplatform.modules.media.application.service.MediaPolicy;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(MediaStorageProperties.class)
public class MediaConfig {
    @Bean
    MediaPolicy mediaPolicy(MediaStorageProperties properties) {
        return new MediaPolicy(properties.maxSizeBytes(), properties.allowedContentTypes());
    }
}
