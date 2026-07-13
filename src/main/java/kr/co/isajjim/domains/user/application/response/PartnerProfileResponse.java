package kr.co.isajjim.domains.user.application.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.isajjim.domains.user.domain.constant.ApprovalStatus;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record PartnerProfileResponse(
        @Schema(description = "파트너 프로필 ID", example = "1")
        Long id,

        @Schema(description = "유저 ID", example = "1")
        Long userId,

        @Schema(description = "업체명", example = "이삿찜 이사")
        String companyName,

        @Schema(description = "대표자명", example = "홍길동")
        String representativeName,

        @Schema(description = "사업자등록번호", example = "123-45-67890")
        String businessRegistrationNumber,

        @Schema(description = "사업장 주소", example = "서울특별시 강남구 테헤란로 123")
        String businessAddress,

        @Schema(description = "업체 연락처", example = "02-1234-5678")
        String contactPhone,

        @Schema(description = "업체 소개", example = "이사 전문 업체입니다.")
        String introduction,

        @Schema(description = "사업자등록증 이미지 URL")
        String businessRegistrationImageUrl,

        @Schema(description = "승인 상태", example = "PENDING")
        ApprovalStatus approvalStatus,

        @Schema(description = "반려 사유 (approvalStatus가 REJECTED인 경우에만 값 존재)", example = "사업자등록증 이미지가 확인되지 않습니다.")
        String rejectionReason,

        @Schema(description = "신청 일시")
        LocalDateTime createdDate
) {
}
