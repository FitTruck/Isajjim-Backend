package kr.co.isajjim.global.config.llm;

import com.google.genai.Client;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LlmConfig {

    @Bean
    public Client geminiClient() {
        return new Client();
    }
}
