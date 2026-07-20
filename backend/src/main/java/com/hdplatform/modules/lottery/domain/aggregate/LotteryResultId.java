package com.hdplatform.modules.lottery.domain.aggregate;

import com.hdplatform.shared.domain.identifier.UUIDIdentifier;

import java.util.UUID;

public final class LotteryResultId extends UUIDIdentifier {
    private LotteryResultId(UUID value) { super(value); }
    public static LotteryResultId newId() { return new LotteryResultId(UUID.randomUUID()); }
    public static LotteryResultId of(UUID value) { return new LotteryResultId(value); }
}
