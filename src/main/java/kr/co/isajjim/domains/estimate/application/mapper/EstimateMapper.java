package kr.co.isajjim.domains.estimate.application.mapper;

import kr.co.isajjim.domains.estimate.application.response.EstimateDetailResponse;
import kr.co.isajjim.domains.estimate.persistence.entity.Estimate;
import kr.co.isajjim.domains.image.application.mapper.ImageMapper;
import kr.co.isajjim.domains.image.application.response.ImageResponse;
import kr.co.isajjim.domains.image.persistence.entity.Image;

import java.util.List;

public class EstimateMapper {
    public static Estimate toEstimate(
    ) {
        return Estimate.builder()
                .build();
    }

    public static EstimateDetailResponse fromEstimate(
            Estimate estimate
    ) {
        List<Image> images = estimate.getImages();
        List<ImageResponse> responses = images.stream().map(ImageMapper::fromImage).toList();

        return EstimateDetailResponse.builder()
                .aiStatus(estimate.getAiStatus())
                .images(responses)
                .build();
    }
}
