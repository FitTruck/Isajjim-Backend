package kr.co.isajjim.global.config.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;

@Configuration
@RequiredArgsConstructor
public class SwaggerConfig {

    @Value("${swagger.server-url}")
    private String serverUrl;
    private final ApiSuccessResponseHandler apiSuccessResponseHandler;
    private final ApiErrorResponseHandler apiErrorResponseHandler;

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .addServersItem(new Server().url(serverUrl))
                .info(new Info()
                        .title("Isajjim API")
                        .description("이삿찜 백엔드 API 문서")
                        .version("v1.0"));
    }

    @Bean
    public OperationCustomizer customize() {
        return (Operation operation, HandlerMethod handlerMethod) -> {
            apiSuccessResponseHandler.handleApiSuccessResponse(operation, handlerMethod);
            apiErrorResponseHandler.handleApiErrorResponse(operation, handlerMethod);
            return operation;
        };
    }
}