package com.hdplatform.system.presentation;

import com.hdplatform.shared.response.ApiResponse;
import com.hdplatform.system.presentation.dto.HealthResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/api/v1/system/health")
    public ApiResponse<HealthResponse> health() {

        return ApiResponse.success(

                new HealthResponse(

                        "UP",

                        "0.0.1"

                )

        );

    }

}