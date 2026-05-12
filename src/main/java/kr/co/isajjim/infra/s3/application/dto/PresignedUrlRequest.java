package kr.co.isajjim.infra.s3.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import kr.co.isajjim.infra.s3.domain.constant.S3Folder;

import java.util.List;

public record PresignedUrlRequest(
        @NotNull
        S3Folder folder,

        @NotEmpty(message = "최소 하나 이상의 파일명이 필요합니다.")
        @Size(max = 100, message = "한 번에 최대 100개까지 요청 가능합니다.")
        @Schema(
                description = "파일 이름 목록(확장자 포함)",
                example = "[\"1.jpg\", \"2.jpg\"]"
        )
        List<
            @NotBlank(message = "파일 이름은 비어있을 수 없습니다.")
            @Size(max = 100, message = "파일 이름은 100자 이하여야 합니다.")
            String> fileNames

            //todo fileSize 검증 로직 추가
//            Long fileSize
) {
}