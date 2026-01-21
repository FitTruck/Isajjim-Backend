package kr.co.isajjim.domains.estimate.domain.service;

import kr.co.isajjim.domains.estimate.application.request.EstimateUpdateRequest;
import kr.co.isajjim.domains.estimate.persistence.entity.Estimate;
import kr.co.isajjim.domains.estimate.persistence.repository.EstimateRepository;
import kr.co.isajjim.global.common.ResponseCode;
import kr.co.isajjim.global.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EstimateService {

    private final EstimateRepository estimateRepository;

    public Estimate getEstimateById(Long estimateId) {
        return getOrThrow(estimateId);
    }

    public Long createEstimate(Estimate estimate) {
        return estimateRepository.save(estimate).getId();
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

    /* HELPER METHOD */
    private Estimate getOrThrow(Long id) {
        return estimateRepository.findById(id)
                .orElseThrow(() -> new BaseException(ResponseCode.NOT_FOUND));
    }
}
