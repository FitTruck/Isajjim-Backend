package kr.co.isajjim.infra.ai.domain.service;

import kr.co.isajjim.domains.estimate.domain.constant.AIStatus;
import kr.co.isajjim.domains.estimate.domain.service.EstimateNotificationService;
import kr.co.isajjim.domains.estimate.domain.service.EstimateService;
import kr.co.isajjim.domains.furniture.domain.constant.FurnitureLabel;
import kr.co.isajjim.domains.furniture.domain.constant.FurnitureType;
import kr.co.isajjim.domains.furniture.domain.service.FurnitureService;
import kr.co.isajjim.domains.image.application.response.ImageAnalysisDto;
import kr.co.isajjim.global.common.ResponseCode;
import kr.co.isajjim.global.exception.BaseException;
import kr.co.isajjim.infra.ai.application.dto.AIAnalysisResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AIService {

    @Value("${infra.ai.base-url}")
    private String url;

    @Value("${infra.ai.use-server}")
    private Boolean aiServerOn;

    private final RestClient restClient;
    private final EstimateService estimateService;
    private final FurnitureService furnitureService;
    private final EstimateNotificationService notificationService;

    @Transactional
    public void analyzeFurniture(Long estimateId, List<ImageAnalysisDto> images) {
        try {
            estimateService.updateAIStatus(estimateId, AIStatus.PROCESSING);
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("estimate_id", estimateId);
            requestBody.put("image_urls", images);

            if (aiServerOn) {
                ResponseEntity<Void> response = restClient.post()
                        .uri(url + "/analyze-furniture")
                        .body(requestBody)
                        .retrieve()
                        .toBodilessEntity();

                if (response.getStatusCode().is2xxSuccessful()) {
                    log.info("AI 서버 요청 성공");
                }
            } else {
                // AI 서버가 꺼져있는 경우 임시 값 전송
                Thread.sleep(3000);
                List<AIAnalysisResponse.ImageResult> mockResults = images.stream()
                        .map(image -> new AIAnalysisResponse.ImageResult(
                                image.id(),
                                List.of(
                                        new AIAnalysisResponse.FurnitureInfo(
                                                FurnitureLabel.SOFA,
                                                FurnitureType.THREE_SEATER_SOFA,
                                                180.0, 85.0, 90.0, 0.45, 0.55
                                        ),
                                        new AIAnalysisResponse.FurnitureInfo(
                                                FurnitureLabel.DESK,
                                                FurnitureType.DESK_NO_DRAWER,
                                                120.0, 60.0, 75.0, 0.25, 0.30
                                        )
                                )
                        ))
                        .toList();
                AIAnalysisResponse response = AIAnalysisResponse.builder()
                        .results(mockResults)
                        .build();
                furnitureService.saveFurnitureList(response.results());
                estimateService.updateAIStatus(estimateId, AIStatus.COMPLETED);
                notificationService.sendNotify(estimateId);
            }
        } catch (Exception e) {
            log.error("AI 분석 중 오류 발생: {}", e.getMessage());
            estimateService.updateAIStatus(estimateId, AIStatus.FAILED);
            throw new BaseException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }
}
