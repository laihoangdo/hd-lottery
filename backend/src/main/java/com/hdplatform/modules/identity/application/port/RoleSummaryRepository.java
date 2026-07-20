package com.hdplatform.modules.identity.application.port;

import com.hdplatform.modules.identity.application.query.RoleSummary;

import java.util.List;

public interface RoleSummaryRepository {
    List<RoleSummary> findPlatformRoles();
}
