package kr.co.isajjim.infra.google.gcs.presentation.controller;

import jakarta.validation.Valid;
import kr.co.isajjim.global.common.ApiResponse;
import kr.co.isajjim.global.common.ResponseCode;
import kr.co.isajjim.infra.google.gcs.application.dto.PresignedUrlListResponse;
import kr.co.isajjim.infra.google.gcs.application.dto.PresignedUrlRequest;
import kr.co.isajjim.infra.google.gcs.application.usecase.GcsUseCase;
import kr.co.isajjim.infra.google.gcs.presentation.api.GcsApi;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/gcs")
@RequiredArgsConstructor
public class GcsController implements GcsApi {

    private final GcsUseCase gcsUseCase;

    @Override
    @PostMapping("/presigned")
    public ResponseEntity<ApiResponse<PresignedUrlListResponse>> getPresignedUrl(
            @RequestBody @Valid PresignedUrlRequest request
    ) {
        PresignedUrlListResponse signedUrl = gcsUseCase.generatePresignedUrl(request);
        return ResponseEntity.ok(ApiResponse.ofSuccess(ResponseCode.OK, signedUrl));
    }
}
