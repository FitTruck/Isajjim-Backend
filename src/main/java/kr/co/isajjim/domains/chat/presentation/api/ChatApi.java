package kr.co.isajjim.domains.chat.presentation.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.co.isajjim.domains.chat.application.dto.request.ChatRoomCreateRequest;
import kr.co.isajjim.domains.chat.application.dto.request.DeviceTokenRequest;
import kr.co.isajjim.domains.chat.application.dto.response.ChatMessagePageResponse;
import kr.co.isajjim.domains.chat.application.dto.response.ChatRoomResponse;
import kr.co.isajjim.global.annotation.swagger.ApiResponseExplanations;
import kr.co.isajjim.global.annotation.swagger.ApiSuccessResponseExplanation;
import kr.co.isajjim.global.common.ApiResponse;
import kr.co.isajjim.global.security.auth.CustomUserDetails;
import kr.co.isajjim.infra.s3.application.dto.PresignedUrlListResponse;
import kr.co.isajjim.infra.s3.application.dto.PresignedUrlRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Chat", description = "채팅 API")
public interface ChatApi {

    @Operation(summary = "채팅방 생성 또는 조회", description = "업체와의 채팅방을 생성하거나 기존 채팅방을 반환합니다.")
    @ApiResponseExplanations(success = @ApiSuccessResponseExplanation(responseClass = ChatRoomResponse.class, description = "채팅방 조회 성공"))
    @PostMapping("/rooms")
    ResponseEntity<ApiResponse<ChatRoomResponse>> createOrGetRoom(
            @RequestBody @Valid ChatRoomCreateRequest request,
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails user
    );

    @Operation(summary = "채팅방 목록 조회", description = "내 채팅방 목록을 최근 메시지 순으로 반환합니다.")
    @ApiResponseExplanations(success = @ApiSuccessResponseExplanation(responseClass = ChatRoomResponse.class, description = "목록 조회 성공"))
    @GetMapping("/rooms")
    ResponseEntity<ApiResponse<List<ChatRoomResponse>>> getChatRooms(
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails user
    );

    @Operation(summary = "이전 메시지 조회", description = "채팅방의 이전 메시지를 페이지네이션으로 조회합니다.")
    @ApiResponseExplanations(success = @ApiSuccessResponseExplanation(responseClass = ChatMessagePageResponse.class, description = "메시지 조회 성공"))
    @GetMapping("/rooms/{roomId}/messages")
    ResponseEntity<ApiResponse<ChatMessagePageResponse>> getMessages(
            @PathVariable Long roomId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size,
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails user
    );

    @Operation(summary = "읽음 처리", description = "채팅방의 읽지 않은 메시지를 모두 읽음 처리합니다.")
    @PutMapping("/rooms/{roomId}/read")
    ResponseEntity<ApiResponse<Void>> markAsRead(
            @PathVariable Long roomId,
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails user
    );

    @Operation(summary = "FCM 토큰 등록", description = "푸시 알림을 위한 FCM 디바이스 토큰을 등록하거나 갱신합니다. 앱 시작 시 호출하세요.")
    @PostMapping("/device-token")
    ResponseEntity<ApiResponse<Void>> saveDeviceToken(
            @RequestBody @Valid DeviceTokenRequest request,
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails user
    );

    @Operation(
            summary = "채팅 이미지 업로드용 Presigned URL 발급",
            description = "S3에 이미지를 직접 업로드하기 위한 Presigned URL을 발급합니다.\n" +
                    "업로드 후 반환된 fileUrl을 WebSocket으로 type=IMAGE 메시지로 전송하세요."
    )
    @ApiResponseExplanations(success = @ApiSuccessResponseExplanation(responseClass = PresignedUrlListResponse.class, description = "URL 발급 성공"))
    @PostMapping("/rooms/{roomId}/images")
    ResponseEntity<ApiResponse<PresignedUrlListResponse>> generateImageUploadUrl(
            @PathVariable Long roomId,
            @RequestBody @Valid PresignedUrlRequest request,
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails user
    );
}
