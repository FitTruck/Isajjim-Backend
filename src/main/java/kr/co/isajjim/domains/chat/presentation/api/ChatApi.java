package kr.co.isajjim.domains.chat.presentation.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.co.isajjim.domains.chat.application.dto.request.ChatRoomCreateRequest;
import kr.co.isajjim.domains.chat.application.dto.response.ChatMessagePageResponse;
import kr.co.isajjim.domains.chat.application.dto.response.ChatRoomResponse;
import kr.co.isajjim.global.annotation.swagger.ApiResponseExplanations;
import kr.co.isajjim.global.annotation.swagger.ApiSuccessResponseExplanation;
import kr.co.isajjim.global.common.ApiResponse;
import kr.co.isajjim.global.security.auth.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Chat", description = "채팅 API")
public interface ChatApi {

    @Operation(summary = "채팅방 생성 또는 조회", description = "상대방과의 채팅방을 생성하거나 기존 채팅방을 반환합니다.")
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

}
