package kr.co.isajjim.infra.google.gcs.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

@Builder
public record PresignedUrlListResponse(
        @Schema(description = "Presigned Url 목록")
        List<PresignedUrlResponse> urls
) {
}
