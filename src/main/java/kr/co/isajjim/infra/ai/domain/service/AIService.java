package kr.co.isajjim.infra.ai.domain.service;

import kr.co.isajjim.domains.estimate.domain.constant.AIStatus;
import kr.co.isajjim.domains.estimate.domain.service.EstimateService;
import kr.co.isajjim.domains.furniture.domain.constant.FurnitureLabel;
import kr.co.isajjim.domains.furniture.domain.service.FurnitureService;
import kr.co.isajjim.domains.image.application.response.ImageAnalysisDto;
import kr.co.isajjim.global.common.ResponseCode;
import kr.co.isajjim.global.exception.BaseException;
import kr.co.isajjim.infra.ai.application.dto.AIAnalysisResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AIService {

    @Value("${infra.ai.base-url}")
    private String url;

    private final RestClient restClient;
    private final FurnitureService furnitureService;
    private final EstimateService estimateService;

    @Async
    public void analyzeFurniture(Long estimateId, List<ImageAnalysisDto> images) {
        try {
            estimateService.updateAIStatus(estimateId, AIStatus.PROCESSING);
            /*
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("image_urls", images);

            AIAnalysisResponse response = restClient.post()
                    .uri(url + "/analyze-furniture")
                    .body(requestBody)
                    .retrieve()
                    .body(AIAnalysisResponse.class);
            */

            // Mock Data 이용
            AIAnalysisResponse response = getMockData(images.get(0).id(), images.get(1).id());

            furnitureService.saveFurnitureList(response.results());
            estimateService.updateAIStatus(estimateId, AIStatus.COMPLETED);
        } catch (Exception e) {
            log.error("AI 분석 중 오류 발생: {}", e.getMessage());
            estimateService.updateAIStatus(estimateId, AIStatus.FAILED);
            throw new BaseException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    private AIAnalysisResponse getMockData(Long imageId1, Long imageId2) {
        AIAnalysisResponse.ImageResult image1 = new AIAnalysisResponse.ImageResult(
                imageId1,
                List.of(
                        new AIAnalysisResponse.FurnitureInfo(FurnitureLabel.BED, 30.5, 20.0, 15.2),
                        new AIAnalysisResponse.FurnitureInfo(FurnitureLabel.TV, 210.0, 90.0, 85.0)
                )
        );

        AIAnalysisResponse.ImageResult image2 = new AIAnalysisResponse.ImageResult(
                imageId2,
                List.of(
                        new AIAnalysisResponse.FurnitureInfo(FurnitureLabel.TV, 45.0, 50.0, 90.0)
                )
        );

        return new AIAnalysisResponse(
                List.of(image1, image2)
        );
    }
}
