package com.hdplatform.modules.tenant.adapter.in.web;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class TenantWebMvcConfig implements WebMvcConfigurer {

    private final TenantRequiredInterceptor tenantRequiredInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tenantRequiredInterceptor);
    }
}
