package kr.co.isajjim.domains.estimate.application.response;

import lombok.Builder;

@Builder
public record EstimateChatSummaryResponse(
        String summary
) {
}
