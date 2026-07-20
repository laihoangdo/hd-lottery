package com.hdplatform.modules.cms.adapter.in.rest.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdatePageRequest(
        @NotBlank @Size(max = 200) String title,
        @NotNull String content
) {
}
