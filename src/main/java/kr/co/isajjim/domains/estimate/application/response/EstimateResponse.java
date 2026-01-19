package kr.co.isajjim.domains.estimate.application.response;

public record EstimateResponse(
        Long estimateId
) {
    public static EstimateResponse from(Long id) {
        return new EstimateResponse(id);
    }
}
