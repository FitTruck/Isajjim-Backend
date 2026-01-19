package kr.co.isajjim.domains.estimate.application.request;

import java.util.List;

public record EstimateRequest(
        List<String> imageUrls
) {
}
