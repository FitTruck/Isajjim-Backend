package kr.co.isajjim.domains.user.presentation.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.isajjim.domains.user.application.response.UserResponse;
import kr.co.isajjim.domains.user.domain.constant.Role;
import kr.co.isajjim.global.annotation.swagger.ApiErrorResponseExplanation;
import kr.co.isajjim.global.annotation.swagger.ApiResponseExplanations;
import kr.co.isajjim.global.annotation.swagger.ApiSuccessResponseExplanation;
import kr.co.isajjim.global.annotation.swagger.PageableAsQueryParam;
import kr.co.isajjim.global.common.ApiResponse;
import kr.co.isajjim.global.common.ResponseCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Admin User", description = "어드민 - 유저 관리 API")
public interface AdminUserApi {

    @Operation(
            summary = "유저 목록 조회",
            description = "전체 유저 목록을 role로 필터링하고, 이름/이메일 키워드(keyword)로 검색하여 조회합니다. 둘 다 생략하면 전체 조회합니다."
    )
    @ApiResponseExplanations(
            success = @ApiSuccessResponseExplanation(
                    responseClass = UserResponse.class,
                    description = "조회 성공"
            ),
            errors = {
                    @ApiErrorResponseExplanation(exceptionCode = ResponseCode.FORBIDDEN)
            }
    )
    @PageableAsQueryParam
    ResponseEntity<ApiResponse<Page<UserResponse>>> getUsers(
            @Parameter(in = ParameterIn.QUERY, description = "역할 필터", example = "USER")
            @RequestParam(required = false) Role role,
            @Parameter(in = ParameterIn.QUERY, description = "이름/이메일 검색 키워드", example = "홍길동")
            @RequestParam(required = false) String keyword,
            @Parameter(hidden = true) Pageable pageable
    );
}
