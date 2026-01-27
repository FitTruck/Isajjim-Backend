package kr.co.isajjim.infra.google.gcs.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.net.URL;

@Builder
public record PresignedUrlResponse(

        @Schema(description = "파일 업로드를 위한 URL", example = "https://storage.googleapis.com/bucket-name/73af9_1.jpg")
        URL presignedUrl,

        @Schema(description = "업로드 후 파일 접근 URL", example = "https://storage.googleapis.com/bucket-name/73af9_1.jpg")
        String fileUrl,

        @Schema(description = "파일 고유 Key", example = "1/73af9_1.jpg")
        String key
) {
}
