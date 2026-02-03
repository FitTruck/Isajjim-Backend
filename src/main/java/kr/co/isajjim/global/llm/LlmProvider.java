package kr.co.isajjim.global.llm;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LlmProvider {

    @Value("${infra.google.gemini.model}")
    private String aiModel;

    private final Client client;

    public String llmCall(String prompt) {
        try {
            GenerateContentResponse response = client.models.generateContent(
                    aiModel,
                    prompt,
                    null);
            return response.text();
        } catch (Exception e) {
            throw new RuntimeException("AI 응답 생성 중 오류가 발생했습니다.", e);
        }
    }
}