package com.hdplatform.modules.tenant.adapter.in.web;

import com.hdplatform.modules.tenant.application.context.TenantContext;
import com.hdplatform.modules.tenant.application.context.TenantContextHolder;
import com.hdplatform.modules.tenant.domain.aggregate.TenantId;
import com.hdplatform.modules.tenant.domain.valueobject.SiteKey;
import com.hdplatform.shared.exception.NotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.method.HandlerMethod;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TenantRequiredInterceptorTest {

    private final TenantRequiredInterceptor interceptor = new TenantRequiredInterceptor();
    private final ExampleController controller = new ExampleController();

    @AfterEach
    void clearContext() {
        TenantContextHolder.clear();
    }

    @Test
    void rejects_tenant_scoped_endpoint_without_an_active_tenant() throws Exception {
        HandlerMethod handler = new HandlerMethod(
                controller,
                ExampleController.class.getDeclaredMethod("tenantEndpoint"));

        assertThatThrownBy(() -> preHandle(handler))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Website not found or unavailable");
    }

    @Test
    void allows_tenant_scoped_endpoint_with_an_active_tenant() throws Exception {
        TenantContextHolder.set(new TenantContext(
                TenantId.newId(),
                SiteKey.of("dealer-one")));
        HandlerMethod handler = new HandlerMethod(
                controller,
                ExampleController.class.getDeclaredMethod("tenantEndpoint"));

        assertThat(preHandle(handler)).isTrue();
    }

    @Test
    void does_not_require_tenant_for_platform_admin_endpoint() throws Exception {
        HandlerMethod handler = new HandlerMethod(
                controller,
                ExampleController.class.getDeclaredMethod("platformEndpoint"));

        assertThat(preHandle(handler)).isTrue();
    }

    private boolean preHandle(HandlerMethod handler) throws Exception {
        return interceptor.preHandle(
                new MockHttpServletRequest(),
                new MockHttpServletResponse(),
                handler);
    }

    private static class ExampleController {

        @TenantRequired
        void tenantEndpoint() {
        }

        void platformEndpoint() {
        }
    }
}
