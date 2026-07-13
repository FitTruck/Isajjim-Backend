package kr.co.isajjim.domains.user.application.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.URL;

public record PartnerApplicationRequest(
        @Schema(description = "업체명", example = "이삿찜 이사")
        @NotBlank
        String companyName,

        @Schema(description = "대표자명", example = "홍길동")
        @NotBlank
        String representativeName,

        @Schema(description = "사업자등록번호", example = "123-45-67890")
        @NotBlank
        @Pattern(regexp = "\\d{3}-\\d{2}-\\d{5}", message = "사업자등록번호 형식이 올바르지 않습니다.")
        String businessRegistrationNumber,

        @Schema(description = "사업장 주소", example = "서울특별시 강남구 테헤란로 123")
        @NotBlank
        String businessAddress,

        @Schema(description = "업체 연락처", example = "02-1234-5678")
        @NotBlank
        String contactPhone,

        @Schema(description = "업체 소개", example = "이사 전문 업체입니다.")
        String introduction,

        @Schema(description = "사업자등록증 이미지 URL", example = "https://example.com/business-registration.jpg")
        @NotBlank
        @URL
        String businessRegistrationImageUrl
) {
}
