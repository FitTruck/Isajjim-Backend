package kr.co.isajjim.infra.google.gcs.presentation.api;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import kr.co.isajjim.global.annotation.swagger.ApiResponseExplanations;
import kr.co.isajjim.global.annotation.swagger.ApiSuccessResponseExplanation;
import kr.co.isajjim.global.common.ApiResponse;
import kr.co.isajjim.infra.google.gcs.application.dto.PresignedUrlRequest;
import kr.co.isajjim.infra.google.gcs.application.dto.PresignedUrlResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface GcsApi {

    @Operation(
            summary = "Presigned Url 발급",
            description = "발급 후 presignedUrl을 PUT 요청으로 클라이언트에서 파일을 gcs에 업로드한 후, 응답받은 fileUrl 목록을 견적서 생성 시 첨부합니다."
    )
    @ApiResponseExplanations(
            success = @ApiSuccessResponseExplanation(
                    responseClass = PresignedUrlResponse.class,
                    description = "발급 성공"
            )
    )
    ResponseEntity<ApiResponse<PresignedUrlResponse>> getPresignedUrl(
            @RequestBody @Valid PresignedUrlRequest request
    );
}
