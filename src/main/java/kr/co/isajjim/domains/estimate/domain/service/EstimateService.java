package kr.co.isajjim.domains.estimate.domain.service;

import kr.co.isajjim.domains.estimate.application.mapper.EstimateItemMapper;
import kr.co.isajjim.domains.estimate.application.mapper.EstimateMapper;
import kr.co.isajjim.domains.estimate.application.request.EstimateItemUpdateRequest;
import kr.co.isajjim.domains.estimate.application.request.EstimateRequest;
import kr.co.isajjim.domains.estimate.application.request.EstimateUpdateRequest;
import kr.co.isajjim.domains.estimate.domain.constant.AIStatus;
import kr.co.isajjim.domains.estimate.persistence.entity.Estimate;
import kr.co.isajjim.domains.estimate.persistence.entity.EstimateItem;
import kr.co.isajjim.domains.estimate.persistence.repository.EstimateRepository;
import kr.co.isajjim.domains.image.application.mapper.ImageMapper;
import kr.co.isajjim.domains.image.application.response.ImageAnalysisDto;
import kr.co.isajjim.domains.image.domain.service.ImageService;
import kr.co.isajjim.global.common.ResponseCode;
import kr.co.isajjim.global.exception.BaseException;
import kr.co.isajjim.global.llm.LlmProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class EstimateService {

    private final EstimateRepository estimateRepository;
    private final ImageService imageService;
    private final LlmProvider llmProvider;

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

    public boolean isAIProcessingCompleted(Long estimateId) {
        Estimate estimate = getOrThrow(estimateId);
        return estimate.getAiStatus() == AIStatus.COMPLETED;
    }

    public void updateDefaultInfo(Estimate estimate, EstimateUpdateRequest request) {
        estimate.updateEstimate(request.date());
        estimate.updateStartLocationDetail(request.startLocation());
        estimate.updateEndLocationDetail(request.endLocation());
    }

    public void updateItems(Long estimateId, EstimateItemUpdateRequest request) {
        Estimate estimate = getOrThrow(estimateId);
        estimate.deleteEstimateItem();

        request.items().forEach(itemRequest -> {
            EstimateItem newItem = EstimateItemMapper.toEstimateItem(estimate, itemRequest.category(), itemRequest.type(), itemRequest.quantity());
            estimate.addEstimateItem(newItem);
        });
    }

    @Transactional
    public void updateAIStatus(Long estimateId, AIStatus status) {
        Estimate estimate = getOrThrow(estimateId);
        estimate.updateAIStatus(status);
    }

    public String generateChatSummary(String chatContent) {
        String prompt = String.format(
                """
                        역할: 이사 전문 상담 요약가
                        대화 내용을 중심으로 이사 견적 확정 사항을 요약해줘. 최종 응답의 앞뒤에 '안녕하세요 이사 전문 상담 요약가입니다 ~를 요약해드릴게요, 편안한 이사 되세요' 등의 미사어구 없이 내용 자체만 요약해줘. 응답은 문단별로 줄바꿈하고, 문단 맨 앞에는 '*'을 넣어서 마크다운을 적용시킬거야.
                        
                        대화 내용:
                        %s""",
                chatContent
        );

        return llmProvider.llmCall(prompt);
    }

    /* HELPER METHOD */
    private Estimate getOrThrow(Long id) {
        return estimateRepository.findById(id)
                .orElseThrow(() -> new BaseException(ResponseCode.NOT_FOUND_ESTIMATE));
    }
}
