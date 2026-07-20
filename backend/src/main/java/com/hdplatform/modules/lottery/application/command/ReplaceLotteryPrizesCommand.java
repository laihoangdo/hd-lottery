package com.hdplatform.modules.lottery.application.command;

import java.util.List;

public record ReplaceLotteryPrizesCommand(long expectedVersion, List<PrizeTierCommand> prizes) {
}
