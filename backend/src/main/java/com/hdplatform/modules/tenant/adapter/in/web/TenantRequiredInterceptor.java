package com.hdplatform.modules.tenant.adapter.in.web;

import com.hdplatform.modules.tenant.application.context.TenantContextHolder;
import com.hdplatform.shared.exception.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class TenantRequiredInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) {
        if (handler instanceof HandlerMethod handlerMethod && requiresTenant(handlerMethod)) {
            TenantContextHolder.current().orElseThrow(() -> new NotFoundException(
                    "TENANT_NOT_FOUND",
                    "Website not found or unavailable"));
        }
        return true;
    }

    private boolean requiresTenant(HandlerMethod handlerMethod) {
        return AnnotatedElementUtils.hasAnnotation(handlerMethod.getMethod(), TenantRequired.class)
                || AnnotatedElementUtils.hasAnnotation(handlerMethod.getBeanType(), TenantRequired.class);
    }
}
