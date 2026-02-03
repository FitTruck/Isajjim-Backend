package kr.co.isajjim.domains.estimate.application.mapper;

import kr.co.isajjim.domains.estimate.application.response.EstimateChatSummaryResponse;

public class EstimateChatSummaryMapper {
    public static EstimateChatSummaryResponse toEstimateChatSummaryResponse(
            String summary
    ) {
        return EstimateChatSummaryResponse.builder()
                .summary(summary)
                .build();
    }
}
