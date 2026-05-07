package kr.co.isajjim.infra.s3.presentation.controller;

import jakarta.validation.Valid;
import kr.co.isajjim.global.common.ApiResponse;
import kr.co.isajjim.global.common.ResponseCode;
import kr.co.isajjim.infra.s3.application.dto.PresignedUrlListResponse;
import kr.co.isajjim.infra.s3.application.dto.PresignedUrlRequest;
import kr.co.isajjim.infra.s3.application.usecase.S3UseCase;
import kr.co.isajjim.infra.s3.presentation.api.S3Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/presigned-url")
@RequiredArgsConstructor
public class S3Controller implements S3Api {

    private final S3UseCase s3UseCase;

    @Override
    @PostMapping
    public ResponseEntity<ApiResponse<PresignedUrlListResponse>> getPresignedUrl(
            @RequestBody @Valid PresignedUrlRequest request
    ) {
        PresignedUrlListResponse signedUrl = s3UseCase.generatePresignedUrl(request);
        return ResponseEntity.ok(ApiResponse.ofSuccess(ResponseCode.OK, signedUrl));
    }
}
