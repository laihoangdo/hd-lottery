package com.hdplatform.modules.lottery.adapter.in.rest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

public record CheckTicketRequest(
        @NotBlank @Pattern(regexp = "[A-Za-z0-9_]{2,20}") String provinceCode,
        @NotNull LocalDate drawDate,
        @NotBlank @Pattern(regexp = "[0-9]{5,6}") String ticketNumber
) {
}
