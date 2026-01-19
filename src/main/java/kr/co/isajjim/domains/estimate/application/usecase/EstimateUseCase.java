package kr.co.isajjim.domains.estimate.application.usecase;

import kr.co.isajjim.domains.estimate.application.mapper.EstimateMapper;
import kr.co.isajjim.domains.estimate.application.request.EstimateRequest;
import kr.co.isajjim.domains.estimate.domain.service.EstimateService;
import kr.co.isajjim.domains.estimate.persistence.entity.Estimate;
import kr.co.isajjim.domains.image.application.mapper.ImageMapper;
import kr.co.isajjim.domains.image.persistence.entity.Image;
import kr.co.isajjim.global.annotation.UseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EstimateUseCase {

    private final EstimateService estimateService;

    @Transactional
    public Long createEstimate(EstimateRequest request) {
        Estimate estimate = EstimateMapper.toEstimate();
        request.imageUrls()
                .forEach(imageUrl -> {
                            Image image = ImageMapper.toImage(imageUrl);
                            estimate.addImage(image);
                        }
                );

        return estimateService.createEstimate(estimate);
    }
}
