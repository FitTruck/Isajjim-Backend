package kr.co.isajjim.domains.credit.application.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record AdminCreditBalanceOverviewResponse(
        @Schema(description = "유저 ID", example = "1")
        Long userId,

        @Schema(description = "업체명", example = "이삿찜 이사")
        String companyName,

        @Schema(description = "대표자명", example = "홍길동")
        String representativeName,

        @Schema(description = "현재 크레딧 잔액", example = "50000")
        Long balance
) {
}
