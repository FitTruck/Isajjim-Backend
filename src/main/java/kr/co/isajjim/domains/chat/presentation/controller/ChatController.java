package kr.co.isajjim.domains.chat.presentation.controller;

import jakarta.validation.Valid;
import kr.co.isajjim.domains.chat.application.dto.request.ChatRoomCreateRequest;
import kr.co.isajjim.domains.chat.application.dto.response.ChatMessagePageResponse;
import kr.co.isajjim.domains.chat.application.dto.response.ChatRoomResponse;
import kr.co.isajjim.domains.chat.application.usecase.ChatUseCase;
import kr.co.isajjim.domains.chat.presentation.api.ChatApi;
import kr.co.isajjim.global.common.ApiResponse;
import kr.co.isajjim.global.common.ResponseCode;
import kr.co.isajjim.global.security.auth.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class ChatController implements ChatApi {

    private final ChatUseCase chatUseCase;

    @Override
    @PostMapping("/rooms")
    public ResponseEntity<ApiResponse<ChatRoomResponse>> createOrGetRoom(
            @RequestBody @Valid ChatRoomCreateRequest request,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        ChatRoomResponse response = chatUseCase.getOrCreateRoom(user.getUserId(), request.targetId());
        return ResponseEntity.ok(ApiResponse.ofSuccess(ResponseCode.OK, response));
    }

    @Override
    @GetMapping("/rooms")
    public ResponseEntity<ApiResponse<List<ChatRoomResponse>>> getChatRooms(
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        List<ChatRoomResponse> response = chatUseCase.getChatRooms(user.getUserId());
        return ResponseEntity.ok(ApiResponse.ofSuccess(ResponseCode.OK, response));
    }

    @Override
    @GetMapping("/rooms/{roomId}/messages")
    public ResponseEntity<ApiResponse<ChatMessagePageResponse>> getMessages(
            @PathVariable Long roomId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        ChatMessagePageResponse response = chatUseCase.getMessages(roomId, page, size, user.getUserId());
        return ResponseEntity.ok(ApiResponse.ofSuccess(ResponseCode.OK, response));
    }

    @Override
    @PutMapping("/rooms/{roomId}/read")
    public ResponseEntity<ApiResponse<Void>> markAsRead(
            @PathVariable Long roomId,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        chatUseCase.markAsRead(roomId, user.getUserId());
        return ResponseEntity.ok(ApiResponse.ofSuccess(ResponseCode.OK));
    }


}
