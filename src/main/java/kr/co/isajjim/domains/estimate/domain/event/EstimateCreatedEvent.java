package kr.co.isajjim.domains.estimate.domain.event;

import kr.co.isajjim.domains.image.application.response.ImageAnalysisDto;

import java.util.List;

public record EstimateCreatedEvent(
    Long estimateId,
    List<ImageAnalysisDto> imageDtos
) {}