package kr.co.isajjim.infra.ai.domain.service;

import kr.co.isajjim.domains.estimate.domain.constant.AIStatus;
import kr.co.isajjim.domains.estimate.domain.service.EstimateService;
import kr.co.isajjim.domains.image.application.response.ImageAnalysisDto;
import kr.co.isajjim.global.common.ResponseCode;
import kr.co.isajjim.global.exception.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
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

    public void analyzeFurniture(Long estimateId, List<ImageAnalysisDto> images) {
        try {
            estimateService.updateAIStatus(estimateId, AIStatus.PROCESSING);
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("estimate_id", estimateId);
            requestBody.put("image_urls", images);

            if (aiServerOn) {
                restClient.post()
                        .uri(url + "/analyze-furniture")
                        .body(requestBody);
            }
        } catch (Exception e) {
            log.error("AI 분석 중 오류 발생: {}", e.getMessage());
            estimateService.updateAIStatus(estimateId, AIStatus.FAILED);
            throw new BaseException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }
}
