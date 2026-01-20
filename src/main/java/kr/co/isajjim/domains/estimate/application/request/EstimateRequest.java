package kr.co.isajjim.domains.estimate.application.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.URL;

import java.util.List;

public record EstimateRequest(

        @Schema(
                description = "이미지 URL 목록",
                example = "[\"https://example.com/image1.jpg\", \"https://example.com/image2.jpg\"]"
        )
        @NotEmpty
        List<@NotBlank @URL String> imageUrls
) {
}
