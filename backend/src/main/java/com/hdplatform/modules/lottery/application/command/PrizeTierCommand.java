package com.hdplatform.modules.lottery.application.command;

import com.hdplatform.modules.lottery.domain.valueobject.PrizeTierCode;

import java.util.List;

public record PrizeTierCommand(PrizeTierCode code, List<String> numbers) {
}
