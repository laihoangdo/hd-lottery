package com.hdplatform.modules.cms.adapter.in.rest.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreatePageRequest(
        @NotBlank @Size(max = 160) String slug,
        @NotBlank @Size(max = 200) String title,
        @NotBlank String content
) {
}
