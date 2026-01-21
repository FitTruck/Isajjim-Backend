package kr.co.isajjim.domains.estimate.domain.service;

import kr.co.isajjim.domains.estimate.application.mapper.EstimateMapper;
import kr.co.isajjim.domains.estimate.application.request.EstimateRequest;
import kr.co.isajjim.domains.estimate.application.request.EstimateUpdateRequest;
import kr.co.isajjim.domains.estimate.domain.constant.AIStatus;
import kr.co.isajjim.domains.estimate.persistence.entity.Estimate;
import kr.co.isajjim.domains.estimate.persistence.repository.EstimateRepository;
import kr.co.isajjim.domains.image.application.mapper.ImageMapper;
import kr.co.isajjim.domains.image.application.response.ImageAnalysisDto;
import kr.co.isajjim.domains.image.domain.service.ImageService;
import kr.co.isajjim.global.common.ResponseCode;
import kr.co.isajjim.global.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EstimateService {

    private final EstimateRepository estimateRepository;
    private final ImageService imageService;

    public Estimate getEstimateById(Long estimateId) {
        return getOrThrow(estimateId);
    }

    public Long createEstimate(EstimateRequest request) {
        Estimate estimate = EstimateMapper.toEstimate();
        request.imageUrls().forEach(url -> estimate.addImage(ImageMapper.toImage(url)));

        return estimateRepository.save(estimate).getId();
    }

    public List<ImageAnalysisDto> getImageDtos(Long estimateId) {
        return imageService.getAllByEstimateId(estimateId).stream()
                .map(img -> new ImageAnalysisDto(img.getId(), img.getImageUrl()))
                .toList();
    }

    public void updateDefaultInfo(Estimate estimate, EstimateUpdateRequest request) {
        estimate.updateEstimate(
                request.buildingType(),
                request.roomSize(),
                request.floor(),
                request.elevator(),
                request.ladderTruck(),
                request.roomType(),
                request.duplex(),
                request.groundStair(),
                request.parking()
        );
    }

    @Transactional
    public void updateAIStatus(Long estimateId, AIStatus status) {
        Estimate estimate = getOrThrow(estimateId);
        estimate.updateAIStatus(status);
    }

    /* HELPER METHOD */
    private Estimate getOrThrow(Long id) {
        return estimateRepository.findById(id)
                .orElseThrow(() -> new BaseException(ResponseCode.NOT_FOUND));
    }
}
