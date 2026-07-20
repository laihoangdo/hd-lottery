package com.hdplatform.modules.lottery.application.command;

import java.time.LocalDate;

public record CheckTicketCommand(String provinceCode, LocalDate drawDate, String ticketNumber) {
}
