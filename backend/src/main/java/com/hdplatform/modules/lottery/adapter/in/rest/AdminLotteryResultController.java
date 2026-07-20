package com.hdplatform.modules.lottery.adapter.in.rest;

import com.hdplatform.modules.lottery.application.command.CreateLotteryResultCommand;
import com.hdplatform.modules.lottery.application.command.PrizeTierCommand;
import com.hdplatform.modules.lottery.application.command.ReplaceLotteryPrizesCommand;
import com.hdplatform.modules.lottery.application.service.LotteryResultService;
import com.hdplatform.modules.lottery.domain.aggregate.LotteryResultId;
import com.hdplatform.shared.authorization.AuthorizationExpressions;
import com.hdplatform.shared.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/lottery/results")
@PreAuthorize(AuthorizationExpressions.LOTTERY_RESULT_MANAGE)
public class AdminLotteryResultController {
    private final LotteryResultService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<LotteryResultResponse> create(
            @Valid @RequestBody CreateLotteryResultRequest request) {
        return ApiResponse.success(LotteryResultResponse.from(service.createDraft(
                new CreateLotteryResultCommand(
                        request.provinceCode(), request.provinceName(), request.region(),
                        request.drawDate(), request.prizes().stream()
                        .map(prize -> new PrizeTierCommand(prize.code(), prize.numbers()))
                        .toList()))));
    }

    @PostMapping("/{id}/publish")
    public ApiResponse<LotteryResultResponse> publish(@PathVariable UUID id) {
        return ApiResponse.success(LotteryResultResponse.from(
                service.publish(LotteryResultId.of(id))));
    }

    @PostMapping("/{id}/start")
    public ApiResponse<LotteryResultResponse> startDrawing(@PathVariable UUID id) {
        return ApiResponse.success(LotteryResultResponse.from(
                service.startDrawing(LotteryResultId.of(id))));
    }

    @PutMapping("/{id}/prizes")
    public ApiResponse<LotteryResultResponse> replacePrizes(
            @PathVariable UUID id,
            @Valid @RequestBody ReplaceLotteryPrizesRequest request) {
        return ApiResponse.success(LotteryResultResponse.from(service.replacePrizes(
                LotteryResultId.of(id),
                new ReplaceLotteryPrizesCommand(request.expectedVersion(),
                        request.prizes().stream()
                                .map(prize -> new PrizeTierCommand(
                                        prize.code(), prize.numbers()))
                                .toList()))));
    }
}
